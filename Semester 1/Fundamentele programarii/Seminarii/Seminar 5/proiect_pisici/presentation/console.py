from business.cat_service import *
def ui_add_cat_to_shelter(shelter):
    """
    function that reads an id, name and value from the user
        parses the id to an int, value to a float and calls a function to
        create validate and add the cat to the shelter
    :param shelter:
    :return: -
    """
    try:
        id_cat = int(input("Enter cat id: "))
    except ValueError:
        print("Invalid cat id!")
        return
    name = input("Enter cat name: ")
    try:
        value = float(input("Enter cat value: "))
    except ValueError:
        print("Invalid cat value!")
        return
    create_validate_and_add_cat_to_shelter(id_cat,name,value,shelter)
    print("the cat has been added to the shelter successfully!")

def ui_print_shelter(shelter):
    """
    function that prints the cats from the shelter
    :param shelter: dictionary with uniquely identifiable cats by their int id
    :return: -
    """
    if len(shelter) == 0:
        print("the shelter is empty!")
        return
    print("the cats in the shelter are:")
    for cat in shelter:
        print(print_cat(shelter[cat]))


def ui_average_cat_value(shelter):
    """
    function that prints the average value of the cats from the shelter
    :param shelter: dictionary of uniquely identifiable cats by their int id
    :return: -
    """
    if len(shelter) == 0:
        print("the shelter is empty!cannot compute average")
        return
    average = compute_cat_value_average(shelter)
    print(f"The average cat value in the shelter is: {average}")
def run():
    shelter = {}
    commands = {
        "add_cat":ui_add_cat_to_shelter,
        "print_cats":ui_print_shelter,
        "average_cat_value":ui_average_cat_value
    }
    while True:
        command_name = input(">>>")
        command_name = command_name.lower()
        command_name = command_name.strip()
        if command_name == "":
            continue
        if command_name == "quit":
            break
        if command_name in commands:
            try:
                commands[command_name](shelter)
            except ValueError as ve:
                print(ve)
        else:
            print("invalid command!")
