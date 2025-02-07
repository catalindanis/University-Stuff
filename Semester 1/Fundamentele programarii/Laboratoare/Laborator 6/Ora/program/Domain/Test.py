from Domain.Expense import Expense
from Domain.ExpenseType import ExpenseType

def runTests():
    e1 = Expense(1, 1, 1)
    assert e1.getDay() == 1
    assert e1.getSum() == 1
    assert e1.getType() == ExpenseType.MANCARE.value
