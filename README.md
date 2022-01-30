# The Power of Anchor Text in the Neural Retrieval Era

We have extracted anchor text pointing to documents in MS MARCO (both, version 1 and version 2) and have reproduced two seminal works on the effectiveness of anchor text for navigational queries and on the similarity of queries to anchor text.

All data is available [online](https://files.webis.de/data-in-progress/ecir22-anchor-text/), and our extracted anchor text comes in 2 versions:

- Anchor texts with meta data is available at [https://files.webis.de/data-in-progress/ecir22-anchor-text/anchor-text-samples/](https://files.webis.de/data-in-progress/ecir22-anchor-text/anchor-text-samples/).
- Anchor texts that can be directly indexed in anserini is available at [https://files.webis.de/data-in-progress/ecir22-anchor-text/anchor-text-anserini-docs/](https://files.webis.de/data-in-progress/ecir22-anchor-text/anchor-text-anserini-docs/)
- Additionally, we currently move the data to [Hugging Face](https://huggingface.co/datasets/webis/ms-marco-anchor-text) and the [integration to ir_datasets](https://github.com/allenai/ir_datasets/issues/154) is pending.
- Data on Zenodo: [https://zenodo.org/record/5883456](https://zenodo.org/record/5883456)


# Training and Availability of Models

All trained Models are available online at **ToDo: Add link**.
Additionally, we have a detailed step-by-step guide at [src/deepct/README.md](src/deepct/README.md) on how we have trained all the DeepCT models used in the paper.


# Sampling

```
./src/main/bash/run-sampling.sh cc-16-07-v2
./src/main/bash/run-resampling.sh cc-16-07-v2

./src/main/bash/run-sampling.sh cc-17-04-v2
./src/main/bash/run-resampling.sh cc-17-04-v2

./src/main/bash/run-sampling.sh cc-18-13-v2
./src/main/bash/run-resampling.sh cc-18-13-v2

./src/main/bash/run-sampling.sh cc-20-05-v2
./src/main/bash/run-resampling.sh cc-20-05-v2

./src/main/bash/run-sampling.sh cc-21-04-v2
./src/main/bash/run-resampling.sh cc-21-04-v2


./src/main/bash/run-sampling-new.sh script-filtered-commoncrawl-2016-07-repartitioned

./src/main/bash/run-sampling-new.sh script-filtered-commoncrawl-2017-04-repartitioned
```

