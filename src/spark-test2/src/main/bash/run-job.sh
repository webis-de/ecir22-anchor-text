#!/bin/bash -e

if [ "${OPERATION}" = "extract" ]; then
	src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.jsonl" 
	CLASS="Main" src/main/bash/run-anchor-text-extraction-job.sh \
		--inputFormat $FORMAT \
		--input "${INPUT_DIR}${PART}.warc.gz" \
		--output "${OUTPUT_DIR}-PART-${PART}.jsonl"
fi

if [ "${OPERATION}" = "analyze" ]; then
	src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="analyzer.AnalyzeAnchors" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}-PART-${PART}.jsonl/*.gz" \
		--output "${OUTPUT_DIR}-PART-${PART}.txt"
fi

if [ "${OPERATION}" = "context-reduction" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}" 
	CLASS="scripts.LowerContextSize" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}" \
		--sizeOfContext ${SIZEOFCONTEXT}
fi

if [ "${OPERATION}" = "combineSamples" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="scripts.CombineSamples" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}/part-*.gz" \
		--inputType "${INPUT_TYPE}"
fi

if [ "${OPERATION}" = "countFiltered" ]; then
	src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="analyzer.CountFiltered" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}-PART-${PART}.jsonl/*.gz" \
		--output "${OUTPUT_DIR}-PART-${PART}.txt"
fi

if [ "${OPERATION}" = "repartitionParts" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}" 
	CLASS="scripts.Repartition" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}" \
		--compression "${COMPRESSION}" \
		--numberOfPartitions ${PARTITIONS}
fi

if [ "${OPERATION}" = "repartitionWhole" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}" 
	CLASS="scripts.Repartition" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}*-PART-*.jsonl/*.gz" \
		--compression "${COMPRESSION}" \
		--numberOfPartitions ${PARTITIONS} \
		--whole true
fi

if [ "${OPERATION}" = "sampleAllParts" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="scripts.Sample" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}*-PART-*.jsonl/*.gz" \
		--sampleSize ${SAMPLE_SIZE} \
		--anchorInformationType ${INFORMATION_TYPE} \
		--inputType PARTS \
		--outputType WHOLE
fi

if [ "${OPERATION}" = "sampleParts" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="scripts.Sample" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}" \
		--sampleSize ${SAMPLE_SIZE} \
		--anchorInformationType ${INFORMATION_TYPE} \
		--inputType PARTS \
		--outputType PARTS
fi

if [ "${OPERATION}" = "sampleWhole" ]; then
	#src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f "${OUTPUT_DIR}-PART-${PART}.txt" 
	CLASS="scripts.Sample" src/main/bash/run-anchor-text-extraction-job.sh \
		--input "${INPUT_DIR}/*.gz" \
		--sampleSize ${SAMPLE_SIZE} \
		--anchorInformationType ${INFORMATION_TYPE} \
		--inputType WHOLE \
		--outputType WHOLE
fi
