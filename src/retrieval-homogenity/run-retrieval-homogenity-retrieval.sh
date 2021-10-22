#!/bin/bash -e

INDEX="/anserini-indexes/${INDEX_NAME}"
RUN_DIR="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/runs-retrieval-homogenity/"
TOPICS="/home/webis/thesis-probst/Data/topics.retrieval-homogenity.tsv"

if [ ! -d "${INDEX}" ];
then
    echo "File ${INDEX} does not exist."
    exit 1
fi

###############################################################################
# BM25 (conjunction)
###############################################################################
echo "Run BM25 Retrieval"

java -cp target/anserini-0.13.4-SNAPSHOT-fatjar.jar:../src/retrieval-homogenity/target/anserini-retrieval-homogenity-1.0-SNAPSHOT-jar-with-dependencies.jar io.anserini.search.SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.bm25-conjunction-default.txt \
	-querygenerator ConjunctiveBagOfWordsQueryGenerator \
	-bm25


###############################################################################
# BM25
###############################################################################
echo "Run BM25 Retrieval"

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.bm25-default.txt \
	-bm25

###############################################################################
# BM25+RM3
###############################################################################
echo "Run BM25+RM3 Retrieval"
target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.bm25+rm3-default.txt \
	-bm25 -rm3

###############################################################################
# QLD
###############################################################################
echo "Run QLD Retrieval"
target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.qld-default.txt \
	-qld

