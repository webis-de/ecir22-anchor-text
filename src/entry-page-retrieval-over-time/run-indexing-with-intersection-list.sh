#!/bin/bash -e

ALLOW_LIST="/mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/entry-page-retrieval-over-time/anchor-text-intersection-list-ms-marco-${VERSION}.txt"
DATASETS=( "2016-07" "2017-04" "2018-13" "2019-47" "2020-05" "2021-04" "union-2016-to-2021" )

if [ ! -f "${ALLOW_LIST}" ]
then
	echo "Please pass a version"
	exit 1
fi


for DATASET in "${DATASETS[@]}"
do
	INPUT="/mnt/ceph/storage/data-in-progress/data-research/web-search/ECIR-22/ecir22-anchor-text/anchor-text-anserini-docs/common-crawl-${DATASET}-to-ms-marco-${VERSION}/"
	echo "Process ${INPUT}"
	./target/appassembler/bin/IndexCollection \
		-collection JsonCollection \
		-whitelist ${ALLOW_LIST} \
		-input ${INPUT} \
		-generator DefaultLuceneDocumentGenerator -threads 10 \
		-index /anserini-indexes/intersection-index-${DATASET}-${VERSION} \
		-storePositions -storeDocvectors
done


