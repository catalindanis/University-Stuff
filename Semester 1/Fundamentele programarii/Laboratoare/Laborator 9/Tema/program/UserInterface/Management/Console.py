from UserInterface.Management.Client.Console import ClientMenu
from UserInterface.Generals import clearScreen, getUserInput
from UserInterface.Management.Movie.Console import MovieMenu


class ManagementMenu():

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul de gestionare
        :return:
        """
        text = (""
                "~~~Gestionare~~~\n"
                "1.Filme\n"
                "2.Clienti\n"
                "3.Inapoi")

        print(text + (ManagementMenu.__extra_text if ManagementMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService, clientService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul de gestionare
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """

        ManagementMenu.__extra_text = ""

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
            ManagementMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def run(movieService, clientService):
        """
        Functia principala a clasei ManagementMenu care dirijeaza executia acestuia
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            ManagementMenu.__print()
            input = getUserInput()
            if ManagementMenu.__handleInput(input, movieService, clientService) == False:
                stop_requested = True
