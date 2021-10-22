package de.webis.trec_dl_21_ltr.feature_extraction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import lombok.SneakyThrows;

public class SimilarityCalculator {

  private final Map<String, DocumentSimilarityScore> docSimilarityScores;

  private final FeatureExtractionArguments args;
  
  public SimilarityCalculator(FeatureExtractionArguments args) {
    this.docSimilarityScores = initializeSimilarityReaders(args);
    this.args = args;
  }

  public Map<String, Map<String, Float>> calculateFeaturesForDocuments(String query, List<String> documentIds) {
    Map<String, Map<String, Map<String, Float>>> fieldToDocScores = new LinkedHashMap<>();
    
    for(String field: args.getFieldToIndex().keySet().stream().sorted().collect(Collectors.toList())) {
      fieldToDocScores.put(field, calculateFeaturesForDocumentsOnField(query, documentIds, field));
    }
    
    Map<String, Map<String, Float>> ret = new LinkedHashMap<>();
    for(String doc: documentIds.stream().sorted().collect(Collectors.toList())) {
      Map<String, Float> docFeatures = new LinkedHashMap<>();
      for(String field: args.getFieldToIndex().keySet()) {
        Map<String, Float> tmp = fieldToDocScores.get(field).get(doc);
        if(tmp == null || tmp.isEmpty()) {
          throw new RuntimeException("This should not happen.");
        }
        
        docFeatures.putAll(tmp);
      }
      
      ret.put(doc, docFeatures);
    }

    return ret;
  }

  private Map<String, Map<String, Float>> calculateFeaturesForDocumentsOnField(String query, List<String> documentIds, String field) {
    Map<String, Map<String, Float>> ret = new LinkedHashMap<>();

    for(String docId: documentIds) {
      ret.put(docId, calculateFeaturesForDocumentOnField(query, docId, field));
    }

    return ret;
  }
  
  @SneakyThrows
  private Map<String, Float> calculateFeaturesForDocumentOnField(String query, String documentId, String field) {
    DocumentSimilarityScore sim = docSimilarityScores.get(field);
    Map<String, Float> ret = new LinkedHashMap<>();
    ret.put(field + "-bm25-similarity", sim.bm25Similarity(query, documentId));
    ret.put(field + "-f2exp-similarity", sim.f2expSimilarity(query, documentId));
    ret.put(field + "-spl-similarity", sim.splSimilarity(query, documentId));
    ret.put(field + "-pl2-similarity", sim.pl2Similarity(query, documentId));
    ret.put(field + "-ql-similarity", sim.qlSimilarity(query, documentId));
    ret.put(field + "-qljm-similarity", sim.qljmSimilarity(query, documentId));
    ret.put(field + "-f2log-similarity", sim.f2logSimilarity(query, documentId));
    ret.put(field + "-tf-idf-similarity", sim.tfIdfSimilarity(query, documentId));
    ret.put(field + "-tf-similarity", sim.tfSimilarity(query, documentId));
    
    return ret;
  }

  @SneakyThrows
  private static Map<String, DocumentSimilarityScore> initializeSimilarityReaders(FeatureExtractionArguments args) {
    Map<String, DocumentSimilarityScore> ret = new LinkedHashMap<>();

    for(String field : args.getFieldToIndex().keySet()) {
      IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPathForField(args, field)));
      ret.put(field, new DocumentSimilarityScore(reader));
    }

    return ret;
  }

  private static Path indexPathForField(FeatureExtractionArguments args, String field) {
    return Paths.get(args.getIndexDir()).resolve(args.getFieldToIndex().get(field));
  }
}