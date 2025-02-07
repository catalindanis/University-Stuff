class ExpensesService:

    def __init__(self, repository):
        self.__repository = repository

    def addExpense(self, expense):
        """
        Functia realizeaza operatia de adaugare a unei cheltuieli in lista de cheltuieli
        :param expense: cheltuiala (Expense)
        :return: False (daca cheltuiala exista deja) / True (daca operatia s-a efectuat)
        """
        expense_index = self.__repository.getExpenseIndex(expense)

        if expense_index != -1:
            return False

        self.__repository.addExpense(expense)
        self.__repository.addCurrentToHistory()

        return True

    def updateExpense(self, old_expense, new_expense):
        """
        Functia realizeaza operatia de actualizare a unei cheltuieli vechi cu una noua
        :param old_expense: cheltuiala veche (Expense)
        :param new_expense: cheltuiala noua (Expense)
        :return: -1 (daca vechea cheltuiala nu exista)
                  0 (daca noua cheltuiala exista deja)
                  1 (daca operatia s-a efectuat)
        """
        expense_index = self.__repository.getExpenseIndex(old_expense)

        if expense_index == -1:
            return -1

        if (old_expense == new_expense or
                self.__repository.getExpenseIndex(new_expense) != -1):
            return 0

        self.__repository.setExpenseByIndex(expense_index, new_expense)
        self.__repository.addCurrentToHistory()
        return 1

    def deleteExpensesFromDay(self, day):
        """
        Functia realizeaza operatia de stergere a cheltuielilor dintr-o zi precizata
        :param day: ziua (integer)
        :return: numarul de zile sterse (integer)
        """
        counter = 0

        for expense in self.__repository.getAllExpenses():
            if expense.getDay() == day:
                self.__repository.deleteExpense(expense)
                counter += 1

        if counter > 0:
            self.__repository.addCurrentToHistory()
        return counter

    def deleteExpensesFromInterval(self, day1, day2):
        """
        Functia realizeaza operatia de stergere a cheltuielilor dintr-un interval precizat
        :param day1: capatul din stanga al intervalului (integer)
        :param day2: capatul din dreapta al interalulului (integer)
        :return: numarul de zile sterse (integer)
        """
        counter = 0

        for day in range(day1, day2+1):
            counter += self.deleteExpensesFromDay(day)

        if counter > 0:
            self.__repository.addCurrentToHistory()
        return counter

    def deleteExpensesOfType(self, type):
        """
        Functia realizeaza operatia de stergere a cheltuielilor de un anumit tip
        :param type: tipul cheltuielii (ExpenseType)
        :return: numarul de cheltuieli sterse (integer)
        """
        counter = 0

        for expense in self.__repository.getAllExpenses():
            if expense.getType() == type:
                self.__repository.deleteExpense(expense)
                counter += 1

        if counter > 0:
            self.__repository.addCurrentToHistory()
        return counter

    def searchExpensesGreaterThanSum(self, sum):
        """
        Functia realizeaza operatia de cautare a cheltuielilor cu suma mai mare decat
        suma precizata
        :param sum: suma (float)
        :return: lista de cheltuieli corespunzatoare (List of Expense)
        """
        result = [expense for expense in self.__repository.getAllExpenses() if expense.getSum() > sum]

        return result

    def searchExpensesBeforeDayAndLessThanSum(self, day, sum):
        """
        Functia realizeaza operatia de cautare a cheltuielilor realizate inainte de o zi
        precizata si cu suma mai mica decat o suma precizata
        :param day: ziua (integer)
        :param sum: suma (float)
        :return: lista de cheltuieli corespunzatoare (List of Expense)
        """
        result = [expense for expense in self.__repository.getAllExpenses()
                  if expense.getDay() < day and expense.getSum() < sum]

        return result

    def searchExpensesOfType(self, type):
        """
        Functia realizeaza operatia de cautare a cheltuielilor de un anumit tip precizat
        :param type: tipul cheltuielii (ExpenseType)
        :return: lista corespunzatoare (List of Expense)
        """
        result = [expense for expense in self.__repository.getAllExpenses()
                  if expense.getType() == type]

        return result

    def filterExpensesByType(self, type):
        """
        Functia realizeaza operatia de filtrare a listei de cheltuieli dupa tipul precizat
        :param type: tipul cheltuielii (ExpenseType)
        :return: numarul de cheltuieli eliminate (integer)
        """
        counter = 0

        for expense in self.__repository.getAllExpenses():
            if expense.getType() == type:
                self.__repository.deleteExpense(expense)
                counter += 1

        if counter > 0:
            self.__repository.addCurrentToHistory()
        return counter

    def filterExpensesLessThanSum(self, sum):
        """
        Functia realizeaza operatia de filtrare a listei de cheltuieli dupa cheltuielile
        cu suma mai mica decat suma data
        :param sum: suma (float)
        :return: numarul de cheltuieli eliminate (integer)
        """
        counter = 0

        for expense in self.__repository.getAllExpenses():
            if expense.getSum() < sum:
                self.__repository.deleteExpense(expense)
                counter += 1

        if counter > 0:
            self.__repository.addCurrentToHistory()
        return counter

    def undoLastOperation(self):
        """
        Functia realizeaza operatia de undo a ultimei operatii efectuate pe lista
        de cheltuieli
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        return self.__repository.removeLastFromHistory()