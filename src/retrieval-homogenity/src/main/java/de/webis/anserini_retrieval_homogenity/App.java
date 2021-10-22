package de.webis.anserini_retrieval_homogenity;

import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		if(args.length != 2) {
			throw new RuntimeException("Can not handle: " + args.length);
		}
		
		RetrievalHomogenityFeatureExtraction featureExtraction = new RetrievalHomogenityFeatureExtraction(Paths.get(args[1]));
		List<RunLine> runLines = RunLine.parseRunlines(Paths.get(args[0]));
		featureExtraction.extractFeatures(runLines, null)
			.forEach(System.out::println);
	}
}
