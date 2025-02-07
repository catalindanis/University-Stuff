from Utils.Generals import *

class Expense:
    """
    Clasa defineste o cheltuiala
    """
    def __init__(self, day, sum, type):
        """
        Constructorul clasei cheltuiala
        :param day: ziua (integer)
        :param sum: suma (float)
        :param type: tipul (ExpenseType)
        """
        self.day = day
        self.sum = convertToIntIfPossible(sum)
        self.type = type

    """
    Functii de tip get care returneaza atributele unui obiect de tip cheltuiala
    """
    def getDay(self):
        return self.day

    def getSum(self):
        return self.sum

    def getType(self):
        return self.type

    def __str__(self):
        """
        Suprascrierea functiei folosita la tiparirea obiectului
        """
        return (f"Cheltuiala din ziua {self.day}, "
                f"cu suma {self.sum} si "
                f"tipul {self.type.name.lower()}")

    def __eq__(self, other):
        """
        Suprascrierea functiei folosita la compararea egalitatii cu un alt obiect
        de tipul cheltuiala
        """
        return self.day == other.day and self.sum == other.sum  and self.type == other.type