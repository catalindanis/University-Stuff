from UserInterface.Generals import clearScreen, getUserInput


class RentMenu:

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul de inchiriere
        :return:
        """
        text = (""
                "~~~Inchiriere / Returnare~~~\n"
                "1.Inchiriere\n"
                "2.Returnare\n"
                "3.Iesire")

        print(text + (RentMenu.__extra_text if RentMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, rentService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul principal
        :param input: input-ul introdus de utilizator
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """

        RentMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 and input > 3:
                raise ValueError

            if input == 3:
                return False
            elif input == 1:
                RentMenu.__rentMovie(rentService)
            elif input == 2:
                RentMenu.__unrentMovie(rentService)

        except ValueError:
            RentMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def __rentMovie(rentService):
        """
        Functia apeleaza inchirierea unui film prin service si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        clearScreen()
        client_id = getUserInput("Introduceti id-ul clientului: ")
        movie_id = getUserInput("Introduceti id-ul filmului: ")

        result = rentService.rentMovie(movie_id, client_id)

        if result == -2:
            RentMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == -1:
            RentMenu.__extra_text = "\nNu exista acest film!"
        elif result == 0:
            RentMenu.__extra_text = "\nNu exista acest client!"
        elif result == 2:
            RentMenu.__extra_text = "\nClientul acesta a inchiriat deja acest film!"
        else:
            RentMenu.__extra_text = "\nFilmul a fost inchiriat cu succes!"


    @staticmethod
    def __unrentMovie(rentService):
        """
        Functia apeleaza returnarea unui film prin service si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        clearScreen()
        client_id = getUserInput("Introduceti id-ul clientului: ")
        movie_id = getUserInput("Introduceti id-ul filmului: ")

        result = rentService.unrentMovie(movie_id, client_id)

        if result == -2:
            RentMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == -1:
            RentMenu.__extra_text = "\nNu exista acest film!"
        elif result == 0:
            RentMenu.__extra_text = "\nNu exista acest client!"
        elif result == 2:
            RentMenu.__extra_text = "\nClientul acesta nu a inchiriat acest film!"
        else:
            RentMenu.__extra_text = "\nFilmul a fost returnat cu succes!"

    @staticmethod
    def run(rentService):
        """
        Functia principala a clasei RentMenu care dirijeaza executia acestuia
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            RentMenu.__print()
            input = getUserInput()
            if RentMenu.__handleInput(input, rentService) == False:
                stop_requested = True
