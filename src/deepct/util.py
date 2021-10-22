def clean_qtokens(qtokens):
    ret = {}
    blocklist = load_entrypage_queries()

    for doc, doc_tokens in qtokens.items():
        if len(doc_tokens.keys()) == 0 or len([i for i in doc_tokens.keys() if i in blocklist])/len(doc_tokens.keys()) > 0.2:
            continue
        ret[doc] = doc_tokens

    print('From qtokens with ' + str(len(qtokens)) + ' documents, I filtered terms that may cause train/test overlap so that I now have ' + str(len(ret)) + ' documents.')

    return ret


def load_entrypage_queries():
    from glob import glob
    import importlib
    text_clean = importlib.import_module('train-deep-ct').text_clean

    ret = []
    for f in glob('../../Data/navigational-topics-and-qrels-ms-marco-v1/topics*.tsv'):
        with open(f, 'r') as f:

            for l in f:
                l = l.split('\t')
                if len(l) != 2:
                    raise ValueError('Can not handle ' + str(l))

                ret += text_clean(l[1].strip(), True, True).split(' ')

    return set(ret)


def sample_anchors(anchors_per_doc):
    import json
    import importlib
    text_clean = importlib.import_module('train-deep-ct').text_clean
    blocklist = load_entrypage_queries()
    ret = set()
    for anchors in anchors_per_doc:
        for anchor in set(json.loads(anchors)['anchorText']):
            anchor_terms = text_clean(anchor.strip(), True, True).split(' ')
            if len(anchor_terms) > 0 and len([i for i in anchor_terms if i in blocklist])/len(anchor_terms) < 0.51:
                ret |= set([anchor])

    return ret
