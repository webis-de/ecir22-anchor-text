package de.webis.trec_dl_21_ltr.lightgbm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.anserini.eval.RelevanceJudgments;
import lombok.SneakyThrows;

public class LightGBMExtractionTest {

  private static final String DOCUMENT_FEATURES_FILE = "src/test/resources/feature-extraction/ms-marco-document-features.jsonl";
  private static final String QUERY_FEATURES_FILE = "src/test/resources/feature-extraction/ms-marco-query-features.jsonl";
  private static final String QUERY_DOCUMENT_FEATURES_DIR = "src/test/resources/feature-extraction/";

  @Test
  public void assert50DistinctFeaturesAreUsed() {
    Assert.assertEquals(50, new HashSet<>(App.FEATURES).size());
  }
  
  @Test
  public void assert121DistinctV1FeaturesAreUsed() {
    Assert.assertEquals(121, new HashSet<>(App.FEATURES_V1).size());
  }
  
  @Test
  public void assert112DistinctV1WithoutOrcasFeaturesAreUsed() {
    Assert.assertEquals(112, new HashSet<>(App.FEATURES_V1_NO_ORCAS).size());
  }
 
  @Test
  public void assert49DistinctV1WithoutAnchorFeaturesAreUsed() {
    Assert.assertEquals(49, new HashSet<>(App.FEATURES_V1_NO_ANCHOR).size());
  }
  
  @Test
  public void assert40DistinctV1WithoutAnchorWithoutOrcasFeaturesAreUsed() {
    Assert.assertEquals(40, new HashSet<>(App.FEATURES_V1_NO_ANCHOR_NO_ORCAS).size());
  }
  
