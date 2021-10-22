from util import *
import json


def test_term_extraction_without_duplicated():
    expected = {'rumour', 'manchest', 'latest', 'harri', 'unit', 'to', 'kane', 'sign', 'is', 'look'}
    actual = distinct_terms(extract_terms('Latest Rumours: Manchester United is looking to sign Harry Kane'))

    print(actual)

    assert expected == actual

def test_term_extraction_with_exact_duplicates():
    expected = {'rumour', 'manchest', 'latest', 'harri', 'unit', 'to', 'kane', 'sign', 'is', 'look'}
    actual = distinct_terms(extract_terms('Latest Rumours: Manchester United is looking to sign Harry Kane to Manchester United.'))

    print(actual)

    assert expected == actual


def test_sample_orcas_query():
    expected = {'github'}
    actual = distinct_terms(extract_terms('github'))

    assert expected == actual

def test_sample_orcas_query_with_special_chars():
    expected = {'c++'}
    actual = distinct_terms(extract_terms('! c++'))

    assert expected == actual

def test_term_extraction_with_exact_duplicates_and_duplicates_after_stemming():
    expected = {'rumour', 'manchest', 'latest', 'harri', 'unit', 'to', 'kane', 'sign', 'is', 'look'}
    actual = distinct_terms(extract_terms('Latest Rumours: Manchester United is looking to sign Harry Kane to Manchester United. Look!'))

    print(actual)

    assert expected == actual


def test_enriched_representation_with_contents():
    expected = json.dumps({'id': 1, 'contents': 'github', 'contentsProcessed': [{'term': 'github', 'stemmed': 'github'}]})
    actual = json.dumps(enrich_presentation({'id': 1, 'contents': 'github'}))
    
    assert expected == actual


def test_enriched_representation_with_contents_and_anchor():
    expected = json.dumps({'id': 1, 'anchorText': ['github'], 'contents': '! looking', 'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'anchorTextProcessed': [[{'term': 'github', 'stemmed': 'github'}]]})
    actual = json.dumps(enrich_presentation({'id': 1, 'anchorText': ['github'], 'contents': '! looking'}))
    
    assert expected == actual


def test_enriched_representation_with_contents_and_multiple_anchors():
    expected = json.dumps({'id': 1, 'anchorText': ['github', 'Github United'], 'contents': '! looking', 'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'anchorTextProcessed': [[{'term': 'github', 'stemmed': 'github'}], [{'term': 'Github', 'stemmed': 'github'}, {'term': 'United', 'stemmed': 'unit'}]]})
    actual = json.dumps(enrich_presentation({'id': 1, 'anchorText': ['github', 'Github United'], 'contents': '! looking'}))
    print(actual)
    
    assert expected == actual


def test_distribution_is_correct_for_content_example():
    expected = {'1': 2, '2': 1}
    actual = distribution_distinct_terms([
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}]}),
        json.dumps({'contentsProcessed': [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]}),
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}]}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_content_example_with_single_target_doc():
    expected = {'1': 2, '2': 1}
    actual = distribution_distinct_terms([
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'target_document': ['D84115']}),
        json.dumps({'contentsProcessed': [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}], 'target_document': ['D84115']}),
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'target_document': ['D84115']}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_content_example_with_single_target_doc():
    expected = {'1': 3, '2': 3}
    actual = distribution_distinct_terms([
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'target_document': ['D84115', 'a']}),
        json.dumps({'contentsProcessed': [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}], 'target_document': ['D84115', 'a', 'b']}),
        json.dumps({'contentsProcessed': [{'term': 'looking', 'stemmed': 'look'}], 'target_document': ['D84115']}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_anchor_text_example():
    expected = {'1': 2, '2': 1}
    actual = anchor_text_distribution_distinct_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_anchor_text_example_2():
    expected = {'1': 2, '2': 3}
    actual = anchor_text_distribution_distinct_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_anchor_text_example_2():
    expected = {'1': 1, '2': 2}
    actual = aggregated_anchor_text_distribution_distinct_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'a', 'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_distribution_is_correct_for_anchor_text_example_3():
    expected = {'1': 1, '2': 3}
    actual = aggregated_anchor_text_distribution_distinct_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'a', 'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'stemmed': 'a'}], [{'stemmed': 'b'}, {'stemmed': 'b'}, {'stemmed': 'b'}], [{'stemmed': 'a'}, {'stemmed': 'a'}, {'stemmed': 'b'}]]}),
    ])

    print(json.dumps(actual))
    assert expected == actual

