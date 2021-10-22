package de.webis.trec_dl_21_ltr.parsing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class RunFileDocumentExtractorTest {

  @Test
  public void testTopic112FromSmallSample() {
    Map<Integer, Set<String>> topicToDocs = RunFileDocumentExtractor.parseRunFile("src/test/resources/small-run-file.txt");
    Set<String> expected = new HashSet<>(Arrays.asList("msmarco_doc_00_1", "msmarco_doc_00_2"));
    
    Assert.assertEquals(2, topicToDocs.size());
    Assert.assertEquals(expected, topicToDocs.get(112));
  }
  
  @Test
  public void testTopic111FromSmallSample() {
    Map<Integer, Set<String>> topicToDocs = RunFileDocumentExtractor.parseRunFile("src/test/resources/small-run-file.txt");
    Set<String> expected = new HashSet<>(Arrays.asList("msmarco_doc_00_1", "msmarco_doc_00_3", "msmarco_doc_00_4", "msmarco_doc_00_5"));
    
    Assert.assertEquals(2, topicToDocs.size());
    Assert.assertEquals(expected, topicToDocs.get(111));
  }

  @Test
  public void testTopic916917From2021RunFile() {
    Map<Integer, Set<String>> topicToDocs = RunFileDocumentExtractor.parseRunFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/2021_document_top100.txt.gz");
    Set<String> expected = new HashSet<>(Arrays.asList("msmarco_doc_00_453601986", "msmarco_doc_00_805860302", "msmarco_doc_00_933892870", "msmarco_doc_01_1322156182", "msmarco_doc_01_1322175547", "msmarco_doc_01_1361399595", "msmarco_doc_01_1365284950", "msmarco_doc_03_1578934545", "msmarco_doc_03_1585003109", "msmarco_doc_03_1587195726", "msmarco_doc_03_1688699654", "msmarco_doc_03_1805220658", "msmarco_doc_03_473343149", "msmarco_doc_04_1249803918", "msmarco_doc_04_1249821750", "msmarco_doc_04_1249856845", "msmarco_doc_04_1250156436", "msmarco_doc_04_1250249744", "msmarco_doc_04_1250272741", "msmarco_doc_04_1250385082", "msmarco_doc_04_190752351", "msmarco_doc_04_31130565", "msmarco_doc_07_1449744251", "msmarco_doc_08_1138778862", "msmarco_doc_08_1174496509", "msmarco_doc_09_1794500299", "msmarco_doc_09_977979959", "msmarco_doc_09_981860694", "msmarco_doc_11_692819739", "msmarco_doc_11_821388039", "msmarco_doc_12_1304109390", "msmarco_doc_12_1455239449", "msmarco_doc_12_878611480", "msmarco_doc_14_1622204414", "msmarco_doc_14_287756243", "msmarco_doc_15_1370447083", "msmarco_doc_15_1371447653", "msmarco_doc_15_1602754737", "msmarco_doc_15_1737830352", "msmarco_doc_15_1919864937", "msmarco_doc_19_2320831329", "msmarco_doc_20_222728845", "msmarco_doc_21_447895957", "msmarco_doc_21_447915322", "msmarco_doc_21_806911146", "msmarco_doc_21_80967330", "msmarco_doc_21_825383873", "msmarco_doc_21_826255502", "msmarco_doc_21_828553976", "msmarco_doc_21_831135652", "msmarco_doc_21_837133280", "msmarco_doc_22_948345270", "msmarco_doc_23_550000135", "msmarco_doc_25_1052445072", "msmarco_doc_25_1076301062", "msmarco_doc_25_1858305587", "msmarco_doc_26_1443220812", "msmarco_doc_26_1443398306", "msmarco_doc_26_2001191136", "msmarco_doc_26_4911423", "msmarco_doc_26_670445678", "msmarco_doc_27_1030474275", "msmarco_doc_28_1165769108", "msmarco_doc_28_582905805", "msmarco_doc_29_1277341054", "msmarco_doc_29_1277352954", "msmarco_doc_29_1459819885", "msmarco_doc_29_793751009", "msmarco_doc_30_292559668", "msmarco_doc_30_660907637", "msmarco_doc_31_1440367978", "msmarco_doc_31_945908142", "msmarco_doc_33_358479806", "msmarco_doc_34_79425212", "msmarco_doc_35_494633561", "msmarco_doc_35_713009434", "msmarco_doc_38_1324888115", "msmarco_doc_38_496629112", "msmarco_doc_42_1006194887", "msmarco_doc_42_1449488963", "msmarco_doc_42_874061239", "msmarco_doc_44_2001428930", "msmarco_doc_45_1346522588", "msmarco_doc_46_887801437", "msmarco_doc_46_887814730", "msmarco_doc_48_1366333044", "msmarco_doc_49_1096189100", "msmarco_doc_49_1472931301", "msmarco_doc_50_1724124410", "msmarco_doc_50_609384313", "msmarco_doc_50_609764830", "msmarco_doc_50_826702971", "msmarco_doc_50_884038782", "msmarco_doc_52_1177914486", "msmarco_doc_53_1270896581", "msmarco_doc_53_597334212", "msmarco_doc_55_723488533", "msmarco_doc_55_791975508", "msmarco_doc_57_1123225422", "msmarco_doc_59_872957537"));
    
    Assert.assertEquals(477, topicToDocs.size());
    Assert.assertEquals(expected, topicToDocs.get(916917));
  }
  
  @Test
  public void testSampleMarciV1RunLine() {
    String input = "1049519 Q0 D3466 1 -5.57078 IndriQueryLikelihood";
    Pair<Integer, String> actual = RunFileDocumentExtractor.extractTopicAndMarcoDocOrFail(input);
    
    Assert.assertEquals(Integer.valueOf(1049519), actual.getKey());
    Assert.assertEquals("D3466", actual.getValue());
  }
  
  @Test(expected = RuntimeException.class)
  public void testUnknownDocumentIdFails() {
    String input = "1049519 Q0 3466 1 -5.57078 IndriQueryLikelihood";
    RunFileDocumentExtractor.extractTopicAndMarcoDocOrFail(input);
  }
}
