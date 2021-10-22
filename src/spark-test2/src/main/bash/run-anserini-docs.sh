#!/bin/bash -e

if [ -z "${1}" ];
then
	echo "Can not handle ${1}..."
	exit 1
fi

spark-submit \
	--conf "spark.speculation=true" \
	--conf "spark.speculation.interval=5000ms" \
	--conf "spark.speculation.multiplier=5" \
	--conf "spark.speculation.quantile=0.90" \
	--conf "spark.dynamicAllocation.maxExecutors=1500" \
	--deploy-mode cluster \
	--master yarn \
	--class scripts.RepartitionToDocumentAnchorText \
	--conf spark.default.parallelism=500\
	--num-executors 500\
	--driver-memory 15G\
	--executor-memory 15G\
	target/spark-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
	-i "file:///mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-samples/${1}/" \
	-o "file:///mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/${1}/" \

