# Anserini: MS MARCO Document Ranking Experiments

This document describes the steps to reproduce the BM25 results from (--insert paper here--)
in Anserini.

## 1. Installation

1.1 Make sure Java is installed and the JAVA_HOME variable is set correctly:

```
java jdk installieren: apt-get -y install openjdk-11-jdk
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
```

1.2 install maven: 

```
sudo apt-get -y install maven
```

1.3 Install Anserini using git and Maven:

```
git clone https://github.com/castorini/anserini --recurse-submodules
cd anserini
mvn clean package appassembler:assemble
```

1.2. Then we build the `tools/` directory, which contains evaluation tools and other scripts:

```
cd tools/eval && tar xvfz trec_eval.9.0.4.tar.gz && cd trec_eval.9.0.4 && make && cd ../../..
cd tools/eval/ndeval && make && cd ../../..
```


## 2. Data Prep

Now we need to obtain the datasets. For now they will be located under ```/mnt/ceph/storage/data-tmp/2021/anjyc/samples/ ```.


We can now index these datasets as a `JsonCollection` using Anserini:

2.1. Dataset "500 Character Anchor Context":

```
CC_19_47_CONTEXT500="/mnt/ceph/storage/data-tmp/2021/anjyc/samples/commoncrawl-2019-47-context-one-part/"

sh ./target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 8 \
 -input ${CC_19_47_CONTEXT500} \
 -index ./indexes/cc-19-47-context500 -optimize -storePositions -storeDocvectors -storeRaw
```

The output message should be something like this:

```
2021-05-07 16:03:37,018 INFO  [main] index.IndexCollection (IndexCollection.java:878) - Total 202,117 documents indexed in 00:36:28

```
2.2. Dataset "250 Character Anchor Context":

```
CC_19_47_CONTEXT250="/mnt/ceph/storage/data-tmp/2021/anjyc/samples/cc-19-47-context250/"

sh ./target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 8 \
 -input ${CC_19_47_CONTEXT250} \
 -index ./indexes/cc-19-47-context250 -optimize -storePositions -storeDocvectors -storeRaw
```

2.3. Dataset "Anchor Text":

```
CC_19_47_ANCHORTEXT="/mnt/ceph/storage/data-tmp/2021/anjyc/samples/cc-19-47-anchortext/"

sh ./target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 8 \
 -input ${CC_19_47_ANCHORTEXT} \
 -index ./indexes/cc-19-47-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2016-07-to-ms-marco-v2/ \
  -index ./indexes/cc-16-07-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2017-04-to-ms-marco-v2/ \
  -index ./indexes/cc-17-04-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 10 \
 -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2018-13-to-ms-marco-v2/ \
 -index ./indexes/cc-18-13-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 10 \
 -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2019-47-to-ms-marco-v2/ \
 -index ./indexes/cc-19-47-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2020-05-to-ms-marco-v2/ \
  -index ./indexes/cc-20-05-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-2021-04-to-ms-marco-v2/ \
  -index ./indexes/cc-21-04-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-union-2016-to-2021-to-ms-marco-v2/ \
  -index ./indexes/cc-union-16-to-21-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

```
./target/appassembler/bin/IndexCollection -collection JsonCollection \
  -generator DefaultLuceneDocumentGenerator -threads 10 \
  -input /mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-union-2016-to-2021-to-ms-marco-v2-1000/ \
  -index ./indexes/cc-union-16-to-21-anchortext -optimize -storePositions -storeDocvectors -storeRaw
```

2.4. Dataset "ORCAS":

```
/target/appassembler/bin/IndexCollection -collection JsonCollection \
 -generator DefaultLuceneDocumentGenerator -threads 8 \
 -input /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/samples/orcas/orcas-marco-v2-docs \
 -index ./indexes/orcas-ms-marco-v2 -optimize -storePositions -storeDocvectors -storeRaw
```

## 3. Retrieval

Topics and qrels are stored in [`src/main/resources/topics-and-qrels/`](../src/main/resources/topics-and-qrels/).
The regression experiments here evaluate on the 5193 dev set questions.

After indexing has completed, you should be able to perform retrieval as follows:

```
INDEX="cc-19-47-context500" # Please repeat the process for "cc-19-47-context250" and "cc-19-47-anchortext"

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-default.dev.txt \
 -bm25

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-default+rm3.dev.txt \
 -bm25 -rm3

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-default+ax.dev.txt \
 -bm25 -axiom -axiom.deterministic -rerankCutoff 20

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-default+prf.dev.txt \
 -bm25 -bm25prf

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-tuned.dev.txt \
 -bm25 -bm25.k1 3.5 -bm25.b 0.95

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-tuned+rm3.dev.txt \
 -bm25 -bm25.k1 3.5 -bm25.b 0.95 -rm3

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-tuned+ax.dev.txt \
 -bm25 -bm25.k1 3.5 -bm25.b 0.95 -axiom -axiom.deterministic -rerankCutoff 20

