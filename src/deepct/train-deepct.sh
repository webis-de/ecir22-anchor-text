#!/bin/bash -e

DIR='/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/'
DOCTERM_RECALL="${DIR}${DATASET}.jsonl"
OUTPUT_DIR="${DIR}trained-models/${DATASET}"
LOG_FILE="${DIR}training-logs/${DATASET}.log"


if [ ! -f "${DOCTERM_RECALL}" ];
then
	echo "File ${DOCTERM_RECALL} does not exist."
	exit 1
fi

echo "Train model on $(ls -lha ${DOCTERM_RECALL})..."

srun \
    --gres gpu:ampere:1 -c 4  --mem=100G \
    --container-mounts=/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/ \
    --container-workdir=/workspace/DeepCT \
    --container-name=DeepCT --pty \
    python3 run_deepct.py \
        --task_name=marcodoc \
        --do_train=true \
        --do_eval=false \
        --do_predict=false \
        --data_dir=${DOCTERM_RECALL} \
        --vocab_file=/workspace/bert-uncased_L-12_H-768_A-12/vocab.txt \
        --bert_config_file=/workspace/bert-uncased_L-12_H-768_A-12/bert_config.json \
        --init_checkpoint=/workspace/bert-uncased_L-12_H-768_A-12/bert_model.ckpt \
        --max_seq_length=512 \
        --train_batch_size=16 \
        --learning_rate=2e-5 \
        --num_train_epochs=3.0 \
        --recall_field=title \
        --output_dir=${OUTPUT_DIR}

