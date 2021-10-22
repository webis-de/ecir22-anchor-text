package de.webis.trec_dl_21_ltr.feature_extraction;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class FeatureExtractor {
  private final Map<Integer, String> idToQuery;
  private final Map<Integer, Set<String>> topicToDocs;

  private static final Logger LOG = LogManager.getLogger(FeatureExtractor.class);
  
  public void runFeatureExtraction(FeatureExtractionArguments args) {
    topicToDocs.entrySet().parallelStream().forEach(i -> runFeatureExtraction(i.getKey(), i.getValue(), args));
  }

  @SneakyThrows
  private void runFeatureExtraction(Integer topic, Set<String> docs, FeatureExtractionArguments args) {
    Path outputFile = args.outputFileForTopic(topic);
    if(outputFile.toFile().exists()) {
      LOG.info("Skip existing output: " + topic);
      return;
    }
    
    Files.writeString(outputFile, calculateFeatures(topic, docs, args));
  }

  @SneakyThrows
  public String calculateFeatures(Integer topic, Set<String> docs, FeatureExtractionArguments args) {
    long start = System.currentTimeMillis();

    String query = idToQuery.get(topic);
    if (query == null || query.trim().isEmpty()) {
      throw new RuntimeException("Could not find query for topic " + topic);
    }

    LOG.info("Start extracting features for topic " + topic + " with " + docs.size() + " documents.");
    SimilarityCalculator calc = new SimilarityCalculator(args);
    Map<String, Map<String, Float>> features = calc.calculateFeaturesForDocuments(query, new ArrayList<>(docs));
    
    Map<String, Object> ret = new LinkedHashMap<>();
    ret.put("query", query);
    ret.put("topic", topic);
    ret.put("documentFeatures", features);
    
    LOG.info("Finished extraction of features for topic " + topic + " with " + docs.size() + " documents in " + (System.currentTimeMillis() - start) + "ms.");

    return new ObjectMapper().writeValueAsString(ret);
  }
}