target/appassembler/bin/SearchCollection -index indexes/${INDEX} \
 -topicreader TsvInt -topics src/main/resources/topics-and-qrels/topics.msmarco-doc.dev.txt \
 -output runs/run.msmarco-doc.${INDEX}.bm25-tuned+prf.dev.txt \
 -bm25 -bm25.k1 3.5 -bm25.b 0.95 -bm25prf

```

The output message should be something like this:

```
2021-05-07 19:57:49,319 INFO  [pool-2-thread-1] search.SearchCollection$SearcherThread (SearchCollection.java:252) - 5193 topics processed in 02:48:57
2021-05-07 19:57:49,458 INFO  [main] search.SearchCollection (SearchCollection.java:733) - Total run time: 02:48:57
```

## 4. Evaluation
Evaluation can be performed using `trec_eval`:

```
INDEX="cc-19-47-context500" # Please repeat the process for "cc-19-47-context250" and "cc-19-47-anchortext"

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-default.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-default+rm3.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-default+ax.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-default+prf.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-tuned.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-tuned+rm3.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-tuned+ax.dev.txt

tools/eval/trec_eval.9.0.4/trec_eval -m map -c -m recall.100 -c -m recall.1000 -c src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt runs/run.msmarco-doc.${INDEX}.bm25-tuned+prf.dev.txt
```

## 5. Effectiveness (!Placeholder / WIP!)

With the above commands, you should be able to reproduce the following results (WIP!):

context500 (200.000 lines):

MAP                                     | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.0111    | 0.0065    | -    | -    | -    | -    | -    | -    |


R@100                                   | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.0416    | 0.0327    | -    | -    | -    | -    | -    | -    |


R@1000                                  | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| -    | -    | -    | -    | -    | -    | -    | -    |



anchortext (200.000 lines):

MAP                                     | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.0221    | -    | -    | -    | -    | -    | -    | -    |


R@100                                   | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.0568    | -    | -    | -    | -    | -    | -    | -    |


R@1000                                  | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| -    | -    | -    | -    | -    | -    | -    | -    |



anchortext (1.163.000 lines):

MAP                                     | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.0815    | 0.0687    | 0.0706    | 0.0560    | 0.0952    | 0.0843    | 0.0900    | 0.0673    |


R@100                                   | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| 0.2448    | 0.2405    | 0.2465    | 0.2303    | 0.2580    | 0.2515    | 0.2663    | 0.2351    |


R@1000                                  | BM25 (Default)| +RM3      | +Ax       | +PRF      | BM25 (Tuned)| +RM3      | +Ax       | +PRF      |
:---------------------------------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
[MS MARCO Doc Ranking: Dev](https://github.com/microsoft/MSMARCO-Document-Ranking)| -    | -    | -    | -    | -    | -    | -    | -    |




The setting "default" refers the default BM25 settings of `k1=0.9`, `b=0.4`, while "tuned" refers to the tuned setting of `k1=3.5`, `b=0.95`.
See [this page](experiments-msmarco-doc.md) for more details.
Note that here we are using `trec_eval` to evaluate the top 1000 hits for each query; beware, an official MS MARCO document ranking task leaderboard submission comprises only 100 hits per query.

Use the following commands to convert the TREC run files into the MS MARCO format and use the official eval script to compute MRR@100:

```bash
$ python tools/scripts/msmarco/convert_trec_to_msmarco_run.py --input runs/run.msmarco-doc.bm25-default.topics.msmarco-doc.dev.txt --output runs/run.msmarco-doc.bm25-default.topics.msmarco-doc.dev.msmarco.txt --k 100 --quiet
$ python tools/scripts/msmarco/msmarco_doc_eval.py --judgments src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt --run runs/run.msmarco-doc.bm25-default.topics.msmarco-doc.dev.msmarco.txt
#####################
MRR @100: 0.08143502961581157 (AnchorText 1.163.000 lines)
QueriesRanked: 5193
#####################

$ python tools/scripts/msmarco/convert_trec_to_msmarco_run.py --input runs/run.msmarco-doc.bm25-tuned.topics.msmarco-doc.dev.txt --output runs/run.msmarco-doc.bm25-tuned.topics.msmarco-doc.dev.msmarco.txt --k 100 --quiet
$ python tools/scripts/msmarco/msmarco_doc_eval.py --judgments src/main/resources/topics-and-qrels/qrels.msmarco-doc.dev.txt --run runs/run.msmarco-doc.bm25-tuned.topics.msmarco-doc.dev.msmarco.txt
#####################
MRR @100: 0.09496999605064066 (AnchorText 1.163.000 lines, tuned)
QueriesRanked: 5193
#####################
```
