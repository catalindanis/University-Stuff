from UserInterface.Clasic.AddMenu import *
from UserInterface.Clasic.DeleteMenu import *
from UserInterface.Clasic.FilterMenu import *
from UserInterface.Clasic.SearchMenu import *

class MainMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul principal.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """

    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului principal
        :return:
        """
        Menu.options_message = """1.Adauga
2.Sterge
3.Cauta
4.Filtrare
5.Undo
6.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul principal
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        Menu.extra_message = ""

        value_limits = [1, 6]
        actions = [AddMenu.run,
                   DeleteMenu.run,
                   SearchMenu.run,
                   FilterMenu.run,
                   MainMenu.__undoOption]

        try:
            input = int(input)

            if input < value_limits[0] or input > value_limits[1]:
                raise ValueError

            if input == value_limits[1]:
                return False

            actions[input-1](service)
        except ValueError:
            Menu.extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    @staticmethod
    def run(service):
        """
        Functia principala a meniului principal care dirijeaza executia acestuia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            MainMenu.__print()
            input = getUserInput()
            if MainMenu.__handleInput(input, service) == False:
                stop_requested = True

    @staticmethod
    def __undoOption(service):
        """
        Functia apeleaza operatia de undo a ultimei operatii efectuate si afiseaza
        mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        if service.undoLastOperation() == False:
            Menu.extra_message = "Nu se mai poate realiza operatia de undo!"
        else:
            Menu.extra_message = "Operatia undo a fost realizata cu succes!"
