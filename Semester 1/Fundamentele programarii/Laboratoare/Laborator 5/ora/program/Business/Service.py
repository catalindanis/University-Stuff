from Infrastructure.Repository import *


def searchExpensesOfType(expenses, type):
    """
    Functia apeleaza cautarea cheltuielilor inainte de o zi si mai mici
    input  - lista de cheltuieli, tipul
    output - lista rezultat
    """

    result = searchExpensesOfTypeFromList(expenses, type)

    return result

def searchExpensesBeforeDayAndLessThanSum(expenses, day, sum):
    """
    Functia apeleaza cautarea cheltuielilor inainte de o zi si mai mici
    input  - lista de cheltuieli, ziua si suma
    output - lista rezultat
    """

    result = searchExpensesBeforeDayAndLessThanSumFromList(expenses, day, sum)

    return result

def searchExpensesGreaterThanSum(expenses, sum):
    """
    Functia apeleaza cautarea cheltuielilor mai mari decat o suma
    input  - lista de cheltuieli, suma
    output - lista rezultat
    """

    result = searchExpensesGreaterThanSumFromList(expenses, sum)

    return result

def filterExpensesLessThanSum(expenses, sum):
    """
    Functia apeleaza filtrarea cheltuielilor mai mici decat o suma data
    input  - lisdta de cheltuieli, suma
    output - lista rezultat
    """

    result = filterExpensesLessThanSumFromList(expenses, sum)

    return result

def filterExpensesByType(expenses, type):
    """
    Functia apeleaza filtrarea cheltuielilor de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii
    output - lista rezultat
    """

    result = filterExpensesByTypeFromList(expenses, type)

    return result

def deleteExpensesFromDay(expenses, day):
    """
    Functia apeleaza stergerea cheltuielilor dintr-o anumita zi
    input  - lista de cheltuieli, ziua
    output - numarul de cheltuieli sterse
    """
    counter = deleteExpensesFromDayFromList(expenses, day)

    return counter

def deleteExpensesFromInterval(expenses, day1, day2):
    """
    Functia apeleaza stergerea cheltuielilor dintr-un anumit interval
    input  - lista de cheltuieli, prima zi, a doua zi
    output - numarul de cheltuieli sterse
    """
    counter = deleteExpensesFromIntervalFromList(expenses, day1, day2)

    return counter

def deleteExpensesOfType(expenses, type):
    """
    Functia apeleaza stergerea cheltuielilor de un anumit tip
    input  - lista de cheltuieli, tipul cheltuielii
    output - numarul de cheltuieli sterse
    """
    counter = deleteExpensesOfTypeFromList(expenses, type)

    return counter

def updateExpense(expenses, old_expense, new_expense):
    """
    Functia apeleaza actualizarea unei noi cheltuieli
    input  - lista de cheltuieli, cheltuiala veche si cheltuiala noua
    output - False daca cheltuiala veche nu exista / True in caz contrar
    """
    if not expenseExistsInList(expenses, old_expense):
        return False

    updateExpenseInList(expenses, old_expense, new_expense)

    return True

def addExpense(expenses, expense):
    """
    Functia apeleaza adaugarea unei noi cheltuieli
    input  - lista de cheltuieli, cheltuiala
    output - False daca cheltuiala exista deja / True in caz contrar
    """
    if expenseExistsInList(expenses, expense):
        return False

    addExpenseInList(expenses, expense)

    return True




