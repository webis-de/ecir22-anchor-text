package warc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import anchor.AnchorContextExtractor;
import anchor.AnchorElement;
import anchor.NaughtyWordsExtractor;
import filters.Filter;
import filters.Filter.Filters;
import filters.ImproperTextFilter;
import filters.OnlyInternalURIsFilter;
import filters.SrcTargetDomainFilter;
import filters.StopAnchorsFilter;
import de.webis.chatnoir2.mapfile_generator.inputformats.WarcInputFormat;
import de.webis.chatnoir2.mapfile_generator.warc.WarcRecord;

import webis.WARCParsingUtil;

public class Crawler {
    /*
     * crawls a warc file for it's anchor texts and indexes them using Importer.indexAnchors()
     */
	
	private static Logger LOGGER = LoggerFactory.getLogger(Crawler.class);
    
	private static final int CONTEXT_SIZE_IN_CHARACTERS = 0;
	
    public static void crawlWarc(String warcFile, String output, Class<? extends WarcInputFormat> format, String keepLinksPath, String cutOffDomainsPath, String NaughtyWordsPath) throws IOException{
    	try (JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName(warcFile))) {
	    	JavaRDD<WarcRecord> warcRecords = WARCParsingUtil.records(sc, warcFile, format).values();
	    	
	    	OnlyInternalURIsFilter internalUrisFilter = OnlyInternalURIsFilter.productionFilter(cutOffDomainsPath, keepLinksPath);
	    	NaughtyWordsExtractor naughtyWordsExtractor = new NaughtyWordsExtractor(NaughtyWordsPath);
	    	
	    	Filters filters = new Filters(	  internalUrisFilter
	    									, new ImproperTextFilter()
	    									, new SrcTargetDomainFilter()
	    									, new StopAnchorsFilter()
	    									);
	    	
	    	//extracting href and anchor using custom anchor class:
	    	JavaRDD<String> anchor = warcRecords.flatMap(r -> anchorElementsInRecord(r, filters, internalUrisFilter, naughtyWordsExtractor, CONTEXT_SIZE_IN_CHARACTERS));
	    	anchor = anchor.filter(i -> i != null);
	    	anchor = anchor.repartition(100);
	    	
			anchor.saveAsTextFile(output, GzipCodec.class);
    	}
    }
    
    
    
    static Iterator<String> anchorElementsInRecord(WarcRecord record
    													, Filter.Filters filters
    													, OnlyInternalURIsFilter internalUrisFilter // bad style?
    													, NaughtyWordsExtractor naughtyWordsExtractor
    													, int contextSizeInCharacters) {
    	if (record == null) {
    		LOGGER.warn("Unable to parse warc-record! record is null");
    		return Collections.emptyIterator();
    	}
    	
    	if (record.getHeader() == null) {
    		LOGGER.warn("Unable to parse warc-header! Header is null");
    		return Collections.emptyIterator();
    	}
    	
    	WarcHeaderCustom header = new WarcHeaderCustom(record.getHeader());
    	
    	
    	
    	
    	if (record.getContent() == null) {
    		LOGGER.warn("Unable to parse warc-record! record.getContent() is null");
    		return Collections.emptyIterator();
    	}
    	
    	if (header.getSrcUrl() == null) {
    		LOGGER.warn("Unable to parse warc-header! header.getTargetURI() is null");
    		return Collections.emptyIterator();
    	}
    	
    	Document doc;
    	
    	
    	try {
    		doc = Jsoup.parse(record.getContent(),header.getSrcUrl());
    	} catch(Exception e) {
    		boolean isContentNull = (record.getContent() == null);
    		boolean isHeaderNull = (header.getSrcUrl()== null);
    		
    		LOGGER.warn("Unable to parse warc-content or -header! isContentNull: "+isContentNull
    				+ ", isHeaderNull: " + isHeaderNull);
    		return Collections.emptyIterator();
    	}
    	
    	Elements links = doc.select("a[href]");
    	Map<Element, AnchorElement> linkToAnchorElement = new LinkedHashMap<>();
    	
    	for(Element link : links) {
    		String targetUrl = link.attr("abs:href");
    		
    		if(internalUrisFilter.filterElement(targetUrl) && SrcTargetDomainFilter.filterElement(targetUrl, header.getSrcUrl())) {
    		
	    		AnchorElement anchor = new AnchorElement(link.text(), targetUrl, header);
	    		
	    		if(filters.filterElement(anchor)) {
	    			linkToAnchorElement.put(link, anchor);
	    			
	    			if (internalUrisFilter != null)	anchor.setTargetMsMarcoDocIds(internalUrisFilter.getDocIds(anchor.getTargetUrl()));
	    		}
    		}
    	}
    	
    	if (naughtyWordsExtractor != null) header.setNaughtyWords(naughtyWordsExtractor.extractNaughtyWords(doc.text()));
    	
    	if(contextSizeInCharacters > 0) {
    		AnchorContextExtractor contextExctractor = new AnchorContextExtractor(linkToAnchorElement.keySet(), doc); 
    	
        	for(Map.Entry<Element, AnchorElement> i: linkToAnchorElement.entrySet()) {
        		Element link = i.getKey();
        		AnchorElement anchor = i.getValue();
        		anchor.setAnchorContext(contextExctractor.extractContext(link, contextSizeInCharacters));
        	}
    	}

    	return linkToAnchorElement.values().stream()
    		.map(i -> anchorToString(i))
    		.filter(i -> i != null)
    		.iterator();
    }
    
    private static String anchorToString(AnchorElement elem) {
    	try {
    		return elem.toString();
    	} catch(Exception e) {
    		return null;
    	}
    }
    
    @SuppressWarnings("rawtypes")
	public static void printToFile(List in, String filename) throws IOException {
    	//print output to file:
    	FileWriter writer = new FileWriter("outputTestFiles/"+filename);
    	for(int i = 0; i < in.size(); i++) {
    		writer.write(in.get(i).toString() + "\n\n");
    	}
    	writer.close();
    	System.out.println("Successfully wrote to the file.");
    }
}
