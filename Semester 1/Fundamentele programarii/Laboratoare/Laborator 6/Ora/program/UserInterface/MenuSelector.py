from UserInterface.Batch.BatchMenu import BatchMenu
from UserInterface.Clasic.MainMenu import MainMenu
from UserInterface.InputHandler import *

class MenuSelector(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de selectare a tipului de meniu.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """

    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului de selectare
        :return:
        """
        Menu.title_message = "~~Selector de meniu~~"
        Menu.options_message = """1.Clasic
2.Batch
3.Iesire"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul selectare
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        Menu.extra_message = ""

        value_limits = [1, 3]
        actions = [MainMenu.run,
                   BatchMenu.run]

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
        Functia principala a meniului de selectare care dirijeaza executia acestuia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        stop_requested = False

        while not stop_requested:
            clearScreen()
            MenuSelector.__print()
            input = getUserInput()
            if MenuSelector.__handleInput(input, service) == False:
                stop_requested = True

            print("La revedere!")