package preprocessors;

import anchor.AnchorElement;

public class ProcessSpecialChars implements Preprocessor {
	/*
	 * preprocessor class for removing special characters in the anchor text of an AnchorElement
	 */
	private static final long serialVersionUID = 1L;

	public AnchorElement processElement(AnchorElement element) {
		String anchorText = element.getAnchorText();
		
		anchorText = anchorText.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit} ]", "").toLowerCase();
		AnchorElement result = new AnchorElement(anchorText, element.getTargetUrl(), element.getDocument());
		
		return result;
	}
}