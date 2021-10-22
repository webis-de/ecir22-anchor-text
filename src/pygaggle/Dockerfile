FROM nvidia/cuda:11.1.1-cudnn8-devel-ubuntu18.04

RUN apt-get update \
	&& apt-get install -y openjdk-11-jdk
RUN apt-get install -y python3.6 python3-pip language-pack-en

ENV LC_ALL=en_US.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

RUN pip3 install --upgrade pip
RUN python3 -m pip install torch==1.8.1+cu111 -f https://download.pytorch.org/whl/lts/1.8/torch_lts.html
RUN python3 -m pip install tensorflow==2.5.0
RUN python3 -m pip install pygaggle
RUN python3 -m pip install transformers==4.6.1

RUN python3 -c 'from pygaggle.rerank.transformer import MonoT5; MonoT5()'
RUN python3 -c 'from pygaggle.rerank.transformer import MonoBERT; MonoBERT()'

RUN python3 -m pip install jupyter
RUN python3 -m pip install ir_datasets

