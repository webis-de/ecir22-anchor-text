#!/bin/bash -e

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--version ms-marco-v1 \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/marco-v1-feature-extraction-in-progress \
	--runFile /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources-trec-dl-20/msmarco-docdev-top100.gz | tee feature-extraction-marco-v1-dev.log

