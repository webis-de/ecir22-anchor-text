package scripts;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class DocumentAnchorText {
	private String id;
	private String contents;
	
	public DocumentAnchorText(String id, String contents) {
		this.id = id;
		this.contents = contents;
	}
	
	//dummy constructor for json
	public DocumentAnchorText() {
		
	}
	
	public String getId() {
		return id;
	}
	
	public String getContents() {
		return contents;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static DocumentAnchorText fromString(String in) {
		try {
			return new ObjectMapper().readValue(in, DocumentAnchorText.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
