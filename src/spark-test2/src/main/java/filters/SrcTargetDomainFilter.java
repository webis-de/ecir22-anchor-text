package filters;

import java.net.URI;

import com.shekhargulati.urlcleaner.UrlCleaner;

import anchor.AnchorElement;

public class SrcTargetDomainFilter implements Filter {
	/*
	 * filter class for filtering out intra-site anchors (anchors, which point to their source-domain)
	 */
	private static final long serialVersionUID = 1L;

	public boolean filterElement(AnchorElement element) {
		String targetUrl = element.getTargetUrl();
		String srcUrl = element.getDocument().getSrcUrl();
		
		return filterElement(srcUrl, targetUrl);
	}
	
	public static boolean filterElement(String srcUrl, String targetUrl) {
		String targetDomain = toDomain(targetUrl);
		String srcDomain = toDomain(srcUrl);
		
		return targetDomain != null && (!targetDomain.equals(srcDomain)); //returns true (keeping the anchor) if target!=src domain
	}
	
	private static String toDomain(String in) {
		try {
			
			in = UrlCleaner.normalizeUrl(in.trim());
			
			URI uri = new URI(in);
			
			String domain = uri.getHost();

			return domain;
			} catch (Exception e) {
				return in+"_ERROR";
			}
	}
}