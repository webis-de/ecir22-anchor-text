package filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.shekhargulati.urlcleaner.UrlCleaner;

import anchor.AnchorElement;

@SuppressWarnings("serial")
public class OnlyInternalURIsFilter implements Filter {
	/*
	 * filter class for filtering out anchors which aren't linking to a specific list of sites (used to isolate links which relate to the corpus)
	 */
	
	private final HashMap<String, List<String>> normalizedUrlToDocIds;
	private final static Set<String> cutOffDomains = new HashSet<>();
	
	public OnlyInternalURIsFilter (String pathToCutOffDomains, Collection<String> IdsUris) {
		
		if (pathToCutOffDomains != null && !pathToCutOffDomains.equals("")) {
			
		
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(
						pathToCutOffDomains));
				String line = reader.readLine();
				while (line != null) {
					cutOffDomains.add(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.normalizedUrlToDocIds = new HashMap<>();
		for(String IdUri :  IdsUris) {
			String[] parts;
			String id="NOT_YET_SET";
			String base;
			try {
				parts = IdUri.split(" ");
				id = parts[0];
				base  = toBase(parts[1]);
			}catch(Exception e) {
				break;
			}
			
			
			if(normalizedUrlToDocIds.containsKey(base)) {
				normalizedUrlToDocIds.get(base).add(id);
			} else {
				List<String> ids = new ArrayList<String>();
				ids.add(id);
				
				this.normalizedUrlToDocIds.put(base, ids);
			}
		}
	}
	
	public OnlyInternalURIsFilter (String pathToCutOffDomains, File... files) {
		this(pathToCutOffDomains, extractAllLinesFrom(files));
		
	}
	
	public OnlyInternalURIsFilter (String pathToCutOffDomains, String... IdsUris) {
		this(pathToCutOffDomains, Arrays.asList(IdsUris));
	}
	
	public OnlyInternalURIsFilter (List<String> cutOffDomainsList, boolean onlyForTestingPurposes, String... IdsUris) {
		for(String domain : cutOffDomainsList) {
			cutOffDomains.add(domain);
		}
		
		this.normalizedUrlToDocIds = new HashMap<>();
		for(String IdUri :  IdsUris) {
			
			String[] parts = IdUri.split(" ");
			String id = "";
			String base = "";
			try {
				id = parts[0];
				base  = toBase(parts[1]);
			}catch(Exception e) {
				System.out.println("ERROR! IdUri= " + IdUri + " part[0]= " + parts[0] + "\n");
				throw e;
			}
			
			
			if(normalizedUrlToDocIds.containsKey(base)) {
				normalizedUrlToDocIds.get(base).add(id);
			} else {
				List<String> ids = new ArrayList<String>();
				ids.add(id);
				
				this.normalizedUrlToDocIds.put(base, ids);
			}
		}
	}
	
	public boolean filterElement(AnchorElement element) {
		return filterElement(element.getTargetUrl());
	}
	
	public boolean filterElement(String targetUrl) {
		String currentURI = toBase(targetUrl);
		
		return normalizedUrlToDocIds.containsKey(currentURI);
	}
	
	
	public List<String> getDocIds(String url){
		return normalizedUrlToDocIds.get(toBase(url));
	}
	
	public static OnlyInternalURIsFilter productionFilter(String pathToCutOffDomains, String keepLinksPath) {
		File folder = new File(keepLinksPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> ret = new ArrayList<>();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        ret.add(file);
		    }
		}

		return new OnlyInternalURIsFilter(pathToCutOffDomains, ret.toArray(new File[0]));
	}
	
	public static String toBase(String in) {
		try {
			
			in = UrlCleaner.normalizeUrl(in.trim());
			
			URI uri = new URI(in);
			
			String domain = uri.getHost();
			
			boolean cutDomain = cutOffDomains.contains(domain);
			
			in = StringUtils.replace(in, "https://", "");
			in = StringUtils.replace(in, "http://", "");
		
			if(cutDomain) in = StringUtils.substringBefore(in, "?");

			return in;
			} catch (Exception e) {
				return in+"neverMatch";
			}
	}
	
	private static List<String> extractAllLinesFrom(File...files) {
		List<String> ret = new ArrayList<>();
		
		for (File file : files) {
			try {
				ret.addAll(Files.readAllLines(file.toPath()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return ret;
	}
}