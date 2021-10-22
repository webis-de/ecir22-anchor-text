#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

OPERATION="repartitionParts" INPUT_DIR='script-filtered-commoncrawl-2019-47/' COMPRESSION=TRUE PARTITIONS=100 ./src/main/bash/run-job.sh
