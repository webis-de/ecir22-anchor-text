def arg_parser():
    import argparse
    parser = argparse.ArgumentParser(description='Process ms-marco passages with DeepCT.')

    parser.add_argument('--vocab_file', type=str, required=True)
    parser.add_argument('--bert_config_file', type=str, required=True)
    parser.add_argument('--checkpoint_file', type=str, required=True)
    parser.add_argument('--max_seq_length', type=int, required=True)
    parser.add_argument('--input', type=str, required=True)
    parser.add_argument('--output', type=str, required=True)
    parser.add_argument('--batch_size', type=int, default=10000, required=False)
    parser.add_argument('--allow_file', type=str, required=False, default='')

    return parser

def deep_ct_transformer(args):
    from pyterrier_deepct import DeepCTTransformer
    return DeepCTTransformer(args.bert_config_file, args.checkpoint_file, vocab_file=args.vocab_file, max_seq_length=args.max_seq_length)


def detect_already_covered_ids(args):
    import json
    ret = []

    for file_name in all_files(args):
        with open(file_name, 'r') as f:
            for l in f:
                ret += [json.loads(l)['docno'].split('___')[0]]

    return set(ret)


def all_files(args):
    from glob import glob
    return glob(args.output + '/part-*.jsonl')


def next_out_file(args):
    from glob import glob
    ret = [0]
    for f in all_files(args):
        ret += [int(f.split('/part-')[-1].split('.jsonl')[0])]

    return args.output + '/part-' + str(max(ret) +1) + '.jsonl'


def allowed_ids(args):
    if args.allow_file:
        with open(args.allow_file, 'r') as f:
            return set([i.strip() for i in f])


def load_next_document_batch(args):
    import pandas as pd
    from tqdm import tqdm
    import json

    covered_ids = detect_already_covered_ids(args)
    allow_ids = allowed_ids(args)
    print('I skip ', len(covered_ids), ' already covered documents and will use an allow-list with ', (len(allow_ids) if allow_ids else 0), ' entries.')
    ret = []
    covered_docs = 0

    with open(args.input, 'r') as documents:
        for document in tqdm(documents):
            document = json.loads(document)

            if document['id'] in covered_ids:
                continue

            if allow_ids != None and len(allow_ids) > 0 and document['id'] not in allow_ids:
                continue

            covered_docs += 1
            if covered_docs > args.batch_size:
                break

            for passage in document['passages']:
                ret += [{'docno': document['id'] + '___' + str(passage[0]), 'text': passage[1]}]
    return pd.DataFrame(ret)


if __name__ == '__main__':
    next_batch_size = 1
    args = arg_parser().parse_args()
    deep_ct = deep_ct_transformer(args)

    while next_batch_size > 0:
        docs = load_next_document_batch(args)
        out_file = next_out_file(args)
        next_batch_size = len(docs)
        print('I will apply deepCT to a batch of size ', next_batch_size, ' documents and write the result to ', out_file ,'.')
        docs = deep_ct(docs)
        docs.to_json(out_file, lines=True, orient='records')

