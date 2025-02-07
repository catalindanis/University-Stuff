class ExpensesRepository:

    def __init__(self):
        self.__expenses = list()
        self.__expensesHistory = (list(list()))

    def addExpense(self, expense):
        """
        Functia adauga cheltuiala transmisa in lista de cheltuieli
        :param expense: cheltuiala (Expense)
        :return: False (daca operatia nu a reusit) / True (in caz contrar)
        """
        try:
            self.__expenses.append(expense)
            return True
        except:
            return False

    def deleteExpenseByIndex(self, index):
        """
        Functia sterge cheltuiala de la indicele precizat din lista de cheltuieli
        :param index: indicele cheltuielii (integer)
        :return: False (daca operatia nu a reusit) / True (in caz contrar)
        """
        try:
            self.__expenses.pop(index)
            return True
        except IndexError:
            return False

    def deleteExpense(self, expense):
        """
        Functia sterge cheltuiala precizata din lista de cheltuieli
        :param expense: cheltuiala (Expense)
        :return: False (daca operatia nu a reusit) / True (in caz contrar)
        """
        try:
            self.__expenses.remove(expense)
            return True
        except ValueError:
            return False

    def setExpenseByIndex(self, index, expense):
        """
        Functia seteaza elementul din lista de cheltuieli de la indicele precizat
        cu cheltuiala transmisa
        :param index: indicele cheltuielii (integer)
        :param expense: cheltuiala (Expense)
        :return: False (daca operatia nu a reusit) / True (in caz contrar)
        """
        try:
            self.__expenses[index] = expense
            return True
        except:
            return False

    def getExpenseIndex(self, expense):
        """
        Functia returneaza indexul unei cheltuieli din lista de cheltuieli
        :param expense: cheltuiala (Expense)
        :return: id-ul cheltuielii / -1 (daca nu exista cheltuiala) (integer)
        """
        try:
            return self.__expenses.index(expense)
        except ValueError:
            return -1

    def getExpenseByIndex(self, index):
        """
        Functia returneaza cheltuiala de la indicele precizat din lista de cheltuieli
        :param index: indicele cheltuielii (integer)
        :return: indicele cheltuielii (integer) (daca exista) / None (daca nu exista cheltuiala)
        """
        try:
            return self.__expenses[index]
        except IndexError:
            return None

    def getAllExpenses(self):
        """
        Functia returneaza o copie dupa lista de cheltuieli
        :return: copia dupa lista de cheltuieli
        """
        return list(self.__expenses)

    def addCurrentToHistory(self):
        """
        Functia adauga lista de cheltuieli curenta in lista istoricului listelor de cheltuieli
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__expensesHistory.append(list(self.__expenses))
            return True
        except:
            return False

    def removeLastFromHistory(self):
        """
        Functia sterge ultimul element (daca e posibil) din lista istoricului listelor
        de cheltuieli si reinitializeaza lista de cheltuieli cu ultima valoare curenta
        :return: True (daca operatia s-a efectuat) /
                 False (daca operatia nu se mai poate efectua)
        """
        if len(self.__expensesHistory) > 0:
            self.__expensesHistory.pop(-1)
            if len(self.__expensesHistory) == 0:
                self.__expenses = list()
            else:
                self.__expenses = list(self.__expensesHistory[-1])
            return True

        return False
