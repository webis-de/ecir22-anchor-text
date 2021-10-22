package index;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.log4j.BasicConfigurator;
import org.apache.spark.api.java.JavaRDD;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import anchor.AnchorElement;
import filters.Filter;
import preprocessors.Preprocessor;
import warc.Crawler;

public class Importer {
	/*
	 * class used to index anchor elements and import them into an elasticSearch database
	 */
	
	static void indexAnchors(JavaRDD<AnchorElement> anchor, String indexName) throws IOException {
		
		//converting anchors to json:
    	JavaRDD<String> json = anchor.map(s -> s.toString());	//output see jsonLinkTest.txt
    	List<String> jsonList = json.collect();
    	
    	//indexing links via ElasticSearch:
    	BasicConfigurator.configure();
    	RestHighLevelClient client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http")));
    	
    	IndexRequest request = new IndexRequest(indexName);
    	
    	IndexResponse indexResponse = null;
		for (int i = 0; i < jsonList.size(); i++) {
			
			request.id(Integer.toString(i+1));
			request.source(jsonList.get(i), XContentType.JSON);
			indexResponse = client.index(request, RequestOptions.DEFAULT);
			if (i % 100 == 0) System.out.println(indexResponse.getId());
		} 
    	
		/*// print test anchor
		GetRequest getRequest = new GetRequest(
				"anchors", 
				"146");
		
		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		String message = getResponse.getFields().toString();
		String index = getResponse.getIndex();
		String id = getResponse.getId();
			
		System.out.println(id + "\n" + index + "\n" + message);
		System.out.println(getResponse.getSourceAsString());
		System.out.println(getResponse.getSourceAsMap());
		System.out.println(getResponse.getType());
		*/
    	
    	client.close();
	}
	
	public static void indexAnchorsProcessed(JavaRDD<AnchorElement> anchor, String indexName, Filter.Filters filters, List<Preprocessor> preprocessors) throws IOException {
		JavaRDD<AnchorElement> filteredAnchors = anchor.filter(s -> filters.filterElement(s));
		
		JavaRDD<AnchorElement> processedAnchors = filteredAnchors;
		for(Preprocessor preprocessor : preprocessors) {
			processedAnchors = filteredAnchors.map(s -> preprocessor.processElement(s)); 
		}
		
		//indexAnchors(processedAnchors, indexName);
		Crawler.printToFile(processedAnchors.collect(), "processedTest2");
	}
	
}
