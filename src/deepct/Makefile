DEEP_CT_MARCO_V1="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/expanded-datasets/msmarco-document-passages/"
DEEP_CT_MARCO_V2="/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/expanded-datasets/msmarco-document-passages-v2/"


train-orcas:
	DATASET=deep-ct-training-data-orcas-sampled-test-overlap-removed ./train-deepct.sh 

train-ms-marco-training-set:
	DATASET=deep-ct-training-data-ms-marco-training-set-test-overlap-removed ./train-deepct.sh 

train-cc-2019-47:
	DATASET=deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed ./train-deepct.sh 

inference-ms-marco-v1-cc-2019-47-modell:
	INPUT_DATASET=msmarco-document-passages MODEL_DIR=deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed MODEL_CHECKPOINT=389979 ./inference-deepct.sh

inference-ms-marco-v1-orcas-modell:
	INPUT_DATASET=msmarco-document-passages MODEL_DIR=deep-ct-training-data-orcas-sampled-test-overlap-removed MODEL_CHECKPOINT=390009 ./inference-deepct.sh

inference-ms-marco-v1-train-modell:
	INPUT_DATASET=msmarco-document-passages MODEL_DIR=deep-ct-training-data-ms-marco-training-set-test-overlap-removed MODEL_CHECKPOINT=389973 ./inference-deepct.sh

inference-ms-marco-v2-cc-2019-47-modell:
	INPUT_DATASET=msmarco-document-passages-v2 MODEL_DIR=deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed MODEL_CHECKPOINT=389979 ./inference-deepct.sh

inference-ms-marco-v2-orcas-modell:
	INPUT_DATASET=msmarco-document-passages-v2 MODEL_DIR=deep-ct-training-data-orcas-sampled-test-overlap-removed MODEL_CHECKPOINT=390009 ./inference-deepct.sh

inference-ms-marco-v2-train-modell:
	INPUT_DATASET=msmarco-document-passages-v2 MODEL_DIR=deep-ct-training-data-ms-marco-training-set-test-overlap-removed MODEL_CHECKPOINT=389973 ./inference-deepct.sh

expanded-pyterrier-passages-to-anserini:
	./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V1}deep-ct-training-data-orcas-sampled-test-overlap-removed-390009/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V1}msmarco-document-passages/anserini-docs-orcas-sampled-test-overlap-removed-390009.jsonl' \
	&& ./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V1}deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed-389979/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V1}anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979.jsonl' \
	&& ./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V1}deep-ct-training-data-ms-marco-training-set-test-overlap-removed-389973/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V1}anserini-docs-ms-marco-training-set-test-overlap-removed-389973.jsonl' \
	&& ./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V2}deep-ct-training-data-orcas-sampled-test-overlap-removed-390009/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V2}anserini-docs-orcas-sampled-test-overlap-removed-390009.jsonl' \
	&& ./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V2}deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed-389979/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V2}anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979.jsonl' \
	&& ./pyterrier-deepct-expansions-to-anserini-docs.py \
		--input '${DEEP_CT_MARCO_V2}deep-ct-training-data-ms-marco-training-set-test-overlap-removed-389973/part*.jsonl' \
		--output '${DEEP_CT_MARCO_V2}anserini-docs-ms-marco-training-set-test-overlap-removed-389973.jsonl'


