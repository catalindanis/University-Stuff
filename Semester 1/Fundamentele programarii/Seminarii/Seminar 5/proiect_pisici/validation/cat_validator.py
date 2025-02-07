from domain.cat import *
def validate_cat(cat):
    """
    function that checks if the cat cat has
        an id_cat >0, an nonempty name and a value greater than 0.0
    :param cat: a cat with an int id_cat, a string name and a float value
    :return: -, if the cat is valid
    :raises: ValueError with string message:
                "invalid cat id!\n", if the id_cat is <=0
                "invalid name!\n", if the name is ""
                "invalid value!", if the value is<=0.0
    """
    errors = ""
    if get_id_cat(cat)<0:
        errors += "invalid cat id!\n"
    if get_name_cat(cat)=="":
        errors += "invalid name!\n"
    if get_value_cat(cat)<0:
        errors += "invalid value!"
    if len(errors)>0:
        raise ValueError(errors)
