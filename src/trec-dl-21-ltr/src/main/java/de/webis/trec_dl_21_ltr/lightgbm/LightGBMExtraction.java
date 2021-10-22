package de.webis.trec_dl_21_ltr.lightgbm;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.anserini.eval.RelevanceJudgments;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@Builder
public class LightGBMExtraction {
  private final String queryDocumentFeaturesDirectory,
    documentFeaturesFile,
    queryFeaturesFile;
  private final List<String> features;
  private final RelevanceJudgments relevanceJudgments;
  private final Map<Integer, Set<String>> trainingTopics, testTopics;
  private final List<Map<Integer, Set<String>>> validationTopics;
  private final LightGBMConfiguration lightgbmConfig;

  @Builder.Default
  private Map<String, Map<String, Object>> queryFeatures = null,
    documentFeatures = null;
  
  private static final Logger LOG = LogManager.getLogger(LightGBMExtraction.class);

  public void buildLightGbmDir(String outDir) {
    Path o = Paths.get(outDir);
    
    produceRankingFile(testTopics, null, o.resolve(lightgbmConfig.getTestFile()));
    produceRerankingFile(testTopics, null, o.resolve(lightgbmConfig.getTestFile()));
    if(validationTopics != null) {
      for(int i=0; i< validationTopics.size(); i++) {
        produceRankingFile(validationTopics.get(i), relevanceJudgments, o.resolve(lightgbmConfig.getValidationFiles().get(i)));
        produceRerankingFile(validationTopics.get(i), relevanceJudgments, o.resolve(lightgbmConfig.getValidationFiles().get(i)));
      }
    }
    
    lightgbmConfig.writeTrainConfigFile(o);
    produceRankingFile(trainingTopics, relevanceJudgments, o.resolve(lightgbmConfig.getTrainFile()));
  }

  @SneakyThrows
  private void produceRerankingFile(Map<Integer, Set<String>> topics, RelevanceJudgments relevanceJudgments, Path outFile) {
    FileWriter fw = new FileWriter(outFile.toFile() +".rerank", false);
    
    for(int topic: topics(topics)) {
      LOG.info("Process topic " + topic);
      Map<String, Map<String, Object>> queryDocumentFeatures = queryDocumentFeatures(String.valueOf(topic));
      Map<String, Object> ret = new LinkedHashMap<>();
      ret.put("topic", String.valueOf(topic));
      List<Map<String, Object>> docToRerank = new ArrayList<>();
      
      for(String document: docs(topics.get(topic))) {
        Map<String, Object> features = features(String.valueOf(topic), document, queryDocumentFeatures);
        Map<String, Object> tmp = new LinkedHashMap<>();
        tmp.put("documentId", document);
        tmp.put("features", formatFeaturesMap(features, getFeatures()));
        
        docToRerank.add(tmp);
      }
      
      ret.put("documents", docToRerank);
      
      fw.write(new ObjectMapper().writeValueAsString(ret) + "\n");
    }
    
    fw.close();
  }
  
  @SneakyThrows
  private void produceRankingFile(Map<Integer, Set<String>> topics, RelevanceJudgments relevanceJudgments, Path outFile) {
    if(topics == null) {
      return;
    }

    FileWriter fw = new FileWriter(outFile.toFile(), false);
    
    for(int topic: topics(topics)) {
      LOG.info("Process topic " + topic);
      Map<String, Map<String, Object>> queryDocumentFeatures = queryDocumentFeatures(String.valueOf(topic));
      
      for(String document: docs(topics.get(topic))) {
        Map<String, Object> features = features(String.valueOf(topic), document, queryDocumentFeatures);
        Integer relevanceLabel = null;
        if(relevanceJudgments != null && relevanceJudgments.isDocJudged(String.valueOf(topic), document)) {
          relevanceLabel = relevanceJudgments.getRelevanceGrade(String.valueOf(topic), document);
        }
        fw.write(formatLine(String.valueOf(topic), document, features, getFeatures(), relevanceLabel) + "\n");
      }
    }
    
    fw.close();
  }
  
