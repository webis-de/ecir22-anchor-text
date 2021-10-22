package de.webis.trec_dl_21_ltr.feature_extraction;

import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
 * TF-Similarity as feature for TREC-DL 2021.
 * 
 * @author Maik Fröbe
 *
 */
public class TfSimilarity extends ClassicSimilarity {
  @Override
  public float idf(long docFreq, long docCount) {
    return 1.0f;
  }
  
  @Override
  public float lengthNorm(int numTerms) {
    return 1.0f;
  }
  
  @Override
  public float tf(float freq) {
    return freq;
  }
}