#!/bin/bash -e

./create-retrieval-homogenity-features.sh run.ms-marco-content.bm25-default.txt ms-marco-content
./create-retrieval-homogenity-features.sh run.cc-19-47-anchortext.bm25-default.txt cc-19-47-anchortext