  @Test
  @SneakyThrows
  public void approveFeatures() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES.size(); i++) {
      actual.put(i+2, App.FEATURES.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }

  @Test
  @SneakyThrows
  public void approveFeaturesWithoutAnchors() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES_NO_ANCHOR.size(); i++) {
      actual.put(i+2, App.FEATURES_NO_ANCHOR.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }

  @Test
  @SneakyThrows
  public void approveFeaturesV1() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES_V1.size(); i++) {
      actual.put(i+2, App.FEATURES_V1.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }

  @Test
  @SneakyThrows
  public void approveFeaturesV1NoOrcas() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES_V1_NO_ORCAS.size(); i++) {
      actual.put(i+2, App.FEATURES_V1_NO_ORCAS.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }

  @Test
  @SneakyThrows
  public void approveFeaturesV1NoAnchorNoOrcas() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES_V1_NO_ANCHOR_NO_ORCAS.size(); i++) {
      actual.put(i+2, App.FEATURES_V1_NO_ANCHOR_NO_ORCAS.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }

  @Test
  @SneakyThrows
  public void approveFeaturesV1NoAnchor() {
    Map<Integer, String> actual = new LinkedHashMap<>();
    for(int i=0; i< App.FEATURES_V1_NO_ANCHOR.size(); i++) {
      actual.put(i+2, App.FEATURES_V1_NO_ANCHOR.get(i));
    }

    Approvals.verify(new ObjectMapper().writeValueAsString(actual));
  }  

  @Test
  public void testRunFile() {
    Integer relevanceLabel = null;
    Map<String, Object> features = Map.of(
      "f1", 0.01,
      "f2", 0.02,
      "f3", 0.03
    );
    
    String expected = "0 1:-4 2:0.02 3:0.01";
    String queryId = "-4";
    String documentId = "doc-2";
    
    String actual = LightGBMExtraction.formatLine(queryId, documentId, features, Arrays.asList("f2","f1"), relevanceLabel);
    
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testRunFile2() {
    Integer relevanceLabel = null;
    Map<String, Object> features = Map.of(
      "f1", 0.01,
      "f2", 0.02,
      "f3", 0.03
    );
    
    String expected = "0 1:queryId 2:0.01 3:0.02 4:0.03";
    String queryId = "queryId";
    String documentId = "doc";
    
    String actual = LightGBMExtraction.formatLine(queryId, documentId, features, Arrays.asList("f1","f2", "f3"), relevanceLabel);
    
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected = RuntimeException.class)
  public void testExtractionOfNonExistingFeature() {
    Integer relevanceLabel = null;
    Map<String, Object> features = Map.of(
      "f1", 0.01,
      "f2", 0.02,
      "f3", 0.03
    );
    String queryId = "queryId";
    String documentId = "doc";
    
    LightGBMExtraction.formatLine(queryId, documentId, features, Arrays.asList("f1","f2", "f4"), relevanceLabel);
  }

  @Test
  @SneakyThrows
  public void approveTestFeatureFile() {
    String outDir = Files.createTempDirectory("lightgbm-test").toAbsolutePath().toString();
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
    	.documentFeaturesFile(DOCUMENT_FEATURES_FILE)
    	.queryFeaturesFile(QUERY_FEATURES_FILE)
    	.queryDocumentFeaturesDirectory(QUERY_DOCUMENT_FEATURES_DIR)
    	.features(App.FEATURES)
    	.lightgbmConfig(conf)
    	.relevanceJudgments(relevanceJudgments())
    	.trainingTopics(dummyTopics())
    	.validationTopics(Arrays.asList(dummyTopics()))
    	.testTopics(dummyTopics())
        .build();
    extract.buildLightGbmDir(outDir);
    
    Approvals.verify(Files.readString(Paths.get(outDir).resolve("2021_trec_dl_test")));
  }

  @Test
  @SneakyThrows
  public void approveTestRerankFile() {
    String outDir = Files.createTempDirectory("lightgbm-test").toAbsolutePath().toString();
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
    	.documentFeaturesFile(DOCUMENT_FEATURES_FILE)
    	.queryFeaturesFile(QUERY_FEATURES_FILE)
    	.queryDocumentFeaturesDirectory(QUERY_DOCUMENT_FEATURES_DIR)
    	.features(App.FEATURES)
    	.lightgbmConfig(conf)
    	.relevanceJudgments(relevanceJudgments())
    	.trainingTopics(dummyTopics())
    	.validationTopics(Arrays.asList(dummyTopics()))
    	.testTopics(dummyTopics())
        .build();
    extract.buildLightGbmDir(outDir);
    
    Approvals.verify(Files.readString(Paths.get(outDir).resolve("2021_trec_dl_test.rerank")));
  }
  
  @Test
  @SneakyThrows
  public void approveValidationFeatureFile() {
    String outDir = Files.createTempDirectory("lightgbm-test").toAbsolutePath().toString();
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
    	.documentFeaturesFile(DOCUMENT_FEATURES_FILE)
    	.queryFeaturesFile(QUERY_FEATURES_FILE)
    	.queryDocumentFeaturesDirectory(QUERY_DOCUMENT_FEATURES_DIR)
    	.features(App.FEATURES)
    	.lightgbmConfig(conf)
    	.relevanceJudgments(relevanceJudgments())
    	.trainingTopics(dummyTopics())
    	.validationTopics(Arrays.asList(dummyTopics()))
    	.testTopics(dummyTopics())
        .build();
    extract.buildLightGbmDir(outDir);
    
    Approvals.verify(Files.readString(Paths.get(outDir).resolve("docv2_dev1")));
  }

  @Test
  @SneakyThrows
  public void approveTrainFeatureFile() {
    String outDir = Files.createTempDirectory("lightgbm-test").toAbsolutePath().toString();
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
    	.documentFeaturesFile(DOCUMENT_FEATURES_FILE)
    	.queryFeaturesFile(QUERY_FEATURES_FILE)
    	.queryDocumentFeaturesDirectory(QUERY_DOCUMENT_FEATURES_DIR)
    	.features(App.FEATURES)
    	.lightgbmConfig(conf)
    	.relevanceJudgments(relevanceJudgments())
    	.trainingTopics(dummyTopics())
    	.validationTopics(Arrays.asList(dummyTopics()))
    	.testTopics(dummyTopics())
        .build();
    extract.buildLightGbmDir(outDir);
    
    Approvals.verify(Files.readString(Paths.get(outDir).resolve("docv2_train")));
  }
  
  @Test
  @SneakyThrows
  public void approveTrainFeatureFileMsMarcoV1() {
    String outDir = Files.createTempDirectory("lightgbm-test").toAbsolutePath().toString();
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
    	.documentFeaturesFile("src/test/resources/feature-extraction/ms-marco-v1-document-features.jsonl")
    	.queryFeaturesFile("src/test/resources/feature-extraction/ms-marco-v1-query-features.jsonl")
    	.queryDocumentFeaturesDirectory(QUERY_DOCUMENT_FEATURES_DIR)
    	.features(App.FEATURES_V1)
    	.lightgbmConfig(conf)
    	.relevanceJudgments(relevanceJudgments())
    	.trainingTopics(dummyTopicsV1())
    	.validationTopics(Arrays.asList(dummyTopicsV1()))
    	.testTopics(dummyTopicsV1())
        .build();
    extract.buildLightGbmDir(outDir);
    
    Approvals.verify(Files.readString(Paths.get(outDir).resolve("docv2_train")));
  }
  
  private Map<Integer, Set<String>> dummyTopics() {
    return Map.of(
      1000006, Set.of("msmarco_doc_09_1282503587", "msmarco_doc_14_800591714"),
      1000412, Set.of("msmarco_doc_21_243726663", "msmarco_doc_21_243753677")
    );
  }
  
  private Map<Integer, Set<String>> dummyTopicsV1() {
    return Map.of(
      1090311, Set.of("D99747", "D99748")
    );
  }

  private RelevanceJudgments relevanceJudgments() {
    RelevanceJudgments relevanceJudgments = Mockito.mock(RelevanceJudgments.class);
    Mockito.when(relevanceJudgments.getRelevanceGrade(Matchers.any(), Matchers.any())).thenAnswer(new Answer<Integer>() {
      @Override
      public Integer answer(InvocationOnMock invocation) throws Throwable {
        return Arrays.asList("msmarco_doc_09_1282503587", "msmarco_doc_21_243753677", "D99747").contains(invocation.getArguments()[1]) ? 1 : 0;
      }
    });
	Mockito.when(relevanceJudgments.isDocJudged(Matchers.any(), Matchers.any())).thenReturn(true);
    
    return relevanceJudgments;
  }
}
