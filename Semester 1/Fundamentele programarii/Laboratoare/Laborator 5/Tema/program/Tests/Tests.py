from Business.Service import *
from Domain.Expense import *
from Domain.ExpenseType import *

def test():
    """
    Functia apeleaza metodele de testare pentru fiecare functionalitate
    a aplicatiei
    :return:
    """
    testGetExpenseIndex()
    testExpenseExists()
    testAddExpense()
    testDeleteExpense()
    testSearchExpense()
    testFilterExpense()


def testGetExpenseIndex():
    """
    Functia testeaza metoda de preluare a indexului unei cheltuieli
    din lista de cheltuieli cu un set de valori cunoscute
    :return:
    """
    expenses = []
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 150, ExpenseType.ALTELE)
    expenses.append(e1)

    assert getExpenseIndexFromList(expenses, e1) == 0
    assert getExpenseIndexFromList(expenses, e2) == -1


def testExpenseExists():
    """"
    Functia testeaza metoda de verificare a existentei unei cheltuieli
    din lista de cheltuieli cu un set de valori cunoscute
    :return:
    """
    expenses = []
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 150, ExpenseType.ALTELE)
    expenses.append(e1)

    assert expenseExistsInList(expenses, e1) == True
    assert expenseExistsInList(expenses, e2) == False


def testAddExpense():
    """
    Functia testeaza metoda de adaugare si de actualizare a cheltuielilor
    in lista de cheltuieli cu un set de valori cunoscute
    :return:
    """
    expenses = []
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 150, ExpenseType.ALTELE)

    assert addExpense(expenses, e1) == True
    assert addExpense(expenses, e1) == False
    assert updateExpense(expenses, e1, e1) == True
    assert updateExpense(expenses, e2, e2) == False


def testDeleteExpense():
    """
    Functia testeaza metodele de stergere a cheltuielilor
    din lista de cheltuieli cu un set de valori cunoscute
    :return:
    """
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 102, ExpenseType.ALTELE)
    e3 = Expense(2, 100.1, ExpenseType.TELEFON)
    e4 = Expense(4, 2.5, ExpenseType.ALTELE)

    expenses = [e1, e2, e3, e4]

    assert deleteExpensesFromDay(expenses, 3) == 0
    assert deleteExpensesFromDay(expenses, 1) == 1
    assert deleteExpensesFromDay(expenses, 2) == 2

    expenses = [e1, e2, e3, e4]

    assert deleteExpensesFromInterval(expenses,1, 3) == 3
    assert deleteExpensesFromInterval(expenses,5, 8) == 0

    expenses = [e1, e2, e3, e4]

    assert deleteExpensesOfType(expenses, ExpenseType.TELEFON) == 2
    assert deleteExpensesOfType(expenses, ExpenseType.INTRETINERE) == 0


def testSearchExpense():
    """
    Functia testeaza metodele de cautare in lista de cheltuieli
    cu un set de valori cunoscute
    :return:
    """
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 102, ExpenseType.ALTELE)
    e3 = Expense(2, 100.1, ExpenseType.TELEFON)
    e4 = Expense(4, 2.5, ExpenseType.ALTELE)

    expenses = [e1, e2, e3, e4]

    assert searchExpensesGreaterThanSum(expenses,100) == [e1, e2, e3]
    assert searchExpensesGreaterThanSum(expenses,150) == []
    assert searchExpensesBeforeDayAndLessThanSum(expenses,3, 150) == [e2, e3]
    assert searchExpensesBeforeDayAndLessThanSum(expenses,4, 2.5) == []
    assert searchExpensesOfType(expenses, ExpenseType.TELEFON) == [e1, e3]
    assert searchExpensesOfType(expenses, ExpenseType.IMBRACAMINTE) == []


def testFilterExpense():
    """
    Functia testeaza metodele de filtrare a listei de cheltuieli cu un set de valori cunoscute
    :return:
    """
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 102, ExpenseType.ALTELE)
    e3 = Expense(2, 100.1, ExpenseType.TELEFON)
    e4 = Expense(4, 2.5, ExpenseType.ALTELE)

    expenses = [e1, e2, e3, e4]

    assert filterExpensesByType(expenses,ExpenseType.IMBRACAMINTE) == [e1, e2, e3, e4]
    assert filterExpensesByType(expenses,ExpenseType.TELEFON) == [e2, e4]
    assert filterExpensesLessThanSum(expenses,102) == [e1, e2]
    assert filterExpensesLessThanSum(expenses,150) == [e1]
    assert filterExpensesLessThanSum(expenses,2.4) == [e1, e2, e3, e4]