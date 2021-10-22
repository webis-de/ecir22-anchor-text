#!/bin/bash -e

if [ -z "${1}" ];
then
	echo "Can not handle ${1}..."
	exit 1
fi

for PART in $(seq 0 9)
do
	echo -e "\nRun part: ${PART}\n\n"

	spark-submit \
		--conf "spark.speculation=true" \
		--conf "spark.speculation.interval=5000ms" \
		--conf "spark.speculation.multiplier=5" \
		--conf "spark.speculation.quantile=0.90" \
		--conf "spark.dynamicAllocation.maxExecutors=1500" \
		--deploy-mode cluster \
		--master yarn \
		--class scripts.SampleAnchorTexts \
		--conf spark.default.parallelism=500\
		--num-executors 500\
		--driver-memory 15G\
		--executor-memory 15G\
		target/spark-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
		-s 1000 \
		-i "../anjyc/${1}/*${PART}.jsonl/" \
		-o "ecir22-filtered-commoncrawl-${1}-sample/part-${PART}"
done