  private List<String> docs(Set<String> set) {
    return set.stream().sorted().collect(Collectors.toList());
  }

  private List<Integer> topics(Map<Integer, Set<String>> topics) {
	return topics.keySet().stream().sorted().collect(Collectors.toList());
  }

  private synchronized Map<String, Map<String, Object>> queryFeatures() {
    if(queryFeatures == null) {
      queryFeatures = new HashMap<>();
      
      if(queryFeaturesFile != null) {
        parseJsonl(queryFeaturesFile).forEach(i -> {
          queryFeatures.put((String) i.get("query_id"), i);
        });
      }
    }

    return queryFeatures;
  }
  
  public static String formatLine(String queryId, String documentId, Map<String, Object> featuresValues, List<String> features, Integer relevanceLabel) {
    if(relevanceLabel == null) {
      relevanceLabel = 0;
    }
    
    return relevanceLabel + " 1:" + queryId + " " + formatFeatures(featuresValues, features);
  }
  
  private static String formatFeatures(Map<String, Object> featuresValues, List<String> features) {
    return formatFeaturesMap(featuresValues, features).entrySet().stream()
      .map(i -> i.getKey() + ":" + i.getValue())
      .collect(Collectors.joining(" "));
  }

  private static Map<Integer, Object> formatFeaturesMap(Map<String, Object> featuresValues, List<String> features) {
    Map<Integer, Object> ret = new LinkedHashMap<>();
    for(int i=0; i< features.size(); i++) {
      if(!featuresValues.containsKey(features.get(i))) {
        throw new RuntimeException("Could not load feature " + features.get(i));
      }
      ret.put((i+2), featuresValues.get(features.get(i)));
    }

    return ret;
  }

  private Map<String, Object> features(String queryId, String documentId, Map<String, Map<String, Object>> queryDocumentFeatures) {
    Map<String, Object> ret = new HashMap<>();
    if(queryFeatures().containsKey(queryId)) {
      ret.putAll(queryFeatures().get(queryId));
    }

    if(documentFeatures().containsKey(documentId)) {
      ret.putAll(documentFeatures().get(documentId));
    }

    ret.putAll(queryDocumentFeatures.get(documentId));
    
    return ret;
  }
  
  private synchronized Map<String, Map<String, Object>> documentFeatures() {
    if(documentFeatures == null) {
      Set<String> expectedIds = documentsUsedInRanking();
      documentFeatures = new HashMap<>();
      if(documentFeaturesFile != null) {
        parseJsonl(documentFeaturesFile).forEach(i -> {
          if(expectedIds.contains(i.get("doc_id"))) {
            documentFeatures.put((String) i.get("doc_id"), i);
          }
        });
      }
    }
    
    return documentFeatures;
  }
  
  private Set<String> documentsUsedInRanking() {
    List<Map<Integer, Set<String>>> tmp = new ArrayList<>();
    if(validationTopics != null) {
      tmp.addAll(validationTopics);
    }
    
    if(trainingTopics != null) {
      tmp.add(trainingTopics);
    }
    
    tmp.add(testTopics);
    Set<String> ret = new HashSet<>();
    for(Map<Integer, Set<String>> i: tmp) {
      for(Set<String> j: i.values()) {
        ret.addAll(j);
      }
    }
    
    return ret;
  }
  
  @SneakyThrows
  private Stream<Map<String, Object>> parseJsonl(String file) {
    return Files.lines(Paths.get(file)).map(i -> toJson(i));
  }
  
  @SneakyThrows
  @SuppressWarnings("unchecked")
  private Map<String, Map<String, Object>> queryDocumentFeatures(String queryId) {
    Map<String, Object> ret = new ObjectMapper().readValue(Paths.get(queryDocumentFeaturesDirectory).resolve(queryId + ".json").toFile(), Map.class);
    if(!String.valueOf(ret.get("topic")).equals(queryId)) {
      throw new RuntimeException("ttttt");
    }
    
    return (Map<String, Map<String, Object>>) ret.get("documentFeatures");
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  private static Map<String, Object> toJson(String json) {
    return new ObjectMapper().readValue(json, Map.class);
  }
}
