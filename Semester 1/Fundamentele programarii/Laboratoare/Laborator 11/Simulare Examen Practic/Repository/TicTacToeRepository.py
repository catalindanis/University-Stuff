from Domain.TicTacToeGame import TicTacToeGame


class TicTacToeRepository:
    """
    Clasa repository pentru jocurile TicTacToe
    """

    def __init__(self, file_path):
        """
        Constructorul clasei TicTacToeRepository
        """
        self.__file_path = file_path
        self.__games = []
        self.__read_from_file()

    def __read_from_file(self):
        """
        Functia realizeaza citirea datelor din fisier si actualizarea listei
        de jocuri existente
        :return:
        """
        self.__games = []
        with open(self.__file_path, 'r') as file:
            lines = file.readlines()
            for line in lines:
                line = line.split(",")
                nr = int(line[0])
                table = [[0, 0, 0],[0, 0, 0],[0, 0, 0]]
                table[0][0] = line[1].strip()
                table[0][1] = line[2].strip()
                table[0][2] = line[3].strip()
                table[1][0] = line[4].strip()
                table[1][1] = line[5].strip()
                table[1][2] = line[6].strip()
                table[2][0] = line[7].strip()
                table[2][1] = line[8].strip()
                table[2][2] = line[9].strip()
                game = TicTacToeGame(nr, table)
                self.__games.append(game)

    def __write_to_file(self):
        """
        Functia realizeaza scrierea datelor in fisier
        :return:
        """
        with open(self.__file_path, 'w') as file:
            for game in self.__games:
                file.write(str(game) + "\n")

    def find_by_nr(self, nr):
        """
        Functia realizeaza cautarea unui joc dupa numar si returneaza acest joc
        :param nr: numarul jocului (Integer)
        :return: jocul cautat (TicTacToeGame)
        """
        self.__read_from_file()
        for game in self.__games:
            if game.get_nr() == nr:
                return game

    def update(self, nr, new_game):
        """
        Functia realizeaza actualizarea unui joc cu un alt joc folosind numarul acestuia
        :param nr: numarul jocului (Integer)
        :param new_game: celalalt joc (TicTacToeGame)
        :return:
        """
        self.__read_from_file()
        self.find_by_nr(nr).set_table(new_game.get_table())
        self.__write_to_file()

    def get_all(self):
        """
        Functia returneaza toate jocurile existente
        :return: lista de jocuri (List of TicTacToeGame)
        """
        self.__read_from_file()
        return self.__games