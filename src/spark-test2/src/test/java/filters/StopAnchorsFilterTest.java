package filters;

import org.junit.Test;

import anchor.AnchorElement;

import org.junit.Assert;

public class StopAnchorsFilterTest {
	
	@Test
	public void testLegitAnchorText() {
		Filter stopFilter = new StopAnchorsFilter();
		AnchorElement element = e("hello, this text is legit");
		
		Assert.assertTrue(stopFilter.filterElement(element));
	}
	
	@Test
	public void testForEmptyAnchorText() {
		Filter stopFilter = new StopAnchorsFilter();
		AnchorElement element = e("");
		
		Assert.assertFalse(stopFilter.filterElement(element));
	}
	
	@Test
	public void testForEmptyAnchorText2() {
		Filter stopFilter = new StopAnchorsFilter();
		AnchorElement element = e("+#*- ");
		
		Assert.assertFalse(stopFilter.filterElement(element));
	}
	
	@Test
	public void testForStopAnchorText() {
		Filter stopFilter = new StopAnchorsFilter();
		AnchorElement element = e("please click here");
		
		Assert.assertFalse(stopFilter.filterElement(element));
	}
	
	@Test
	public void testForStopAnchorText2() {
		Filter stopFilter = new StopAnchorsFilter();
		AnchorElement element = e("please#click+here");
		
		Assert.assertFalse(stopFilter.filterElement(element));
	}
	
	
	private AnchorElement e(String anchorText) {
		AnchorElement anchor = new AnchorElement(anchorText, "www.example.com", null);
		return anchor;
	}
}
