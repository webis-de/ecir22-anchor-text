package de.webis.trec_dl_21_ltr.parsing;

import org.junit.Assert;
import org.junit.Test;

public class QueryExtractorTest {
  @Test
  public void test2021Query() {
    String expected = "what is a nixie clerk";
    String actual = QueryExtractor.msMarcoV2Queries().idToQuery().get(1116388);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test2020Query() {
    String expected = "when did rock n roll begin?";
    String actual = QueryExtractor.msMarcoV1Queries().idToQuery().get(940547);
    
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testDevQuery() {
    String expected = "population of skagway alaska";
    String actual = QueryExtractor.msMarcoV2Queries().idToQuery().get(478220);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testV1DevQuery() {
    String expected = "sustained definition law";
    String actual = QueryExtractor.msMarcoV1Queries().idToQuery().get(1090311);
    
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testDev2Query() {
    String expected = "washington heights crime";
    String actual = QueryExtractor.msMarcoV2Queries().idToQuery().get(1088796);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testTrainQuery() {
    String expected = "what is the name of the energy that a mitochondrion produces?";
    String actual = QueryExtractor.msMarcoV2Queries().idToQuery().get(835091);
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testV1TrainQuery() {
    String expected = "how to do a poll on twitter";
    String actual = QueryExtractor.msMarcoV1Queries().idToQuery().get(355647);
    
    Assert.assertEquals(expected, actual);
  }  
}
