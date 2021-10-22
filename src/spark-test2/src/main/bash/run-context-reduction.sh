#!/bin/bash

( ! echo $TERM | grep -q 'screen' ) && { echo 'NOT A SCREEN SESSION'; exit -1; }

OPERATION="context-reduction" INPUT_DIR='script-filtered-commoncrawl-2019-47/' SIZEOFCONTEXT=250 ./src/main/bash/run-job.sh
