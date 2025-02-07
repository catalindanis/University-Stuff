from Controller.Service import ExpensesService
from UserInterface.InputHandler import *
import re

class BatchMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul batch.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """

    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului de selectare
        :return:
        """
        Menu.options_message = ("Introduceti setul de comenzi separat prin simbolul ';'\n"
                                "Pentru a reveni inapoi, nu introduceti nimic\n"
                                "Comenzile disponibile:\n"
                                "-add zi suma tip\n"
                                "-sterge zi\n"
                                "-modifica zi suma tip zi_noua suma_noua tip_noua\n"
                                "-afiseaza tip")

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
        if input == "":
            return False

        commands_list = input.split(";")

        for index in range(len(commands_list)):
            commands_list[index] = commands_list[index].strip()

        for command in commands_list:
            values = command.split()

            if len(values) == 2:
                if values[0] == "afiseaza":
                    try:
                        type = int(values[1])

                        if type >= 1 and type <= 5:
                            result = service.searchExpensesOfType(ExpenseType(type))
                            result = formatListToPrint(result)
                            if result == "":
                                print("Nu exista cheltuieli de acest tip")
                            else:
                                print(result)
                    except ValueError:
                        pass
                elif values[0] == "sterge":
                    try:
                        day = int(values[1])

                        if day >= 1 and day <= 31:
                            result = service.deleteExpensesFromDay(day)
                            print(f"S-au sters {result} cheltuieli")
                    except ValueError:
                        pass
            elif len(values) == 4:
                if values[0] == "add":
                    try:
                        day = int(values[1])
                        sum = float(values[2])
                        type = int(values[3])

                        if day >= 1 and day <= 31 and sum > 0 and type >= 1 and type <= 5:
                            result = service.addExpense(Expense(day, sum, ExpenseType(type)))
                            if result == True:
                                print("Cheltuiala adaugata")
                    except ValueError:
                        pass
            elif len(values) == 7:
                if values[0] == "modifica":
                    try:
                        day = int(values[1])
                        sum = float(values[2])
                        type = int(values[3])

                        new_day = int(values[4])
                        new_sum = float(values[5])
                        new_type = int(values[6])

                        if day >= 1 and day <= 31 and sum > 0 and type >= 1 and type <= 5 and new_day >= 1 and new_day <= 31 and new_sum > 0 and new_type >= 1 and new_type <= 5:
                            result = service.updateExpense(Expense(day, sum, ExpenseType(type)),
                                                           Expense(new_day, new_sum, ExpenseType(new_type)))
                            if result == True:
                                print("Cheltuiala modificata cu succes")
                    except ValueError:
                        pass
        getUserInput("Apasa ENTER pentru a continua")



    @staticmethod
    def run(service):
        """
        Functia principala a meniului batch care dirijeaza executia acestuia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        stop_requested = False

        while not stop_requested:
            clearScreen()
            BatchMenu.__print()
            input = getUserInput()
            if BatchMenu.__handleInput(input, service) == False:
                stop_requested = True