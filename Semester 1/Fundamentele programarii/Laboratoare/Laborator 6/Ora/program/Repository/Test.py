from Domain.Expense import Expense
from Repository.Repository import ExpensesRepository

def runTests():
    """
    Functia ruleaza un scenariu de teste pentru functiile clasei repository cu niste valori
    cunsocute
    :return:
    """

    repo = ExpensesRepository()

    assert repo.addExpense(Expense(1, 1, 1)) == True
    assert repo.addExpense(Expense(2, 2, 2)) == True
    assert repo.addExpense(Expense(3, 3, 3)) == True
    assert repo.deleteExpenseByIndex(0) == True
    assert repo.addExpense(Expense(4, 4, 4)) == True
    assert repo.deleteExpense(Expense(4, 4, 4)) == True
    assert repo.deleteExpense(Expense(1, 1, 1)) == False
    assert repo.deleteExpenseByIndex(-5) == False
    assert repo.setExpenseByIndex(0, Expense(1, 1, 1)) == True
    assert repo.setExpenseByIndex(-3, Expense(1, 1, 1)) == False
    assert repo.getExpenseIndex(Expense(1, 1, 1)) == 0
    assert repo.getExpenseIndex(Expense(2, 2, 2)) == -1
    assert repo.getExpenseByIndex(0) == Expense(1, 1, 1)
    assert repo.getExpenseByIndex(-5) == None
    assert repo.getAllExpenses() == [Expense(1, 1, 1),
                                     Expense(3, 3, 3)]
    assert repo.addCurrentToHistory() == True
    assert repo.addExpense(Expense(2, 2, 2)) == True
    assert repo.addCurrentToHistory() == True
    assert repo.getAllExpenses() == [Expense(1, 1, 1),
                                     Expense(3, 3, 3),
                                     Expense(2, 2, 2)]
    assert repo.removeLastFromHistory() == True
    assert repo.getAllExpenses() == [Expense(1, 1, 1),
                                     Expense(3, 3, 3)]