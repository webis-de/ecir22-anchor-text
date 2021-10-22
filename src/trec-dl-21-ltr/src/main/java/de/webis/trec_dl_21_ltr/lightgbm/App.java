package de.webis.trec_dl_21_ltr.lightgbm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import de.webis.trec_dl_21_ltr.parsing.RunFileDocumentExtractor;
import io.anserini.eval.RelevanceJudgments;

public class App {
  public static List<String> FEATURES = Arrays.asList("anchor-tf-similarity", "anchor-f2log-similarity", "anchor-f2exp-similarity", "anchor-tf-idf-similarity", "anchor-spl-similarity", "anchor-ql-similarity", "anchor-bm25-similarity", "anchor-pl2-similarity", "anchor-qljm-similarity", "body-qljm-similarity", "body-bm25-similarity", "body-f2exp-similarity", "body-ql-similarity", "body-f2log-similarity", "body-pl2-similarity", "body-tf-similarity", "body-spl-similarity", "body-tf-idf-similarity", "url-ql-similarity", "url-bm25-similarity", "url-tf-similarity", "url-spl-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-qljm-similarity", "url-pl2-similarity", "url-f2exp-similarity", "title-tf-similarity", "title-bm25-similarity", "title-ql-similarity", "title-pl2-similarity", "title-qljm-similarity", "title-tf-idf-similarity", "title-spl-similarity", "title-f2exp-similarity", "title-f2log-similarity", "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank", "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities", "feature_query_is_comparative");
  public static List<String> FEATURES_NO_ANCHOR = Arrays.asList("body-qljm-similarity", "body-bm25-similarity", "body-f2exp-similarity", "body-ql-similarity", "body-f2log-similarity", "body-pl2-similarity", "body-tf-similarity", "body-spl-similarity", "body-tf-idf-similarity", "url-ql-similarity", "url-bm25-similarity", "url-tf-similarity", "url-spl-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-qljm-similarity", "url-pl2-similarity", "url-f2exp-similarity", "title-tf-similarity", "title-bm25-similarity", "title-ql-similarity", "title-pl2-similarity", "title-qljm-similarity", "title-tf-idf-similarity", "title-spl-similarity", "title-f2exp-similarity", "title-f2log-similarity", "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank", "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities", "feature_query_is_comparative");

