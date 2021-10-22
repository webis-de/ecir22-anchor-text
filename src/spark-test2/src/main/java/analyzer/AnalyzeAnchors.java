package analyzer;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import anchor.AnchorElement;
import filters.OnlyInternalURIsFilter;
import filters.OnlyInternalURIsFilterExact;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import webis.Args;

import static org.apache.spark.sql.functions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AnalyzeAnchors {
 public static void main(String[] args) {
	 Namespace parsedArgs = validArgumentsOrNull(args);
		
		if(parsedArgs == null) {
			return;
		}
		analyze(parsedArgs.getString(Args.ARG_INPUT)
						, parsedArgs.getString("exactMatchUrls")
						, parsedArgs.getString(Args.ARG_OUTPUT)
						, parsedArgs.getString("cumulativeResults")
						, parsedArgs.getString("topAnchors"));
 }
 
 private static void analyze(String input, String exactMatchUrlsPath, String output, String cumulativeResults, String topAnchors) {
	 SparkSession spark = SparkSession
             .builder()
             .appName(input)
             //.master("local[*]") 	//for local tests
             .getOrCreate();
	 
	 
	 List<Long> previous = readPreviousCounts(cumulativeResults);
	 
	 String jsonPath = input;
	 Dataset<Row> items = spark.read().json(jsonPath);
	 
	 long countLocal = items.count(); //eintraege insgesamt (in dieser Datei)
	 long count = countLocal + previous.get(0); //eintraege insgesamt (overall)
	 
	 long countAnchorEmpty = items.where("anchorText = \"\" ").count() + previous.get(1); //leere texte
	 long countAnchorNotEmpty = count - countAnchorEmpty;
	 
	 long anchorTextLength = items.agg(sum(length(col("anchorText")))) //durchschnittliche AnchorText laenge
			 .first().getLong(0) + previous.get(2);
	 double countAvgAnchorTextLength = anchorTextLength / (double)count;
	 
	 long anchorContextLength = items.agg(sum(length(col("anchorContext"))))  //durchschnittliche context laenge
			 .first().getLong(0) + previous.get(3);
	 
	 double countAvgAnchorContextLength = anchorContextLength / (double)count;
	 
	 //TODO: save top100 anchor texts and compare for global ranking
	 List<Row> MostFrequentAnchorText = new ArrayList<Row>(items.select(lower(col("anchorText")).as("lowerAnchor"))		//haeufigste anchorTexts
	 	.where("lowerAnchor != \"\"")
	 	.groupBy("lowerAnchor")
	 	.count()
	 	.orderBy(desc("count"))
	 	.limit(100)
	 	.collectAsList());
	 
	 //if topAnchors != null
	 
	 List<Row> previousTopAnchors = readTopAnchors(topAnchors);
	 
	 System.out.println("previousAnchors: " + previousTopAnchors);
	 
	 if(previousTopAnchors!=null) {
		MostFrequentAnchorText = combineRows(MostFrequentAnchorText, previousTopAnchors, spark);
	 }
	 System.out.println("combined Anchors: " + MostFrequentAnchorText);
	 saveTopAnchors(MostFrequentAnchorText, topAnchors);
	 
	 	
	 
	 
	 /*
	 Dataset<Row> NaughtyWords = items.select("document")
			 .where("naughtyWords.count() != 0");
	 
	 long sitesWithNaughtyWords = NaughtyWords.count();
	 
	 List<Row> mostFrequentNaughtyWords = NaughtyWords.groupBy("naughtyWords")
			 .count()
			 .orderBy(desc("count"))
			 .limit(10)
			 .collectAsList();
	 */
	 
	 JavaRDD<Row> filtered = items.toJavaRDD();
	 System.out.println(filtered.first());
	 filtered = filtered.filter(s -> OnlyInternalURIsFilter.toBase(s.getString(4)).equals(	//source url ist target url
			OnlyInternalURIsFilter.toBase(s.getStruct(2).getString(3))));
	 long countSrcIsTarget = filtered.count() + previous.get(4);	
	 
	 long countSrcNotTarget = count - countSrcIsTarget;		//source url ist nicht target url
	 
	 
	 filtered = items.toJavaRDD();
	 long countNumberSign = filtered.filter(s -> s.getString(4).contains("#")).count() + previous.get(5);	//menge an links, die "#" in url haben
	 
	 OnlyInternalURIsFilterExact exactFilter = OnlyInternalURIsFilterExact.productionFilter(
			 exactMatchUrlsPath);
	 
	 filtered = items.toJavaRDD();
	 
	 JavaRDD<Row> exactMatches = filtered.filter(s -> exactFilter.filterElement(	//menge an url's, die exact mit ms-marco matchen
			 new AnchorElement(null, s.get(4).toString(), null)));
	 
	 long countExactMatches = exactMatches.count() + previous.get(6);
	 
	 long countExactMatchesSrcIsTarget = exactMatches.filter(s -> OnlyInternalURIsFilter.toBase(s.getString(4)).equals(	//source url ist target url von exakt gematchten urls
				OnlyInternalURIsFilter.toBase(s.getStruct(2).getString(3)))).count() + previous.get(7);
	 
	 long countExactMatchesSrcNotTarget = countExactMatches - countExactMatchesSrcIsTarget;
	 
	 String cumulative = count + "\n" 
			 + countAnchorEmpty + "\n" 
			 + anchorTextLength + "\n" 
			 + anchorContextLength + "\n" 
			 + countSrcIsTarget + "\n" 
			 + countNumberSign + "\n"
			 + countExactMatches + "\n"
			 + countExactMatchesSrcIsTarget;
	 

	 String out = "count: " + count + "\n"
	 			+ "\n"
				+ "empty: " + countAnchorEmpty + "\n"
				+ "not empty: " + countAnchorNotEmpty + "\n"
				+ "\n"
				+ "src is target: " + countSrcIsTarget + "\n"
				+ "src is not target: " + countSrcNotTarget + "\n"
				+ "\n"
				+ "exact url matches: " + countExactMatches + "\n"
				+ "exact url matches where src = target: " + countExactMatchesSrcIsTarget + "\n"
				+ "exact url matches where src != target: " + countExactMatchesSrcNotTarget + "\n"
				+ "\n"
				+ "avg anchorText length: " + countAvgAnchorTextLength + "\n"
				+ "avg anchorContext length: " + countAvgAnchorContextLength + "\n"
				+ "\n"
				+ "most frequent anchorText: " + MostFrequentAnchorText + "\n"
				+ "\n"
				+ "amount of uri's which use anchor tag #: " + countNumberSign + "\n"
				//+ "amount of sites with naughty words: " + sitesWithNaughtyWords + "\n"
				//+ "most frequent naughty words: " + mostFrequentNaughtyWords + "\n"
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

 private static List<Row> combineRows(List<Row> MostFrequentAnchorText, List<Row> previousTopAnchors, SparkSession spark){
	 for(Row previousRow : previousTopAnchors) {
			boolean isNew = true;
			for(int i = 0; i < MostFrequentAnchorText.size(); i++) {
				long previousCount = previousRow.getLong(1);
				long frequentAnchorCount = MostFrequentAnchorText.get(i).getLong(1);
				 
				String previousRowString = previousRow.getString(0);
				String frequentAnchorString = MostFrequentAnchorText.get(i).getString(0);
				 
				if(previousRowString.equals(frequentAnchorString)) {
					MostFrequentAnchorText.set(i, RowFactory.create(frequentAnchorString, previousCount + frequentAnchorCount));
					isNew = false;
					break;
				}
			}
			if(isNew) {
				 MostFrequentAnchorText.add(previousRow);
			}
		}
		
		List<org.apache.spark.sql.types.StructField> listOfStructField=new ArrayList<org.apache.spark.sql.types.StructField>();
		listOfStructField.add(DataTypes.createStructField("lowerAnchor", DataTypes.StringType, true));
		listOfStructField.add(DataTypes.createStructField("count", DataTypes.LongType, true));
		StructType structType=DataTypes.createStructType(listOfStructField);
		Dataset<Row> mostFrequentAnchorTextDS=spark.createDataFrame(MostFrequentAnchorText,structType);
		mostFrequentAnchorTextDS.show();
		
		MostFrequentAnchorText = mostFrequentAnchorTextDS
			.orderBy(desc("count"))
			.limit(100)
			.collectAsList();
		
		return MostFrequentAnchorText;
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
		 for(int i = 0; i<8; i++) {
			 ret.add(0L);
		 }
	 }

	 return ret;
 }

 private static List<Row> readTopAnchors(String path) {
	 try {
         FileInputStream fi = new FileInputStream(new File(path));
         ObjectInputStream oi = new ObjectInputStream(fi);

         // Read object
         List<Row> ret = (List<Row>) oi.readObject();

         oi.close();
         fi.close();
         
         return ret;
         
	 } catch (FileNotFoundException e) {
         System.out.println("File not found, returning null");
         return null;
	 } catch (Exception e) {
         System.out.println("Error initializing stream");
     }
	 return null;
 }
 
 private static void saveTopAnchors(List<Row> data, String path) {
	 try {
		 //clear file
		 new FileWriter(path, false).close();
		 
         FileOutputStream f = new FileOutputStream(new File(path));
         ObjectOutputStream o = new ObjectOutputStream(f);

         // Write object to file
         o.writeObject(data);

         o.close();
         f.close();

     } catch (FileNotFoundException e) {
         System.out.println("File not found");
     } catch (IOException e) {
         System.out.println("Error initializing stream");
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
		.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/tmp/cumulativeResultsAnalysis");
		
	ret.addArgument("-t", "--topAnchors")
		.required(Boolean.FALSE)
		.help("Output-file for cumulative results")
		.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/tmp/topAnchorsAnalysis");

	ret.addArgument("-e", "--exactMatchUrls")
		.type(String.class)
		.required(Boolean.FALSE)
		.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/")
		.help("Folder which contains links for filtering purposes (see OnlyInternalURIsFilterExact)");
			
	return ret;

 }

 
}
