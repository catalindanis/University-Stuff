from domain.cat import *
def add_cat_to_shelter(cat,shelter):
    """
    function that adds a cat to the shelter that contains cats that are uniquely identifiable by their int id
    :param cat: a cat with an int id_cat
    :param shelter: a dictionary with the unique set of keys representing the unique ids of the cats and the values each cat corresponding to their  int id
    :return:-
    :raises: ValueError with string message:
                "cat id already exists in the shelter!\n", if there is a cat with the same id already in the shelter
    """
    if get_id_cat(cat) in shelter:
            raise ValueError("cat id already exists in the shelter!\n")
    shelter[get_id_cat(cat)]=cat

def search_cat_in_shelter_by_id(cat_id,shelter):
    """
    function that searches a cat by its id in the shelter of cats uniquely identifiable by their int id
    :param cat_id: int, the id of a cat
    :param shelter: dictionary of uniquely identifiable cats by their int id
    :return: cat, the cat with the given cat_id in the shelter, if it exists
    :raises: ValueError with string message:
                "inexistent id!\n", if the cat with the given cat id is not in the shelter
    """
    ok = False
    if cat_id in shelter:
        ok = True
        return shelter[cat_id]
    if not ok:
        raise ValueError("inexistent id!\n")
