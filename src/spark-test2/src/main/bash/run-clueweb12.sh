#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

for part in $(seq 0 9)
do
	OPERATION="extract" PART=$part OUTPUT_DIR="script_test.jsonl" INPUT_DIR='/corpora/corpora-thirdparty/corpus-clueweb/12/*/*/*/*' FORMAT="CLUEWEB12" ./src/main/bash/run-job.sh 
done    
