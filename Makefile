run-all-parts-commoncrawl:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-commoncrawl-2019-47.sh


run-all-parts-clueweb12:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-clueweb12.sh

analyze:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-analysis.sh

combine-samples:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-combine-samples.sh

context-reduction:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-context-reduction.sh

custom-script:
	cd src/spark-test2 && \
	bash ./src/main/bash/custom-script.sh

sample-parts:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-sample-parts.sh

sample-all-parts:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-sample-all-parts.sh

sample-whole:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-sample-whole.sh

repartition-parts:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-repartition-parts.sh

repartition-whole:
	cd src/spark-test2 && \
	bash ./src/main/bash/run-repartition-whole.sh

run-job:
	cd src/spark-test2 && \
	./src/main/bash/run-job.sh \
	-f CLUEWEB12 \
	-o clueweb-run-job-test # .jsonl nicht nötig

commoncrawl-2019-47-small:
	cd src/spark-test2 && \
	./src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f commoncrawl-2019-47-small-keep-links-test.jsonl && \
	./src/main/bash/run-anchor-text-extraction-job.sh \
		--inputFormat COMMON_CRAWL \
		--input 's3a://corpus-commoncrawl-main-2019-47/crawl-data/CC-MAIN-2019-47/segments/1573496670743.44/warc/CC-MAIN-20191121074016-20191121102016-00559.warc.gz' \
		--keepLinks /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/keepLinks/ \
		--output commoncrawl-2019-47-small-keep-links-test.jsonl

clueweb12:
	cd src/spark-test2 && \
	./src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f first-test.jsonl && \
	./src/main/bash/run-anchor-text-extraction-job.sh \
		--inputFormat CLUEWEB12 \
		--input '/corpora/corpora-thirdparty/corpus-clueweb/12/*/*/*/*.warc.gz' \
		--output first-test.jsonl

clueweb12-test:
	cd src/spark-test2 && \
        ./src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f clueweb12-test.jsonl && \
        ./src/main/bash/run-anchor-text-extraction-job.sh \
                --inputFormat CLUEWEB12 \
                --input '/corpora/corpora-thirdparty/corpus-clueweb/12/Disk1/ClueWeb12_00/0013wb/*.warc.gz' \
                --output clueweb12-test.jsonl

clueweb12-s3-test:
	cd src/spark-test2 && \
        ./src/main/bash/run-in-docker-container-with-spark.sh hdfs dfs -rm -r -f clueweb12-s3-test.jsonl && \
        ./src/main/bash/run-anchor-text-extraction-job.sh \
                --inputFormat CLUEWEB12 \
                --input 's3a://corpus-clueweb12/parts/Disk1/ClueWeb12_00/0000tw/*.warc.gz' \
                --output clueweb12-s3-test.jsonl

sample-cc19-anchors: install
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
		--driver-memory 30G\
		--executor-memory 15G\
		src/spark-test2/target/spark-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
			-i ../anjyc/script-filtered-commoncrawl-2019-47-repartitioned-250context/*/* \
			-o ecir22-filtered-commoncrawl-2019-47-sample

navigational-run-files-marco-v1:
	@cd anserini \
		&& mvn clean package appassembler:assemble \
		&& INDEX_NAME=cc-16-07-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-17-04-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-18-13-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-19-47-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-20-05-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-21-04-anchortext ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=cc-combined-anchortext ../src/bash/ecir22-navigational-retrieval.sh
		&& INDEX_NAME=orcas ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-content ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=msmarco-document-v1-title-only.pos+docvectors+raw ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=msmarco-document-v1-url-only.pos+docvectors+raw ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v1-anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979 ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v1-anserini-docs-ms-marco-training-set-test-overlap-removed-389973 ../src/bash/ecir22-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v1-anserini-docs-orcas-sampled-test-overlap-removed-390009 ../src/bash/ecir22-navigational-retrieval.sh

navigational-run-files-marco-v2:
	@cd anserini \
		&& mvn clean package appassembler:assemble \
		&& INDEX_NAME=msmarco-doc-v2 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-19-47-anchortext-v2 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v2-anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v2-anserini-docs-ms-marco-training-set-test-overlap-removed-389973 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=ms-marco-deepct-v2-anserini-docs-orcas-sampled-test-overlap-removed-390009 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=orcas-ms-marco-v2 ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-16-07-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-17-04-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-18-13-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-19-47-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-20-05-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-21-04-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh \
		&& INDEX_NAME=cc-union-16-to-21-anchortext ../src/bash/ecir22-marco-v2-navigational-retrieval.sh


retrieval-homogenity:
	@cd anserini \
		&& mvn clean package appassembler:assemble \
		&& INDEX_NAME=cc-16-07-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=cc-17-04-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=cc-18-13-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=cc-19-47-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=cc-20-05-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=cc-21-04-anchortext ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=orcas ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=ms-marco-content ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \
		&& INDEX_NAME=msmarco-document-v1-title-only.pos+docvectors+raw ../src/retrieval-homogenity/run-retrieval-homogenity-retrieval.sh \


jupyter-notebook:
	docker run --rm -ti -p 8888:8888 \
		-v "${PWD}":/home/jovyan/work \
		-v /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/:/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/ \
		jupyter/datascience-notebook

srun-jupyter-notebook:
	srun \
		--container-name=thesis-probst-eval-jupyter \
		--mem=50G -c 3 \
		--container-image=jupyter/datascience-notebook \
		--container-writable \
		--container-mounts=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst:/workspace/,/mnt/ceph/storage/data-tmp/2021/kibi9872/.ir_datasets:/root/.ir_datasets,/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/,/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/ \
		--pty \
		bash -c 'cd /workspace && jupyter notebook --ip 0.0.0.0 --allow-root'

jupyter-notebook-pyserini:
	srun \
		--container-name=trec-dl-21-health-misinfo-delete-me --container-writable \
		--mem=10G -c 2 --gres=gpu:ampere:1 \
		--container-mounts=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst:/workspace/,/mnt/ceph/storage/data-tmp/2021/kibi9872/.ir_datasets:/root/.ir_datasets,/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/,/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/ \
		--pty \
		bash -c 'cd /workspace && jupyter notebook --ip 0.0.0.0 --allow-root'


edit-script:
	echo "vim src/spark-test2/src/main/bash/run-"

install:
	cd src/spark-test2 && \
	./mvnw clean install


