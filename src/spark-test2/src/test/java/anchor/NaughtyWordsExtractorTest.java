package anchor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

public class NaughtyWordsExtractorTest {

	@Test
	public void testForSingleNaughtyWord() {
		AnchorElement anchor = createAnchor("foo naughty1 bar");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {"naughty1"};
		Set<String> expected = new TreeSet<>(Arrays.asList((expectedWords)));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForMultipleNaughtyWords() {
		AnchorElement anchor = createAnchor("foo naughty1 bar naughty2");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {"naughty1", "naughty2"};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForNoNaughtyWords() {
		AnchorElement anchor = createAnchor("foo bar");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForNaughtyWordGroup() {
		AnchorElement anchor = createAnchor("foo word group 1 bar");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {"word group 1"};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForNaughtyWordGroups() {
		AnchorElement anchor = createAnchor("foo word group 1 bar word group 2 baz");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {"word group 1", "word group 2"};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForNaughtyWordsAndWordGroups() {
		AnchorElement anchor = createAnchor("foo naughty1 bar word group 2  baz");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {"naughty1", "word group 2"};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testForEmptyAnchorTextOrContext() {
		AnchorElement anchor = createAnchor("");
		NaughtyWordsExtractor extractor = createExtractor();
		
		String[] expectedWords = {};
		Set<String> expected = new TreeSet<>(Arrays.asList(expectedWords));
		
		Set<String> actual = extractor.extractNaughtyWords(anchor);
		
		Assert.assertEquals(expected, actual);
	}
	
	
	
	private NaughtyWordsExtractor createExtractor() {
		String[] naughtyWords = { "naughty1"
								, "naughty2"
								, "word group 1"
								, "word group 2"
								};
		
		List<String> naughtyWordsList = new ArrayList<>(Arrays.asList(naughtyWords));
		return new NaughtyWordsExtractor(naughtyWordsList);
	}
	
	private AnchorElement createAnchor(String text) {
		AnchorElement anchor = new AnchorElement(text, "", null);
		
		return anchor;
	}
}
