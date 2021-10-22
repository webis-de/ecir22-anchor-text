import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.http.HttpHost;
import org.apache.log4j.BasicConfigurator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;

import anchor.AnchorElement;
import filters.StopAnchorsFilter;
import index.Importer;

public class MainTest {
	/*
	@Test
	public void testImporter() throws IOException {
    	SparkConf sparkConf = new SparkConf().setAppName("SparkAnchors").setMaster("local").set("spark.executor.memory","2g");
    	JavaSparkContext sc = new JavaSparkContext(sparkConf);
    	
    	List<AnchorElement> examples = new ArrayList<>();
    	examples.add(new AnchorElement("Example anchor 1", "https://www.example.com", null));
    	examples.add(new AnchorElement("Example anchor[³¼¹²$§(/%# 2 ö ä ü ß", "https://www.example.com", null));
    	
    	JavaRDD<AnchorElement> anchors = sc.parallelize(examples);
    	
    	Importer.indexAnchors(anchors, "testanchors");
    	sc.close();
    	
    	BasicConfigurator.configure();
    	RestHighLevelClient client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http")));
    	
		String messageActual0 = getTestMessage("testanchors", "1", client);
		String messageActual1 = getTestMessage("testanchors", "2", client);
		
		Assert.assertEquals("{\"anchor\":\"Example anchor 1\",\"url\":\"https://www.example.com\",\"header\":\"header is null!\"}", messageActual0);
		Assert.assertEquals("{\"anchor\":\"Example anchor[³¼¹²$§(/%# 2 ö ä ü ß\",\"url\":\"https://www.example.com\",\"header\":\"header is null!\"}", messageActual1);
    	
	}
	@Test
	public void testStopFilter() {
		AnchorElement element1 = new AnchorElement("Example anchor 1", "https://www.example.com", null);
		AnchorElement element2 = new AnchorElement("", "https://www.example.com", null);
		AnchorElement element3 = new AnchorElement("    ", "https://www.example.com", null);
		AnchorElement element4 = new AnchorElement("Exammple anchor with click stop word ", "https://www.example.com", null);
		
		StopAnchorsFilter filter = new StopAnchorsFilter();
		
		Assert.assertEquals(true, filter.filterElement(element1));
		Assert.assertEquals(false, filter.filterElement(element2));
		Assert.assertEquals(false, filter.filterElement(element3));
		Assert.assertEquals(false, filter.filterElement(element4));
	}
	
	@Test
	public void testEnglishFilter() {
		AnchorElement element1 = new AnchorElement("Example anchor 1", "https://www.example.com", new WarcHeaderCustom("","","clueweb09-en0039-05-00001",""));
		AnchorElement element2 = new AnchorElement("Example anchor 2", "https://www.example.com", new WarcHeaderCustom("","","clueweb09-de0039-05-00001",""));
		AnchorElement element3 = new AnchorElement("Example anchor 3", "https://www.example.com", new WarcHeaderCustom("","","clueweb09",""));
		AnchorElement element4 = new AnchorElement("Example anchor 4", "https://www.example.com", new WarcHeaderCustom("","","",""));
		
		EnglishFilter filter = new EnglishFilter();
		
		Assert.assertEquals(true, filter.filterElement(element1));
		Assert.assertEquals(false, filter.filterElement(element2));
		Assert.assertEquals(false, filter.filterElement(element3));
		Assert.assertEquals(false, filter.filterElement(element4));
	}
	
	@Test
	public void testProcessSpecialCharacters() {
		AnchorElement element1 = new AnchorElement("Example anchor 1", "https://www.example.com", new WarcHeaderCustom("","","clueweb09-en0039-05-00001",""));
		AnchorElement element2 = new AnchorElement("Example anchor[³¼¹²$§(/%# 2 ö ä ü ß", "https://www.example.com", null);
		
		ProcessSpecialChars preprocessor = new ProcessSpecialChars();
		
		Assert.assertEquals("example anchor 1", preprocessor.processElement(element1).getAnchor());
		Assert.assertEquals("example anchor 2 ö ä ü ß", preprocessor.processElement(element2).getAnchor());

	}
	
	private String getTestMessage(String index, String id, RestHighLevelClient client) throws IOException {
		GetRequest getRequest = new GetRequest(
				index, 
				id);
		
		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		
		return getResponse.getSourceAsString();
	}
	*/
}
