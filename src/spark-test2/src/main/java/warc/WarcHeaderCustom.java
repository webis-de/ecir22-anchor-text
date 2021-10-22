package warc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.webis.chatnoir2.mapfile_generator.warc.WarcHeader;

@SuppressWarnings("serial")
public class WarcHeaderCustom implements Serializable {

	private String srcUrl = "";
	private String recordID = "";
	private String trecID = "";
	private String infoID = "";
	private Set<String> naughtyWords = new TreeSet<>();

	public WarcHeaderCustom(String targetURI, String recordID, String trecID, String infoID) {
		this.srcUrl = targetURI;
		this.recordID = recordID;
		this.trecID = trecID;
		this.infoID = infoID;
		
	}
	
	public WarcHeaderCustom(WarcHeader header) {
		TreeMap<String, String> meta = header.getHeaderMetadata();
		
		for (Map.Entry<String, String> thisEntry : meta.entrySet()) {
            if(thisEntry.getKey().equals("WARC-Target-URI")) srcUrl = thisEntry.getValue();
            if(thisEntry.getKey().equals("WARC-Record-ID")) recordID = thisEntry.getValue();
            if(thisEntry.getKey().equals("WARC-TREC-ID")) trecID = thisEntry.getValue();
            if(thisEntry.getKey().equals("WARC-Warcinfo-ID")) infoID = thisEntry.getValue();
        }
	}
	
	//dummy constructor
	public WarcHeaderCustom() {
		
	}
	
	public String getSrcUrl() {
		return srcUrl;
	}
	
	public String getRecordID() {
		return recordID;
	}
	
	public String getTrecID() {
		return trecID;
	}
	
	public String getInfoID() {
		return infoID;
	}
	
	public Set<String> getNaughtyWords(){
		return naughtyWords;
	}
	
	public void setNaughtyWords(Set<String> set) {
		this.naughtyWords = set;
	}
}
