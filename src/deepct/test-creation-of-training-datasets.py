from util import load_entrypage_queries, clean_qtokens, sample_anchors


def test_load_entrypage_queries():
    actual = load_entrypage_queries()
    assert 'imdb' in actual
    assert 'estat' in actual
    assert 'real' in actual
    assert 538 == len(actual)

def test_clean_qtokens_without_docs_to_remove():
    expected = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
        "D576811": {"mean": 1, "elegxo": 1}
    }
    input_data = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
        "D576811": {"mean": 1, "elegxo": 1}
    }
    
    actual = clean_qtokens(input_data)

    assert expected == actual

def test_clean_qtokens_with_single_doc_to_remove():
    expected = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
    }
    input_data = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
        "D576811": {"mean": 1, "elegxo": 1, "imdb": 1}
    }
    
    actual = clean_qtokens(input_data)

    assert expected == actual

def test_clean_qtokens_with_multiple_doc_to_remove():
    expected = {
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
    }
    input_data = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1, "imdb": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
        "D576811": {"mean": 1, "elegxo": 1, "imdb": 1}
    }
    
    actual = clean_qtokens(input_data)

    assert expected == actual

def test_clean_qtokens_with_multiple_doc_to_remove_with_empty_docs():
    expected = {
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
    }
    input_data = {
        "D59219": {"project": 1, "manhattan": 1, "immedi": 1, "success": 1, "impact": 1, "imdb": 1},
        "D59235": {"repair": 0.5, "caus": 0.5, "offend": 0.5, "victim": 1, "justic": 1, "commun": 0.5, "act": 0.5, "design": 0.5, "crimin": 0.5, "19": 0.5, "option": 0.5, "harm": 0.5, "question": 0.5, "process": 0.5, "help": 0.5, "restor": 0.5},
        "D576811": {"mean": 1, "elegxo": 1, "imdb": 1},
        "Daaa": {}
    }
    
    actual = clean_qtokens(input_data)

    assert expected == actual


def test_anchor_text_extraction_no_overlap():
    input_data=['{"targetDocumentId": "D1258457", "anchorText": ["Scientific American", "process", "secret of fireflies", "use their", "Marc Branham, in Scientific American, 2005"]}', '{"targetDocumentId": "D532003", "anchorText": ["online calendar test page", "online calendar test page", "online calendar test page"]}']

    expected = set(["Scientific American", "process", "secret of fireflies", "use their", "Marc Branham, in Scientific American, 2005", "online calendar test page"])
    actual = sample_anchors(input_data)

    assert expected == actual


def test_anchor_text_extraction_no_large_overlap():
    input_data=['{"targetDocumentId": "D1258457", "anchorText": ["Scientific American", "process", "communicate", "secret of fireflies", "use their", "Marc Branham, in Scientific American, 2005"]}', '{"targetDocumentId": "D532003", "anchorText": ["online calendar page", "online calendar page", "online calendar page", "iMdb page test"]}']

    expected = set(["Scientific American", "process", "secret of fireflies", "use their", "Marc Branham, in Scientific American, 2005", "iMdb page test"])
    actual = sample_anchors(input_data)
    print('-> ', actual)

    assert expected == actual

