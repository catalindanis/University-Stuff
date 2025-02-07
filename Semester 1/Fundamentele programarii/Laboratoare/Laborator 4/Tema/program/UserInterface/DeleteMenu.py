from program.UserInterface.Menu import *
from program.Domain.Logic import *
from program.UserInterface.InputHandler import *

class DeleteMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de stergere.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului stergere.
        input  -
        output -
        """
        Menu.options_message = """1.Sterge cheltuielile dintr-o zi
2.Sterge cheltuielile din interval
3.Sterge cheltuielile de un anumit tip
4.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, expenses):
        """
        Functia reprezinta partea de interpretare a input-ului a meniului stergere.
        input  - datele introduse de utilizator (string)
        output -
        """
        Menu.extra_message = ""

        value_limits = [1, 4]
        actions = [DeleteMenu.__deleteExpensesFromDay,
                   DeleteMenu.__deleteExpensesFromInterval,
                   DeleteMenu.__deleteExpensesOfType]

        try:
            input = int(input)

            if input < value_limits[0] or input > value_limits[1]:
                raise ValueError

            if input == value_limits[1]:
                return False

            actions[input - 1](expenses)
        except ValueError:
            Menu.extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    @staticmethod
    def __deleteExpensesFromDay(expenses):
        """
        Functia apeleaza stergerea cheltuielilor dintr-o anumita zi
        si afiseaza mesaje corespunzatoare in functie de rezultat
        input  -
        output -
        """
        day = getDayFromUser()

        counter = removeExpensesFromDay(day, expenses)
        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli in ziua {day}!"
        else:
            Menu.extra_message = "S-a sters 1 cheltuiala!" if counter == 1 else\
                (f"S-au sters {counter} cheltuieli!", counter)

    @staticmethod
    def __deleteExpensesFromInterval(expenses):
        """
        Functia apeleaza stergerea cheltuielilor dintr-un anumit interval
        si afiseaza mesaje corespunzatoare in functie de rezultat
        input  -
        output -
        """
        day1 = getDayFromUser()
        day2 = getDayFromUser(day1)

        counter = removeExpensesFromInterval(day1, day2, expenses)
        if counter == 0:
            if day1 == day2:
                Menu.extra_message = f"Nu exista cheltuieli in ziua {day1}!"
            else:
                Menu.extra_message = f"Nu exista cheltuieli intre zilele {day1}-{day2}!"
        else:
            Menu.extra_message = "S-a sters 1 cheltuiala!" if counter == 1 else \
                (f"S-au sters {counter} cheltuieli!", counter)

    @staticmethod
    def __deleteExpensesOfType(expenses):
        """
        Functia apeleaza stergerea cheltuielilor de un anumit tip
        si afiseaza mesaje corespunzatoare in functie de rezultat
        input  -
        output -
        """
        type = getTypeFromUser()

        counter = removeExpensesOfType(type, expenses)
        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = "S-a sters 1 cheltuiala!" if counter == 1 else \
                (f"S-au sters {counter} cheltuieli!", counter)

    @staticmethod
    def run(expenses):
        """
        Functia principala a clasei care dirijeaza executia acesteia.
        input  -
        output -
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            DeleteMenu.__print()
            input = getUserInput()
            if DeleteMenu.__handleInput(input, expenses) == False:
                stop_requested = True