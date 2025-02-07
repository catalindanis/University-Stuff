from validation.cat_validator import *
from infrastructure.repo_cats import *
def create_validate_and_add_cat_to_shelter(id_cat,name,value,shelter):
    """
    function that creates a cat from an int id_cat, string name and float value
    validates if the cat id is >0, the name is not empty and the value >0.0
    and if it is so adds the cat to the shelter if there is no other cat with the same id already in the shelter
    :param id_cat: int
    :param name: string
    :param value: float
    :param shelter: a dictionary of uniquely identifiable cats by their int id
    :return: -
    :raises: ValueError with string message:
                "invalid cat id!\n", if the id_cat is <=0
                "invalid name!\n", if the name is ""
                "invalid value!", if the value is<=0.0
            ValueError with string message:
                "inexistent id!\n", if the cat with the given cat id is not in the shelter
    """
    cat = create_cat(id_cat,name,value)
    validate_cat(cat)
    add_cat_to_shelter(cat,shelter)



def compute_cat_value_average(shelter):
    """
    function that computes the average of the values of the cats from the shelter
    :param shelter: dictionary of uniquely identifiable cats by their int id
    :return: result - float, sum(get_value(cat) for all cats in shelter)/ no of cats in shelter
    """
    return sum([get_value_cat(shelter[cat]) for cat in shelter])/len(shelter)
