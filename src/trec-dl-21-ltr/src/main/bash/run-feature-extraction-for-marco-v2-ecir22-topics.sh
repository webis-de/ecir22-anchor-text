#!/bin/bash -e

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v2-nav-random \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v2-feature-extraction-ecir22/nav-random \
	--runFile /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-marco-v2-ecir22/entrypage-random/run.msmarco-doc-v2.bm25-default.txt | tee feature-extraction-marco-v2-nav-random.log

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v2-nav-popular \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v2-feature-extraction-ecir22/nav-popular \
	--runFile /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-marco-v2-ecir22/entrypage-popular/run.msmarco-doc-v2.bm25-default.txt | tee feature-extraction-marco-v2-nav-popular.log

