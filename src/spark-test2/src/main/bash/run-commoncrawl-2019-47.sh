#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

for part in $(seq -w 00 01)
do
	OPERATION="extract" PART=$part OUTPUT_DIR="cc-20-05-v2/script-commoncrawl-2020-05" INPUT_DIR='s3a://corpus-commoncrawl-main-2020-05/crawl-data/CC-MAIN-2020-05/segments/*/*/*' FORMAT="COMMON_CRAWL" ./src/main/bash/run-job.sh
done    
