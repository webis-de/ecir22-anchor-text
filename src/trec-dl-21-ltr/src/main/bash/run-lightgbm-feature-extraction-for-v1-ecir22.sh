#!/bin/bash -e

echo "ecir22-nav-v1"

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	ecir22-nav-v1

echo "ecir22-nav-v1-no-anchor"

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	ecir22-nav-v1-no-anchor

echo "ecir22-nav-v1-no-orcas"

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	ecir22-nav-v1-no-orcas

echo "Run: ecir22-nav-v1-no-anchor-no-orcas"

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	ecir22-nav-v1-no-anchor-no-orcas

