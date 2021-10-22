package analyzer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.shekhargulati.urlcleaner.UrlCleaner;

public class AnalyzeDomains {

	public static void main(String[] args) throws IOException, URISyntaxException {
		HashMap<String,List<String>> domainToUrls = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				"/home/anjyc/ms-marcoUrls/ms-marco-ids-urls.csv"));
				//"/home/anjyc/ms-marcoUrls/testUrls.csv"));
		
		String output = "/home/anjyc/outputs/output.txt";
		
		int count = 0;
		int countErrors = 0;
		
		String line = reader.readLine();
		while (line != null) {
			if (count % 100 == 0) System.out.println(count);
			
			String url = getUrl(line);
			String domain = getDomain(url);
			
			if (domain != null) {
				if (domainToUrls.containsKey(domain)) {
					domainToUrls.get(domain).add(url);
				}else {
					List<String> urls = new ArrayList<String>();
					urls.add(url);
					domainToUrls.put(domain,urls);
					
				}
			} else {
				countErrors++;
			}
			
			count ++;
			line = reader.readLine();
		}
		reader.close();
		System.out.println("done reading. Urls read: "+count+", errors encountered: "+countErrors);
		
		List<String> outUrls = new ArrayList<String>();
		
		int count2 = 0;
		
		for (Entry<String, List<String>> entry : domainToUrls.entrySet()) {
			String domain = entry.getKey();
		    List<String> urls = entry.getValue();
		    
		    int size = urls.size();
			Set<String> baseUrls = new HashSet<String>();
			
			if(size >= 100) {
				for (String url : urls) {
					baseUrls.add(StringUtils.substringBefore(url, "?"));
				}
				int sizeBaseUrls = baseUrls.size();
				
				if(sizeBaseUrls > size*0.995) {
					outUrls.add(domain);
				}
			}
			
			
			if (count2 % 100 == 0) System.out.println(count2);
			count2++;
		}
		
		outUrls.sort(Comparator.comparing( String::toString ));
		
		System.out.println(
					  "urls read: " + count + "\n"
					+ "# of domains: " + count2 + "\n"
					+ "domains to be cut: " + outUrls.size()
					);
		
		 try {
		      FileWriter myWriter = new FileWriter(output);
		      
		      for (String domain : outUrls) {
		    	  myWriter.write(domain + "\n");
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	private static String getUrl(String line) {
		String[] parts = line.split(" ");
		String Url  = parts[1];
		
		return Url;
	}
	
	private static String getDomain(String url) {
		URI uri;
		String domain = null;
		try {
			
			url = UrlCleaner.normalizeUrl(url.trim());
			uri = new URI(url);
			
			domain = uri.getHost();
		} catch(Exception e) {
			//System.out.println(e);
		}
		
	    return domain;
	}

}
