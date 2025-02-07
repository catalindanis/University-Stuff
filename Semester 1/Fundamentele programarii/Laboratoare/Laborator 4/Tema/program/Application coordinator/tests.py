from program.Domain.Logic import *
from program.Domain.Expense import *

def test():
    """
    Functia testeaza valori cunoscute inainte de inceperea programului
    pentru a asigura buna functionare a aplicatiei
    input  -
    output -
    """
    e1 = Expense(1, 150, ExpenseType.TELEFON)
    e2 = Expense(2, 253.2, ExpenseType.ALTELE)
    e3 = Expense(10, 125, ExpenseType.MANCARE)
    e4 = Expense(20, 1000, ExpenseType.INTRETINERE)
    e5 = Expense(20, 25, ExpenseType.MANCARE)
    e6 = Expense(20, 100, ExpenseType.TELEFON)
    e7 = Expense(10, 311, ExpenseType.MANCARE)
    e8 = Expense(10, 512.22, ExpenseType.TELEFON)

    expenses = []

    assert expenseExists(e1, expenses) == False
    assert getExpenseIndex(e2, expenses) == -1
    assert addExpense(e1, expenses) == True
    assert getExpenseIndex(e1, expenses) == 0
    assert updateExpense(e1, e4, expenses) == True
    assert getExpenseIndex(e1, expenses) == -1
    assert getExpenseIndex(e4, expenses) == 0
    assert addExpense(e4, expenses) == False
    assert updateExpense(e3, e2, expenses) == False
    assert addExpense(e5, expenses) == True
    assert addExpense(e6, expenses) == True
    assert expenses == [e4, e5, e6]
    assert removeExpensesFromDay(e4.getDay(), expenses) == 3
    assert removeExpensesFromDay(1, expenses) == 0
    assert addExpense(e7, expenses) == True
    assert addExpense(e8, expenses) == True
    assert removeExpensesFromDay(e7.getDay(), expenses) == 2
    assert expenses == []
    assert addExpense(e1, expenses) == True
    assert addExpense(e2, expenses) == True
    assert addExpense(e3, expenses) == True
    assert addExpense(e4, expenses) == True
    assert removeExpensesFromInterval(2, 20, expenses) == 3
    assert expenses == [e1]
    assert removeExpensesFromInterval(2, 20, expenses) == 0
    assert addExpense(e6, expenses) == True
    assert addExpense(e8, expenses) == True
    assert removeExpensesOfType(ExpenseType.TELEFON, expenses) == 3
    assert removeExpensesOfType(ExpenseType.MANCARE, expenses) == 0
    assert expenses == []
    assert addExpense(e5, expenses) == True
    assert addExpense(e7, expenses) == True
    assert addExpense(e8, expenses) == True
    assert searchExpensesGreaterThanSum(100, expenses) == [e7, e8]
    assert searchExpensesGreaterThanSum(10, expenses) == [e5, e7, e8]
    assert searchExpensesGreaterThanSum(511.22, expenses) == [e8]
    assert searchExpensesGreaterThanSum(512.22, expenses) == []
    assert removeExpensesFromInterval(1, 30, expenses)
    assert addExpense(e1, expenses) == True
    assert addExpense(e2, expenses) == True
    assert addExpense(e3, expenses) == True
    assert addExpense(e4, expenses) == True
    assert addExpense(e5, expenses) == True
    assert addExpense(e6, expenses) == True
    assert addExpense(e7, expenses) == True
    assert addExpense(e8, expenses) == True
    assert searchExpensesBeforeDayAndLessThanSum(20, 300, expenses) == [e1, e2, e3]
    assert searchExpensesBeforeDayAndLessThanSum(21, 300, expenses) == [e1, e2, e3, e5, e6]
    assert searchExpensesBeforeDayAndLessThanSum(11, 1000, expenses) == [e1, e2, e3, e7, e8]
    assert searchExpensesOfType(ExpenseType.TELEFON, expenses) == [e1, e6, e8]
    assert searchExpensesOfType(ExpenseType.ALTELE, expenses) == [e2]
    assert searchExpensesOfType(ExpenseType.MANCARE, expenses) == [e3, e5, e7]
    assert removeExpensesFromInterval(1, 30, expenses) == 8
    assert searchExpensesOfType(ExpenseType.TELEFON, expenses) == []

    expenses.clear()