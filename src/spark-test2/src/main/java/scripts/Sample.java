package scripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import anchor.AnchorElement;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import scala.Tuple2;
import webis.Args;
import webis.TakeRandom;

public class Sample {
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
		SparkConf sparkConf = new SparkConf().setAppName("sample");
		try(JavaSparkContext sc = new JavaSparkContext(sparkConf)){
			Namespace parsedArgs = validArgumentsOrNull(args);
			
			if(parsedArgs == null) {
				return;
			}
			
			boolean textOrContext = parsedArgs.getString("anchorInformationType").equals("TEXT");
			boolean wholeOrPartsOut = parsedArgs.getString("outputType").equals("WHOLE");
			boolean wholeOrPartsIn = parsedArgs.getString("inputType").equals("WHOLE");
			
			sample(  sc
						, parsedArgs.getString(Args.ARG_INPUT)
						, parsedArgs.getInt("sampleSize")
						, textOrContext
						, wholeOrPartsOut
						, wholeOrPartsIn
						);
		}
	}
	
	private static void sample(JavaSparkContext sc, String input, int s, boolean textOrContext, boolean wholeOrPartsOut, boolean wholeOrPartsIn) throws IOException {
		
		JavaPairRDD<String,String> targetDocToAnchorText;
		
		JavaRDD<DocumentAnchorText> docAnchorText = null;
		
		String out = "";
		int repart = 0;
		
		if(wholeOrPartsOut) {
			Path inPath = new Path(input);
			
			targetDocToAnchorText = sc.textFile(input).flatMapToPair(i -> targetDocToAnchorText(i, textOrContext));
			docAnchorText = targetDocToAnchorText.groupByKey().map(i -> sampleAnchorText(i,s));
			
			if(wholeOrPartsIn) {
				if (textOrContext) {
					out = inPath.getParent().toString()+"-sample-"+s+"-text";
				}else {
					out = inPath.getParent().toString()+"-sample-"+s+"-context";
				}
				repart = 10000;
			} else {
				if (textOrContext) {
					out = inPath.getParent().getParent().toString()+"-sample-"+s+"-text";
				}else {
					out = inPath.getParent().getParent().toString()+"-sample-"+s+"-context";
				}
				repart = 10000;
			}
			
			docAnchorText.map(i -> i.toString()).repartition(repart).saveAsTextFile(out, GzipCodec.class);
			
		}else {
			repart = 100;
			
			FileSystem fs = FileSystem.get(sc.hadoopConfiguration());

			
			FileStatus[] fileStatus = fs.listStatus(new Path(input));
			
			for(FileStatus status : fileStatus) {
				Path path = status.getPath();
				if (textOrContext) {
					out = path.getParent().toString()+"-sample-"+s+"-text/"+path.getName();
				}else {
					out = path.getParent().toString()+"-sample-"+s+"-context/"+path.getName();
				}
				
				
				targetDocToAnchorText = sc.textFile(path.toString()).flatMapToPair(i -> targetDocToAnchorText(i, textOrContext));
				docAnchorText = targetDocToAnchorText.groupByKey().map(i -> sampleAnchorText(i,s));
				
				docAnchorText.map(i -> i.toString()).repartition(repart).saveAsTextFile(out, GzipCodec.class);
			}
			
			
		}
		
		
		
		
		
	}
	
	private static DocumentAnchorText sampleAnchorText(Tuple2<String, Iterable<String>> i, int s) {
		String id = i._1;
		List<String> anchorContexts = TakeRandom.takeRandomElements(s, i._2);
		String contents = anchorContexts.stream().collect(Collectors.joining(" "));
		
		return new DocumentAnchorText(id, contents);
	}

	private static Iterator<Tuple2<String,String>> targetDocToAnchorText(String i, boolean textOrContext) {
		AnchorElement anchor = AnchorElement.fromString(i);
		List<Tuple2<String,String>> ret = new ArrayList<>();
		if(anchor.getDocument() != null) {
			for(String targetDoc : anchor.getTargetMsMarcoDocIds()) {
				if(textOrContext) {
					ret.add(new Tuple2<>(targetDoc,anchor.getAnchorText()));
				} else {
					ret.add(new Tuple2<>(targetDoc,anchor.getAnchorContext()));
				}
			}
			
		}
		return ret.iterator();
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
		
		ret.addArgument("-s", "--sampleSize")
			.required(Boolean.TRUE)
			.type(Integer.class)
			.help("The amount of samples to retrieve");
		
		ret.addArgument("-a", "--anchorInformationType")
			.required(Boolean.FALSE)
			.choices("TEXT","CONTEXT")
			.setDefault("TEXT")
			.help("Wether to sample the anchorTexts or the anchorContexts");
		
		ret.addArgument("-t", "--outputType")
			.required(Boolean.TRUE)
			.choices("WHOLE","PARTS")
			.help("Wether the samples are processed part by part or as a whole");
		
		ret.addArgument("-z", "--inputType")
			.required(Boolean.TRUE)
			.choices("WHOLE","PARTS")
			.help("Wether the samples are processed part by part or as a whole");
				
		return ret;

	}
}
