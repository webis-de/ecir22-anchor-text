package de.webis.anserini_retrieval_homogenity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.anserini.index.IndexArgs;

public class RetrievalHomogenityFeatureExtractionTest extends LuceneTestCase {
	protected Path tempDir;

	private void buildTestIndex() throws IOException {
		Directory dir = FSDirectory.open(tempDir);

		Analyzer analyzer = new EnglishAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		IndexWriter writer = new IndexWriter(dir, config);

		FieldType textOptions = new FieldType();
		textOptions.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		textOptions.setStored(true);
		textOptions.setTokenized(true);
		textOptions.setStoreTermVectors(true);
		textOptions.setStoreTermVectorPositions(true);

		Document doc1 = new Document();
		String doc1Text = "here is some text here is some more text. city.";
		doc1.add(new StringField(IndexArgs.ID, "doc1", Field.Store.YES));
		doc1.add(new SortedDocValuesField(IndexArgs.ID, new BytesRef("doc1".getBytes())));
		doc1.add(new Field(IndexArgs.CONTENTS, doc1Text, textOptions));
		// specifically demonstrate how "contents" and "raw" might diverge:
		doc1.add(new StoredField(IndexArgs.RAW, String.format("{\"contents\": \"%s\"}", doc1Text)));
		writer.addDocument(doc1);

		Document doc2 = new Document();
		String doc2Text = "more texts";
		doc2.add(new StringField(IndexArgs.ID, "doc2", Field.Store.YES));
		doc2.add(new SortedDocValuesField(IndexArgs.ID, new BytesRef("doc2".getBytes())));
		doc2.add(new Field(IndexArgs.CONTENTS, doc2Text, textOptions)); // Note plural, to test stemming
		// specifically demonstrate how "contents" and "raw" might diverge:
		doc2.add(new StoredField(IndexArgs.RAW, String.format("{\"contents\": \"%s\"}", doc2Text)));
		writer.addDocument(doc2);

		Document doc3 = new Document();
		String doc3Text = "here is a test";
		doc3.add(new StringField(IndexArgs.ID, "doc3", Field.Store.YES));
		doc3.add(new SortedDocValuesField(IndexArgs.ID, new BytesRef("doc3".getBytes())));
		doc3.add(new Field(IndexArgs.CONTENTS, doc3Text, textOptions));
		// specifically demonstrate how "contents" and "raw" might diverge:
		doc3.add(new StoredField(IndexArgs.RAW, String.format("{\"contents\": \"%s\"}", doc3Text)));
		writer.addDocument(doc3);

		// prevent stopwords
		for (int i = 5; i < 15; i++) {
			Document doc4 = new Document();
			String doc4Text = "dummy documents ftw";
			doc4.add(new StringField(IndexArgs.ID, "doc" + i, Field.Store.YES));
			doc4.add(new SortedDocValuesField(IndexArgs.ID, new BytesRef(("doc" + i).getBytes())));
			doc4.add(new Field(IndexArgs.CONTENTS, doc4Text, textOptions));
			// specifically demonstrate how "contents" and "raw" might diverge:
			doc4.add(new StoredField(IndexArgs.RAW, String.format("{\"contents\": \"%s\"}", doc4Text)));
			writer.addDocument(doc4);
		}

		writer.commit();
		writer.forceMerge(1);
		writer.close();

		dir.close();
	}

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		tempDir = createTempDir();
		buildTestIndex();
	}

	@Test
	public void testDocumentFeaturesForRankingWithSingleDocuments() {
		List<RunLine> runLines = runLines(
			"2004033 Q0 doc1 1 16.961201 Anserini",
			"2004032 Q0 doc2 2 12.332800 Anserini"
		);
		
		RetrievalHomogenityFeatureExtraction featureExtraction = new RetrievalHomogenityFeatureExtraction(tempDir);
		String expected = "[{\"topic\":\"2004033\",\"documents\":[{\"docId\":\"doc1\",\"documentRepresentation\":{\"citi\":1,\"here\":2,\"more\":1,\"some\":2,\"text\":2}}]}, {\"topic\":\"2004032\",\"documents\":[{\"docId\":\"doc2\",\"documentRepresentation\":{\"more\":1,\"text\":1}}]}]";
		
		String actual = featureExtraction.extractFeatures(runLines, null).collect(Collectors.toList()).toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDocumentFeaturesForRankingWithMultipleDocuments() {
		List<RunLine> runLines = runLines(
			"1 Q0 doc1 1 16.961201 Anserini",
			"1 Q0 doc2 2 12.332800 Anserini",
			"1 Q0 doc3 2 12.332800 Anserini"
		);
		
		RetrievalHomogenityFeatureExtraction featureExtraction = new RetrievalHomogenityFeatureExtraction(tempDir);
		String expected = "[{\"topic\":\"1\",\"documents\":[{\"docId\":\"doc1\",\"documentRepresentation\":{\"citi\":1,\"here\":2,\"more\":1,\"some\":2,\"text\":2}},{\"docId\":\"doc2\",\"documentRepresentation\":{\"more\":1,\"text\":1}},{\"docId\":\"doc3\",\"documentRepresentation\":{\"here\":1,\"test\":1}}]}]";
		
		String actual = featureExtraction.extractFeatures(runLines, null).collect(Collectors.toList()).toString();
		
		Assert.assertEquals(expected, actual);
	}
	
	private static List<RunLine> runLines(String...lines) {
		return RunLine.parseRunlines(new ByteArrayInputStream(Stream.of(lines).collect(Collectors.joining("\n")).getBytes()));
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		// Call garbage collector for Windows compatibility
		System.gc();
		super.tearDown();
	}
}
