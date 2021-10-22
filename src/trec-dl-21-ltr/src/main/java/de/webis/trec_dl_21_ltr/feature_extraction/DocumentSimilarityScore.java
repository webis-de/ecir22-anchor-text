package de.webis.trec_dl_21_ltr.feature_extraction;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.AfterEffectL;
import org.apache.lucene.search.similarities.AxiomaticF2EXP;
import org.apache.lucene.search.similarities.AxiomaticF2LOG;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.DistributionSPL;
import org.apache.lucene.search.similarities.IBSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.LambdaDF;
import org.apache.lucene.search.similarities.NormalizationH2;
import org.apache.lucene.search.similarities.Similarity;

import io.anserini.index.IndexArgs;
import io.anserini.search.SearchArgs;
import io.anserini.search.query.BagOfWordsQueryGenerator;

/**
 * Feature extraction for TREC DL 2021.
 * 
 * @author Maik Fröbe
 *
 */
public class DocumentSimilarityScore {

  private final IndexReader indexReader;
  
  private final SearchArgs args;
  
  private final Analyzer analyzer;
  
  public DocumentSimilarityScore(IndexReader indexReader) {
    this.indexReader = indexReader;
    this.args = new SearchArgs();
    this.analyzer = AnalyzerUtil.analyzer(this.args);
  }

  public float tfSimilarity(String query, String documentId) {
    return calculate(new TfSimilarity(), query, documentId);
  }
  
  public float tfIdfSimilarity(String query, String documentId) {
    return calculate(new ClassicSimilarity(), query, documentId);
  }

  public float bm25Similarity(String query, String documentId) {
    return calculate(bm25(), query, documentId);
  }

  private BM25Similarity bm25() {
    return new BM25Similarity(uniqueFloat(args.bm25_k1), uniqueFloat(args.bm25_b));
  }

  public float pl2Similarity(String query, String documentId) {
    return calculate(pl2(), query, documentId);
  }
  
  private DFRSimilarity pl2() {
    return new DFRSimilarity(new BasicModelIn(), new AfterEffectL(), new NormalizationH2(uniqueFloat(args.inl2_c)));
  }

  public float qlSimilarity(String query, String documentId) {
    return calculate(ql(), query, documentId);
  }

  private LMDirichletSimilarity ql() {
    return new LMDirichletSimilarity(uniqueFloat(args.qld_mu));
  }
  
  public float qljmSimilarity(String query, String documentId) {
    return calculate(qljm(), query, documentId);
  }
  
  private LMJelinekMercerSimilarity qljm() {
	  return new LMJelinekMercerSimilarity(uniqueFloat(args.qljm_lambda));
  }
  
  public float splSimilarity(String query, String documentId) {
    return calculate(spl(), query, documentId);
  }
  
  private IBSimilarity spl() {
	 return new IBSimilarity(new DistributionSPL(), new LambdaDF(),  new NormalizationH2(uniqueFloat(args.spl_c)));
  }
  
  public float f2expSimilarity(String query, String documentId) {
    return calculate(f2exp(), query, documentId);
  }
  
  private AxiomaticF2EXP f2exp() {
    return new AxiomaticF2EXP(uniqueFloat(args.f2exp_s));
  }
  
  public float f2logSimilarity(String query, String documentId) {
    return calculate(f2log(), query, documentId);
  }

  private AxiomaticF2LOG f2log() {
    return new AxiomaticF2LOG(uniqueFloat(args.f2log_s));
  }
  
  public float calculate(Similarity similarity, String query, String documentId) {
    return calculate(searcher(similarity, indexReader), query, documentId);
  }
  
  public float calculate(IndexSearcher searcher, String query, String documentId) {
    return calculate(searcher, similarityInBody(query), documentId);
  }

  public static float calculate(IndexSearcher searcher, Query query, String documentId) {
    BooleanQuery scoreForDoc = new BooleanQuery.Builder()
      .add(idIs(documentId), BooleanClause.Occur.FILTER)
      .add(query, BooleanClause.Occur.MUST)
      .build();

    return retrieveScoreOrFail(searcher, scoreForDoc, documentId);
  }

  private static IndexSearcher searcher(Similarity similarity, IndexReader reader) {
    IndexSearcher ret = new IndexSearcher(reader);
    ret.setSimilarity(similarity);
    
    return ret;
  }

  private static float retrieveScoreOrFail(IndexSearcher searcher, Query query, String docId) {
    try {
      return retrieveScore(searcher, query, docId);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static float retrieveScore(IndexSearcher searcher, Query query, String docId) throws IOException {
    TopDocs ret = searcher.search(query, 10);

    if(ret.scoreDocs.length == 0) {
      return 0;
    } else if (ret.scoreDocs.length == 1) {
      return ret.scoreDocs[0].score;
    }
    
    Document firstDoc = searcher.getIndexReader().document(ret.scoreDocs[0].doc);
    String actualId = firstDoc.get(IndexArgs.ID);

    if(docId == null || !docId.equals(actualId)) {
      throw new RuntimeException("I expected a document with id '" + docId + "', but got '" + actualId + "'.");
    }

    return ret.scoreDocs[0].score;
  }
  
  private static Query idIs(String documentId) {
    return new TermQuery(new Term(IndexArgs.ID, documentId));
  }

  private Query similarityInBody(String queryString) {
    return new BagOfWordsQueryGenerator().buildQuery(IndexArgs.CONTENTS, analyzer, queryString);
  }
  
  private static float uniqueFloat(String[] choices) {
    return Float.valueOf(uniqueElementOfFail(choices));
  }
  
  private static String uniqueElementOfFail(String[] choices) {
    if(choices.length != 1) {
      throw new RuntimeException("Cant extract unique element from array of length " + choices.length);
    }
    
    return choices[0];
  }
}