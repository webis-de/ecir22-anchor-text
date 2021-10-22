package filters;

import anchor.AnchorElement;

public class ImproperTextFilter implements Filter {
	/*
	 * filter class for filtering out anchorTexts with more than 10 words or 60 chars, or (if selected) anchorTexts which only consist of the target Url
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean noUrlAnchorText;
	
	public ImproperTextFilter() {
		this.noUrlAnchorText = false;
	}
	
	public ImproperTextFilter(boolean noUrlAnchorText) {
		this.noUrlAnchorText = noUrlAnchorText;
	}
	
	public boolean filterElement(AnchorElement element) {
		String anchorText = element.getAnchorText();
		String[] words = anchorText.split(" ");
		
		if (anchorText.length() > 60) {
			return false;
		} else {
			if(words.length > 10) {
				return false;
			} else {
				if(noUrlAnchorText) {
					if(anchorText.equals(element.getTargetUrl())) {
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}
	}
}