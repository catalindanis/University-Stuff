from UserInterface.Generals import clearScreen, getUserInput


class MovieMenu:
    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul film
        :return:
        """
        text = (""
                "~~~Rapoarte pentru filme~~~\n"
                "1.Cele mai inchiriate filme\n"
                "2.Inapoi")

        print(text + (MovieMenu.__extra_text if MovieMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, rentService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul film
        :param input: input-ul introdus de utilizator
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """

        MovieMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 2:
                raise ValueError

            if input == 2:
                return False

            MovieMenu.__topRentedMovies(rentService)
        except ValueError as e:
            MovieMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def __topRentedMovies(rentService):
        """
        Functia preia din service mesajul cu lista de filme ordonate dupa numarul de inchirieri
        si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        sortedDict = rentService.getTopRentedMovies()

        movies_formatted_list = rentService.formatMoviesDict(sortedDict)

        if movies_formatted_list == "":
            MovieMenu.__extra_text = "\nLista de inchirieri este goala!"
        else:
            MovieMenu.__extra_text = "\n" + movies_formatted_list

    @staticmethod
    def run(rentService):
        """
        Functia principala a meniului rapoarte lista filme care dirijeaza executia acestuia
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            MovieMenu.__print()
            input = getUserInput()
            if MovieMenu.__handleInput(input, rentService) == False:
                stop_requested = True