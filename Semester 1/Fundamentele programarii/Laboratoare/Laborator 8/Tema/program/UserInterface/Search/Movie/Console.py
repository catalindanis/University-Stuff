from UserInterface.Generals import getUserInput, clearScreen


class MovieMenu:
    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul cautare film
        :return:
        """
        text = ("~~~Cautare filme~~~\n"
                "1.Dupa titlu\n"
                "2.Dupa gen\n"
                "3.Inapoi")

        print(text + (MovieMenu.__extra_text if MovieMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul cautare film
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        options = [MovieMenu.searchMovieByTitle,
                   MovieMenu.searchMovieByType]

        MovieMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 5:
                raise ValueError

            if input == 3:
                return False

            options[input - 1](movieService)
        except ValueError:
            MovieMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def searchMovieByTitle(movieService):
        """
        Functia apeleaza cautarea unui film dupa titlu prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()
        title = getUserInput("Introduceti titlul filmului: ")

        result = movieService.searchMovieByTitle(title)

        if result == None:
            MovieMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == []:
            MovieMenu.__extra_text = "\nNu exista astfel de filme!"
        else:
            movies_formatted_list = movieService.formatMoviesList(result)
            MovieMenu.__extra_text = "\n" + movies_formatted_list

    @staticmethod
    def searchMovieByType(movieService):
        """
        Functia apeleaza cautarea unui film dupa gen prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()
        type = getUserInput("Introduceti tipul filmului: \n1.Comedie\n2.Horror\n3.Drama\n4.Actiune\n")

        result = movieService.searchMovieByType(type)

        if result == None:
            MovieMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == []:
            MovieMenu.__extra_text = "\nNu exista astfel de filme!"
        else:
            movies_formatted_list = movieService.formatMoviesList(result)
            MovieMenu.__extra_text = "\n" + movies_formatted_list

    @staticmethod
    def run(movieService):
        """
        Functia principala a meniului cautare film care dirijeaza executia acestuia
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            MovieMenu.__print()
            input = getUserInput()
            if MovieMenu.__handleInput(input, movieService) == False:
                stop_requested = True