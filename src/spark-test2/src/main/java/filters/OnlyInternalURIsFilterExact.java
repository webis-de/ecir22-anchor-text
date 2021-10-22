package filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.shekhargulati.urlcleaner.UrlCleaner;

import anchor.AnchorElement;

@SuppressWarnings("serial")
public class OnlyInternalURIsFilterExact implements Filter {
	/*
	 * filter class for filtering out anchors which aren't linking to a specific list of sites (used to isolate links which relate to the corpus) (does not simplify url's)
	 */
	
	public static void main(String[] args) {
		OnlyInternalURIsFilterExact filter = new OnlyInternalURIsFilterExact(new File("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/ms-marco-urls.txt"), new File("/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/args-me-urls.txt"));
		AnchorElement element1 = new AnchorElement(null, "https://www.debate.org/debates/Contraceptive-Forms-for-High-School-Students/1", null);
		AnchorElement element2 = new AnchorElement(null, "http://www.sikh-history.com/sikhhist/gurus/nanak3.html", null);
		System.out.println(filter.filterElement(element1));
		System.out.println(filter.filterElement(element2));
	}	
	private final HashSet<String> URIs;
	
	public OnlyInternalURIsFilterExact (Collection<String> IdURIs) {
		this.URIs = new HashSet<>();
		
		for (String idUri: IdURIs) {
			String[] parts;
			//String id="NOT_YET_SET";
			String base;
			try {
				parts = idUri.split(" ");
				base  = toBase(parts[1]);
			}catch(Exception e) {
				break;
			}
			this.URIs.add(base);
		}
	}
	
	public OnlyInternalURIsFilterExact (File... files) {
		this(extractAllLinesFrom(files));
		
	}
	
	public OnlyInternalURIsFilterExact (String... URIs) {
		this(Arrays.asList(URIs));
	}
	
	public boolean filterElement(AnchorElement element) {
		String currentURI = toBase(element.getTargetUrl());
		
		if (URIs.contains(currentURI)) {
			return true;
		}
		
		return false;
	}
	
	public static OnlyInternalURIsFilterExact productionFilter(String keepLinksPath) {
		File folder = new File(keepLinksPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> ret = new ArrayList<>();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        ret.add(file);
		    }
		}

		return new OnlyInternalURIsFilterExact(ret.toArray(new File[0]));
	}
	
	public static String toBase(String in) {
		try {
		in = UrlCleaner.normalizeUrl(in.trim());
		
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