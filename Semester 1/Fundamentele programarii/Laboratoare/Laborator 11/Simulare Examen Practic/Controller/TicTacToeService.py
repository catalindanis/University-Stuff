from Domain.TicTacToeGameStatus import TicTacToeGameStatus


class TicTacToeService:
    """
    Clasa service folosita pentru jocurile TicTacToe
    """
    def __init__(self, repository):
        """
        Constructorul clasei TicTacToeService
        :param repository: repository-ul folosit de service (TicTacToeRepository)
        """
        self.__repository = repository

    def search_unfinished_games(self):
        """
        Functia realizeaza cu ajutorul repository-ului operatia de cautare a
        jocurilor neterminate
        :return: lista jocurilor neterminate (List of TicTacToeGame)
        """
        unfinished_games = []
        for game in self.__repository.get_all():
            game_status = TicTacToeGameStatus(game)
            if game_status.get_status() == 0:
                unfinished_games.append(game)
        return unfinished_games

    def play(self, nr, line, column):
        """
        Functia realizeaza cu ajutorul repository-ului operatia de marcare a unei casute
        :param nr: numarul jocului (Integer)
        :param line: linia casutei (Integer)
        :param column: coloana casutei (Integer)
        :return: statusul jocului (TicTacToeGameStatus)
        """
        game = self.__repository.find_by_nr(nr)
        game_table = game.get_table()
        numbers_of_X = 0
        numbers_of_0 = 0
        for i in range(0, 3):
            for j in range(0, 3):
                if game_table[i][j] == 'X':
                    numbers_of_X += 1
                else:
                    numbers_of_0 += 1
        if numbers_of_X <= numbers_of_0:
            game_table[line][column] = 'X'
            self.__repository.update(nr, game)
        else:
            game_table[line][column] = 'O'
            self.__repository.update(nr, game)

        return TicTacToeGameStatus(game)