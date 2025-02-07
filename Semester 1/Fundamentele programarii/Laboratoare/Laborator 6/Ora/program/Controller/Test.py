from Controller.Service import ExpensesService
from Domain.Expense import Expense
from Repository.Repository import ExpensesRepository

def runTests():
    """
    Functia ruleaza un scenariu de teste pentru functiile clasei service cu niste valori
    cunsocute
    :return:
    """
    repository = ExpensesRepository()
    service = ExpensesService(repository)

    #add
    assert service.addExpense(Expense(1, 1, 1)) == True
    assert service.addExpense(Expense(1, 1, 1)) == False

    assert service.updateExpense(Expense(1, 1, 1),
                                 Expense(2, 2, 2)) == 1
    assert service.updateExpense(Expense(1, 1, 1),
                                 Expense(2, 2, 2)) == -1
    assert service.updateExpense(Expense(2, 2, 2),
                                 Expense(2, 2, 2)) == 0

    #delete
    assert service.deleteExpensesFromDay(2) == 1
    assert service.deleteExpensesFromDay(1) == 0
    assert service.addExpense(Expense(1, 1, 1)) == True
    assert service.addExpense(Expense(2, 2, 2)) == True
    assert service.addExpense(Expense(3, 3, 3)) == True
    assert service.deleteExpensesFromInterval(1, 2) == 2
    assert service.deleteExpensesFromInterval(1, 2) == 0
    assert service.deleteExpensesOfType(3) == 1
    assert service.deleteExpensesOfType(1) == 0

    #search
    e1 = Expense(1, 1, 1)
    e2 = Expense(2, 2, 2)
    e3 = Expense(3, 3, 3)
    assert service.addExpense(e1) == True
    assert service.addExpense(e2) == True
    assert service.addExpense(e3) == True
    assert service.searchExpensesGreaterThanSum(1) == [e2, e3]
    assert service.searchExpensesGreaterThanSum(3) == []
    assert service.searchExpensesBeforeDayAndLessThanSum(3, 3) == [e1, e2]
    assert service.searchExpensesBeforeDayAndLessThanSum(3, 2) == [e1]
    assert service.searchExpensesBeforeDayAndLessThanSum(1, 5) == []
    assert service.searchExpensesOfType(2) == [e2]
    assert service.searchExpensesOfType(1) == [e1]
    assert service.searchExpensesOfType(4) == []

    #filter
    assert service.filterExpensesByType(1) == 1
    assert service.filterExpensesByType(1) == 0
    assert service.filterExpensesByType(4) == 0
    assert service.filterExpensesLessThanSum(3) == 1
    assert service.filterExpensesLessThanSum(4) == 1
    assert service.filterExpensesLessThanSum(4) == 0

    #undo
    assert service.addExpense(e1) == True
    assert service.addExpense(e2) == True
    assert service.addExpense(e3) == True
    assert service.searchExpensesGreaterThanSum(1) == [e2, e3]
    assert service.undoLastOperation() == True
    assert service.searchExpensesGreaterThanSum(1) == [e2]
    assert service.searchExpensesOfType(2) == [e2]
    assert service.undoLastOperation() == True
    assert service.searchExpensesOfType(2) == []


