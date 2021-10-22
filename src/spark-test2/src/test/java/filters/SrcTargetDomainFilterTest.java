package filters;

import org.junit.Test;

import anchor.AnchorElement;
import warc.WarcHeaderCustom;

import org.junit.Assert;

public class SrcTargetDomainFilterTest {
	
	@Test
	public void testSrcEqTarget() {
		Filter srcTargetFilter = new SrcTargetDomainFilter();
		AnchorElement element = e("http://www.example.com","http://www.example.com");
		
		Assert.assertFalse(srcTargetFilter.filterElement(element));
	}
	
	@Test
	public void testSrcNotEqTarget() {
		Filter srcTargetFilter = new SrcTargetDomainFilter();
		AnchorElement element = e("http://www.example.com","http://www.beispiel.de");
		
		Assert.assertTrue(srcTargetFilter.filterElement(element));
	}
	
	@Test
	public void testSrcEqTargetDomain() {
		Filter srcTargetFilter = new SrcTargetDomainFilter();
		AnchorElement element = e("http://www.google.com/bla/blub","http://www.google.com/other/path");
		
		Assert.assertFalse(srcTargetFilter.filterElement(element));
	}
	
	
	private AnchorElement e(String srcUrl, String targetUrl) {
		WarcHeaderCustom document = new WarcHeaderCustom(targetUrl, "","","");
		AnchorElement anchor = new AnchorElement("anchorText", srcUrl, document);
		return anchor;
	}
}
