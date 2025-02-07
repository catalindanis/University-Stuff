from UserInterface.Client.Console import ClientMenu
from UserInterface.Generals import clearScreen, getUserInput
from UserInterface.Movie.Console import MovieMenu


class MainMenu():

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul principal
        :return:
        """
        text = (""
                "~~~Selectati optiunea pentru gestiune~~~\n"
                "1.Filme\n"
                "2.Clienti\n"
                "3.Iesire")

        print(text + (MainMenu.__extra_text if MainMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService, clientService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul principal
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """

        MainMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 3:
                raise ValueError

            if input == 3:
                return False
            elif input == 1:
                MovieMenu.run(movieService)
            else:
                ClientMenu.run(clientService)

        except ValueError:
            MainMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def run(movieService, clientService):
        """
        Functia principala a clasei MainMenu care dirijeaza executia acestuia
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            MainMenu.__print()
            input = getUserInput()
            if MainMenu.__handleInput(input, movieService, clientService) == False:
                stop_requested = True

        print("La revedere!")
