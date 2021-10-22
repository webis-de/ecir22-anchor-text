package scripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import anchor.AnchorElement;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import webis.Args;


public class LowerContextSize {
public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
	SparkConf sparkConf = new SparkConf().setAppName("repartition");
	try(JavaSparkContext sc = new JavaSparkContext(sparkConf)){
		Namespace parsedArgs = validArgumentsOrNull(args);
		
		if(parsedArgs == null) {
			return;
		}
		lowerContextSize(  sc
					, parsedArgs.getString(Args.ARG_INPUT)
					, parsedArgs.getInt("sizeOfContext"));
	}
	
}
 
private static void lowerContextSize(JavaSparkContext sc,String input, int sizeOfContext) throws FileNotFoundException, IllegalArgumentException, IOException {
	FileSystem fs = FileSystem.get(sc.hadoopConfiguration());

	
	FileStatus[] fileStatus = fs.listStatus(new Path(input));
	
	for(FileStatus status : fileStatus) {
		Path path = status.getPath();
		lowerContext(sc, path.toString(), path.getParent().toString()+"-"+sizeOfContext+"context/"+path.getName(), sizeOfContext);
	}
}

private static void lowerContext(JavaSparkContext sc, String in, String out, int sizeOfContext) {

	JavaRDD<AnchorElement> anchors = sc.textFile(in).map(i -> AnchorElement.fromString(i));
	anchors = anchors.map(i -> shortenString(i, sizeOfContext));
	anchors.map(s -> s.toString()).repartition(100).saveAsTextFile(out, GzipCodec.class);
	
}

public static AnchorElement shortenString (AnchorElement elem, int sizeOfContext) {
	String anchor = elem.getAnchorText();
	String context = elem.getAnchorContext();
	String newContext = "";
	
	if (context.length() <= sizeOfContext/2) return elem;
	if (anchor.length() >= sizeOfContext) {
		elem.setAnchorContext(anchor);
		return elem;
	}
	
	int currentIndex = context.indexOf(anchor);
	int lastIndex = context.lastIndexOf(anchor);
	if(currentIndex == -1) return elem;
	
	List <Integer> indices = new ArrayList<>();
	indices.add(currentIndex);
	
	while (currentIndex != lastIndex) {
		currentIndex = context.indexOf(anchor, currentIndex + 1);
		indices.add(currentIndex);
	}
	
	int closestIndex = 0;
    int diff = Integer.MAX_VALUE;
    int targetInt = context.length()/2;
    
    for (int i = 0; i<indices.size(); i++) {
    	int middleOfAnchor = indices.get(i) + anchor.length()/2;
    	int diff2 = Math.abs(middleOfAnchor - targetInt);
      
    	if (diff2 < diff) {
    		closestIndex = middleOfAnchor;
    		diff = diff2;
    	}
	}

    int left = Math.max(0, (closestIndex - sizeOfContext/2));
	int right = Math.min(context.length(), (closestIndex + sizeOfContext/2));
	
	
	
	newContext = context.substring(left, right);
	elem.setAnchorContext(newContext);
	
	return elem;
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
	
	ret.addArgument("-s", "--sizeOfContext")
		.required(Boolean.TRUE)
		.type(Integer.class)
		.help("the new total size of the context in characters");
			
	return ret;

}

 
}

