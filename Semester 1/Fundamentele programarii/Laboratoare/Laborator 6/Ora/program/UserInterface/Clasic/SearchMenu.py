from UserInterface.InputHandler import *
from Utils.Generals import *

class SearchMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de cautare.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului cautare.
        :return:
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
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul cautare
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
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
            SearchMenu.__print()
            input = getUserInput()
            if SearchMenu.__handleInput(input, service) == False:
                stop_requested = True

    @staticmethod
    def __searchExpensesGreaterThanSum(service):
        """
        Functia apeleaza citirea unei sume, cautarea cheltuielilor cu suma mai mare decat
        suma precizata si afiseaza mesaje corespunzatoare
        :param service: service-ul utilizat in aplicatie (ExpensesService)
        :return:
        """
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        result = service.searchExpensesGreaterThanSum(sum)

        if result == []:
            Menu.extra_message = f"Nu exista cheltuieli cu suma mai mare de {sum}!"
        else:
            Menu.extra_message = formatListToPrint(result)

    @staticmethod
    def __searchExpensesBeforeDayAndLessThanSum(service):
        """
        Functia apeleaza citirea unei zile, citirea unei cheltuieli, cautarea cheltuielilor
        realizate inainte de o ziua precizata si cu suma mai mica decat suma precizata
        si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        day = getDayFromUser()
        sum = getSumFromUser()
        sum = convertToIntIfPossible(sum)

        result = service.searchExpensesBeforeDayAndLessThanSum(day, sum)

        if result == []:
            Menu.extra_message = f"Nu exista cheltuieli inainte de ziua {day} si cu suma mai mica de {sum}!"
        else:
            Menu.extra_message = formatListToPrint(result)

    @staticmethod
    def __searchExpensesOfType(service):
        """
        Functia apeleaza citirea unui tip de cheltuiala, cautarea cheltuielilor de un
        anumit tip si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        type = getTypeFromUser()

        result = service.searchExpensesOfType(type)

        if result == []:
            Menu.extra_message = f"Nu exista cheltuieli de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = formatListToPrint(result)
