package de.webis.trec_dl_21_ltr.parsing;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import io.anserini.search.topicreader.TsvIntTopicReader;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class QueryExtractor {
  private final List<String> queryFiles;
  private final String baseDir;

  public Map<Integer, String> idToQuery() {
    Map<Integer, String> ret = new HashMap<>();

    for(String file: queryFiles) {
      ret.putAll(idToQuery(file));
    }

    return ret;
  }

  @SneakyThrows
  private Map<Integer, String> idToQuery(String file) {
    Path p = Paths.get(baseDir).resolve(file);
    SortedMap<Integer, Map<String, String>> ret = new TsvIntTopicReader(p).read();

    return ret.entrySet().stream()
      .collect(Collectors.toMap(i -> i.getKey(), i -> i.getValue().get("title")));
  }
  
  public static QueryExtractor msMarcoV2Queries() {
    return new QueryExtractor(Arrays.asList("2021_queries.tsv", "docv2_dev2_queries.tsv", "docv2_dev_queries.tsv", "docv2_train_queries.tsv"), "/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources");
  }
  
  public static QueryExtractor msMarcoV1Queries() {
    return new QueryExtractor(Arrays.asList("msmarco-docdev-queries.tsv", "msmarco-doctrain-queries.tsv", "msmarco-test2020-queries.tsv"), "/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources-trec-dl-20");
  }

  public static QueryExtractor msMarcoV1_DL_19_20() {
    return new QueryExtractor(Arrays.asList("dl19+20-queries.tsv"), "Data/dl19+20/");
  }
  
  public static QueryExtractor msMarcoV1NavPopular() {
    return new QueryExtractor(Arrays.asList("topics.msmarco-entrypage-popular.tsv"), "Data/navigational-topics-and-qrels-ms-marco-v1/");
  }
  
  public static QueryExtractor msMarcoV2NavPopular() {
    return new QueryExtractor(Arrays.asList("topics.msmarco-v2-entrypage-popular.tsv"), "Data/navigational-topics-and-qrels-ms-marco-v2/");
  }
  
  public static QueryExtractor msMarcoV1NavRandom() {
    return new QueryExtractor(Arrays.asList("topics.msmarco-entrypage-random.tsv"), "Data/navigational-topics-and-qrels-ms-marco-v1/");
  }
  
  public static QueryExtractor msMarcoV2NavRandom() {
    return new QueryExtractor(Arrays.asList("topics.msmarco-v2-entrypage-random.tsv"), "Data/navigational-topics-and-qrels-ms-marco-v2/");
  }
}
