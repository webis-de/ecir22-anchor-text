package de.webis.trec_dl_21_ltr.parsing;

import java.util.Map;
import java.util.Set;

import org.approvaltests.Approvals;
import org.junit.Test;

import de.webis.trec_dl_21_ltr.feature_extraction.FeatureExtractionArguments;
import de.webis.trec_dl_21_ltr.feature_extraction.FeatureExtractor;

public class FeatureExtractorIntegrationTest {
  
  @Test
  public void approveWithQueryFromTitleIndex() {
    //{"id":"msmarco_doc_00_4840315","contents":"Plants and Animals of the Everglades"}
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "Plants and Animals of the Everglades"),
      Map.of(-1, Set.of("msmarco_doc_00_4840315"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("msmarco_doc_00_4840315"), msMarcoV2Args()));
  }
  
  @Test
  public void approveV1DocWithQueryFromTitleIndex() {
    //{"id":"D1885729","contents":"How to Kill Weeds Without Killing Plants"}
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "How to Kill Weeds Without Killing Plants"),
      Map.of(-1, Set.of("D1885729"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("D1885729"), msMarcoV1Args()));
  }
  
  @Test
  public void approveWithQueryFromUrlIndex() {
    //{"id":"msmarco_doc_00_178958616","contents":"http://airportchandigarh.com/transport/chandigarh-volvo-bus-schedules"}
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "http://airportchandigarh.com/transport/chandigarh-volvo-bus-schedules"),
      Map.of(-1, Set.of("msmarco_doc_00_178958616"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("msmarco_doc_00_178958616"), msMarcoV2Args()));
  }
  
  @Test
  public void approveWithQueryFromAnchorIndex() {
    //{"id":"msmarco_doc_17_2981346117","contents":"Join Together (song) The Who reference Join Together"}
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "Join Together (song) The Who reference Join Together"),
      Map.of(-1, Set.of("msmarco_doc_17_2981346117"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("msmarco_doc_17_2981346117"), msMarcoV2Args()));
  }
  
  @Test
  public void approveWithV1DocQueryFromAnchorIndex() {
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "Erstellt mit Storefront & WooCommerce"),
      Map.of(-1, Set.of("D2909755"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("D2909755"), msMarcoV1Args()));
  }
  
  @Test
  public void approveWithQueryFromRunFile() {
    // 257309 Q0 msmarco_doc_01_1995043585 1 15.637100 Anserini
    // 257309 how long does it take to get your bsrn if you already have a bachelors degree
    FeatureExtractor extractor = new FeatureExtractor(
      Map.of(-1, "how long does it take to get your bsrn if you already have a bachelors degree"),
      Map.of(-1, Set.of("msmarco_doc_17_2981346117"))
    );

    Approvals.verify(extractor.calculateFeatures(-1, Set.of("msmarco_doc_17_2981346117"), msMarcoV2Args()));
  }
  
  private static FeatureExtractionArguments msMarcoV1Args() {
    return FeatureExtractionArguments.builder()
      .indexDir("/anserini-indexes")
      .fieldToIndex(FeatureExtractionArguments.msMarcoV1Features())
      .build();
  }
  
  private static FeatureExtractionArguments msMarcoV2Args() {
    return FeatureExtractionArguments.builder()
      .indexDir("/anserini-indexes")
      .fieldToIndex(FeatureExtractionArguments.msMarcoV2Features())
      .build();
  }
}