def test_count_top_terms():
    expected = {'looking': 3, 'a': 2}
    actual = count_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}, {'term': 'looks', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'a', 'stemmed': 'b'}], [{'term': 'a','stemmed': 'b'}]]}),
    ])

    print(actual)
    assert expected == actual

def test_count_top_terms_2():
    expected = {'looking': 3, 'a': 4, 'cooking': 4}
    actual = count_terms([
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'looking', 'stemmed': 'look'}, {'term': 'looks', 'stemmed': 'look'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'a', 'stemmed': 'b'}], [{'term': 'a','stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'c', 'stemmed': 'b'}], [{'term': 'd','stemmed': 'b'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'cooking', 'stemmed': 'cook'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'cooking', 'stemmed': 'cook'}]]}),
        json.dumps({'anchorTextProcessed': [[{'term': 'cooking', 'stemmed': 'cook'}], [{'term': 'cooks', 'stemmed': 'cook'}]]}),
    ])

    print(actual)
    assert expected == actual

def test_build_probability_arrays():
    expected_first = [0.3, 0.0, 0.0, 0.7]
    expected_second = [0.3, 0.3, 0.4, 0.0]

    actual_first, actual_second = build_probability_arrays(
        first={'1': 3, '4': 7},
        second={'1': 3, '2': 3, '3': 4},
    )

    print(actual_first)
    assert expected_first == actual_first
    print(actual_second)
    assert expected_second == actual_second

def test_build_probability_arrays_second_one_is_smaller():
    expected_first = [0.3, 0.7, 0.0, 0.0]
    expected_second = [0.0, 0.0, 0.0, 1.0]

    actual_first, actual_second = build_probability_arrays(
        first={'1': 6, '2': 14},
        second={'4': 4},
    )

    print(actual_first)
    assert expected_first == actual_first
    print(actual_second)
    assert expected_second == actual_second

def test_jensen_shannon_distance_unsimilar_distributions():
    expected = 0.8325546111576977
    first, second = build_probability_arrays(
        first={'1': 6, '2': 14},
        second={'4': 4},
    )
    actual = jensen_shannon_distance(first, second)

    print(actual)
    assert expected == actual

def test_jensen_shannon_distance_similar_distributions():
    expected = 0.0
    first, second = build_probability_arrays(
        first={'1': 6, '2': 14},
        second={'1': 12, '2': 28},
    )
    actual = jensen_shannon_distance(first, second)

    print(actual)
    assert expected == actual

def test_kullback_leibler_divergence_similar_distributions():
    expected = 0.0
    first, second = build_probability_arrays(
        first={'1': 6, '2': 14},
        second={'1': 12, '2': 28},
    )
    actual = kullback_leibler_divergence(first, second)

    print(actual)
    assert expected == actual

def test_kullback_leibler_dicergence_unsimilar_distributions():
    expected = float('inf')
    first, second = build_probability_arrays(
        first={'1': 6, '2': 14},
        second={'4': 4},
    )
    actual = kullback_leibler_divergence(first, second)

    print(actual)
    assert expected == actual

def test_build_probability_arrays_terms():
    input_a = {'a': 2, 'c': 3, 'b': 5}
    input_b = {'e': 5, 'c': 5}

    expected_a = [0.2, 0.5, 0.3, 0.0]
    expected_b = [0.0, 0.0, 0.5, 0.5]

    actual_a, actual_b = build_probability_arrays_terms(input_a, input_b)

    print(actual_a)
    assert expected_a == actual_a
    print(actual_b)
    assert expected_b == actual_b

