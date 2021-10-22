import importlib
to_anserini_docs = importlib.import_module('pyterrier-deepct-expansions-to-anserini-docs').to_anserini_docs
pyterrier_passages = importlib.import_module('pyterrier-deepct-expansions-to-anserini-docs').pyterrier_passages

def test_with_distinct_passages():
    expected = {'a': ' hello a', 'b': ' hello b'}
    actual = to_anserini_docs(['{"docno": "a___0", "text": "hello a"}', '{"docno": "b___0", "text": "hello b"}'])

    print(actual)
    assert expected == actual

def test_with_some_overlapping_passages():
    expected = {'a': ' hello a again a and now a', 'b': ' hello b'}
    actual = to_anserini_docs(['{"docno": "a___0", "text": "hello a"}', '{"docno": "b___0", "text": "hello b"}', '{"docno": "a___0", "text": "again a"}', '{"docno": "a___0", "text": "and now a"}'])

    print(actual)
    assert expected == actual

def test_with_reading_pyterrier_passages_from_file():
    expected = {
        'a': ' hello world a file 1 nr 1 hello world a file 1 nr 2 hello world a file 2 nr 1 hello world a file 2 nr 2',
        'b': ' hello world b file 1 nr 2 hello world b file 1 nr 2 hello world b file 2 nr 2 hello world b file 2 nr 2'
    }
    actual = to_anserini_docs(pyterrier_passages('test-resources/test-resource-pyterrier-doc-part-*.jsonl'))
    
    print(actual)
    assert expected == actual

