package scripts;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import anchor.AnchorElement;
import scripts.LowerContextSize;

public class LowerContextSizeTest {
	
	@Test
	public void testNormalUseCase() {
		
		AnchorElement elem = e("AAAA-anchor-BBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);
		
		Assert.assertEquals("AA-anchor-BB", elemShort.getAnchorContext());
	}
	
	@Test
	public void testContextTooSmall() {
		
		AnchorElement elem = e("AAAA-anchor-BBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 50);
		
		Assert.assertEquals("AAAA-anchor-BBBB", elemShort.getAnchorContext());
	}
	
	@Test
	public void testContextSmallerThanAnchor() {
		
		AnchorElement elem = e("AAAA-anchor-BBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 3);
		
		System.out.println(elemShort.getAnchorContext());
		Assert.assertEquals("anchor", elemShort.getAnchorContext());
	}
	
	@Test
	public void testUnsymmetricalContext() {
		
		AnchorElement elem = e("AAA-anchor-BBBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);
		
		Assert.assertEquals("AA-anchor-BB", elemShort.getAnchorContext());
	}
	
	@Test
	public void testVeryUnsymmetricalContextA() {
		
		AnchorElement elem = e("AAAA-anchor");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);

		Assert.assertEquals("AA-anchor", elemShort.getAnchorContext());
	}
	
	@Test
	public void testVeryUnsymmetricalContextB() {
		
		AnchorElement elem = e("anchor-BBBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);
		
		Assert.assertEquals("anchor-BB", elemShort.getAnchorContext());
	}
	
	@Test
	public void testMultipleAnchorTextsA() {
		
		AnchorElement elem = e("A-anchor-anchor-BBBBBBBB");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);
		
		Assert.assertEquals("or-anchor-BB", elemShort.getAnchorContext());
	}
	
	@Test
	public void testMultipleAnchorTextsB() {
		
		AnchorElement elem = e("AAAAAAAA-anchor-anchor-B");
		
		AnchorElement elemShort = LowerContextSize.shortenString(elem, 12);
		
		Assert.assertEquals("AA-anchor-an", elemShort.getAnchorContext());
	}
	
	
	private AnchorElement e(String context) {
		AnchorElement anchor = new AnchorElement("anchor", context, "http://www.example.com", null, null);
		return anchor;
	}
	
	
}
