#!/bin/bash -e

INDEX="/anserini-indexes/${INDEX_NAME}"
RUN_DIR="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/"
TOPIC_DIR="/home/webis/thesis-probst/Data/navigational-topics-and-qrels-ms-marco-v1/"

if [ ! -d "${INDEX}" ];
then
    echo "File ${INDEX} does not exist."
    exit 1
fi


###############################################################################
# BM25
###############################################################################
echo "Run BM25 Retrieval"

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPIC_DIR}/topics.msmarco-entrypage-random.tsv \
	-output ${RUN_DIR}/entrypage-random/run.${INDEX_NAME}.bm25-default.txt \
	-bm25

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPIC_DIR}/topics.msmarco-entrypage-popular.tsv \
	-output ${RUN_DIR}/entrypage-popular/run.${INDEX_NAME}.bm25-default.txt \
	-bm25


###############################################################################
# BM25 + RM3
###############################################################################
echo "Run BM25+RM3 Retrieval"

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPIC_DIR}/topics.msmarco-entrypage-random.tsv \
	-output ${RUN_DIR}/entrypage-random/run.${INDEX_NAME}.bm25+rm3-default.txt \
	-bm25 -rm3

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPIC_DIR}/topics.msmarco-entrypage-popular.tsv \
	-output ${RUN_DIR}/entrypage-popular/run.${INDEX_NAME}.bm25+rm3-default.txt \
	-bm25 -rm3

