package warc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;

import de.webis.chatnoir2.mapfile_generator.warc.WarcHeader;
import de.webis.chatnoir2.mapfile_generator.warc.WarcRecord;
import filters.Filter.Filters;
import filters.OnlyInternalURIsFilter;

public class TestExtractionOfAnchorElements {

	@Test
	public void testOnHtmlWithoutOutgoingLinks() {
		WarcRecord record = record("<html>here is some html without outgoing links</html>");
		List<String> expected = new ArrayList<>();
		List<String> actual = extractAnchors(record, 10);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithoutOutgoingLinksWithoutContext() {
		WarcRecord record = record("<html>here is some html without outgoing links</html>");
		List<String> expected = new ArrayList<>();
		List<String> actual = extractAnchors(record, 0);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithIrrelevantOutgoingLinks() {
		WarcRecord record = record("<html>here is some html with <a href=\"http://not-interesting-domain.com\">a link</a> that is removed.</html>");
		List<String> expected = new ArrayList<>();
		List<String> actual = extractAnchors(record, 10);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithIrrelevantOutgoingLinksWithoutContext() {
		WarcRecord record = record("<html>here is some html with <a href=\"http://not-interesting-domain.com\">a link</a> that is removed.</html>");
		List<String> expected = new ArrayList<>();
		List<String> actual = extractAnchors(record, 0);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithSingleRelevantOutgoingLinkWithAnchorContextOf5Characters() {
		WarcRecord record = record("<html>here is some html with <a href=\"http://target-url.com\">a link</a> that is relevant.</html>");
		List<String> expected = Arrays.asList(
			expectedJson("a link", "with a link that")
		);
		
		List<String> actual = extractAnchors(record, 5);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithSingleRelevantOutgoingLinkWithAnchorContextOf5CharactersWithoutContext() {
		WarcRecord record = record("<html>here is some html with <a href=\"http://target-url.com\">a link</a> that is relevant.</html>");
		List<String> expected = Arrays.asList(
			expectedJson("a link", null)
		);
		
		List<String> actual = extractAnchors(record, 0);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithSingleRelevantOutgoingLinkWithAnchorContextOf10Characters() {
		WarcRecord record = record("<html>here is some html with <a href=\"http://target-url.com\">a link</a> that is relevant.</html>");
		List<String> expected = Arrays.asList(
			expectedJson("a link", "html with a link that is r")
		);
		List<String> actual = extractAnchors(record, 10);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithMultipleRelevantOutgoingLinksWithAnchorContextOf10Characters() {
		WarcRecord record = record("<html>here is some html and <a href=\"http://target-url.com\">here</a> is the first link, and we have <a href=\"http://target-url.com\">here</a> also a second link.</html>");
		List<String> expected = Arrays.asList(
			expectedJson("here", " html and here is the fi"),
			expectedJson("here", "d we have here also a se")
		);
		List<String> actual = extractAnchors(record, 10);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOnHtmlWithMultipleRelevantOutgoingLinksWithAnchorContextOf10CharactersWithoutContext() {
		WarcRecord record = record("<html>here is some html and <a href=\"http://target-url.com\">here</a> is the first link, and we have <a href=\"http://target-url.com\">here</a> also a second link.</html>");
		List<String> expected = Arrays.asList(
			expectedJson("here", null),
			expectedJson("here", null)
		);
		List<String> actual = extractAnchors(record, 0);
		
		Assert.assertEquals(expected, actual);
	}
	
	private static String expectedJson(String linkText, String anchorContext) {
		if(anchorContext != null) {
			anchorContext = "\"" + anchorContext + "\"";
		}
		
		return "{\"anchorText\":\"" + linkText 
				+ "\",\"anchorContext\":" + anchorContext 
				+ ",\"targetUrl\":\"http://target-url.com\""
				+ ",\"targetMsMarcoDocIds\":[\"id0\"]"
				+ ",\"document\":{"
				+ "\"srcUrl\":\"http://www.google.de\""
				+ ",\"recordID\":\"\""
				+ ",\"trecID\":\"\""
				+ ",\"infoID\":\"\""
				+ ",\"naughtyWords\":[]}}";
	}
	
	private static WarcRecord record(String content) {
		WarcHeader header = headerWithTargetUri();
		
		WarcRecord ret = Mockito.mock(WarcRecord.class);
		Mockito.when(ret.getHeader()).thenReturn(header);
		Mockito.when(ret.getContent()).thenReturn(content);
		
		return ret;
	}
	
	private static List<String> extractAnchors(WarcRecord record, int contextSizeInCharacters) {
		return ImmutableList.copyOf(Crawler.anchorElementsInRecord(record, testFilters(), new OnlyInternalURIsFilter(null, "id0 http://target-url.com"), null, contextSizeInCharacters))
				.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	private static WarcHeader headerWithTargetUri() {
		TreeMap<String, String> header = new TreeMap<>();
		header.put("WARC-Target-URI", "http://www.google.de");
		
		WarcHeader ret = Mockito.mock(WarcHeader.class);
		Mockito.when(ret.getHeaderMetadata()).thenReturn(header);
		
		return ret;
	}
	
	private static Filters testFilters() {
		return new Filters(new OnlyInternalURIsFilter(null, "id0 http://target-url.com"));
	}
}
