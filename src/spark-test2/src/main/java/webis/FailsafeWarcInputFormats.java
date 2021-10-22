package webis;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.http.ConnectionClosedException;

import de.webis.chatnoir2.mapfile_generator.inputformats.ClueWeb09InputFormat;
import de.webis.chatnoir2.mapfile_generator.inputformats.ClueWeb12InputFormat;
import de.webis.chatnoir2.mapfile_generator.inputformats.CommonCrawlInputFormat;
import de.webis.chatnoir2.mapfile_generator.warc.WarcRecord;

public class FailsafeWarcInputFormats {

	public static class FailsafeClueWeb09InputFormat extends ClueWeb09InputFormat{	
		@Override
		public RecordReader<LongWritable, WarcRecord> createRecordReader(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			return new FailsafeRecordReader(super.createRecordReader(split, context));
		}
	}
	
	public static class FailsafeClueWeb12InputFormat extends ClueWeb12InputFormat{	
		@Override
		public RecordReader<LongWritable, WarcRecord> createRecordReader(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			return new FailsafeRecordReader(super.createRecordReader(split, context));
		}
	}
	
	public static class FailsafeCommonCrawlInputFormat extends CommonCrawlInputFormat{	
		@Override
		public RecordReader<LongWritable, WarcRecord> createRecordReader(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			return new FailsafeRecordReader(super.createRecordReader(split, context));
		}
	}
	
	private static class FailsafeRecordReader extends RecordReader<LongWritable, WarcRecord>{
		
		final private RecordReader<LongWritable, WarcRecord> internalReader;
		
		FailsafeRecordReader(RecordReader<LongWritable, WarcRecord> reader){
			internalReader = reader;
		}
		
		@Override
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
			internalReader.initialize(split, context);
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			
			try {
				return internalReader.nextKeyValue();
			} catch(ConnectionClosedException e){
				return false;
			}
		}

		@Override
		public LongWritable getCurrentKey() throws IOException, InterruptedException {
			return internalReader.getCurrentKey();
		}

		@Override
		public WarcRecord getCurrentValue() throws IOException, InterruptedException {
			return internalReader.getCurrentValue();
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return internalReader.getProgress();
		}

		@Override
		public void close() throws IOException {
			internalReader.close();
		}
		
	}
}
