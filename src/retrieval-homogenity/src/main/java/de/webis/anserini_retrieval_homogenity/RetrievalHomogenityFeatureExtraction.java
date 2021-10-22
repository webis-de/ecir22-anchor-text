package de.webis.anserini_retrieval_homogenity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.anserini.index.IndexArgs;

public class RetrievalHomogenityFeatureExtraction {

	private final IndexSearcher searcher;
	public RetrievalHomogenityFeatureExtraction(Path indexDir) {
		this.searcher = searcher(indexDir);
	}

	public Stream<String> extractFeatures(List<RunLine> runLines, List<Integer> list) {
		Map<String, List<String>> topicToDocIds = topicToDocIds(runLines);
		
		return topicToDocIds.keySet().stream().map(topic -> {
			if(list != null && !list.contains(Integer.parseInt(topic))) {
				return null;
			}
			
			System.err.println("Process Topic");
			return processTopic(topic, topicToDocIds.get(topic));
		}).filter(i -> i != null);
	}
	
	private String processTopic(String topic, List<String> list) {
		Map<String, Object> ret = new LinkedHashMap<>();
		ret.put("topic", topic);
		List<Object> tmp = new ArrayList<>();
		ret.put("documents", tmp);
		for(String docId: list) {
			Map<String, Object> doc = new LinkedHashMap<>();
			doc.put("docId", docId);
			doc.put("documentRepresentation", docToFeatures(docId));
			tmp.add(doc);
		}
		
		try {
			return new ObjectMapper().writeValueAsString(ret);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, List<String>> topicToDocIds(List<RunLine> runLines) {
		Map<String, List<String>> ret = new LinkedHashMap<>();

		for(RunLine runLine: runLines) {
			String topic = "" + runLine.getTopic();
			if(!ret.containsKey(topic)) {
				ret.put(topic, new ArrayList<>());
			}
			
			ret.get(topic).add(runLine.getDoucmentID());
		}
		
		return ret;
	}
	
	private Map<String, Integer> docToFeatures(String docId) {
		try {
			Terms t = calculate(searcher, docId);
			TermsEnum terms = t.iterator();
			BytesRef term = null;
			Map<String, Integer> ret = new LinkedHashMap<>();
	
		    while ((term = terms.next()) != null) {
		    	String termText = term.utf8ToString();
		    	ret.put(termText, (int) terms.totalTermFreq());
		    }
		    
		    return ret;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static IndexSearcher searcher(Path path) {
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
			
			return new IndexSearcher(reader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Terms calculate(IndexSearcher searcher, String documentId) {
		BooleanQuery doc = new BooleanQuery.Builder().add(idIs(documentId), BooleanClause.Occur.FILTER).build();

		return retrieveScoreOrFail(searcher, doc, documentId);
	}

	private static Terms retrieveScoreOrFail(IndexSearcher searcher, Query query, String docId) {
		try {
			return retrieveScore(searcher, query, docId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Terms retrieveScore(IndexSearcher searcher, Query query, String docId) throws IOException {
		TopDocs ret = searcher.search(query, 10);

		if (ret.scoreDocs.length != 1) {
			throw new RuntimeException("");
		} 

		Document firstDoc = searcher.getIndexReader().document(ret.scoreDocs[0].doc);
		String actualId = firstDoc.get(IndexArgs.ID);

		if (docId == null || !docId.equals(actualId)) {
			throw new RuntimeException("I expected a document with id '" + docId + "', but got '" + actualId + "'.");
		}

		return searcher.getIndexReader().getTermVector(ret.scoreDocs[0].doc, IndexArgs.CONTENTS);
	}

	private static Query idIs(String documentId) {
		return new TermQuery(new Term(IndexArgs.ID, documentId));
	}
}
