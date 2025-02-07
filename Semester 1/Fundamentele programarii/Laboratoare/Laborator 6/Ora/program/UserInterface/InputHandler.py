from Domain.Expense import *
from Domain.ExpenseType import *
from UserInterface.Generals import *
from UserInterface.Menu import *

def getDayFromUser(*limits):
    """
    Functia preia de la utilizator o anumita zi dintr-o luna a anului
    :param limits: intervalul zilelor (optional) (se poate preciza si numai limita inferioara)
    :return: ziua (numar natural din intervalul [1, 31] (sau intervalul precizat))
    """
    value_limits = [1, 31]
    if len(limits) > 0:
        value_limits[0] = limits[0]
    if len(limits) > 1:
        value_limits[1] = limits[1]

    extra_message = ""
    stop_requested = False

    while not stop_requested:
        clearScreen()
        day = getUserInput((f"Introduceti ziua {value_limits}: ") +
                           ("" if extra_message == "" else "\n" + extra_message + "\n"))
        try:
            day = int(day)

            if day < value_limits[0] or day > value_limits[1]:
                raise ValueError

            stop_requested = True
        except ValueError:
            extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    return day


def getSumFromUser():
    """
    Functia preia de la utilizator o anumita suma
    :return: suma (float, > 0)
    """
    extra_message = ""
    stop_requested = False

    while not stop_requested:
        clearScreen()
        sum = getUserInput("Introduceti suma (numar real, pozitiv): " +
                           ("" if extra_message == "" else "\n" + extra_message + "\n"))
        try:
            sum = float(sum)

            if sum <= 0:
                raise ValueError

            stop_requested = True
        except ValueError:
            extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    return sum


def getTypeFromUser():
    """
    Functia preia de la utilizator tipul unei cheltuieli
    :return: tipul cheltuielii (ExpensesType)
    """
    value_limits = [1, 5]
    extra_message = ""
    stop_requested = False

    while not stop_requested:
        clearScreen()
        type = getUserInput("Introduceti tipul cheltuielii: \n"
                            "1.Mancare\n"
                            "2.Intretinere\n"
                            "3.Imbracaminte\n"
                            "4.Telefon\n"
                            "5.Altele\n"+
                           ("" if extra_message == "" else extra_message + "\n"))
        try:
            type = int(type)

            if type < value_limits[0] or type > value_limits[1]:
                raise ValueError

            type = ExpenseType(type)
            stop_requested = True
        except ValueError:
            extra_message = Menu.INVALID_VALUE_MESSAGE
        except Exception as error:
            Menu.extra_message = error

    return type


def getExpenseFromUser():
    """
    Functia creeaza o cheltuiala pe baza datelor introduse de utilizator de la tastatura
    :return: cheltuiala creata (Expense)
    """
    return Expense(getDayFromUser(), getSumFromUser(), getTypeFromUser())


def getUserInput(*args):
    """
    Functia preia datele introduse de utilizator de la tastatura afisand un mesaj daca
    este transmis ca parametru
    :param args: mesaj de afisat (String) (optional)
    :return: input-ul introdus de utilizator (String)
    """
    return input("" if len(args) == 0 else str(args[0]))