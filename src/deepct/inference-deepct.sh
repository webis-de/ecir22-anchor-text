#!/bin/bash -e

DIR='/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/'

INPUT="${DIR}${INPUT_DATASET}.jsonl"
CHECKPOINT_FILES="${DIR}trained-models/${MODEL_DIR}/model.ckpt-${MODEL_CHECKPOINT}"

OUTPUT_DIR="${DIR}expanded-datasets/${INPUT_DATASET}/${MODEL_DIR}-${MODEL_CHECKPOINT}/"
LOG_FILE="${DIR}training-logs/${DATASET}.log"

if [ ! -f "${INPUT}" ];
then
    echo "File ${INPUT} does not exist."
    exit 1
fi

echo "Inference on $(ls -lha ${INPUT})..."

if [ ! -f "${CHECKPOINT_FILES}.meta" ];
then
    echo "File ${CHECKPOINT_FILES} does not exist."
    exit 1
fi

echo "Will use models $(ls -lha ${CHECKPOINT_FILES}*)..."

echo "Will write output to ${OUTPUT_DIR}"

srun \
    --gres gpu:ampere:1 -c 4  --mem=100G \
    --container-mounts=/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/,/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst/src/deepct \
    --container-workdir=/mnt/ceph/storage/data-tmp/2021/kibi9872/thesis-probst/src/deepct \
    --container-name=DeepCT --pty \
    python3 inference-deepct.py \
        --vocab_file /workspace/bert-uncased_L-12_H-768_A-12/vocab.txt \
        --bert_config_file /workspace/bert-uncased_L-12_H-768_A-12/bert_config.json \
        --checkpoint_file ${CHECKPOINT_FILES} \
        --max_seq_length 512 \
        --input ${INPUT} \
        --output ${OUTPUT_DIR}

