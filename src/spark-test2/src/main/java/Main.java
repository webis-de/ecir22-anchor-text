import java.io.IOException;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import warc.Crawler;

import webis.*;


public class Main {

	public static void main(String[] args) throws IOException {
		
		Namespace parsedArgs = validArgumentsOrNull(args);
		
		if(parsedArgs == null) {
			return;
		}
		Crawler.crawlWarc(parsedArgs.getString(Args.ARG_INPUT)
						, parsedArgs.getString(Args.ARG_OUTPUT)
						, Args.InputFormats.valueOf(parsedArgs.getString(Args.ARG_FORMAT)).getInputFormat()
						, parsedArgs.getString("keepLinks")
						, parsedArgs.getString("cutOffDomains")
						, parsedArgs.getString("naughtyWords"));
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
			.help("The input path that is passed to JavaSparkContext.hadoopFile to extract Documents from warc files. E.g. 's3a://corpus-clueweb09'.");
		
		ret.addArgument("-o", "--" + Args.ARG_OUTPUT)
			.required(Boolean.TRUE)
			.help("The resulting document representations are stored under this location.");

		ret.addArgument("-f", "--" + Args.ARG_FORMAT)
			.required(Boolean.TRUE)
			.choices(Args.InputFormats.allInputFormats());
		
		ret.addArgument("-k", "--keepLinks")
			.type(String.class)
			.required(Boolean.FALSE)
			.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/")
			.help("Folder which contains links to be kept (see OnlyInternalURIsFilter)");
		
		ret.addArgument("-c", "--cutOffDomain")
			.type(String.class)
			.required(Boolean.FALSE)
			.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/cutOffDomains/cutOffDomains.txt")
			.help("File which contains domains to shorten via \"?\" (see OnlyInternalURIsFilter)");
		
		ret.addArgument("-n", "--naughtyWords")
			.type(String.class)
			.required(Boolean.FALSE)
			.setDefault("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/naugtyWords/naughty-words.txt")
			.help("File which contains a list of naughty words (see NaughtyWordsExtractor)");
		
		return ret;

	}
}
