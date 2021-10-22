package de.webis.trec_dl_21_ltr.feature_extraction;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.bn.BengaliAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.sv.SwedishAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.tr.TurkishAnalyzer;

import io.anserini.analysis.DefaultEnglishAnalyzer;
import io.anserini.analysis.TweetAnalyzer;
import io.anserini.search.SearchArgs;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@UtilityClass
public class AnalyzerUtil {

  private static final Logger LOG = LogManager.getLogger(AnalyzerUtil.class);

  @SneakyThrows
  public static Analyzer analyzer(SearchArgs args) {
	// Copied from SearchCollection#SearchCollection(SearchArgs args)
    if (args.searchtweets) {
      LOG.info("Searching tweets? true");
      return new TweetAnalyzer();
    } else if (args.language.equals("ar")) {
      LOG.info("Language: ar");
      return new ArabicAnalyzer();
    } else if (args.language.equals("bn")) {
      LOG.info("Language: bn");
      return new BengaliAnalyzer();
    } else if (args.language.equals("da")) {
      LOG.info("Language: da");
      return new DanishAnalyzer();
    } else if (args.language.equals("de")) {
      LOG.info("Language: de");
      return new GermanAnalyzer();
    } else if (args.language.equals("es")) {
      LOG.info("Language: es");
      return new SpanishAnalyzer();
    } else if (args.language.equals("fi")) {
      LOG.info("Language: fi");
      return new FinnishAnalyzer();
    } else if (args.language.equals("fr")) {
      LOG.info("Language: fr");
      return new FrenchAnalyzer();
    } else if (args.language.equals("hi")) {
      LOG.info("Language: hi");
      return new HindiAnalyzer();
    } else if (args.language.equals("hu")) {
      LOG.info("Language: hu");
      return new HungarianAnalyzer();
    } else if (args.language.equals("id")) {
      LOG.info("Language: id");
      return new IndonesianAnalyzer();
    } else if (args.language.equals("it")) {
      LOG.info("Language: it");
      return new ItalianAnalyzer();
    } else if (args.language.equals("ja")) {
      LOG.info("Language: ja");
      return new JapaneseAnalyzer();
    } else if (args.language.equals("ko")) {
      LOG.info("Language: ko");
      return new CJKAnalyzer();
    } else if (args.language.equals("nl")) {
      LOG.info("Language: nl");
      return new DutchAnalyzer();
    } else if (args.language.equals("no")) {
      LOG.info("Language: no");
      return new NorwegianAnalyzer();
    } else if (args.language.equals("pt")) {
      LOG.info("Language: pt");
      return new PortugueseAnalyzer();
    } else if (args.language.equals("ru")) {
      LOG.info("Language: ru");
      return new RussianAnalyzer();
    } else if (args.language.equals("sv")) {
      LOG.info("Language: sv");
      return new SwedishAnalyzer();
    } else if (args.language.equals("th")) {
      LOG.info("Language: th");
      return new ThaiAnalyzer();
    } else if (args.language.equals("tr")) {
      LOG.info("Language: tr");
      return new TurkishAnalyzer();
    } else if (args.language.equals("zh")) {
      LOG.info("Language: zh");
      return new CJKAnalyzer();
    } else if (args.pretokenized) {
      LOG.info("Pretokenized");
      return new WhitespaceAnalyzer();
    } else {
      // Default to English
      LOG.info("Language: en");
      LOG.info("Stemmer: " + args.stemmer);
      LOG.info("Keep stopwords? " + args.keepstop);
      LOG.info("Stopwords file " + args.stopwords);
      return DefaultEnglishAnalyzer.fromArguments(args.stemmer, args.keepstop, args.stopwords);
    }
  }
}
