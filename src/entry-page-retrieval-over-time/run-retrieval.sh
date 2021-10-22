#!/bin/bash -e

DATASETS=( "2016-07" "2017-04" "2018-13" "2019-47" "2020-05" "2021-04" "union-2016-to-2021" )

for DATASET in "${DATASETS[@]}"
do
	INDEX_NAME=intersection-index-${DATASET}-v2 ../src/entry-page-retrieval-over-time/navigational-retrieval-marco-v2.sh
	INDEX_NAME=intersection-index-${DATASET}-v1 ../src/entry-page-retrieval-over-time/navigational-retrieval-marco-v1.sh
done

