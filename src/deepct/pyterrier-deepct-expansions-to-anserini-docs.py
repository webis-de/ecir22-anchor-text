#!/usr/bin/env python3


def __doc_id(doc):
    ret = doc['docno'].split('___')
    if len(ret) != 2:
        raise ValueError('Invalid input:' + doc['docno'])

    return ret[0]


def pyterrier_passages(path):
    from glob import glob
    for file_name in glob(path):
        with open(file_name, 'r') as docs:
            for doc in docs:
                yield doc


def to_anserini_docs(pyterrier_passages):
    import json
    ret = {}
    for passage in pyterrier_passages:
        passage = json.loads(passage)
        doc_id = __doc_id(passage)

        if doc_id not in ret:
            ret[doc_id] = ''

        ret[doc_id] = ret[doc_id] + ' ' + passage['text']

    return ret


def arg_parser():
    import argparse
    parser = argparse.ArgumentParser(description='Combine expanded PyTerrier-DeepCT passages to anserini documents.')

    parser.add_argument('--input', type=str, required=True)
    parser.add_argument('--output', type=str, required=True)

    return parser


if __name__ == '__main__':
    import json
    from tqdm import tqdm
    args = arg_parser().parse_args()
    anserini_docs = to_anserini_docs(tqdm(pyterrier_passages(args.input)))

    with open(args.output, 'w') as out_file:
        for doc_id, contents in tqdm(anserini_docs.items()):
            out_file.write(json.dumps({'id': doc_id, 'contents': contents}) + '\n')

