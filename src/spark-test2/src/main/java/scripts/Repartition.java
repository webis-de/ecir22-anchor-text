package scripts;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import webis.Args;


public class Repartition {
public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
	SparkConf sparkConf = new SparkConf().setAppName("repartition");
	try(JavaSparkContext sc = new JavaSparkContext(sparkConf)){
		Namespace parsedArgs = validArgumentsOrNull(args);
		
		if(parsedArgs == null) {
			return;
		}
		repartition(  sc
					, parsedArgs.getString(Args.ARG_INPUT)
					, parsedArgs.getBoolean("compression")
					, parsedArgs.getInt("numberOfPartitions")
					, parsedArgs.getBoolean("whole"));
	}
	
}
 
private static void repartition(JavaSparkContext sc,String input, boolean compression, int numberOfPartitions, boolean whole) throws FileNotFoundException, IllegalArgumentException, IOException {
	FileSystem fs = FileSystem.get(sc.hadoopConfiguration());
	String out = "";
	
	if (whole) {
		Path inPath = new Path(input);
		out = inPath.getParent().getParent().toString()+"-repart";
		
		repart(sc, input, out, compression, numberOfPartitions);
		
	}else {
		FileStatus[] fileStatus = fs.listStatus(new Path(input));
		
		for(FileStatus status : fileStatus) {
			Path path = status.getPath();
			out = path.getParent().toString()+"-repartitioned/"+path.getName();
			
			repart(sc, path.toString(), out, compression, numberOfPartitions);
		}
	}
}

private static void repart(JavaSparkContext sc, String in, String out, boolean compression, int n) {
	if(compression) {
		sc.textFile(in).repartition(n).saveAsTextFile(out, GzipCodec.class);
	} else {
		sc.textFile(in).repartition(n).saveAsTextFile(out);
	}
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
	
	ret.addArgument("-n", "--numberOfPartitions")
		.required(Boolean.TRUE)
		.type(Integer.class)
		.help("The number of resulting partitions");
	
	ret.addArgument("-c", "--compression")
		.type(Boolean.class)
		.required(Boolean.FALSE)
		.setDefault(Boolean.TRUE)
		.help("Toggles compression on or off");
	
	ret.addArgument("-d", "--whole")
		.type(Boolean.class)
		.required(Boolean.FALSE)
		.setDefault(Boolean.FALSE)
		.help("throws all partitions into a single folder if set to true, otherwise mimics the usual 100 parts folder structure");
			
	return ret;

}

 
}

