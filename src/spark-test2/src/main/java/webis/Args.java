package webis;

import de.webis.chatnoir2.mapfile_generator.inputformats.WarcInputFormat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import webis.FailsafeWarcInputFormats.FailsafeClueWeb09InputFormat;
import webis.FailsafeWarcInputFormats.FailsafeClueWeb12InputFormat;
import webis.FailsafeWarcInputFormats.FailsafeCommonCrawlInputFormat;

public interface Args {
	public static final String ARG_INPUT = "input";
	public static final String ARG_FORMAT = "inputFormat";
	public static final String ARG_OUTPUT = "output";
	
	public static enum InputFormats {
		CLUEWEB09(FailsafeClueWeb09InputFormat.class),
		CLUEWEB12(FailsafeClueWeb12InputFormat.class),
		COMMON_CRAWL(FailsafeCommonCrawlInputFormat.class);
		
		private InputFormats(Class<? extends WarcInputFormat> inputFormat) {
			this.inputFormat = inputFormat;
		}
		
		private final Class<? extends WarcInputFormat> inputFormat;
		
		public Class<? extends WarcInputFormat> getInputFormat() {
			return this.inputFormat;
		}
		
		public static List<String> allInputFormats() {
			return Arrays.asList(InputFormats.values()).stream()
					.map(i -> i.name())
					.collect(Collectors.toList());
		}
	}
}
