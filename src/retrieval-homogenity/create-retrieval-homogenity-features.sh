#!/bin/bash -e

RUN_DIR="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/runs-retrieval-homogenity/"
OUT="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/features-retrieval-homogenity/${1}.jsonl"

java -jar \
	target/anserini-retrieval-homogenity-1.0-SNAPSHOT-jar-with-dependencies.jar \
	${RUN_DIR}${1} "/anserini-indexes/${2}/" > ${OUT}

