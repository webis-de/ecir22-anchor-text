#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

OPERATION="repartitionWhole" INPUT_DIR='script-filtered-commoncrawl-2019-47/' COMPRESSION=TRUE PARTITIONS=10000 ./src/main/bash/run-job.sh
