def getExpenseIndexFromList(expenses, expense):
    """
    Functia returneaza (daca exista) indicele cheltuielii din lista de cheltuieli
    input  - cheltuiala
    output - indicele / -1 (daca nu exista)
    """
    if expense in expenses:
        return expenses.index(expense)

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
    expenses.append(expense)

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
    copy_list = list(expenses)

    for expense in copy_list:
        if expense.getDay() == day:
            counter += 1
            expenses.remove(expense)

    return counter


def deleteExpensesFromIntervalFromList(expenses, day1, day2):
    """
    Functia sterge toate cheltuielile situate intre 2 zile
    input  - lista de cheltuieli, ziua1, ziua2 (ziua1 <= ziua2)
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_list = list(expenses)

    for expense in copy_list:
        if expense.getDay() >= day1 and expense.getDay() <= day2:
            counter += 1
            expenses.remove(expense)

    return counter


def deleteExpensesOfTypeFromList(expenses, type):
    """
    Functia sterge toate cheltuielile de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_list = list(expenses)

    for expense in copy_list:
        if expense.getType() == type:
            counter += 1
            expenses.remove(expense)

    return counter


def searchExpensesGreaterThanSumFromList(expenses, sum):
    """
    Functia cauta cheltuielile cu suma mai mare decat o suma
    input  - suma
    output - lista cu cheltuielile corespunzatoare
    """
    result = []

    for expense in expenses:
        if sum < expense.getSum():
            result.append(expense)

    return result


def searchExpensesBeforeDayAndLessThanSumFromList(expenses, day, sum):
    """
    Functia cauta cheltuielile inainte de o zi si cu suma mai mica decat
    o suma data
    input  - ziua, suma
    output - lista cu cheltuielile corespunzatoare
    """
    result = []

    for expense in expenses:
        if expense.getDay() < day and expense.getSum() < sum:
            result.append(expense)

    return result


def searchExpensesOfTypeFromList(expenses, type):
    """
    Functia cauta cheltuielile de un anumit tip
    input  - tipul cheltuielii (ExpenseType)
    output - lista cu cheltuielile corespunzatoare
    """
    result = []

    for expense in expenses:
        if expense.getType() == type:
            result.append(expense)

    return result


def filterExpensesByTypeFromList(expenses, type):
    """
    Functia returneaza o lista care contine toate cheltuielile diferite de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii (ExpenseType)
    output - numarul de cheltuieli eliminate (numar natural)
    """
    result = []

    for expense in expenses:
        if expense.getType() != type:
            result.append(expense)

    return result


def filterExpensesLessThanSumFromList(expenses, sum):
    """
    Functia returneaza o lista care contine toate cheltuielile care nu au
    suma mai mica decat o suma data
    input  - suma (numar real, strict pozitiv)
    output - numarul de cheltuieli eliminate (numar natural)
    """
    result = []

    for expense in expenses:
        if expense.getSum() >= sum:
            result.append(expense)

    return result