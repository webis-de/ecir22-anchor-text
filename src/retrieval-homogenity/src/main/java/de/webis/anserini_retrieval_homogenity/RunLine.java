package de.webis.anserini_retrieval_homogenity;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;

public class RunLine implements Serializable{
	
	private static final long serialVersionUID = -7320344797941507736L;
	String[] split;
	
	
	public RunLine(String[] split) {
		this.split = Arrays.copyOf(split, split.length);
	}
	
	public RunLine(String line) {
		this.split=line.split("[\\s\\t]+");
	}
	
	public int getTopic() {
		return Integer.parseInt(this.split[0]);
	}
	
	public String getDoucmentID() {
		return(this.split[2]);
	}
	
	public void setDocumentID(String id) {
		this.split[2]=id;
	}
	
	public int getRank() {
		return Integer.parseInt(this.split[3].replaceAll(",", ""));
	}
	
	private void setRank(int rank) {
		this.split[3]=""+rank;
	}
	
	public RunLine copy() {
		return new RunLine(Arrays.copyOf(split, split.length));
	}
	
	public RunLine createNewWithRankMinus(int diff) {
		try {
			RunLine res = new RunLine(this.split);
			res.setRank(this.getRank()-diff);
			return res;
		} catch (Exception e) {
//			List<String> tmp = Arrays.asList(split);
//			throw new RuntimeException("Error to minus '" + diff + "' from : " + tmp);
			return new RunLine(this.split);
		}
	}
	
	public RunLine createCopyWithDocumentID(String id) {
		RunLine res = new RunLine(this.split);
		res.setDocumentID(id);
		return res;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(split[0]);
		for(int i=1; i<split.length; i++) sb.append(' ').append(split[i]);
		return sb.toString();
	}
	
	public int hashCode() {
		return (getTopic()+getDoucmentID()).hashCode();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof RunLine)) return false;
		RunLine com = (RunLine) o;
		if(com.split.length!=split.length) return false;
		for(int i=0; i<split.length; i++) if(!split[i].equals(com.split[i])) return false;
		return true;
	}

	@SneakyThrows
	public static List<RunLine> parseRunlines(Path runFile) {
		try {
			return parseRunlines(openRunFile(runFile));
		} catch (Exception e) {
			throw new RuntimeException("Could Not parse runlines from " + runFile.toAbsolutePath().toFile().toString(), e);
		}
	}
	
	@SneakyThrows
	public static InputStream openRunFile(Path p) {
		try {
			if (p.toFile().toString().endsWith(".gz")) {
				return new DataInputStream(new GZIPInputStream(new FileInputStream(p.toFile())));
			} else {
				return new DataInputStream(new FileInputStream(p.toFile()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	public static List<RunLine> parseRunlines(InputStream stream) {
		try {
		return Collections.unmodifiableList(IOUtils.readLines(stream, StandardCharsets.UTF_8).stream()
				.filter(StringUtils::isNotEmpty)
				.map(RunLine::new)
				.collect(Collectors.toList()));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}