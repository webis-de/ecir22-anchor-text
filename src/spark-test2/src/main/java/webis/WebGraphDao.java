package webis;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Data Access Objects
 * 
 */
public class WebGraphDao {
	private WebGraphDao() {
		//hide utility class constructor
	}

	@SuppressWarnings("serial")
	public static class WebGraphNode implements Serializable {
		private String sourceURL, crawlingTimestamp;
		private List<WebGraphAnchor> anchors;
		
		public WebGraphNode() {}
		
		public WebGraphNode(String sourceURL, String crawlingTimestamp, List<WebGraphAnchor> anchors) {
			this.sourceURL = sourceURL;
			this.crawlingTimestamp = crawlingTimestamp;
			this.anchors = anchors;
		}
		
		@Override
		public String toString() {
			try {
				return new ObjectMapper().writeValueAsString(this);
			} catch(IOException e) {
				throw new RuntimeException();
			}
		}

		public static WebGraphNode fromString(String src) {
			try {
				return new ObjectMapper().readValue(src, WebGraphNode.class);
			} catch(IOException e) {
				throw new RuntimeException();
			}
		}
		
		public String getSourceURL() {
			return sourceURL;
		}
		
		public String getCrawlingTimestamp() {
			return crawlingTimestamp;
		}
		
		public List<WebGraphAnchor> getAnchors() {
			return anchors;
		}
	}
	
	@SuppressWarnings("serial")
	public static class WebGraphAnchor implements Serializable {
		private String targetURL, anchorText;
		
		public WebGraphAnchor(Element element) {
			this(element.attr("abs:href"), element.text());
		}
		
		public WebGraphAnchor(String targetURL, String anchorText) {
			this.targetURL = targetURL;
			this.anchorText = anchorText;
		}
		
		public String getTargetURL() {
			return targetURL;
		}
		
		public String getAnchorText() {
			return anchorText;
		}
	}
}
