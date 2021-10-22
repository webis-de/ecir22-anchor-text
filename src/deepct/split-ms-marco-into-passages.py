# From https://github.com/grill-lab/trec-cast-tools/blob/master/src/main/python/passage_chunker.py
import re
import spacy
nlp = spacy.load("en_core_web_sm", exclude=["parser", "tagger", "ner", "attribute_ruler", "lemmatizer", "tok2vec"]) #->senter
nlp.enable_pipe("senter")

print(nlp.pipe_names)


class SpacyPassageChunker:
    def __init__(self):
        self.document = None
        self.sentences = None

    
    def sentence_tokenization(self, document):
        self.document = parse_doc(document)
        self.sentences = list(self.document.sents)

    
    def create_passages(self, passage_size = 250):

        passages = []
        content_length = len(self.sentences)
        sentences_word_count = [len([token for token in sentence]) for sentence in self.sentences]
        
        current_idx = 0
        current_passage_word_count = 0
        current_passage = ''
        sub_id = 0
        
        for i in range(content_length):
            if current_passage_word_count >= (passage_size * 0.67):
                passages.append({
                    "body": current_passage,
                    "id": sub_id
                })
                current_passage = ''
                current_passage_word_count = 0
                
                current_idx = i
                sub_id += 1
            
            current_passage += self.sentences[i].text + ' '
            current_passage_word_count += sentences_word_count[i]

        
        current_passage = ' '.join(sentence.text for sentence in self.sentences[current_idx:])
        passages.append({
            "body": current_passage,
            "id": sub_id
        })
        
        return passages
        
        
def parse_doc(document):
    return nlp(sanitize_document(document))

def sanitize_document(doc):
    sanitized = re.compile('<.*?>')
    return re.sub(sanitized, '', doc)[:999999]

def tokens(doc):
    return set([i for i in parse_doc(doc) if not i.is_punct])


def passages(doc):
    ret = [doc.title]
    tmp = SpacyPassageChunker()
    tmp.sentence_tokenization(doc.body)

    ret += [i['body'] for i in tmp.create_passages(passage_size=300)]

    return [(i[0] +1, i[1]) for i in enumerate(ret)]

import json
from tqdm import tqdm
DIR = '/mnt/ceph/storage/data-in-progress/data-teaching/theses/wstud-thesis-probst/deep-ct/'

def docs_iter(dataset_name):
    import ir_datasets
    iter = ir_datasets.load(dataset_name).docs_iter()
    for doc in iter:
        yield doc

import json

dataset_name = 'msmarco-document'
with open(DIR + dataset_name + '-passages.jsonl', 'w') as f:
    for doc in tqdm(ms_marco_docs_iter()):

        f.write(json.dumps({'id': doc.doc_id, 'passages': passages(doc)}) + '\n')

dataset_name = 'msmarco-document-v2'
with open(DIR + dataset_name + '-passages.jsonl', 'w') as f:
    for doc in tqdm(ms_marco_docs_iter()):

        f.write(json.dumps({'id': doc.doc_id, 'passages': passages(doc)}) + '\n')

