from UserInterface.InputHandler import *

class DeleteMenu(Menu):
    """
    Clasa defineste proprietatile pe care le are meniul de stergere.
    Foloseste clasa parinte Meniu de unde preia toate atributele si le suprascrie
    """
    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului stergere
        :return:
        """
        Menu.options_message = """1.Sterge cheltuielile dintr-o zi
2.Sterge cheltuielile din interval
3.Sterge cheltuielile de un anumit tip
4.Inapoi"""

        print(Menu.title_message)
        print(Menu.options_message)
        if Menu.extra_message != "":
            print(Menu.extra_message)

    @staticmethod
    def __handleInput(input, service):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul stergere
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        Menu.extra_message = ""

        value_limits = [1, 4]
        actions = [DeleteMenu.__deleteExpensesFromDay,
                   DeleteMenu.__deleteExpensesFromInterval,
                   DeleteMenu.__deleteExpensesOfType]

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
            DeleteMenu.__print()
            input = getUserInput()
            if DeleteMenu.__handleInput(input, service) == False:
                stop_requested = True

    @staticmethod
    def __deleteExpensesFromDay(service):
        """
        Functia apeleaza citirea unei zile, stergerea cheltuielilor din ziua respectiva
        si afiseaza mesaje corespunzatoare in functie de rezultat
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        day = getDayFromUser()

        counter = service.deleteExpensesFromDay(day)

        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli in ziua {day}!"
        else:
            Menu.extra_message = f"S-a sters 1 cheltuiala din ziua {day}!" if counter == 1 else\
                (f"S-au sters {counter} cheltuieli din ziua {day}!")

    @staticmethod
    def __deleteExpensesFromInterval(service):
        """
        Functia apeleaza citirea a 2 zile reprezentand capetele intervalului, apeleaza
        stergerea cheltuielilor din intervalul precizat si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        day1 = getDayFromUser()
        day2 = getDayFromUser(day1)

        counter = service.deleteExpensesFromInterval(day1, day2)

        if counter == 0:
            if day1 == day2:
                Menu.extra_message = f"Nu exista cheltuieli in ziua {day1}!"
            else:
                Menu.extra_message = f"Nu exista cheltuieli intre zilele {day1}-{day2}!"
        else:
            if day1 == day2:
                Menu.extra_message = f"S-a sters 1 cheltuiala in ziua {day1}!" \
                    if counter == 1 else \
                    (f"S-au sters {counter} cheltuieli in ziua {day1}!")
            else:
                Menu.extra_message = f"S-a sters 1 cheltuiala intre zilele {day1}-{day2}!" \
                    if counter == 1 else \
                    (f"S-au sters {counter} cheltuieli intre zilele {day1}-{day2}!")

    @staticmethod
    def __deleteExpensesOfType(service):
        """
        Functia apeleaza citirea unui tip de cheltuiala, stergerea cheltuielilor de un
        anumit tip si afiseaza mesaje corespunzatoare
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        """
        type = getTypeFromUser()

        counter = service.deleteExpensesOfType(type)

        if counter == 0:
            Menu.extra_message = f"Nu exista cheltuieli de tipul {type.name.lower()}!"
        else:
            Menu.extra_message = f"S-a sters 1 cheltuiala de tipul {type.name.lower()}!" \
                if counter == 1 else \
                (f"S-au sters {counter} cheltuieli de tipul {type.name.lower()}!", counter)