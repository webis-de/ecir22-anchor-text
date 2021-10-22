( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

OPERATION="combineSamples" INPUT_DIR='script-filtered-commoncrawl-2019-47-repartitioned-100context-sample200-context/*PART-??.jsonl' INPUT_TYPE="PARTS" ./src/main/bash/run-job.sh

OPERATION="combineSamples" INPUT_DIR='script-filtered-commoncrawl-2019-47-repartitioned-50context-sample200-context/*PART-??.jsonl' INPUT_TYPE="PARTS" ./src/main/bash/run-job.sh

