# TREC DL Feature extraction

This repository creates the features for TREC DL.

## Parallelization on Epsilonweb

Clone the repo (read only token for this repo):
```
git clone https://thesis-probst-read-token:LrCcM4exJeXn4EAcLBwg@git.webis.de/code-teaching/theses/thesis-probst.git
```

Ensure the indices are available locally:

```
cp -R /mnt/ceph/storage/data-in-progress/data-research/web-search/TREC-21/anserini-indices /home/webis/anserini-indexes
sudo ln -s /home/webis/anserini-indexes /anserini-indexes
```

Install and test the `trec-dl-21-ltr` project with maven:
```
mvn clean install
```

# Training of LambdaMART models (for x trees)

- We run the code below for 100, 1000, 5000
- Adjust the training config to use x trees
- Then train the models

```
./src/main/bash/train-lightgbm-model.sh marco-v1-ecir22-all-36-features
./src/main/bash/train-lightgbm-model.sh marco-v1-ecir22-no-anchor-features
./src/main/bash/train-lightgbm-model.sh marco-v1-ecir22-no-anchor-no-orcas-features
./src/main/bash/train-lightgbm-model.sh marco-v1-ecir22-no-orcas-features
```

- Rename the trained model: `mv LightGBM_model.txt LightGBM-model-x-trees.txt`