def test_parsing_of_xy():
    import json
    expected = json.loads('[{"1-21": 1, "2-21": 2, "1-50": 1, "2-50": 2, "1-60": 1, "2-60": 2, "1-43": 1, "2-43": 2, "1-16": 1, "2-16": 2, "1-69": 1, "2-69": 2, "1-97": 1, "2-97": 2, "1-67": 1, "2-67": 2, "1-94": 1, "2-94": 2, "1-29": 1, "2-29": 2}, {"1-86": 1, "2-86": 2, "1-89": 1, "2-89": 2, "1-74": 1, "2-74": 2, "1-25": 1, "2-25": 2, "1-40": 1, "2-40": 2, "1-7": 1, "2-7": 2, "1-70": 1, "2-70": 2, "1-63": 1, "2-63": 2, "1-13": 1, "2-13": 2, "1-47": 1, "2-47": 2}, {"1-6": 1, "2-6": 2, "1-55": 1, "2-55": 2, "1-39": 1, "2-39": 2, "1-24": 1, "2-24": 2, "1-3": 1, "2-3": 2, "1-68": 1, "2-68": 2, "1-72": 1, "2-72": 2, "1-11": 1, "2-11": 2, "1-18": 1, "2-18": 2, "1-57": 1, "2-57": 2}, {"1-44": 1, "2-44": 2, "1-80": 1, "2-80": 2, "1-76": 1, "2-76": 2, "1-64": 1, "2-64": 2, "1-82": 1, "2-82": 2, "1-95": 1, "2-95": 2, "1-85": 1, "2-85": 2, "1-52": 1, "2-52": 2, "1-93": 1, "2-93": 2, "1-14": 1, "2-14": 2}, {"1-10": 1, "2-10": 2, "1-32": 1, "2-32": 2, "1-51": 1, "2-51": 2, "1-96": 1, "2-96": 2, "1-84": 1, "2-84": 2, "1-87": 1, "2-87": 2, "1-65": 1, "2-65": 2, "1-27": 1, "2-27": 2, "1-28": 1, "2-28": 2, "1-90": 1, "2-90": 2}, {"1-42": 1, "2-42": 2, "1-88": 1, "2-88": 2, "1-92": 1, "2-92": 2, "1-49": 1, "2-49": 2, "1-23": 1, "2-23": 2, "1-34": 1, "2-34": 2, "1-19": 1, "2-19": 2, "1-12": 1, "2-12": 2, "1-8": 1, "2-8": 2, "1-15": 1, "2-15": 2}, {"1-0": 1, "2-0": 2, "1-37": 1, "2-37": 2, "1-2": 1, "2-2": 2, "1-75": 1, "2-75": 2, "1-56": 1, "2-56": 2, "1-30": 1, "2-30": 2, "1-71": 1, "2-71": 2, "1-78": 1, "2-78": 2, "1-81": 1, "2-81": 2, "1-38": 1, "2-38": 2}, {"1-22": 1, "2-22": 2, "1-58": 1, "2-58": 2, "1-33": 1, "2-33": 2, "1-36": 1, "2-36": 2, "1-79": 1, "2-79": 2, "1-48": 1, "2-48": 2, "1-45": 1, "2-45": 2, "1-77": 1, "2-77": 2, "1-17": 1, "2-17": 2, "1-53": 1, "2-53": 2}, {"1-5": 1, "2-5": 2, "1-46": 1, "2-46": 2, "1-31": 1, "2-31": 2, "1-9": 1, "2-9": 2, "1-41": 1, "2-41": 2, "1-91": 1, "2-91": 2, "1-66": 1, "2-66": 2, "1-98": 1, "2-98": 2, "1-20": 1, "2-20": 2, "1-83": 1, "2-83": 2}, {"1-35": 1, "2-35": 2, "1-62": 1, "2-62": 2, "1-59": 1, "2-59": 2, "1-26": 1, "2-26": 2, "1-1": 1, "2-1": 2, "1-99": 1, "2-99": 2, "1-61": 1, "2-61": 2, "1-54": 1, "2-54": 2, "1-4": 1, "2-4": 2, "1-73": 1, "2-73": 2}]')
    docs = []
    for i in range(100):
        docs += [{'docId': str(i), 'documentRepresentation': {'1-' + str(i): 1, '2-' + str(i): 2}}]
        docs += [{'docId': str(i), 'documentRepresentation': {}}]

    input_data = json.dumps({"topic": "2028315","documents": docs})
    actual = combine_retrieved_documents_to_batches(input_data)

    print(json.dumps(actual))

    assert expected == actual

