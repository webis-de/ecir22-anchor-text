package analyzer;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import anchor.AnchorElement;
import filters.Filter;
import filters.ImproperTextFilter;
import filters.SrcTargetDomainFilter;
import filters.StopAnchorsFilter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import warc.WarcHeaderCustom;
import webis.Args;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CountFiltered {
 public static void main(String[] args) {
	 Namespace parsedArgs = validArgumentsOrNull(args);
		
		if(parsedArgs == null) {
			return;
		}
		countFiltered(parsedArgs.getString(Args.ARG_INPUT)
						, parsedArgs.getString("exactMatchUrls")
						, parsedArgs.getString(Args.ARG_OUTPUT)
						, parsedArgs.getString("cumulativeResults"));
 }
 
 private static void countFiltered(String input, String exactMatchUrlsPath, String output, String cumulativeResults) {
	 SparkSession spark = SparkSession
             .builder()
             .appName(input)
             //.master("local[*]") 	//for local tests
             .getOrCreate();
	 
	 
	 String jsonPath = input;
	 Dataset<Row> items = spark.read().json(jsonPath);
	 
	 System.out.println(items.first().schema());
	 System.out.println(items.first()); 
	 
	 List<Long> previous = readPreviousCounts(cumulativeResults);
	 
	 System.out.println(previous);
	 
	 long count = items.count() + previous.get(0); //eintraege insgesamt
	 
	 //einzeln:
	 	 
	 JavaRDD<Row> filtered;

	 
	 Filter stopFilter = new StopAnchorsFilter();
	 filtered = filterRdd(items.toJavaRDD(), stopFilter);
	 long countStopAnchors = filtered.count() + previous.get(1);
	 
	 Filter srcTargetFilter = new SrcTargetDomainFilter();
	 filtered = filterRdd(items.toJavaRDD(), srcTargetFilter);
	 long countSrcIsTarget = filtered.count() + previous.get(2);

	 Filter improper = new ImproperTextFilter();
	 filtered = filterRdd(items.toJavaRDD(), improper);
	 long countImproper = filtered.count() + previous.get(3);
	 
	 Filter noUrlAnchor = new ImproperTextFilter(true);
	 filtered = filterRdd(items.toJavaRDD(), noUrlAnchor);
	 long countNoUrlAnchor = filtered.count() + previous.get(4);
	 
	 //cumulative:
	 filtered = filterRdd(items.toJavaRDD(), stopFilter);
	 long countStopAnchorsCumu = filtered.count() + previous.get(5);
	 
	 filtered = filterRdd(filtered, srcTargetFilter);
	 long countSrcIsTargetCumu = filtered.count() + previous.get(5);
	 
	 filtered = filterRdd(filtered, improper);
	 long countImproperCumu = filtered.count() + previous.get(5);
	 
	 filtered = filterRdd(filtered, noUrlAnchor);
	 long countNoUrlAnchorCumu = filtered.count() + previous.get(6);
	 

	 
	 String cumulative = count + "\n" 
			 + countStopAnchors + "\n" 
			 + countSrcIsTarget + "\n" 
			 + countImproper  + "\n" 
			 + countNoUrlAnchor  + "\n" 
			 + countImproperCumu + "\n" 
			 + countNoUrlAnchorCumu;
	 


	 String out = "count: " + count + "\n"
	 			+ "\n"
				+ "after StopAnchors: " + countStopAnchors + "\n"
				+ "src is target: " + countSrcIsTarget + "\n"
				+ "after improper filter:" + countImproper + "\n"
				+ "after noUrlAnchors: " + countNoUrlAnchor + "\n"
				+ "\n"
				+ "cumulative:"
				+ "afterStopAnchors: " + countStopAnchorsCumu + "\n"
				+ "src is target: " + countSrcIsTargetCumu + "\n"
				+ "after improper filter:" + countImproperCumu + "\n"
				+ "\n"
				+ "after noUrlAnchors (extra): " + countNoUrlAnchorCumu + "\n"
				+ "";
	 
	 System.out.println(out);
	 
	 if(cumulativeResults != null) {
		 try {
			 File file = new File(cumulativeResults);
			 FileWriter myWriter = new FileWriter(file);
			 myWriter.write(cumulative);
			 myWriter.close();
			 System.out.println("Successfully wrote to the file.");
		 } catch (IOException e) {
			 System.out.println("An error occurred.");
			 e.printStackTrace();
		 }
	 }

	 spark.close();
 }

 private static JavaRDD<Row> filterRdd(JavaRDD<Row> rdd, Filter filter){
	 JavaRDD<Row> ret = rdd.filter(s -> filter.filterElement(new AnchorElement(
		 	  s.getAs("anchorText")
		 	, s.getAs("anchorContext")
		 	, s.getAs("targetUrl")
		 	, s.getList(3)
		 	, new WarcHeaderCustom(
		 			  s.getStruct(2).getAs("srcUrl")
		 			, s.getStruct(2).getAs("recordID")
		 			, s.getStruct(2).getAs("trecID")
		 			, s.getStruct(2).getAs("infoID")
		 			))));
	 
	 return ret;
 }
 
 private static List<Long> readPreviousCounts(String path) {
	 File file = new File(path);
	 List<String> strList = new ArrayList<>();
	 List<Long> ret = new ArrayList<>();
	 
	 try {
			strList.addAll(Files.readAllLines(file.toPath()));
			for(String s : strList) ret.add(Long.valueOf(s));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	 
	 if(ret.isEmpty()) {
		 for(int i = 0; i<7; i++) {
			 ret.add((long) 0);
		 }
	 }

	 return ret;
 }
 
 private static Namespace validArgumentsOrNull(String[] args) {
	ArgumentParser parser = argParser();
		
	try {
		return parser.parseArgs(args);
	} catch (ArgumentParserException e) {
		parser.handleError(e);
		return null;
	}

}
	
 private static ArgumentParser argParser() {
	ArgumentParser ret = ArgumentParsers.newFor("FIXME: Starting point that extracts the CreateWebGraph")
		.addHelp(Boolean.TRUE)
		.build();
		
	ret.addArgument("-i", "--" + Args.ARG_INPUT)
		.required(Boolean.TRUE)
		.help("The input path");
		
	ret.addArgument("-o", "--" + Args.ARG_OUTPUT)
		.required(Boolean.FALSE)
		.help("Output-directory for results");
	
	ret.addArgument("-c", "--cumulativeResults")
		.required(Boolean.FALSE)
		.help("Output-file for cumulative results")
		.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/tmp/cumulativeResultsCount");
		
	ret.addArgument("-e", "--exactMatchUrls")
		.type(String.class)
		.required(Boolean.FALSE)
		.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/")
		.help("Folder which contains links for filtering purposes (see OnlyInternalURIsFilterExact)");
			
	return ret;

 }

 
}
