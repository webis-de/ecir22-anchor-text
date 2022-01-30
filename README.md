# The Power of Anchor Text in the Neural Retrieval Era

We have extracted anchor text pointing to documents in MS MARCO (both, version 1 and version 2) and have reproduced two seminal works on the effectiveness of anchor text for navigational queries and on the similarity of queries to anchor text.

All data is available [online](https://webis.de/data/webis-ms-marco-anchor-text-22.html).


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

