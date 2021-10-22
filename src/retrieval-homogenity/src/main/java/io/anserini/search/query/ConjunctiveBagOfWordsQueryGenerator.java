package io.anserini.search.query;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import io.anserini.analysis.AnalyzerUtils;

public class ConjunctiveBagOfWordsQueryGenerator extends QueryGenerator {
	@Override
	public Query buildQuery(String field, Analyzer analyzer, String queryText) {
		List<String> tokens = AnalyzerUtils.analyze(analyzer, queryText);
		Map<String, Long> collect = tokens.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		for (String t : collect.keySet()) {
			builder.add(new BoostQuery(new TermQuery(new Term(field, t)), (float) collect.get(t)),
					BooleanClause.Occur.MUST);
		}
		return builder.build();
	}

	@Override
	public Query buildQuery(Map<String, Float> fields, Analyzer analyzer, String queryText) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		for (Map.Entry<String, Float> entry : fields.entrySet()) {
			String field = entry.getKey();
			float boost = entry.getValue();

			Query clause = buildQuery(field, analyzer, queryText);
			builder.add(new BoostQuery(clause, boost), BooleanClause.Occur.MUST);
		}
		return builder.build();
	}

}
