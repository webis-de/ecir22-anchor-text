jupyter-notebook:
	docker run --rm -ti --net=host \
		-v "${PWD}":/home/jovyan/work \
		-v /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/:/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/ \
		-v /mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/:/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/retrievalExperiments/ \
		jupyter/datascience-notebook

jupyter-notebook-pyserini:
	srun \
		--container-name=trec-dl-21 --container-writable \
		--mem=100G -c 25 \
		--container-mounts=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst:/workspace/,/mnt/ceph/storage/data-tmp/2021/kibi9872/.ir_datasets:/root/.ir_datasets,/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/,/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/ \
		--pty \
		bash -c 'cd /workspace && jupyter notebook --ip 0.0.0.0 --allow-root'

test-python:
	@PYTHONPATH=src/main/python/ nosetests src/test/python/

