# Available trained DeepCT Models

We make all the models available **ToDO: Add link**.

# Training of DeepCT Models

First, you have to split the MS-Marco dataset into its passages with executing [split-ms-marco-into-passages.py](split-ms-marco-into-passages.py).
Splitting MS-Marco into passages uses [ir_datasets](https://github.com/allenai/ir_datasets) to provide easy access to the documents, and [the TREC CAsT Tools](https://github.com/grill-lab/trec-cast-tools).
After splitting MS-Marco into passages, create the training data for DeepCT with the jupyter notebook [create-training-data-for-deep-ct.ipynb](create-training-data-for-deep-ct.ipynb).
After you have created the training data, you can use the script [train-deepct.sh](train-deepct.sh) to train DeepCT.

We have documented the creation of all models in the associated [Makefile](Makefile).
To train all models used in the paper (and some more for testing), you can run the following commands:

```
make train-orcas-all
make train-ms-marco-training-set-all
make train-cc-2019-47-all-all
```

# Trained Models:

```
/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/trained-models/deep-ct-training-data-ms-marco-training-set-test-overlap-removed/model.ckpt. -> 0.003031822.


/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/trained-models/deep-ct-training-data-orcas-sampled-test-overlap-removed/model.ckpt. -> 0.006508048.


/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/trained-models/deep-ct-training-data-cc-2019-47-sampled-test-overlap-removed/model.ckpt -> 0.0034446667
```

# Inference with Trained Models.

Install dependencies with
```
pip install python-terrier
pip install --upgrade git+https://github.com/terrierteam/pyterrier_deepct.git
```

Run all the unit tests to verify your installation with: `nosetests`. Then, run the inference on MS-Marco v1 using the make targets:
```
make inference-ms-marco-v1-orcas-modell
make inference-ms-marco-v1-cc-2019-47-modell
make inference-ms-marco-v1-train-modell
```

After we have created the expanded passages with pyterrier, we combine the expanded passages into anserini documents with the script [pyterrier-deepct-expansions-to-anserini-docs.py](pyterrier-deepct-expansions-to-anserini-docs.py).
We have documented all calls to the script pyterrier-deepct-expansions-to-anserini-docs.py in the [Makefile](Makefile).
I.e., we produced all DeepCT document representations for Anserini with:

```
make expanded-pyterrier-passages-to-anserini
```

# Anserini Indexes

After the creation of expanded documents, create the indexes with:
```
MARCO_VERSION=-v1 DATASET=anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979 ./index-anserini.sh
MARCO_VERSION=-v1 DATASET=anserini-docs-orcas-sampled-test-overlap-removed-390009 ./index-anserini.sh
MARCO_VERSION=-v1 DATASET=anserini-docs-ms-marco-training-set-test-overlap-removed-389973 ./index-anserini.sh
MARCO_VERSION=-v2 DATASET=anserini-docs-cc-2019-47-sampled-test-overlap-removed-389979 ./index-anserini.sh
```
