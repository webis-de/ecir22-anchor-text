package anchor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class NaughtyWordsExtractor implements Serializable{
	private List<String> naughtyWords = new ArrayList<>();
	private List<String> naughtyWordGroups = new ArrayList<>();
	
	public NaughtyWordsExtractor(List<String> naughtyWordList) {
		for(String line : naughtyWordList) {
			String[] words = line.split(" ");
			if(words.length == 1) {
				naughtyWords.add(line);
			} else {
				naughtyWordGroups.add(line);
			}
		}
	}
	
	public NaughtyWordsExtractor(String pathToNaughtyWords) {
		this(readWords(pathToNaughtyWords));
	}
	
	public Set<String> extractNaughtyWords(AnchorElement anchor){
		String anchorTextAndContext = anchor.getAnchorText() + " " + anchor.getAnchorContext();
		
		return extractNaughtyWords(anchorTextAndContext);
	}
	
	public Set<String> extractNaughtyWords(String input){
		Set<String> ret = new TreeSet<>();
		
		Set<String> anchorWords = new HashSet<>(Arrays
										.asList(input
										.replaceAll("[^\\p{IsAlphabetic}0-9]", " ")
										.toLowerCase()
										.split(" ")));
		
		for (String word : naughtyWords) {
			if(anchorWords.contains(word)) ret.add(word);
		}
		
		for (String wordGroup : naughtyWordGroups) {
			if(input.contains(wordGroup)) ret.add(wordGroup);
		}
		
		return ret;
	}
	
	private static List<String> readWords (String path){
		List<String> list = new ArrayList<>();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					path));
			String line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
