#!/bin/bash -e

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v1-dl \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-ecir22/dl \
	--runFile /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/dl19+20/run.ms-marco-content.bm25-default.txt | tee feature-extraction-marco-v1-dl.log

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v1-nav-random \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-ecir22/nav-random \
	--runFile /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/entrypage-random/run.ms-marco-content.bm25-default.txt | tee feature-extraction-marco-v1-nav-random.log

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v1-nav-popular \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-ecir22/nav-popular \
	--runFile /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/runs-ecir22/entrypage-popular/run.ms-marco-content.bm25-default.txt | tee feature-extraction-marco-v1-nav-popular.log

