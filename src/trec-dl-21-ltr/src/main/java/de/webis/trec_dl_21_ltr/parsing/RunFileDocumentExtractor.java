package de.webis.trec_dl_21_ltr.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class RunFileDocumentExtractor {

  @SneakyThrows
  public static void main(String[] args) {
    Map<Integer, Set<String>> ret = new HashMap<>();
    for(String f: Arrays.asList("2021_document_top100.txt.gz", "docv2_dev2_top100.txt.gz", "docv2_dev_top100.txt.gz", "docv2_train_top100.txt.gz")) {
      ret.putAll(parseRunFile("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/" + f));
    }

    Files.write(Paths.get("/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/all-top-100.json"), new ObjectMapper().writeValueAsString(ret).getBytes());
  }

  @SneakyThrows
  @SuppressWarnings("resource")
  public static Map<Integer, Set<String>> parseRunFile(String file) {
    if(file.endsWith(".gz")) {
      return parseRunFile(new GZIPInputStream(new FileInputStream(new File(file))));
    } else {
      return parseRunFile(new FileInputStream(new File(file)));
    }
  }

  @SneakyThrows
  public static Map<Integer, Set<String>> parseRunFile(InputStream is) {
    List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_8);
    Map<Integer, Set<String>> ret = new LinkedHashMap<>();

    for(String line: lines) {
      Pair<Integer, String> topicToDoc = extractTopicAndMarcoDocOrFail(line);
      if(!ret.containsKey(topicToDoc.getLeft())) {
        ret.put(topicToDoc.getLeft(), new HashSet<>());
      }

      ret.get(topicToDoc.getLeft()).add(topicToDoc.getRight());
    }

    return ret;
  }

  static Pair<Integer, String> extractTopicAndMarcoDocOrFail(String line) {
    String[] parts = line.split("\\s+");
    if (parts.length != 6 || (!parts[2].startsWith("msmarco_doc_") && !parts[2].startsWith("D"))) {
    	throw new RuntimeException("Line is invalid '" + line + "'.");
    }
    
    return Pair.of(Integer.parseInt(parts[0]), parts[2]);
  }
}
