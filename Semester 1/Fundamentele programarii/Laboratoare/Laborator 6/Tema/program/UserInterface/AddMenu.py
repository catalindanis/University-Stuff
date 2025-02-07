from UserInterface.InputHandler import *

class AddMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de adaugare.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului adaugare.
        :return:
        """
        Menu.options_message = """1.Adauga o noua cheltuiala
2.Actualizeaza o cheltuiala
3.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul adaugare
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        Menu.extra_message = ""

        value_limits = [1, 3]
        actions = [AddMenu.__addExpense, AddMenu.__updateExpense]

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
        Functia principala a meniului adaugare care dirijeaza executia acestuia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            AddMenu.__print()
            input = getUserInput()
            if AddMenu.__handleInput(input, service) == False:
                stop_requested = True

    @staticmethod
    def __updateExpense(service):
        """
        Functia apeleaza citirea a 2 cheltuieli, apeleaza actualizarea unei cheltuieli
        si afiseaza mesaje corespunzatoare in functie de rezultat
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        old_expense = getExpenseFromUser()
        new_expense = getExpenseFromUser()

        result = service.updateExpense(old_expense, new_expense)

        if result == -1:
            Menu.extra_message = "Cheltuiala nu exista!"
        elif result == 0:
            Menu.extra_message = "Cheltuiala exista deja!"
        else:
            Menu.extra_message = "Cheltuiala actualizata cu succes!"

    @staticmethod
    def __addExpense(service):
        """
        Functia apeleaza citirea unei cheltuieli, apeleaza adaugarea unei noi cheltuieli
        si afiseaza mesaje corespunzatoare in functie de rezultat
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        expense = getExpenseFromUser()

        if service.addExpense(expense) == False:
            Menu.extra_message = "Cheltuiala exista deja!"
        else:
            Menu.extra_message = "Cheltuiala adaugata cu succes!"