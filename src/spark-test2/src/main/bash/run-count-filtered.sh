#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

for part in $(seq -w 00 99)
do
	OPERATION="countFiltered" PART=$part OUTPUT_DIR="analysis-commoncrawl-2019-47/analysis-commoncrawl-2019-47" INPUT_DIR='script-commoncrawl-2019-47/script-commoncrawl-2019-47' ./src/main/bash/run-job.sh 
done    
