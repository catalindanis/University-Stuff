class TicTacToeGameStatus:
    """
    Clasa de status al unui joc TicTacToe
    """

    def __init__(self, game):
        """
        Constructorul clasei TicTacToeGameStatus
        :param game: un joc de TicTacToe (TicTacToeGame)
        """
        self.__game = game
        game_table = game.get_table()
        self.__status = 0

        if game_table[0][0] == game_table[0][1] == game_table[0][2]:
            self.__status = 1
            self.__winner = game_table[0][0]
        if game_table[1][0] == game_table[1][1] == game_table[1][2]:
            self.__status = 1
            self.__winner = game_table[1][0]
        if game_table[2][0] == game_table[2][1] == game_table[2][2]:
            self.__status = 1
            self.__winner = game_table[2][0]
        if game_table[0][0] == game_table[1][0] == game_table[2][0]:
            self.__status = 1
            self.__winner = game_table[0][0]
        if game_table[0][1] == game_table[1][1] == game_table[2][1]:
            self.__status = 1
            self.__winner = game_table[0][1]
        if game_table[0][2] == game_table[1][2] == game_table[2][2]:
            self.__status = 1
            self.__winner = game_table[0][2]
        if game_table[0][0] == game_table[1][1] == game_table[2][2]:
            self.__status = 1
            self.__winner = game_table[0][0]
        if game_table[0][2] == game_table[1][1] == game_table[2][0]:
            self.__status = 1
            self.__winner = game_table[0][2]

    """
    Getteri si setteri ai clasei TicTacToeGameStatus
    """
    def get_status(self):
        return self.__status

    def get_winner(self):
        return self.__winner

    def get_game(self):
        return self.__game

    def set_status(self, status):
        self.__status = status

    def set_winner(self, winner):
        self.__winner = winner

    def set_game(self, game):
        self.__game = game