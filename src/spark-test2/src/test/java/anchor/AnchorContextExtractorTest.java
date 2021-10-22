package anchor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class AnchorContextExtractorTest {
	@Test
	public void testWithNullAsInput() {
		Collection<Element> elements = null;
		Document doc = null;
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.getTranslatedText();
		
		Assert.assertNull(actual);
	}
	
	@Test
	public void testWithoutElementsToTranslate() {
		Collection<Element> elements = null;
		Document doc = Jsoup.parse("<html>This is some html document</html>");
		String expected = "This is some html document";
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.getTranslatedText();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testWithSingleElementsToTranslate() {
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document</html>");
		Collection<Element> elements = elements(doc, "a");
		String expected = "This is some html WEBIS_REPLACEMENT_0 document";
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.getTranslatedText();
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractionOfContextForSingleElementWithContextSize5() {
		String expected = "html test docu";
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document</html>");
		List<Element> elements = elements(doc, "a");
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.extractContext(elements.get(0), 5);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testExtractionOfContextForSingleElementWithContextSize10() {
		String expected = "some html test document";
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document</html>");
		List<Element> elements = elements(doc, "a");
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.extractContext(elements.get(0), 10);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testWithMultipleElementsToTranslate() {
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document <a>sdadasda</a>.</html>");
		Collection<Element> elements = elements(doc, "a");
		String expected = "This is some html WEBIS_REPLACEMENT_0 document WEBIS_REPLACEMENT_1.";
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);
		String actual = extractor.getTranslatedText();
		
		Assert.assertEquals(expected, actual);
	}
	

	@Test
	public void testExtractionOfContextForMultipleElements() {
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document <a>sdadasda</a>.</html>");
		List<Element> elements = elements(doc, "a");
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);

		String actual = extractor.extractContext(elements.get(0), 5);
		Assert.assertEquals("html test docu", actual);
		
		actual = extractor.extractContext(elements.get(1), 5);
		Assert.assertEquals("ment sdadasda.", actual);
	}
	
	/*
	@Test
	public void testNoReplacementsLeftInFinnishedContext() {
		Document doc = Jsoup.parse("<html>This is some html <a>test</a> document <a>sdadasda</a>.</html>");
		List<Element> elements = elements(doc, "a");
		
		AnchorContextExtractor extractor = new AnchorContextExtractor(elements, doc);

		String actual = extractor.extractContext(elements.get(0), 15);
		Assert.assertEquals("s is some html test document sdada", actual);
		
		actual = extractor.extractContext(elements.get(1), 15);
		Assert.assertEquals(" test document sdadasda.", actual);
	}
	*/
	
	private List<Element> elements(Document doc, String cssSelector) {
		Elements elements = doc.select(cssSelector);
		List<Element> ret = new ArrayList<>();
		
		for(Element e: elements) {
			ret.add(e);
		}
		
		return ret;
	}
}
