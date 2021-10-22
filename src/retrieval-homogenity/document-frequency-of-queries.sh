#!/bin/bash -e

OUT="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/features-retrieval-homogenity/document-frequency-of-queries-on-${1}.jsonl"

java \
	-cp target/anserini-retrieval-homogenity-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.anserini_retrieval_homogenity.QueryTermDocumentFrequency \
	../../Data/topics.retrieval-homogenity.tsv \
	"/anserini-indexes/${1}/" > ${OUT}

