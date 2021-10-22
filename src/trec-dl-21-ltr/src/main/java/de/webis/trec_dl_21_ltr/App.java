package de.webis.trec_dl_21_ltr;

import java.util.Map;
import java.util.Set;

import de.webis.trec_dl_21_ltr.feature_extraction.FeatureExtractionArguments;
import de.webis.trec_dl_21_ltr.feature_extraction.FeatureExtractor;
import de.webis.trec_dl_21_ltr.parsing.QueryExtractor;
import de.webis.trec_dl_21_ltr.parsing.RunFileDocumentExtractor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class App {
  public static void main(String[] args) {
    FeatureExtractionArguments parsedArgs = parseFeatureExtractionArgs(args);

    if(parsedArgs == null) {
      return;
    }

    Map<Integer, String> idToQuery = parsedArgs.getQueryExtractor().idToQuery();
    Map<Integer, Set<String>> tasks = RunFileDocumentExtractor.parseRunFile(parsedArgs.getRunFileDir());

    new FeatureExtractor(idToQuery, tasks).runFeatureExtraction(parsedArgs);
  }

  static FeatureExtractionArguments parseFeatureExtractionArgs(String[] args) {
    Namespace parsedArgs = parseArgs(args);
    if(parsedArgs == null) {
      return null;
    }
    
    Map<String, String> features = null;
    QueryExtractor qe = null;
    
    if("ms-marco-v1".equals(parsedArgs.getString("version"))) {
      features = FeatureExtractionArguments.msMarcoV1Features();
      qe = QueryExtractor.msMarcoV1Queries();
    } else if("ms-marco-v1-dl".equals(parsedArgs.getString("version"))) {
          features = FeatureExtractionArguments.msMarcoV1Features();
          qe = QueryExtractor.msMarcoV1_DL_19_20();
    } else if("ms-marco-v2-dl".equals(parsedArgs.getString("version"))) {
      features = FeatureExtractionArguments.msMarcoV2Features();
      qe = QueryExtractor.msMarcoV1_DL_19_20();
    }
    
    else if("ms-marco-v1-nav-popular".equals(parsedArgs.getString("version"))) {
        features = FeatureExtractionArguments.msMarcoV1Features();
        qe = QueryExtractor.msMarcoV1NavPopular();
    } else if("ms-marco-v1-nav-random".equals(parsedArgs.getString("version"))) {
        features = FeatureExtractionArguments.msMarcoV1Features();
        qe = QueryExtractor.msMarcoV1NavRandom();
    } else if("ms-marco-v2-nav-popular".equals(parsedArgs.getString("version"))) {
        features = FeatureExtractionArguments.msMarcoV2Features();
        qe = QueryExtractor.msMarcoV2NavPopular();
    } else if("ms-marco-v2-nav-random".equals(parsedArgs.getString("version"))) {
        features = FeatureExtractionArguments.msMarcoV2Features();
        qe = QueryExtractor.msMarcoV2NavRandom();
    } else if("ms-marco-v2".equals(parsedArgs.getString("version"))) {
      features = FeatureExtractionArguments.msMarcoV2Features();
      qe = QueryExtractor.msMarcoV2Queries();
    } else {
      throw new RuntimeException("Please specify the version of ms-marco to use.");
    }
    
    return FeatureExtractionArguments.builder()
      .indexDir(parsedArgs.getString("indexDir"))
      .outputDir(parsedArgs.getString("outputDir"))
      .runFileDir(parsedArgs.getString("runFile"))
      .fieldToIndex(features)
      .queryExtractor(qe)
      .build();
  }
  
  static Namespace parseArgs(String[] args) {
    ArgumentParser parser = argParser();

    try {
      return parser.parseArgs(args);
    } catch (ArgumentParserException e) {
      parser.handleError(e);
      return null;
    }
  }

  static ArgumentParser argParser() {
    ArgumentParser ret = ArgumentParsers.newFor("TREC-21 DL Feature Extraction: Run the feature extraction for the trec dl submission.").build();

    ret.addArgument("--indexDir")
      .help("The directory that contains all indices.")
      .required(true);

    ret.addArgument("--outputDir")
      .help("The output structure is here.")
      .required(true);

    ret.addArgument("--runFile")
      .help("The run file to extract features.")
      .required(true);

    ret.addArgument("--version")
      .help("The version of MS-MARCO to use.")
      .choices("ms-marco-v1", "ms-marco-v1-dl", "ms-marco-v1-nav-random", "ms-marco-v1-nav-popular", "ms-marco-v2", "ms-marco-v2-dl", "ms-marco-v2-nav-random", "ms-marco-v2-nav-popular")
      .required(true);
    
    return ret;
  }
}
