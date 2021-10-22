package filters;

import anchor.AnchorElement;

public class EnglishFilter implements Filter {
	/*
	 * filter class for filtering out anchors which aren't linking English sites (only works for ClueWeb)
	 */
	private static final long serialVersionUID = 1L;

	public boolean filterElement(AnchorElement element) {
		String trecId = element.getDocument().getTrecID();
		
		if(trecId.length() < 12) {
			return false;
		}else {
			if (trecId.charAt(10) == 'e' && trecId.charAt(11) == 'n') {
				return true;
			}else {
				return false;
			}
		}
	}
}