from business.cat_service import *
def test_create_cat():
    id_cat  = 23
    name = "Speedy"
    value = 9000.1
    epsilon = 0.0001
    cat = create_cat(id_cat,name,value)
    assert id_cat == get_id_cat(cat)
    assert name == get_name_cat(cat)
    assert abs(value - get_value_cat(cat))<epsilon


def test_validate_cat():
    id_cat = 23
    name = "Speedy"
    value = 9000.1
    cat = create_cat(id_cat,name,value)
    validate_cat(cat)
    invalid_id_cat = -23
    invalid_name = ""
    invalid_value = -9000.1
    invalid_cat = create_cat(invalid_id_cat,invalid_name,invalid_value)
    try:
        validate_cat(invalid_cat)
        assert False
    except ValueError as ve:
        assert str(ve) == "invalid cat id!\ninvalid name!\ninvalid value!"


def test_add_cat_to_shelter():
    shelter = {}
    assert len(shelter) == 0
    id_cat = 23
    name = "Speedy"
    value = 9000.1
    cat = create_cat(id_cat, name, value)
    add_cat_to_shelter(cat,shelter)
    assert len(shelter) == 1
    found_cat = search_cat_in_shelter_by_id(id_cat,shelter)
    assert equal_cats(cat,found_cat)
    try:
        add_cat_to_shelter(cat,shelter)
        assert False
    except ValueError as ve:
        assert str(ve) == "cat id already exists in the shelter!\n"


def test_create_validate_and_add_cat_to_shelter():
    pass


def test_compute_cat_value_average():
    shelter = {}
    id_cat = 23
    name = "Jordan"
    value_a = 9000.1
    cat = create_cat(id_cat, name, value_a)
    add_cat_to_shelter(cat,shelter)

    id_cat = 24
    name = "Kobe"
    value_b = 9000.05
    cat = create_cat(id_cat, name, value_b)
    add_cat_to_shelter(cat, shelter)

    id_cat = 30
    name = "Curry"
    value_c = 8000.1
    cat = create_cat(id_cat, name, value_c)
    add_cat_to_shelter(cat,shelter)


    epsilon = 0.0001
    average = compute_cat_value_average(shelter)
    expected_average = (value_a + value_b + value_c)/3
    assert(abs(average-expected_average)<epsilon)

def run_all_tests():
    test_create_cat()
    test_validate_cat()
    test_add_cat_to_shelter()
    test_create_validate_and_add_cat_to_shelter()
    test_compute_cat_value_average()
