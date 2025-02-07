from logging import Filter

from program.UserInterface.Menu import *
from program.UserInterface.AddMenu import *
from program.UserInterface.DeleteMenu import *
from program.UserInterface.SearchMenu import *
from program.UserInterface.FilterMenu import *

class MainMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul principal.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """

    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului principal.
        input  -
        output -
        """
        Menu.options_message = """1.Adauga
2.Sterge
3.Cauta
4.Filtrare
5.Iesire"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, expenses):
        """
        Functia reprezinta partea de interpretare a input-ului a meniului principal.
        input  - datele introduse de utilizator (string)
        output -
        """
        Menu.extra_message = ""

        value_limits = [1, 5]
        actions = [AddMenu.run, DeleteMenu.run, SearchMenu.run, FilterMenu.run]

        try:
            input = int(input)

            if input < value_limits[0] or input > value_limits[1]:
                raise ValueError

            if input == value_limits[1]:
                return False

            actions[input-1](expenses)
        except ValueError:
            Menu.extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

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
            MainMenu.__print()
            input = getUserInput()
            if MainMenu.__handleInput(input, expenses) == False:
                stop_requested = True

        print("La revedere!")
