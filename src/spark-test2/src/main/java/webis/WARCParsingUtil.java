package webis;

import org.apache.hadoop.io.LongWritable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import de.webis.chatnoir2.mapfile_generator.inputformats.WarcInputFormat;
import de.webis.chatnoir2.mapfile_generator.warc.WarcRecord;
import net.sourceforge.argparse4j.inf.Namespace;

public class WARCParsingUtil implements Args {
	
	private WARCParsingUtil() {
		// hide utility class constructor
	}
	
	public static JavaPairRDD<LongWritable, WarcRecord> records(JavaSparkContext sc, Namespace parsedArgs) {
		Class<? extends WarcInputFormat> inputFormat = InputFormats.valueOf(parsedArgs.get(ARG_FORMAT)).getInputFormat();
		
		return records(sc, parsedArgs.getString(ARG_INPUT), inputFormat);
	}
	
	public static JavaPairRDD<LongWritable, WarcRecord> records(JavaSparkContext sc, String path, Class<? extends WarcInputFormat> inputFormat) {
		return sc.newAPIHadoopFile(path, inputFormat, LongWritable.class, WarcRecord.class, sc.hadoopConfiguration());
	}
	
	public static boolean isResponse(WarcRecord record) {
		return record != null && record.getRecordType() != null && "response".equalsIgnoreCase(record.getRecordType().trim());
	}
}
