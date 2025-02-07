from Presentation.Menu import *
from Presentation.InputHandler import *
from Utils.Generals import *
from Business.Service import *

class SearchMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de cautare.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului cautare.
        input  -
        output -
        """
        Menu.options_message = """1.Tipareste cheltuielile mai mari decat o suma
2.Tipareste cheltuielile inainte de o zi si mai mici decat o suma
3.Tipareste cheltuielile de un anumit tip
4.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, expenses):
        """
        Functia reprezinta partea de interpretare a input-ului a meniului cautare.
        input  - datele introduse de utilizator (string)
        output -
        """
        Menu.extra_message = ""

        value_limits = [1, 4]
        actions = [SearchMenu.__searchExpensesGreaterThanSum,
                   SearchMenu.__searchExpensesBeforeDayAndLessThanSum,
                   SearchMenu.__searchExpensesOfType]

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
    def __searchExpensesGreaterThanSum(expenses):
        """
        Functia apeleaza cautarea cheltuielilor mai mari decat o suma
        si afiseaza mesajele corespunzatoare in functie de rezultat
        input  -
        output -
        """
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        result = searchExpensesGreaterThanSum(expenses, sum)

        if result == {}:
            Menu.extra_message = f"Nu exista cheltuieli cu suma mai mare de {sum}!"
        else:
            Menu.extra_message = formatListToPrint(result)

    @staticmethod
    def __searchExpensesBeforeDayAndLessThanSum(expenses):
        """
        Functia apeleaza cautarea cheltuielilor inainte de o zi si mai mici
        decat o suma si afiseaza mesajele corespunzatoare in functie de rezultat
        input  -
        output -
        """
        day = getDayFromUser()
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        result = searchExpensesBeforeDayAndLessThanSum(expenses, day, sum)

        if result == {}:
            Menu.extra_message = f"Nu exista cheltuieli inainte de ziua {day} si cu suma mai mica de {sum}!"
        else:
            Menu.extra_message = formatListToPrint(result)

    @staticmethod
    def __searchExpensesOfType(expenses):
        """
        Functia apeleaza cautarea cheltuielilor inainte de o zi si mai mici
        decat o suma si afiseaza mesajele corespunzatoare in functie de rezultat
        input  -
        output -
        """
        type = getTypeFromUser()

        result = searchExpensesOfType(expenses, type)

        if result == {}:
            Menu.extra_message = f"Nu exista cheltuieli de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = formatListToPrint(result)

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
            SearchMenu.__print()
            input = getUserInput()
            if SearchMenu.__handleInput(input, expenses) == False:
                stop_requested = True