  public static List<String> FEATURES_V1 = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity", "url-bm25-similarity", "url-f2exp-similarity", "url-spl-similarity", "url-pl2-similarity", "url-ql-similarity", "url-qljm-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-tf-similarity", "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity", "anchor-16-07-bm25-similarity", "anchor-16-07-f2exp-similarity", "anchor-16-07-spl-similarity", "anchor-16-07-pl2-similarity", "anchor-16-07-ql-similarity", "anchor-16-07-qljm-similarity", "anchor-16-07-f2log-similarity", "anchor-16-07-tf-idf-similarity", "anchor-16-07-tf-similarity", "anchor-17-04-bm25-similarity", "anchor-17-04-f2exp-similarity", "anchor-17-04-spl-similarity", "anchor-17-04-pl2-similarity", "anchor-17-04-ql-similarity", "anchor-17-04-qljm-similarity", "anchor-17-04-f2log-similarity", "anchor-17-04-tf-idf-similarity", "anchor-17-04-tf-similarity", "anchor-18-13-bm25-similarity", "anchor-18-13-f2exp-similarity", "anchor-18-13-spl-similarity", "anchor-18-13-pl2-similarity", "anchor-18-13-ql-similarity", "anchor-18-13-qljm-similarity", "anchor-18-13-f2log-similarity", "anchor-18-13-tf-idf-similarity", "anchor-18-13-tf-similarity", "anchor-19-47-bm25-similarity", "anchor-19-47-f2exp-similarity", "anchor-19-47-spl-similarity", "anchor-19-47-pl2-similarity", "anchor-19-47-ql-similarity", "anchor-19-47-qljm-similarity", "anchor-19-47-f2log-similarity", "anchor-19-47-tf-idf-similarity", "anchor-19-47-tf-similarity", "anchor-20-05-bm25-similarity", "anchor-20-05-f2exp-similarity", "anchor-20-05-spl-similarity", "anchor-20-05-pl2-similarity", "anchor-20-05-ql-similarity", "anchor-20-05-qljm-similarity", "anchor-20-05-f2log-similarity", "anchor-20-05-tf-idf-similarity", "anchor-20-05-tf-similarity", "anchor-21-04-bm25-similarity", "anchor-21-04-f2exp-similarity", "anchor-21-04-spl-similarity", "anchor-21-04-pl2-similarity", "anchor-21-04-ql-similarity", "anchor-21-04-qljm-similarity", "anchor-21-04-f2log-similarity", "anchor-21-04-tf-idf-similarity", "anchor-21-04-tf-similarity", "anchor-comb-bm25-similarity", "anchor-comb-f2exp-similarity", "anchor-comb-spl-similarity", "anchor-comb-pl2-similarity", "anchor-comb-ql-similarity", "anchor-comb-qljm-similarity", "anchor-comb-f2log-similarity", "anchor-comb-tf-idf-similarity", "anchor-comb-tf-similarity", "anchor-comb-no-16-bm25-similarity", "anchor-comb-no-16-f2exp-similarity", "anchor-comb-no-16-spl-similarity", "anchor-comb-no-16-pl2-similarity", "anchor-comb-no-16-ql-similarity", "anchor-comb-no-16-qljm-similarity", "anchor-comb-no-16-f2log-similarity", "anchor-comb-no-16-tf-idf-similarity", "anchor-comb-no-16-tf-similarity", "orcas-bm25-similarity", "orcas-f2exp-similarity", "orcas-spl-similarity", "orcas-pl2-similarity", "orcas-ql-similarity", "orcas-qljm-similarity", "orcas-f2log-similarity", "orcas-tf-idf-similarity", "orcas-tf-similarity", 
    "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities",
    "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank"
  );
  
  public static List<String> FEATURES_V1_NO_ANCHOR = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity", "url-bm25-similarity", "url-f2exp-similarity", "url-spl-similarity", "url-pl2-similarity", "url-ql-similarity", "url-qljm-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-tf-similarity", "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity", "orcas-bm25-similarity", "orcas-f2exp-similarity", "orcas-spl-similarity", "orcas-pl2-similarity", "orcas-ql-similarity", "orcas-qljm-similarity", "orcas-f2log-similarity", "orcas-tf-idf-similarity", "orcas-tf-similarity", 
    "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities",
    "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank"
  );
  
  public static List<String> FEATURES_V1_NO_ORCAS = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity", "url-bm25-similarity", "url-f2exp-similarity", "url-spl-similarity", "url-pl2-similarity", "url-ql-similarity", "url-qljm-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-tf-similarity", "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity", "anchor-16-07-bm25-similarity", "anchor-16-07-f2exp-similarity", "anchor-16-07-spl-similarity", "anchor-16-07-pl2-similarity", "anchor-16-07-ql-similarity", "anchor-16-07-qljm-similarity", "anchor-16-07-f2log-similarity", "anchor-16-07-tf-idf-similarity", "anchor-16-07-tf-similarity", "anchor-17-04-bm25-similarity", "anchor-17-04-f2exp-similarity", "anchor-17-04-spl-similarity", "anchor-17-04-pl2-similarity", "anchor-17-04-ql-similarity", "anchor-17-04-qljm-similarity", "anchor-17-04-f2log-similarity", "anchor-17-04-tf-idf-similarity", "anchor-17-04-tf-similarity", "anchor-18-13-bm25-similarity", "anchor-18-13-f2exp-similarity", "anchor-18-13-spl-similarity", "anchor-18-13-pl2-similarity", "anchor-18-13-ql-similarity", "anchor-18-13-qljm-similarity", "anchor-18-13-f2log-similarity", "anchor-18-13-tf-idf-similarity", "anchor-18-13-tf-similarity", "anchor-19-47-bm25-similarity", "anchor-19-47-f2exp-similarity", "anchor-19-47-spl-similarity", "anchor-19-47-pl2-similarity", "anchor-19-47-ql-similarity", "anchor-19-47-qljm-similarity", "anchor-19-47-f2log-similarity", "anchor-19-47-tf-idf-similarity", "anchor-19-47-tf-similarity", "anchor-20-05-bm25-similarity", "anchor-20-05-f2exp-similarity", "anchor-20-05-spl-similarity", "anchor-20-05-pl2-similarity", "anchor-20-05-ql-similarity", "anchor-20-05-qljm-similarity", "anchor-20-05-f2log-similarity", "anchor-20-05-tf-idf-similarity", "anchor-20-05-tf-similarity", "anchor-21-04-bm25-similarity", "anchor-21-04-f2exp-similarity", "anchor-21-04-spl-similarity", "anchor-21-04-pl2-similarity", "anchor-21-04-ql-similarity", "anchor-21-04-qljm-similarity", "anchor-21-04-f2log-similarity", "anchor-21-04-tf-idf-similarity", "anchor-21-04-tf-similarity", "anchor-comb-bm25-similarity", "anchor-comb-f2exp-similarity", "anchor-comb-spl-similarity", "anchor-comb-pl2-similarity", "anchor-comb-ql-similarity", "anchor-comb-qljm-similarity", "anchor-comb-f2log-similarity", "anchor-comb-tf-idf-similarity", "anchor-comb-tf-similarity", "anchor-comb-no-16-bm25-similarity", "anchor-comb-no-16-f2exp-similarity", "anchor-comb-no-16-spl-similarity", "anchor-comb-no-16-pl2-similarity", "anchor-comb-no-16-ql-similarity", "anchor-comb-no-16-qljm-similarity", "anchor-comb-no-16-f2log-similarity", "anchor-comb-no-16-tf-idf-similarity", "anchor-comb-no-16-tf-similarity",
    "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities",
    "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank"
  );
  
  public static List<String> FEATURES_V1_NO_ANCHOR_NO_ORCAS = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity", "url-bm25-similarity", "url-f2exp-similarity", "url-spl-similarity", "url-pl2-similarity", "url-ql-similarity", "url-qljm-similarity", "url-f2log-similarity", "url-tf-idf-similarity", "url-tf-similarity", "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity",
    "feature_is_5w1h_query", "feature_query_length_in_tokens", "feature_gpe_entities", "feature_org_entities", "feature_person_entities",
    "feature_url_length", "feature_slashes_in_url", "feature_dots_in_host", "feature_body_length", "feature_title_length", "feature_pagerank", "feature_harmonic_mean", "feature_alexa_rank"
  );
  
  
  public static List<String> ECIR_NAV_FEATURES_V1= Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity",
    "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity", 
    "anchor-comb-bm25-similarity", "anchor-comb-f2exp-similarity", "anchor-comb-spl-similarity", "anchor-comb-pl2-similarity", "anchor-comb-ql-similarity", "anchor-comb-qljm-similarity", "anchor-comb-f2log-similarity", "anchor-comb-tf-idf-similarity", "anchor-comb-tf-similarity", 
    "orcas-bm25-similarity", "orcas-f2exp-similarity", "orcas-spl-similarity", "orcas-pl2-similarity", "orcas-ql-similarity", "orcas-qljm-similarity", "orcas-f2log-similarity", "orcas-tf-idf-similarity", "orcas-tf-similarity"
  );
  
  public static List<String> ECIR_NAV_FEATURES_V1_NO_ORCAS= Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity",
    "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity", 
    "anchor-comb-bm25-similarity", "anchor-comb-f2exp-similarity", "anchor-comb-spl-similarity", "anchor-comb-pl2-similarity", "anchor-comb-ql-similarity", "anchor-comb-qljm-similarity", "anchor-comb-f2log-similarity", "anchor-comb-tf-idf-similarity", "anchor-comb-tf-similarity"
  );
  
  public static List<String> ECIR_NAV_FEATURES_V1_NO_ANCHOR = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity",
    "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity",  
    "orcas-bm25-similarity", "orcas-f2exp-similarity", "orcas-spl-similarity", "orcas-pl2-similarity", "orcas-ql-similarity", "orcas-qljm-similarity", "orcas-f2log-similarity", "orcas-tf-idf-similarity", "orcas-tf-similarity"
  );
  
  public static List<String> ECIR_NAV_FEATURES_V1_NO_ANCHOR_NO_ORCAS = Arrays.asList(
    "body-bm25-similarity", "body-f2exp-similarity", "body-spl-similarity", "body-pl2-similarity", "body-ql-similarity", "body-qljm-similarity", "body-f2log-similarity", "body-tf-idf-similarity", "body-tf-similarity",
    "title-bm25-similarity", "title-f2exp-similarity", "title-spl-similarity", "title-pl2-similarity", "title-ql-similarity", "title-qljm-similarity", "title-f2log-similarity", "title-tf-idf-similarity", "title-tf-similarity"
  );
  
  public static void main(String[] args) {
    if(args[0].equals("v2")) {
      runV2();
    } else if(args[0].equals("v2-no-anchor")) {
      runV2NoAnchor();
    } else if (args[0].equals("ecir22-nav-v1")) {
      runV1(App.ECIR_NAV_FEATURES_V1, "ecir22-all-36-features");
      runV1EcirTopics(App.ECIR_NAV_FEATURES_V1, "ecir22-all-36-features");
    } else if (args[0].equals("ecir22-nav-v1-no-anchor")) {
      runV1(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR, "ecir22-no-anchor-features");
      runV1EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR, "ecir22-no-anchor-features");
    } else if (args[0].equals("ecir22-nav-v1-no-orcas")) {
      runV1(App.ECIR_NAV_FEATURES_V1_NO_ORCAS, "ecir22-no-orcas-features");
      runV1EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ORCAS, "ecir22-no-orcas-features");
    } else if (args[0].equals("ecir22-nav-v1-no-anchor-no-orcas")) {
      runV1(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR_NO_ORCAS, "ecir22-no-anchor-no-orcas-features");
      runV1EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR_NO_ORCAS, "ecir22-no-anchor-no-orcas-features");
    } else if (args[0].equals("ecir22-nav-v2")) {
      runV2EcirTopics(App.ECIR_NAV_FEATURES_V1, "ecir22-all-36-features");
    } else if (args[0].equals("ecir22-nav-v2-no-anchor")) {
      runV2EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR, "ecir22-no-anchor-features");
    } else if (args[0].equals("ecir22-nav-v2-no-orcas")) {
      runV2EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ORCAS, "ecir22-no-orcas-features");
    } else if (args[0].equals("ecir22-nav-v2-no-anchor-no-orcas")) {
      runV2EcirTopics(App.ECIR_NAV_FEATURES_V1_NO_ANCHOR_NO_ORCAS, "ecir22-no-anchor-no-orcas-features");
    } else if(args[0].equals("v1")) {
      runV1(App.FEATURES_V1, "all-121-features");
    } else if(args[0].equals("v1-no-anchor")) {
      runV1(App.FEATURES_V1_NO_ANCHOR, "no-anchor");
    } else if(args[0].equals("v1-no-orcas")) {
      runV1(App.FEATURES_V1_NO_ORCAS, "no-orcas");
    } else if(args[0].equals("v1-no-anchor-no-orcas")) {
      runV1(App.FEATURES_V1_NO_ANCHOR_NO_ORCAS, "no-anchor-no-orcas");
    } else {
      throw new RuntimeException("");
    }
  }
  
  private static void runV2() {
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
      .documentFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/document-features/ms-marco-document-features.jsonl")
	  .queryFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/query-features/ms-marco-query-features.jsonl")
	  .queryDocumentFeaturesDirectory("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/feature-extraction-in-progress/")
	  .features(App.FEATURES)
	  .lightgbmConfig(conf)
	  .relevanceJudgments(relevanceJudgments())
	  .trainingTopics(topics("docv2_train_top100.txt.gz"))
	  .validationTopics(Arrays.asList(topics("docv2_dev_top100.txt.gz"), topics("docv2_dev2_top100.txt.gz")))
	  .testTopics(topics("2021_document_top100.txt.gz"))
	  .build();

	  extract.buildLightGbmDir("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/all-50-features");
  }

  private static void runV1(List<String> features, String suffix) {
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
      .documentFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-document-features/ms-marco-v1-document-features.jsonl")
      .queryFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-query-features/ms-marco-v1-query-features.jsonl")
      .queryDocumentFeaturesDirectory("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-in-progress/")
      .features(features)
      .lightgbmConfig(conf)
      .relevanceJudgments(relevanceJudgmentsV1())
      .trainingTopics(topicsV1("msmarco-doctrain-top100.gz"))
      .validationTopics(Arrays.asList(topicsV1("msmarco-docdev-top100.gz")))
      .testTopics(topicsV1("msmarco-doctest2020-top100.gz"))
      .build();

    extract.buildLightGbmDir("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/marco-v1-" + suffix);
  }
  
  private static void runV1EcirTopics(List<String> features, String suffix) {
    List<Pair<String, String>> topics = Arrays.asList(
      Pair.of("dl19+20", "dl"),
      Pair.of("entrypage-random", "nav-random"),
      Pair.of("entrypage-popular", "nav-popular")
    );

    for(Pair<String, String> topic: topics) {
      LightGBMConfiguration conf = new LightGBMConfiguration();
      LightGBMExtraction extract = LightGBMExtraction.builder()
        .queryDocumentFeaturesDirectory("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-ecir22/" + topic.getRight())
        .features(features)
        .lightgbmConfig(conf)
        .testTopics(RunFileDocumentExtractor.parseRunFile("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/" + topic.getLeft() + "/run.ms-marco-content.bm25-default.txt"))
        .build();

      extract.buildLightGbmDir("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/marco-v1-" + suffix +"/" + topic.getLeft());
    }
  }
  
  private static void runV2EcirTopics(List<String> features, String suffix) {
    List<Pair<String, String>> topics = Arrays.asList(
      Pair.of("entrypage-random", "nav-random"),
      Pair.of("entrypage-popular", "nav-popular")
    );

    for(Pair<String, String> topic: topics) {
      LightGBMConfiguration conf = new LightGBMConfiguration();
      LightGBMExtraction extract = LightGBMExtraction.builder()
        .queryDocumentFeaturesDirectory("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v2-feature-extraction-ecir22/" + topic.getRight())
        .features(features)
        .lightgbmConfig(conf)
        .testTopics(RunFileDocumentExtractor.parseRunFile("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-marco-v2-ecir22/" + topic.getLeft() + "/run.msmarco-doc-v2.bm25-default.txt"))
        .build();

      extract.buildLightGbmDir("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/marco-v2-" + suffix +"/" + topic.getLeft());
    }
  }
  
  private static void runV2NoAnchor() {
    LightGBMConfiguration conf = new LightGBMConfiguration();
    LightGBMExtraction extract = LightGBMExtraction.builder()
      .documentFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/document-features/ms-marco-document-features.jsonl")
      .queryFeaturesFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/query-features/ms-marco-query-features.jsonl")
      .queryDocumentFeaturesDirectory("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/feature-extraction-in-progress/")
      .features(App.FEATURES_NO_ANCHOR)
      .lightgbmConfig(conf)
      .relevanceJudgments(relevanceJudgments())
      .trainingTopics(topics("docv2_train_top100.txt.gz"))
      .validationTopics(Arrays.asList(topics("docv2_dev_top100.txt.gz"), topics("docv2_dev2_top100.txt.gz")))
      .testTopics(topics("2021_document_top100.txt.gz"))
      .build();

    extract.buildLightGbmDir("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/2-no-anchor");
  }
  
  private static Map<Integer, Set<String>> topics(String file) {
	return RunFileDocumentExtractor.parseRunFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/" + file);
  }
  
  private static Map<Integer, Set<String>> topicsV1(String file) {
	return RunFileDocumentExtractor.parseRunFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources-trec-dl-20/" + file);
  }

  private static RelevanceJudgments relevanceJudgments() {
    return new RelevanceJudgments("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/all-qrels-combined.tsv");
  }
  
  private static RelevanceJudgments relevanceJudgmentsV1() {
    return new RelevanceJudgments("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources-trec-dl-20/all-qrels-combined.tsv");
  }
}
