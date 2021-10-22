package filters;

import java.util.ArrayList;
import java.util.Arrays;

import anchor.AnchorElement;

public class StopAnchorsFilter implements Filter {
	/*
	 * filter class for filtering out anchors which contain one or more "stop words" (like "click", or "read") aswell as empty text
	 */
	private static final long serialVersionUID = 1L;

	public boolean filterElement(AnchorElement element) {
		String anchorText = element.getAnchorText();
		
		String[] words = anchorText.replaceAll("[^\\p{IsAlphabetic}]", " ").toLowerCase().split(" ");
		if (words.length == 0 || (words.length == 1 && words[0].equals(""))) return false; 
			//returns false if anchor text is empty or only contains spaces
		
		
		ArrayList <String> stopWords = new ArrayList<>(Arrays.asList("click", "read", "mail", "here", "open", "link"));
		
		for(String word : words) {
			if (stopWords.contains(word.toLowerCase())) return false;	//returns false if anchor text contains a stop word
		}

		return true;
	}
}