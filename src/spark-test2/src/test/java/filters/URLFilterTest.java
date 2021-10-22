package filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import anchor.AnchorElement;


public class URLFilterTest {
	@Test
	public void testIdenticalURLs() {
		String id = "id0";
		String url = "https://www.google.de?bla";
		String idUrl = id+" "+url;
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,idUrl);
		
		Assert.assertTrue(filter.filterElement(e(url)));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	

	@Test
	public void testProtocolIsIgnored() {
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,"id0 https://www.google.de");
		
		Assert.assertTrue(filter.filterElement(e("http://www.google.de")));
		Assert.assertFalse(filter.filterElement(e("http://www.google.com")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testPathMustBeIdentical() {
		String id = "id0";
		String url = "https://www.google.de/bla";
		String idUrl = id+" "+url;
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,idUrl);
		
		Assert.assertTrue(filter.filterElement(e(url)));
		Assert.assertFalse(filter.filterElement(e("https://www.google.de/h2")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testURLParametersAreNotIgnored() { // if the domain uses "?" like youtube in a significant way and is not in the cutOff list
		String idUrl = "id0 https://www.youtube.com?bla";
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null, idUrl);
		
		Assert.assertFalse(filter.filterElement(e("https://www.youtube.com?foo")));
		Assert.assertFalse(filter.filterElement(e("https://www.youtube.com/h2")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testURLParametersAreIgnored() { // if the domain does not frequently use "?" and is therefore in the cutOffDomains List
		String idUrl = "id0 https://www.twitter.com?bla";
		
		String[] domains = {"twitter.com"};
		List<String> domainsList = new ArrayList<>(Arrays.asList(domains));
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(domainsList, true, idUrl);

		Assert.assertTrue(filter.filterElement(e("https://www.twitter.com?foo")));
		Assert.assertFalse(filter.filterElement(e("https://www.twitter.com/h2")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testURLEntitiesAreIgnored() {
		String idUrl = "id0 http://shekhargulati.com/~about";
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,idUrl);
		
		Assert.assertTrue(filter.filterElement(e("http://shekhargulati.com/%7Eabout/")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testURLFragmentsAreIgnored() {
		String idUrl = "id0 https://www.google.de#bla";
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,idUrl);
		
		Assert.assertTrue(filter.filterElement(e("https://www.google.de#foo")));
		Assert.assertFalse(filter.filterElement(e("https://www.google.de/h2")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	@Ignore
	public void testURLExtensionsAreIgnored() {
		String idUrl = "id0 https://www.google.de/bla.html";
		
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null,idUrl);
		
		Assert.assertTrue(filter.filterElement(e("https://www.google.de/bla")));
		Assert.assertFalse(filter.filterElement(e("https://www.google.de/h2")));
		Assert.assertFalse(filter.filterElement(e("google.com")));
	}
	
	@Test
	public void testMultipleIdsForIdenticalLink() {
		String url0 = "http://shekhargulati.com/~about";
		String url1 = "http://shekhargulati.com/%7Eabout/";
		
		String id0 = "id0";
		String id1 = "id1";
		
		String idUrl0 = id0+" "+url0;
		String idUrl1 = id1+" "+url1;
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null, idUrl0, idUrl1);
		
		List<String> ids0 = filter.getDocIds(url0);
		List<String> ids1 = filter.getDocIds(url1);
		
		Assert.assertEquals(ids0.get(0), id0);
		Assert.assertEquals(ids0.get(1), id1);
		
		Assert.assertEquals(ids1.get(0), id0);
		Assert.assertEquals(ids1.get(1), id1);
	}
	
	
	private AnchorElement e(String url) {
		AnchorElement anchor = new AnchorElement("anchor", url, null);
		return anchor;
	}
	
	
}
