from UserInterface.Generals import clearScreen, getUserInput
from UserInterface.Search.Client.Console import ClientMenu
from UserInterface.Search.Movie.Console import MovieMenu

class SearchMenu():

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul cautare
        :return:
        """
        text = (""
                "~~~Cautare~~~\n"
                "1.Filme\n"
                "2.Clienti\n"
                "3.Inapoi")

        print(text + (SearchMenu.__extra_text if SearchMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService, clientService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul cautare
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """

        SearchMenu.__extra_text = ""

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
            SearchMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def run(movieService, clientService):
        """
        Functia principala a clasei SearchMenu care dirijeaza executia acestuia
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            SearchMenu.__print()
            input = getUserInput()
            if SearchMenu.__handleInput(input, movieService, clientService) == False:
                stop_requested = True
