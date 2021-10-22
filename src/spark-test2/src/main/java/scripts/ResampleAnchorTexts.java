package scripts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import anchor.AnchorElement;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import scala.Tuple2;
import webis.Args;
import webis.TakeRandom;

public class ResampleAnchorTexts {
	public static void main(String[] args) throws Exception {
		try (JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("sample"))) {
			Namespace parsedArgs = validArgumentsOrNull(args);

			if (parsedArgs == null) {
				return;
			}

			int size = parsedArgs.getInt("sampleSize");
			JavaPairRDD<String, List<AnchorElement>> anchors = sc.textFile(parsedArgs.getString(Args.ARG_INPUT))
					.mapToPair(i -> targetDocToAnchorTexts(i, size));

			JavaRDD<String> out = anchors.groupByKey().map(i -> sampleAnchorText(i, size));

			out.repartition(10).saveAsTextFile(parsedArgs.getString(Args.ARG_OUTPUT), GzipCodec.class);
		}
	}

	private static String sampleAnchorText(Tuple2<String, Iterable<List<AnchorElement>>> i, int size) {
		List<AnchorElement> tmp = new ArrayList<>();
		for(List<AnchorElement> t: i._2) {
			tmp.addAll(t);
		}
		
		List<AnchorElement> anchorContexts = TakeRandom.takeRandomElements(size, tmp);

		Map<String, Object> ret = new LinkedHashMap<>();
		ret.put("targetDocumentId", i._1);
		ret.put("anchorTextSample", anchorContexts);

		try {
			return new ObjectMapper().writeValueAsString(ret);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class AnchorTextSample {
		String targetDocumentId;
		List<AnchorElement> anchorTextSample;
		
		public void setTargetDocumentId(String targetDocumentId) {
			this.targetDocumentId = targetDocumentId;
		}
		
		public void setAnchorTextSample(List<AnchorElement> anchorTextSample) {
			this.anchorTextSample = anchorTextSample;
		}
	}

	private static Tuple2<String, List<AnchorElement>> targetDocToAnchorTexts(String i, int sampleSize) {
		AnchorTextSample ret;
		try {
			ret = new ObjectMapper().readValue(i, AnchorTextSample.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return new Tuple2<>(ret.targetDocumentId, TakeRandom.takeRandomElements(sampleSize, ret.anchorTextSample));
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
		ArgumentParser ret = ArgumentParsers.newFor("ResampleAnchorTexts").addHelp(Boolean.TRUE).build();

		ret.addArgument("-i", "--" + Args.ARG_INPUT).required(Boolean.TRUE).help("The input path (folder)");

		ret.addArgument("-o", "--" + Args.ARG_OUTPUT).required(Boolean.TRUE).help("The output path (folder)");

		ret.addArgument("-s", "--sampleSize").required(Boolean.TRUE).type(Integer.class).setDefault(1000)
				.help("The amount of samples to retrieve");

		return ret;
	}
}
