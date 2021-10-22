REGISTRY=registry.webis.de
IMAGE=code-lib/public-images/webis/pygaggle-trec-dl
VERSION=0.0.1-cu111

docker-build:
	docker build -t ${REGISTRY}/${IMAGE}:${VERSION} .

docker-publish-image:
	docker login registry.webis.de
	docker push ${REGISTRY}/${IMAGE}:${VERSION}

srun-jupyter-notebook:
	srun --container-image=${REGISTRY}/${IMAGE}:${VERSION} \
	--container-name=trec-dl-21 --container-writable --mem=100G -c 4 --gres=gpu:ampere:1 \
	--container-mounts=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst/src/pygaggle/ipynb:/workspace,/mnt/ceph/storage/data-tmp/2021/kibi9872/.ir_datasets:/root/.ir_datasets,/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/:/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/ \
	bash -c 'cd /workspace && jupyter notebook --ip 0.0.0.0 --allow-root'

