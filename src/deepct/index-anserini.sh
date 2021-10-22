#!/bin/bash -e

IN_DIR="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/expanded-datasets/msmarco-document-passages${MARCO_VERSION}/${DATASET}"

if [ ! -d "${IN_DIR}" ]
then
	echo "could not find ${IN_DIR}"
fi

./target/appassembler/bin/IndexCollection \
	-collection JsonCollection \
	-generator DefaultLuceneDocumentGenerator \
	-threads 8 \
	-input ${IN_DIR} \
	-index ./indexes/ms-marco-deepct${MARCO_VERSION}-${DATASET} \
	-optimize \
	-storePositions \
	-storeDocvectors

