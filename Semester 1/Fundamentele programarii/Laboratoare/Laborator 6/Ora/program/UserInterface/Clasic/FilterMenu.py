from UserInterface.InputHandler import *
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
        :return:
        """
        Menu.options_message = """1.Elimina toate cheltuielile de un anumit tip
2.Elimina toate cheltuielile mai mici decat o suma data
3.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul filtrare
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
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

            actions[input - 1](service)
        except ValueError:
            Menu.extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    @staticmethod
    def run(service):
        """
        Functia principala a clasei care dirijeaza executia acesteia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            FilterMenu.__print()
            input = getUserInput()
            if FilterMenu.__handleInput(input, service) == False:
                stop_requested = True

    @staticmethod
    def __filterExpensesByType(service):
        """
        Functia apeleaza citirea unui tip de cheltuiala, filtrarea cheltuielilor de
        tipul precizat si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        type = getTypeFromUser()

        counter = service.filterExpensesByType(type)

        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli cu tipul diferit de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = f"Au fost eliminate {counter} cheltuieli!"

    @staticmethod
    def __filterExpensesLessThanSum(service):
        """
        Functia apeleaza citirea unei sume, filtrarea cheltuielilor mai mici decat suma
        data si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        counter = service.filterExpensesLessThanSum(sum)

        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli cu suma mai mare sau egala decat {sum}!"
        else:
            Menu.extra_message = f"Au fost eliminate {counter} cheltuieli!"
