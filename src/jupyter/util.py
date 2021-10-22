import spacy
from copy import deepcopy
import random
import json
import numpy as np
from nltk.stem.porter import PorterStemmer
stemmer = PorterStemmer()
import itertools
nlp = None
ALL_PERMUTATIONS=set([i for i in itertools.permutations([0,0,0,0,0,1,1,1,1,1])])


def distinct_terms(processed_terms):
    return set([i['stemmed'] for i in processed_terms])


def extract_terms(sentence):
    global nlp
    if nlp is None:
        nlp = spacy.load("en_core_web_sm", exclude=["parser", "tagger", "ner", "attribute_ruler", "lemmatizer", "tok2vec"])
    ret = []
    for i in nlp(sentence):
        if not i.is_punct:
            ret += [{'term': str(i), 'stemmed': stemmer.stem(str(i))}]
    return ret


def json_lines(filename):
    from tqdm import tqdm
    import json
    with open(filename, 'r') as f:
        for i in tqdm(f):
            yield json.loads(i)


def enrich_presentation(presentation):
    ret = deepcopy(presentation)
    if 'contents' in ret:
        ret['contentsProcessed'] = extract_terms(ret['contents'])
    if 'anchorText' in ret:
        ret['anchorTextProcessed'] = []
        for anchor in ret['anchorText']:
            ret['anchorTextProcessed'] += [extract_terms(anchor)]

    return ret


def __terms_for_counting(line):
    if 'contentsProcessed' in line:
        yield line['contentsProcessed']
    if 'anchorTextProcessed' in line:
        for anchor in line['anchorTextProcessed']:
            yield anchor


def __flatten(terms):
    ret = {}
    for term in terms.keys():
        term_with_count = [(i,c) for i,c in terms[term].items()]

        most_term = sorted(term_with_count, key=lambda i: i[1], reverse=True)[0][0]
        ret[most_term] = sum(i[1] for i in term_with_count)

    return ret


def count_terms(lines):
    ret = {}
    for line in lines:
        if type(line) == str:
            line = json.loads(line)
        for terms in __terms_for_counting(line):
            for term in terms:
                if term['stemmed'] not in ret:
                    ret[term['stemmed']] = {}
                if term['term'] not in ret[term['stemmed']]:
                    ret[term['stemmed']][term['term']] = 0

                ret[term['stemmed']][term['term']] += 1

    return __flatten(ret)
            
def distribution_distinct_terms(lines):
    ret = {}

    for line in lines:
        if type(line) == str:
            line = json.loads(line)

        terms_in_line = str(len(distinct_terms(line['contentsProcessed'])))
        
        if int(terms_in_line) <= 0:
            continue

        if terms_in_line not in ret:
            ret[terms_in_line] = 0

        to_add = 1
        if 'target_document' in line and type(line['target_document']) == list:
            to_add = len(line['target_document'])
        ret[terms_in_line] += to_add

    return ret

def anchor_text_distribution_distinct_terms(lines, limit=None):
    ret = []
    for line in lines:
        if type(line) == str:
            line = json.loads(line)

        if limit != None and limit< len(line['anchorTextProcessed']):
            continue

        for anchor in line['anchorTextProcessed']:
            ret += [{'contentsProcessed': anchor}]

    return distribution_distinct_terms(ret)

def aggregated_anchor_text_distribution_distinct_terms(lines, limit=None):
    ret = []
    for line in lines:
        if type(line) == str:
            line = json.loads(line)
        contents = []
        if limit != None and limit< len(line['anchorTextProcessed']):
            continue

        for anchor in line['anchorTextProcessed']:
            contents += anchor
        ret += [{'contentsProcessed': contents}]

    return distribution_distinct_terms(ret)

def build_probability_arrays(first, second):
    first_ret = []
    second_ret = []
    first_sum = sum(first.values())
    second_sum = sum(second.values())

    max_id = max([int(i) for i in first.keys()] + [int(i) for i in second.keys()])
    for i in range(1, max_id +1):
        first_ret += [first.get(str(i), 0) / first_sum]
        second_ret += [second.get(str(i), 0) / second_sum]

    return first_ret, second_ret


def __combine_terms(docs):
    ret = {}
    for doc in docs:
        for term, count in doc.items():
            if term not in ret:
                ret[term] = 0
            ret[term] += count
    return ret


def build_probability_arrays_terms(first, second):
    first_ret = []
    second_ret = []
    first_sum = sum(first.values())
    second_sum = sum(second.values())

    terms = set([i for i in first.keys()] + [i for i in second.keys()])

    for i in sorted(terms):
        first_ret += [first.get(str(i), 0) / first_sum]
        second_ret += [second.get(str(i), 0) / second_sum]

    return first_ret, second_ret


def run_homogenity_experiments(json_line, num_splits=10):
    batches = combine_retrieved_documents_to_batches(json_line, num_splits)

    for batch in batches:
        if sum(batch.values()) == 0:
            return []

    topic = json.loads(json_line)['topic']
    ret = []

    for slice_ids in ALL_PERMUTATIONS:
        parts = [[], []]

        for i in range(num_splits):
            parts[slice_ids[i]] += [batches[i]]

        assert len(parts) == 2
        assert len(parts[0]) == 5
        assert len(parts[1]) == 5

        part_0 = __combine_terms(parts[0])
        part_1 = __combine_terms(parts[1])

        part_0, part_1 = build_probability_arrays_terms(part_0, part_1)

        ret += [{
            'topic': topic,
            'jensen_shannon_distance': jensen_shannon_distance(part_0, part_1),
        }]

    return ret


def evaluate_retrieval_homogenity(directory, index, retrieval_model, processes=20):
    from multiprocessing import Pool
    with Pool(processes=processes) as pool:
        lines = [i for i in open(directory + '/run.' + index + '.' + retrieval_model + '.txt.jsonl')]
        tmp_ret = [i for i in pool.map(run_homogenity_experiments, lines, 1)]
        ret = []
        for li in tmp_ret:
            for i in li:
                i['index'] = index
                i['retrieval_model'] = retrieval_model
                ret += [i]

        return ret


def combine_retrieved_documents_to_batches(json_line, num_splits=10):
    json_line = json.loads(json_line)
    documents = [i['documentRepresentation'] for i in json_line['documents'] if i is not None and 'documentRepresentation' in i and
 i['documentRepresentation'] is not None and len(i['documentRepresentation'].keys()) >= 2]
    random.Random(num_splits).shuffle(documents)
    
    return [__combine_terms(i) for i in np.array_split(documents, num_splits)]


def jensen_shannon_distance(p, q):
    from scipy.spatial.distance import jensenshannon
    return jensenshannon(p,q)

def kullback_leibler_divergence(p,q):
    from scipy.stats import entropy
    assert p != None
    assert q != None # scipy.stats.entropy calculate KL-divergence if p and q are both not none

    return entropy(p, q)

