def getExpenseIndexFromList(expenses, expense):
    """
    Functia returneaza (daca exista) indicele cheltuielii din lista de cheltuieli
    input  - cheltuiala
    output - indicele / -1 (daca nu exista)
    """
    for key, value in expenses.items():
        if value == expense:
            return key

    return -1


def expenseExistsInList(expenses, expense):
    """
    Functia verifica daca o cheltuiala exista deja in lista de cheltuieli
    input  - lista de cheltuieli, cheltuiala
    output - True / False (in functie de caz)
    """
    return getExpenseIndexFromList(expenses, expense) != -1


def addExpenseInList(expenses, expense):
    """
    Functia adauga o cheltuiala in lista de cheltuieli
    input  - cheltuiala
    output - False (daca cheltuiala exista deja)
    """
    expenses[len(expenses)] = expense

def updateExpenseInList(expenses, old_expense, new_expense):
    """
    Functia actualizeaza o cheltuiala din lista de cheltuieli
    input  - cheltuiala care urmeaza sa fie actualizata
           - cheltuiala cu care va fi actualizata
    output -
    """
    index = getExpenseIndexFromList(expenses, old_expense)
    expenses[index] = new_expense


def deleteExpensesFromDayFromList(expenses, day):
    """
    Functia sterge toate cheltuielile dintr-o anumita zi
    input  - ziua
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_dict = dict(expenses)

    for key, value in copy_dict.items():
        if value.getDay() == day:
            counter += 1
            del expenses[key]

    index = 0
    copy_dict = dict()
    for key, value in expenses.items():
        copy_dict[index] = value
        index += 1

    expenses = copy_dict

    return counter


def deleteExpensesFromIntervalFromList(expenses, day1, day2):
    """
    Functia sterge toate cheltuielile situate intre 2 zile
    input  - lista de cheltuieli, ziua1, ziua2 (ziua1 <= ziua2)
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_dict = dict(expenses)

    for key, value in copy_dict.items():
        if value.getDay() >= day1 and value.getDay() <= day2:
            counter += 1
            del expenses[key]

    index = 0
    copy_dict = dict()
    for key, value in expenses.items():
        copy_dict[index] = value
        index += 1

    expenses = copy_dict

    return counter


def deleteExpensesOfTypeFromList(expenses, type):
    """
    Functia sterge toate cheltuielile de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_dict = dict(expenses)

    for key, value in copy_dict.items():
        if value.getType() == type:
            counter += 1
            del expenses[key]

    index = 0
    copy_dict = dict()
    for key, value in expenses.items():
        copy_dict[index] = value
        index += 1

    expenses = copy_dict

    return counter


def searchExpensesGreaterThanSumFromList(expenses, sum):
    """
    Functia cauta cheltuielile cu suma mai mare decat o suma
    input  - suma
    output - lista cu cheltuielile corespunzatoare
    """
    result = {}

    for key, value in expenses.items():
        if sum < value.getSum():
            result[key] = value

    return result


def searchExpensesBeforeDayAndLessThanSumFromList(expenses, day, sum):
    """
    Functia cauta cheltuielile inainte de o zi si cu suma mai mica decat
    o suma data
    input  - ziua, suma
    output - lista cu cheltuielile corespunzatoare
    """
    result = {}

    for key, value in expenses.items():
        if value.getSum() < sum and value.getDay() < day:
            result[key] = value

    return result


def searchExpensesOfTypeFromList(expenses, type):
    """
    Functia cauta cheltuielile de un anumit tip
    input  - tipul cheltuielii (ExpenseType)
    output - lista cu cheltuielile corespunzatoare
    """
    result = {}

    for key, value in expenses.items():
        if value.getType() == type:
            result[key] = value

    return result


def filterExpensesByTypeFromList(expenses, type):
    """
    Functia returneaza o lista care contine toate cheltuielile diferite de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii (ExpenseType)
    output - numarul de cheltuieli eliminate (numar natural)
    """
    result = {}

    for key, value in expenses.items():
        if value.getType() != type:
            result[key] = value

    return result


def filterExpensesLessThanSumFromList(expenses, sum):
    """
    Functia returneaza o lista care contine toate cheltuielile care nu au
    suma mai mica decat o suma data
    input  - suma (numar real, strict pozitiv)
    output - numarul de cheltuieli eliminate (numar natural)
    """
    result = {}

    for key, value in expenses.items():
        if value.getSum() >= sum:
            result[key] = value

    return result