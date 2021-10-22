package scripts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.codehaus.jackson.map.ObjectMapper;

import anchor.AnchorElement;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import scala.Tuple2;
import webis.Args;
import webis.TakeRandom;

public class SampleAnchorTexts {
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException, IOException {
		try (JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("sample"))) {
			Namespace parsedArgs = validArgumentsOrNull(args);

			if (parsedArgs == null) {
				return;
			}

			int size = parsedArgs.getInt("sampleSize");
			JavaPairRDD<String, AnchorElement> anchors = sc.textFile(parsedArgs.getString(Args.ARG_INPUT))
					.flatMapToPair(i -> targetDocToAnchorText(i));

			JavaRDD<String> out = anchors.groupByKey().map(i -> sampleAnchorText(i, size));

			out.repartition(10).saveAsTextFile(parsedArgs.getString(Args.ARG_OUTPUT));
		}
	}

	private static String sampleAnchorText(Tuple2<String, Iterable<AnchorElement>> i, int size) {
		List<AnchorElement> anchorContexts = TakeRandom.takeRandomElements(size, i._2);

		Map<String, Object> ret = new LinkedHashMap<>();
		ret.put("targetDocumentId", i._1);
		ret.put("anchorTextSample", anchorContexts);

		try {
			return new ObjectMapper().writeValueAsString(ret);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Iterator<Tuple2<String, AnchorElement>> targetDocToAnchorText(String i) {
		AnchorElement anchor = AnchorElement.fromString(i);
		List<Tuple2<String, AnchorElement>> ret = new ArrayList<>();
		if (anchor.getDocument() != null) {
			for (String targetDoc : anchor.getTargetMsMarcoDocIds()) {
				ret.add(new Tuple2<>(targetDoc, anchor));
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
		ArgumentParser ret = ArgumentParsers.newFor("SampleAnchorTexts").addHelp(Boolean.TRUE).build();

		ret.addArgument("-i", "--" + Args.ARG_INPUT).required(Boolean.TRUE).help("The input path (folder)");

		ret.addArgument("-o", "--" + Args.ARG_OUTPUT).required(Boolean.TRUE).help("The output path (folder)");

		ret.addArgument("-s", "--sampleSize").required(Boolean.TRUE).type(Integer.class).setDefault(1000)
				.help("The amount of samples to retrieve");

		return ret;
	}
}
