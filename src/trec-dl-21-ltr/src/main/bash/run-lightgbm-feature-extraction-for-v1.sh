#!/bin/bash -e

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	v1

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	v1-no-anchor

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	v1-no-orcas

java -cp target/trec-dl-21-ltr-1.0-SNAPSHOT-jar-with-dependencies.jar \
	de.webis.trec_dl_21_ltr.lightgbm.App \
	v1-no-anchor-no-orcas

