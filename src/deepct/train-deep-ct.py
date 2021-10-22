import argparse
import json
import re
import string
import nltk
from nltk import word_tokenize
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer

nltk.data.path.append("/bos/usr0/zhuyund/nltk_data")


stopwords = stopwords.words('english') 
stemmer = PorterStemmer()

print(stopwords)

class recommended_args():
    def stem():
        return True
        
    def stop():
        return True

def text_clean(text, stem, stop):
    text = text.replace("\'s", " ")
    text = re.sub(r"[^a-zA-Z0-9]", " ", text)
    tokens = word_tokenize(text) 
    if stop:
       tokens = [t for t in tokens if t.lower() not in stopwords]
    if stem:
        new_tokens = [stemmer.stem(t.lower()) for t in tokens]
    else:
        new_tokens = [t.lower() for t in tokens]
    return ' '.join(new_tokens)

def calculate_qtokens(queries):
    ret = {}
    args = recommended_args()
    for query in queries:
        qtext = text_clean(query, args.stem, args.stop)
        if not qtext: 
            continue
        tokens = set(qtext.split(' '))
        for t in tokens:
            ret[t] = ret.get(t, 0.0) + 1.0/len(queries)

    return ret

def calculate_doc_term_recall(lines, qtokens):
    i=1
    args = recommended_args()

    for json_dict in lines:
        json_dict["term_recall"] = {}

        doc_text = json_dict['doc']['title']
        doc_text = text_clean(doc_text, False, args.stop)
        doc_tokens = set(doc_text.split(' '))
        doc_term_recall = {}
        for ttoken in doc_tokens:
            ttoken2 = ttoken
            if args.stem: ttoken2 = stemmer.stem(ttoken)
            if ttoken2 in qtokens:
                doc_term_recall[ttoken] = qtokens[ttoken2]
        json_dict["term_recall"] = doc_term_recall
        json_dict["doc"]["position"] = 1
        json_dict["doc"]["id"] = i
        i=i+1

        yield json_dict


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("json_in_file", help="Each line: {\"queries\": [\"what kind of animals are in grasslands\", \"tropical grasslands animals\", ...], \"doc\":{\"title\": Tropical grassland animals (which do not all occur in the same area) include giraffes, zebras, buffaloes, ka...}}")
    parser.add_argument("json_out_file", help="just the fucking outfile")
    parser.add_argument("--stem", action="store_true", help="recommend: true")
    parser.add_argument("--stop", action="store_true", help="recommend: true")
    args = parser.parse_args()

    with open(args.json_in_file) as infile, open(args.json_out_file, 'w') as outfile:
        for json_dict in calculate_doc_term_recall(infile, args):
            outfile.write(json.dumps(json_dict) + "\n")

