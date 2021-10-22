package scripts;

import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import anchor.AnchorElement;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import scripts.ResampleAnchorTexts.AnchorTextSample;
import webis.Args;

public class RepartitionToDocumentAnchorText {
	public static void main(String[] args) throws Exception {
		try (JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("sample"))) {
			Namespace parsedArgs = validArgumentsOrNull(args);

			if (parsedArgs == null) {
				return;
			}

			JavaRDD<String> out = sc.textFile(parsedArgs.getString(Args.ARG_INPUT))
					.map(i -> toAnseriniDoc(i));

			out.repartition(10).saveAsTextFile(parsedArgs.getString(Args.ARG_OUTPUT), GzipCodec.class);
		}
	}

	private static String toAnseriniDoc(String json) {
		try {
			AnchorTextSample ret = new ObjectMapper().readValue(json, AnchorTextSample.class);
			String text = "";
			for(AnchorElement anchor: ret.anchorTextSample) {
				text += " " + anchor.getAnchorText();
			}
			
			
			return new ObjectMapper().writeValueAsString(new DocumentAnchorText(ret.targetDocumentId, text.trim()));
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		ArgumentParser ret = ArgumentParsers.newFor("RepartitionToDocumentAnchorText").addHelp(Boolean.TRUE).build();

		ret.addArgument("-i", "--" + Args.ARG_INPUT).required(Boolean.TRUE).help("The input path (folder)");

		ret.addArgument("-o", "--" + Args.ARG_OUTPUT).required(Boolean.TRUE).help("The output path (folder)");

		return ret;
	}
}
