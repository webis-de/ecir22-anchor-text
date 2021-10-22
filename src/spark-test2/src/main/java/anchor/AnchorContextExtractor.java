package anchor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AnchorContextExtractor {
	
	private final Map<Element, String> replacementTable;
	private final Map<Element, String> originalText = new LinkedHashMap<>();
	
	private final String translatedText;

	public AnchorContextExtractor(Collection<Element> elements, Document doc) {
		replacementTable = replacementTable(elements);
		translatedText = applyReplacementTable(doc);
	}

	public String getTranslatedText() {
		return translatedText;
	}
	
	private String applyReplacementTable(Document doc) {
		if(doc == null) {
			return null;
		}
		
		for(Map.Entry<Element, String> i: replacementTable.entrySet()) {
			i.getKey().text(i.getValue());
		}
		
		return doc.text();
	}
	
	private String reverseApplyReplacementTable(String in) {
		if(in == null) {
			return null;
		}
		
		for(Map.Entry<Element, String> i: replacementTable.entrySet()) {
			in = in.replace(i.getValue(), i.getKey().toString());
		}
		for(Map.Entry<Element, String> i: originalText.entrySet()) {
			in = in.replace( i.getKey().toString(), i.getValue());
		}
		
		return in;
	}

	private Map<Element, String> replacementTable(Collection<Element> elements) {
		if(elements == null) {
			return Collections.emptyMap();
		} else {
			return replacementTable(new ArrayList<>(elements));
		}
	}
	
	private Map<Element, String> replacementTable(ArrayList<Element> elements) {
		Map<Element, String> ret = new LinkedHashMap<>();
		
		for(int i=0; i<elements.size(); i++) {
			ret.put(elements.get(i), "WEBIS_REPLACEMENT_" + i);
			originalText.put(elements.get(i), elements.get(i).text());
		}
		
		return ret;
	}

	public String extractContext(Element element, int contextSizeInCharacters) {
		String anchorText = originalText.get(element);
		String replacementText = replacementTable.get(element);
		
		String ret = translatedText;
		
		int left = Math.max(0, (ret.indexOf(replacementText) - contextSizeInCharacters));
		int right = Math.min(ret.length(), (ret.indexOf(replacementText) + contextSizeInCharacters + replacementText.length()));
		
		//return ret.substring(left, right).replace(replacementText, anchorText);
		return reverseApplyReplacementTable(ret.substring(left, right).replace(replacementText, anchorText));
	}
}
