#!/bin/bash -e

PARALLELISM=500

./src/main/bash/run-in-docker-container-with-spark.sh spark-submit \
        --conf "spark.speculation=true" \
        --conf "spark.speculation.interval=5000ms" \
        --conf "spark.speculation.multiplier=5" \
        --conf "spark.speculation.quantile=0.90" \
        --conf "spark.dynamicAllocation.maxExecutors=1500" \
        --deploy-mode cluster \
	--master yarn \
        --class ${CLASS} \
        --conf spark.default.parallelism=${PARALLELISM}\
        --num-executors ${PARALLELISM}\
        --driver-memory 30G\
	--executor-memory 15G\
        /target/spark-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar ${@}
