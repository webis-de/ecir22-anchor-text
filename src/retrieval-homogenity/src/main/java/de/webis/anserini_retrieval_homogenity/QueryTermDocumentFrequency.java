package de.webis.anserini_retrieval_homogenity;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.anserini.analysis.AnalyzerUtils;
import io.anserini.index.IndexArgs;
import io.anserini.search.topicreader.TsvIntTopicReader;

public class QueryTermDocumentFrequency {
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			throw new RuntimeException("Can not handle: " + args.length);
		}
		
		TsvIntTopicReader topics = new TsvIntTopicReader(Paths.get(args[0]));
		IndexSearcher searcher = RetrievalHomogenityFeatureExtraction.searcher(Paths.get(args[1]));
		
		for(Entry<Integer, Map<String, String>> topic: topics.read().entrySet()) {
			System.out.println(process(topic.getKey(), topic.getValue().get("title"), searcher));
		}
	}

	private static String process(Integer queryId, String query, IndexSearcher searcher) throws IOException {
		List<Term> terms = AnalyzerUtils.analyze(query).stream().map(i -> new Term(IndexArgs.CONTENTS, i)).collect(Collectors.toList());
		
		List<Map<String, Object>> termsWithCounts = new ArrayList<>();
		
		for(Term term: terms) {
			Map<String, Object> tmp = new LinkedHashMap<>();
			tmp.put("term", term.text());
			tmp.put("docFreq", searcher.getIndexReader().docFreq(term));
			
			termsWithCounts.add(tmp);
		}
		
		Map<String, Object> ret = new LinkedHashMap<>();
		ret.put("queryId", queryId);
		ret.put("query", query);
		ret.put("terms", termsWithCounts);
		
		return new ObjectMapper().writeValueAsString(ret);
	}
}
