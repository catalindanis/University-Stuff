def getExpenseIndex(expense, expenses):
    """
    Functia returneaza (daca exista) indicele cheltuielii din lista de cheltuieli
    input  - cheltuiala
    output - indicele / -1 (daca nu exista)
    """
    for current_expense in expenses:
        if current_expense == expense:
            return expenses.index(current_expense)
    return -1


def expenseExists(expense, expenses):
    """
    Functia verifica daca o cheltuiala exista deja in lista de cheltuieli
    input  - cheltuiala
    output - True / False (in functie de caz)
    """
    return getExpenseIndex(expense, expenses) != -1


def addExpense(expense, expenses):
    """
    Functia adauga (daca nu exista deja) o cheltuiala in lista de cheltuieli
    input  - cheltuiala
    output - False (daca cheltuiala exista deja)
    """
    if expenseExists(expense, expenses):
        return False

    expenses.append(expense)
    return True


def updateExpense(old_expense, new_expense, expenses):
    """
    Functia actualizeaza (daca exista) o cheltuiala din lista de cheltuieli
    input  - cheltuiala care urmeaza sa fie actualizata
           - cheltuiala cu care va fi actualizata
    output - False (daca cheltuiala nu exista)
    """
    if not expenseExists(old_expense, expenses):
        return False

    index = getExpenseIndex(old_expense, expenses)
    expenses[index] = new_expense

    return True


def removeExpensesFromDay(day, expenses):
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


def removeExpensesFromInterval(day1, day2, expenses):
    """
    Functia sterge toate cheltuielile situate intre 2 zile
    input  - ziua1, ziua2 (ziua1 <= ziua2)
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_list = list(expenses)

    for expense in copy_list:
        if expense.getDay() >= day1 and expense.getDay() <= day2:
            counter += 1
            expenses.remove(expense)

    return counter


def removeExpensesOfType(type, expenses):
    """
    Functia sterge toate cheltuielile de un anumit tip
    input  - tipul cheltuielii
    output - numarul de cheltuieli sterse (numar natural)
    """
    counter = 0
    copy_list = list(expenses)

    for expense in copy_list:
        if expense.getType() == type:
            counter += 1
            expenses.remove(expense)

    return counter


def searchExpensesGreaterThanSum(sum, expenses):
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


def searchExpensesBeforeDayAndLessThanSum(day, sum, expenses):
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


def searchExpensesOfType(type, expenses):
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


def filterExpensesByType(type, expenses):
    """
    Functia returneaza o lista care contine toate cheltuielile diferite de un anumit tip
    input  - tipul cheltuielii (ExpenseType)
    output - numarul de cheltuieli eliminate (numar natural)
    """
    result = []
    for expense in expenses:
        if expense.getType() != type:
            result.append(expense)
    return result


def filterExpensesLessThanSum(sum, expenses):
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