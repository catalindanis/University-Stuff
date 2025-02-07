from UserInterface.Generals import clearScreen, getUserInput
from UserInterface.Management.Console import ManagementMenu
from UserInterface.Rent.Console import RentMenu
from UserInterface.Search.Console import SearchMenu


class MainMenu():

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul principal
        :return:
        """
        text = (""
                "~~~MovieRent~~~\n"
                "1.Gestionare\n"
                "2.Cautare\n"
                "3.Inchiriere/Returnare\n"
                "4.Iesire")

        print(text + (MainMenu.__extra_text if MainMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService, clientService, rentService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul principal
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """

        MainMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 4:
                raise ValueError

            if input == 4:
                return False
            elif input == 1:
                ManagementMenu.run(movieService, clientService)
            elif input == 2:
                SearchMenu.run(movieService, clientService)
            elif input == 3:
                RentMenu.run(rentService)
        except ValueError:
            MainMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def run(movieService, clientService, rentService):
        """
        Functia principala a clasei MainMenu care dirijeaza executia acestuia
        :param movieService: service-ul folosit pentru clasa Service (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            MainMenu.__print()
            input = getUserInput()
            if MainMenu.__handleInput(input, movieService, clientService, rentService) == False:
                stop_requested = True

        print("La revedere!")
