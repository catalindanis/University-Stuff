def create_cat(id_cat,name,value):
    """
    function that creates a cat from an int id_cat, string name and float value
    :param id_cat: int
    :param name: string
    :param value: float
    :return: a cat with the id id_cat, the name name and the value value
    """
    return {
        "id_cat":id_cat,
        "name":name,
        "value":value
    }

def get_id_cat(cat):
    """
    function that returns the int id of the cat cat
    :param cat: the cat with an int id
    :return: the int id of the cat
    """
    return cat["id_cat"]

def get_name_cat(cat):
    return cat["name"]

def get_value_cat(cat):
    return cat["value"]

def equal_cats(cat1,cat2):
    return get_id_cat(cat1) == get_id_cat(cat2)

def print_cat(cat):
    return f"[{get_id_cat(cat)}] {get_name_cat(cat)} : {get_value_cat(cat)}"
