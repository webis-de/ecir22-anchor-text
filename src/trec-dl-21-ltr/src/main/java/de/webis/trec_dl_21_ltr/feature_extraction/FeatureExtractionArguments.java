package de.webis.trec_dl_21_ltr.feature_extraction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import de.webis.trec_dl_21_ltr.parsing.QueryExtractor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureExtractionArguments {
  private final String indexDir, outputDir, runFileDir;

  private final Map<String, String> fieldToIndex;
  
  private QueryExtractor queryExtractor;

  public Path outputFileForTopic(int topic) {
    return Paths.get(outputDir).resolve(topic + ".json");
  }

  public static Map<String, String> msMarcoV2Features() {
    Map<String, String> ret = new LinkedHashMap<>();
    ret.put("body", "msmarco-doc-v2");
    ret.put("orcas", "orcas-ms-marco-v2");
    ret.put("anchor-comb", "cc-union-16-to-21-anchortext-1000-v2");
    ret.put("url", "msmarco-document-v2-url-only.pos+docvectors+raw");
    ret.put("title", "msmarco-document-v2-title-only.pos+docvectors+raw");
    
    return ret;
  }
  
  public static Map<String, String> msMarcoV2FeaturesTrecDL21() {
    Map<String, String> ret = new LinkedHashMap<>();
    ret.put("body", "msmarco-doc-v2");
    ret.put("anchor", "cc-19-47-anchortext-v2");
    ret.put("url", "msmarco-document-v2-url-only.pos+docvectors+raw");
    ret.put("title", "msmarco-document-v2-title-only.pos+docvectors+raw");

    return ret;
  }
  
  public static Map<String, String> msMarcoV1Features() {
    Map<String, String> ret = new LinkedHashMap<>();
    ret.put("body", "ms-marco-content"); 
    ret.put("url", "msmarco-document-v1-url-only.pos+docvectors+raw");
    ret.put("title", "msmarco-document-v1-title-only.pos+docvectors+raw");
    ret.put("anchor-16-07", "cc-16-07-anchortext");
    ret.put("anchor-17-04", "cc-17-04-anchortext");
    ret.put("anchor-18-13", "cc-18-13-anchortext");
    ret.put("anchor-19-47", "cc-19-47-anchortext");
    ret.put("anchor-20-05", "cc-20-05-anchortext");
    ret.put("anchor-21-04", "cc-21-04-anchortext");
    ret.put("anchor-comb", "cc-combined-anchortext");
    ret.put("anchor-comb-no-16", "cc-combined-no16-anchortext");
    ret.put("orcas", "orcas");
    
    return ret;
  }
}
