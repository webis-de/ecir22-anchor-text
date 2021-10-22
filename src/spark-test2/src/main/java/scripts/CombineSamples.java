package scripts;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import scala.Tuple2;
import webis.Args;

public class CombineSamples {
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
		SparkConf sparkConf = new SparkConf().setAppName("sample");
		try(JavaSparkContext sc = new JavaSparkContext(sparkConf)){
			Namespace parsedArgs = validArgumentsOrNull(args);
			
			if(parsedArgs == null) {
				return;
			}
			
			boolean wholeOrPartsIn = parsedArgs.getString("inputType").equals("WHOLE");
			
			sample(  sc
						, parsedArgs.getString(Args.ARG_INPUT)
						, wholeOrPartsIn
						);
		}
	}
	
	private static void sample(JavaSparkContext sc, String input, boolean wholeOrPartsIn) {
		
		Path inPath = new Path(input);
		String out = "";
		
		if(wholeOrPartsIn) {
			out = inPath.getParent().toString()+"-comb";
		} else {
			out = inPath.getParent().getParent().toString()+"-comb";
		}
		
		
		JavaPairRDD<String,String> idContent = sc.textFile(input).mapToPair(i -> getIdAndContent(i));
		idContent = idContent.reduceByKey((s1, s2) -> reduceStrings(s1,s2));
		
		JavaRDD<DocumentAnchorText> docAnchorText = idContent.map(k -> new DocumentAnchorText(k._1, k._2));
		
		docAnchorText.map(i -> i.toString()).repartition(10000).saveAsTextFile(out, GzipCodec.class);
	}
	
	private static Tuple2<String, String> getIdAndContent(String in){
		DocumentAnchorText anchors = DocumentAnchorText.fromString(in);
		Tuple2<String, String> res = new Tuple2<>(anchors.getId(), anchors.getContents());
		
		return res;
	}
	
	private static String reduceStrings(String s1, String s2) {
		return (s1 + " " + s2);
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
			.help("The input path (folder)");
		
		ret.addArgument("-z", "--inputType")
			.required(Boolean.TRUE)
			.choices("WHOLE","PARTS")
			.help("Wether the input is using the PARTS format or not");
				
		return ret;

	}
}
