package filters;

import org.junit.Assert;
import org.junit.Test;

import anchor.AnchorElement;

public class ImproperTextFilterTest {
	
	@Test
	public void properTextTest() {
		Filter textFilter = new ImproperTextFilter();
		AnchorElement element = e("this is proper");
		
		Assert.assertTrue(textFilter.filterElement(element));
	}
	
	@Test
	public void longTextTest() {
		Filter textFilter = new ImproperTextFilter();
		AnchorElement element = e("thisAnchorTextContainsWayTooManyCharactersToBeConsideredProperText");
		
		Assert.assertFalse(textFilter.filterElement(element));
	}
	
	@Test
	public void tooManyWordsTest() {
		Filter textFilter = new ImproperTextFilter();
		AnchorElement element = e("a b c d e f g h i j k");
		
		Assert.assertFalse(textFilter.filterElement(element));
	}
	
	private AnchorElement e(String anchorText) {
		AnchorElement anchor = new AnchorElement(anchorText, "www.example.com", null);
		return anchor;
	}
}
