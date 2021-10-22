package anchor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import warc.WarcHeaderCustom;

@SuppressWarnings("serial")
public class AnchorElement implements Serializable {
	
	private String anchorText;
	private String anchorContext;
	private String targetUrl;
	private List<String> targetMsMarcoDocIds = new ArrayList<>();
	private WarcHeaderCustom document;
	
	public AnchorElement(String anchor, String url, WarcHeaderCustom header) {
		this.anchorText = anchor;
		this.targetUrl = url;
		this.document =  header;
	}
	
	public AnchorElement(String anchor, String anchorContext, String url, List<String> targetMsMarcoDocIds, WarcHeaderCustom header) {
		this.anchorText = anchor;
		this.anchorContext = anchorContext;
		this.targetUrl = url;
		this.targetMsMarcoDocIds = targetMsMarcoDocIds;
		this.document =  header;
	}
	
	//dummy constructor for json
	public AnchorElement() {
		
	}
	
	public String getAnchorText() {
		return anchorText;
	}

	public String getAnchorContext() {
		return anchorContext;
	}
	
	public void setAnchorContext(String anchorContext) {
		this.anchorContext = anchorContext;
	}
	
	public String getTargetUrl() {
		return targetUrl;
	}
	
	public WarcHeaderCustom getDocument() {
		return document;
	}
	
	public List<String> getTargetMsMarcoDocIds() {
		return targetMsMarcoDocIds;
	}
	
	public void setTargetMsMarcoDocIds(List<String> ids) {
		targetMsMarcoDocIds = ids;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static AnchorElement fromString(String in) {
		try {
			return new ObjectMapper().readValue(in, AnchorElement.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
