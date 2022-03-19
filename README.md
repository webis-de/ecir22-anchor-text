# The Power of Anchor Text in the Neural Retrieval Era

We have extracted anchor text pointing to documents in MS MARCO (both, version 1 and version 2) and have reproduced two seminal works on the effectiveness of anchor text for navigational queries and on the similarity of queries to anchor text.

All data is available [online](https://webis.de/data/webis-ms-marco-anchor-text-22.html).

This repository contains the data and code for reproducing results of the paper:

    @InProceedings{froebe:2022a,
        address =               {Berlin Heidelberg New York},
        author =                {Maik Fr{\"o}be and Sebastian G{\"u}nther and Maximilian Probst and Martin Potthast and Matthias Hagen},
        booktitle =             {Advances in Information Retrieval. 44th European Conference on IR Research (ECIR 2022)},
        editor =                {Matthias Hagen and Suzan Verberne and Craig Macdonald and Christin Seifert and Krisztian Balog and Kjetil N{\o}rv\r{a}g and Vinay Setty},
        month =                 apr,
        publisher =             {Springer},
        series =                {Lecture Notes in Computer Science},
        site =                  {Stavanger, Norway},
        title =                 {{The Power of Anchor Text in the Neural Retrieval Era}},
        year =                  2022
    }



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

