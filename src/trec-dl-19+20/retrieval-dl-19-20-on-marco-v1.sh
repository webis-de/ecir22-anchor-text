#!/bin/bash -e

INDEX="/anserini-indexes/${INDEX_NAME}"
RUN_DIR="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/dl19+20"
TOPICS="/home/webis/thesis-probst/Data/dl19+20/dl19+20-queries.tsv"

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
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.bm25-default.txt \
	-bm25

###############################################################################
# BM25 + RM3
###############################################################################
echo "Run BM25+RM3 Retrieval"

target/appassembler/bin/SearchCollection \
	-index ${INDEX} \
	-topicreader TsvInt \
	-topics ${TOPICS} \
	-output ${RUN_DIR}/run.${INDEX_NAME}.bm25+rm3-default.txt \
	-bm25 -rm3

