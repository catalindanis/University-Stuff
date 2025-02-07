class TicTacToeUI:
    """
    Clasa de UI a aplicatiei
    """
    def __init__(self, service, repository):
        """
        Constructorul clasei UI
        :param service: Service-ul folosit in aplicatie (TicTacToeService)
        :param repository: Repository-ul folosit in aplicatie (TicTacToeRepository)
        """
        self.__service = service
        self.__repository = repository
        self.__operation_result = ""

    def print(self):
        """
        Functia afiseaza mesajele ce alcatuiesc meniul aplicatiei
        :return:
        """
        menu = ("TicTacToe Manager\n"
                "1.Afiseaza jocurile neterminate\n"
                "2.Marcheaza o casuta\n"
                "3.Iesire")

        print(menu + "" if self.__operation_result == "" else self.__operation_result)

    def handle_input(self, user_input):
        """
        Functia prelucreaza datele introduse de utilizator de la tastatura
        si realizeaza actiunile necesare
        :param user_input: input-ul introdus de utilizator (String)
        :return: False (daca utilizatorul doreste iesirea din aplicatie) /
                True (daca aplicatia trebuie sa continue rularea)
        """
        self.__operation_result = ""
        action = [self.display_unfinished_games,
                  self.make_a_move]

        try:
            user_input = int(user_input)

            if user_input > 3 or user_input < 1:
                raise ValueError

            if user_input == 3:
                return False

            action[user_input-1]()
        except ValueError:
            self.__operation_result = "Optiune invalida!"

        return True

    def display_unfinished_games(self):
        """
        Functia foloseste service-ul pentru a afisa jocurile neterminate si
        afiseaza mesajele corespunzatoare
        :return:
        """
        unfinished_games = self.__service.search_unfinished_games()

        for game in unfinished_games:
            self.__operation_result += str(game) + "\n"

    def make_a_move(self):
        """
        Functia foloseste service-ul pentru a marca o casuta si afiseaza
        mesajele corespunzatoare
        :return:
        """
        try:
            number = int(input("Numarul jocului: "))
            line = int(input("Linia jocului: ")) - 1
            column = int(input("Coloana jocului: ")) - 1
        except ValueError:
            self.__operation_result = "Optiune invalida!"

        game_status = self.__service.play(number, line, column)

        if game_status.get_status() == 1:
            self.__operation_result = ("Jocul s-a terminat!\n"
                                       + game_status.get_winner() + " a castigat!\n")
        else:
            self.__operation_result = "Jocul inca nu s-a terminat!\n"

    def run(self):
        """
        Functia ce dirijeaza executia clasei TicTacToeUI
        :return:
        """
        exit_requested = False
        while not exit_requested:
            self.print()
            user_input = input("> ")
            if not self.handle_input(user_input):
                exit_requested = True
        print("La revedere!")
