from UserInterface.Generals import clearScreen, getUserInput
from UserInterface.Reports.Client.Console import ClientMenu
from UserInterface.Reports.Movie.Console import MovieMenu


class ReportsMenu:

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul de rapoarte
        :return:
        """
        text = (""
                "~~~Rapoarte~~~\n"
                "1.Filme\n"
                "2.Clienti\n"
                "3.Inapoi")

        print(text + (ReportsMenu.__extra_text if ReportsMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, rentService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul rapoarte
        :param input: input-ul introdus de utilizator
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """

        ReportsMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 and input > 3:
                raise ValueError

            if input == 3:
                return False
            elif input == 1:
                MovieMenu.run(rentService)
            elif input == 2:
                ClientMenu.run(rentService)

        except ValueError:
            ReportsMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def run(rentService):
        """
        Functia principala a clasei ReportsMenu care dirijeaza executia acestuia
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            ReportsMenu.__print()
            input = getUserInput()
            if ReportsMenu.__handleInput(input, rentService) == False:
                stop_requested = True
