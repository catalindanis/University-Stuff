from program.UserInterface.Menu import *
from program.Domain.Logic import *
from program.UserInterface.InputHandler import *

class AddMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de adaugare.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului adaugare.
        input  -
        output -
        """
        Menu.options_message = """1.Adauga o noua cheltuiala
2.Actualizeaza o cheltuiala
3.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, expenses):
        """
        Functia reprezinta partea de interpretare a input-ului a meniului adaugare.
        input  - datele introduse de utilizator (string)
        output -
        """
        Menu.extra_message = ""

        value_limits = [1, 3]
        actions = [AddMenu.__addExpense,
                   AddMenu.__updateExpense]

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
    def __addExpense(expenses):
        """
        Functia apeleaza adaugarea unei noi cheltuieli
        si afiseaza mesaje corespunzatoare in functie de rezultat
        input  -
        output -
        """
        expense = getExpenseFromUser()

        if addExpense(expense, expenses) == False:
            Menu.extra_message = "Cheltuiala exista deja!"
        else:
            Menu.extra_message = "Cheltuiala adaugata cu succes!"

    @staticmethod
    def __updateExpense(expenses):
        """
        Functia apeleaza actualizarea unei noi cheltuieli
        si afiseaza mesaje corespunzatoare in functie de rezultat
        input  -
        output -
        """
        old_expense = getExpenseFromUser()

        if expenseExists(old_expense, expenses) == False:
            Menu.extra_message = "Cheltuiala nu exista!"
            return

        new_expense = getExpenseFromUser()

        updateExpense(old_expense, new_expense, expenses)

        Menu.extra_message = "Cheltuiala actualizata cu succes!"

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
            AddMenu.__print()
            input = getUserInput()
            if AddMenu.__handleInput(input, expenses) == False:
                stop_requested = True