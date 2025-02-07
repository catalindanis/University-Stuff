class TicTacToeGame:
    """
    Clasa unui joc TicTacToe
    """

    def __init__(self, nr, table):
        """
        Constructorul clasei TicTacToeGame
        :param nr: numarul jocului (Integer)
        :param table: tabela jocului (Matrice)
        """
        self.__nr = nr
        self.__table = table

    """
    Getteri si setteri ai clasei TicTacToeGame
    """
    def get_nr(self):
        return self.__nr

    def get_table(self):
        return self.__table

    def set_nr(self, nr):
        self.__nr = nr

    def set_table(self, table):
        self.__table = table

    def __eq__(self, other):
        """
        Functia folosita pentru verificarea egalitatii dintre 2 obiecte
        de tip TicTacToeGame
        :param other: obiectul cu care se compara obiectul curent (TicTacToeGame)
        :return:
        """
        return self.__nr == other.get_nr() and self.__table == other.get_table()

    def __str__(self):
        """
        Functia folosita la transformarea unui obiect de tip TicTacToeGame intr-un
        string
        :return: mesajul generat (String)
        """
        return (f"{self.__nr}, "
                f"{self.__table[0][0]}, "
                f"{self.__table[0][1]}, "
                f"{self.__table[0][2]}, "
                f"{self.__table[1][0]}, "
                f"{self.__table[1][1]}, "
                f"{self.__table[1][2]}, "
                f"{self.__table[2][0]}, "
                f"{self.__table[2][1]}, "
                f"{self.__table[2][2]}")

