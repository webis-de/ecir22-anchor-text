def write_run_file(model_file, feature_file, system_name, out_file):
    with open(out_file, 'w') as f:
        f.write(run_file_content(model_file, feature_file, system_name))

def run_file_content(model_file, feature_file, system_name):
    from lightgbm import Booster
    model = Booster(model_file=model_file)
    ret = ''

    to_rerank = load_ranking_data(feature_file)
    for topic_to_rerank in to_rerank:
        doc_to_score = []
        y_predictions = model.predict(topic_to_rerank['documentFeatures'])
        assert len(y_predictions) == len(topic_to_rerank['documents'])

        for i in range(len(topic_to_rerank['documents'])):
            doc_to_score += [{'documentId': topic_to_rerank['documents'][i], 'score': y_predictions[i]}]

        doc_to_score = sorted(doc_to_score, key=lambda i: i['score'], reverse=True)
        ret += topic_as_run_file(topic_to_rerank['topic'], doc_to_score, system_name)

    return ret.strip()

def topic_as_run_file(topic_nr, doc_to_score, system_name):
    ret = ''

    for i in range(len(doc_to_score)):
        ret += topic_nr + ' Q0 ' + doc_to_score[i]['documentId'] + ' ' + str(i+1) + ' ' + str(doc_to_score[i]['score']) + ' ' + system_name + '\n'

    return ret

def load_ranking_data(file_name):
    import json
    ret = []
    with open(file_name, 'r') as f:
        for l in f:
            l = json.loads(l)

            ret += [{
                'topic': l['topic'],
                'documents': [i['documentId'] for i in l['documents']],
                'documentFeatures': as_np_array(l['documents']),
            }]
    return ret


def as_np_array(docs):
    import numpy as np
    ret = []
    for doc in docs:
        last_feature=1
        features = doc['features']
        # relevance label and query id
        ret_features = [-100, -100]
        for feature_num in features:
            if (int(feature_num) -1) != last_feature:
                raise ValueError('Could not handle...')

            ret_features += [features[feature_num]]
            last_feature = int(feature_num)
        ret += [ret_features]

    return np.array(ret)

