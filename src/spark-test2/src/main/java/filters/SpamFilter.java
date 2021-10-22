package filters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import anchor.AnchorElement;

public class SpamFilter implements Filter {
	/*
	 * filter class for filtering out anchors which contain one or more "stop words" (like "click", or "read") aswell as empty text
	 */
	private static final long serialVersionUID = 1L;

	public boolean filterElement(AnchorElement element) {
		String elementId = element.getDocument().getTrecID();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("clueweb09spam.Fusion"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line="";
		try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] spam = {"", ""};
		
		while(line != null) {
			spam = line.split(" ");
			
			if (spam[1].equals(elementId)) {
				if (Integer.parseInt(spam[0]) < 70) {
					return false;
				} else {
					return true;
				}
			}
		}
		
		return false;
		
	}
}