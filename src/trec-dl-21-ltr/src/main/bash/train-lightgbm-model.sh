#!/bin/bash -e

DIR="/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/lightgbm/${1}"

if [ ! -f "${DIR}/train.conf" ]
then
	echo "Input does not exist: ${DIR}"
	exit 1
fi

srun \
	--container-name=trec-dl-21 \
	--container-writable \
        --mem=100G -c 25 \
        --container-mounts=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst:/workspace/,/mnt/ceph/storage/data-tmp/2021/kibi9872/.ir_datasets:/root/.ir_datasets,/mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/,/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/ \
        --pty \
	bash -c "cd ${DIR} && /opt/LightGBM/lightgbm config=train.conf"

