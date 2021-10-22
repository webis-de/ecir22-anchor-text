#!/bin/bash -e

java -jar target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	--indexDir /anserini-indexes \
	--outputDir /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/feature-extraction-in-progress \
	--runFile /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/resources/docv2_dev_top100.txt.gz | tee feature-extraction-dev.log

