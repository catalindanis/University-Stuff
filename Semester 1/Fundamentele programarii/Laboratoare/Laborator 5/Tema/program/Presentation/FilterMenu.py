from Business.Service import *
from Presentation.InputHandler import *
from Utils.Generals import *

class FilterMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de filtrare.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului filtrare.
        input  -
        output -
        """
        Menu.options_message = """1.Elimina toate cheltuielile de un anumit tip
2.Elimina toate cheltuielile mai mici decat o suma data
3.Inapoi"""

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

        value_limits = [1, 3]
        actions = [FilterMenu.__filterExpensesByType,
                   FilterMenu.__filterExpensesLessThanSum]

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
    def __filterExpensesByType(expenses):
        """
        Functia apeleaza filtrarea cheltuielilor de un anumit tip
        si afiseaza mesajele corespunzatoare in functie de rezultat
        input  -
        output -
        """
        type = getTypeFromUser()

        result = filterExpensesByType(expenses, type)

        if result == []:
            Menu.extra_message = f"Nu exista cheltuieli cu tipul diferit de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = formatListToPrint(result)

    @staticmethod
    def __filterExpensesLessThanSum(expenses):
        """
        Functia apeleaza filtrarea cheltuielilor mai mici decat o suma data
        decat o suma si afiseaza mesajele corespunzatoare in functie de rezultat
        input  -
        output -
        """
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        result = filterExpensesLessThanSum(expenses, sum)

        if result == []:
            Menu.extra_message = f"Nu exista cheltuieli cu suma mai mare sau egala cu {sum}!"
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
            FilterMenu.__print()
            input = getUserInput()
            if FilterMenu.__handleInput(input, expenses) == False:
                stop_requested = True
