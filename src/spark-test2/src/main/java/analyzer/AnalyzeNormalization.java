package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.shekhargulati.urlcleaner.UrlCleaner;

public class AnalyzeNormalization {
	public static void main(String[] args) throws IOException {
		
		TreeSet<String> Urls = new TreeSet<>();
		TreeSet<String> UrlsNormalized = new TreeSet<>();
		
		
		int count = 0;
		
		BufferedReader reader = new BufferedReader(new FileReader(
				"/home/anjyc/ms-marcoUrls/ms-marco-urls.txt"));
		
		String line = reader.readLine();
		while (line != null) {
			//if (count % 100 == 0) System.out.println(count);
			
			Urls.add(line);
			
			count ++;
			line = reader.readLine();
		}
		reader.close();
		
		System.out.println("Count: " + count + ", Set: " + Urls.size());
		
		Iterator<String> UrlsIterator = Urls.iterator();
		
		while (UrlsIterator.hasNext()) {
			UrlsNormalized.add(toBase(UrlsIterator.next()));
		}
		
		System.out.println("Urls Normalized: " + UrlsNormalized.size());
		
	}
	
	private static String toBase(String in) {
		try {
		in = UrlCleaner.normalizeUrl(in.trim());
		in = StringUtils.replace(in, "https://", "");
		in = StringUtils.replace(in, "http://", "");
		in = StringUtils.substringBefore(in, "?");
		
		return in;
		} catch (Exception e) {
			return in+"neverMatch";
		}
	}
	
	
}
