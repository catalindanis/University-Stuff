from Domain.Movie import Movie
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
                "~~~Gestiune lista filme~~~\n"
                "1.Vizualizare lista filme\n"
                "2.Adaugare film\n"
                "3.Stergere film\n"
                "4.Actualizare film\n"
                "5.Generare filme random\n"
                "6.Inapoi")

        print(text + (MovieMenu.__extra_text if MovieMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, movieService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul film
        :param input: input-ul introdus de utilizator
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        options = [MovieMenu.__showMovies,
                   MovieMenu.__addMovie,
                   MovieMenu.__deleteMovie,
                   MovieMenu.__updateMovie,
                   MovieMenu.__generateRandomMovies]

        MovieMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 6:
                raise ValueError

            if input == 6:
                return False

            options[input - 1](movieService)
        except ValueError as e:
            #MovieMenu.__extra_text = "\nOptiune invalida!"
            MovieMenu.__extra_text = str(e)

    @staticmethod
    def __showMovies(movieService):
        """
        Functia preia din service mesajul cu lista de filme si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        movies_formatted_list = movieService.formatMoviesList(movieService.getAllMovies())

        if movies_formatted_list == "":
            MovieMenu.__extra_text = "\nLista de filme este goala!"
        else:
            MovieMenu.__extra_text = "\n" + movies_formatted_list

    @staticmethod
    def __addMovie(movieService):
        """
        Functia apeleaza adaugarea unui film prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()
        title = getUserInput("Introduceti titlul filmului: ")
        description = getUserInput("Introduceti descrierea filmului: ")
        type = getUserInput("Introduceti tipul filmului: \n1.Comedie\n2.Horror\n3.Drama\n4.Actiune\n")

        result = movieService.addMovie(title, description, type)
        if result == -1:
            MovieMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == 0:
            MovieMenu.__extra_text = "\nFilmul exista deja!"
        else:
            MovieMenu.__extra_text = "\nFilm adaugat cu succes!"

    @staticmethod
    def __deleteMovie(movieService):
        """
        Functia apeleaza stergerea unui film prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()
        id = getUserInput("Introduceti id-ul filmului: ")

        result = movieService.deleteMovieById(id)
        if result == -1:
            MovieMenu.__extra_text = "\nId-ul este invalid!"
        elif result == 0:
            MovieMenu.__extra_text = "\nFilmul cu acest id nu exista!"
        else:
            MovieMenu.__extra_text = "\nFilmul a fost sters cu succes!"

    @staticmethod
    def __updateMovie(movieService):
        """
        Functia apeleaza actualizarea unui film prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()
        id = getUserInput("Introduceti id-ul filmului: ")
        title = getUserInput("Introduceti noul titlu al filmului: ")
        description = getUserInput("Introduceti noua descrierea a filmului: ")
        type = getUserInput("Introduceti noul tip al filmului: \n1.Comedie\n2.Horror\n3.Drama\n4.Actiune\n")

        result = movieService.updateMovieById(id, title, description, type)
        if result == -3:
            MovieMenu.__extra_text = "\nExista deja acest film!"
        elif result == -2:
            MovieMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == -1:
            MovieMenu.__extra_text = "\nId-ul este invalid!"
        elif result == 0:
            MovieMenu.__extra_text = "\nFilmul cu acest id nu exista!"
        else:
            MovieMenu.__extra_text = "\nFilmul a fost actualizat cu succes!"

    @staticmethod
    def __generateRandomMovies(movieService):
        """
        Functia apeleaza generarea unor filme random prin service si afiseaza mesajele corespunzatoare
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :return:
        """
        clearScreen()

        numberOfMovies = getUserInput("Introduceti numarul de filme: ")

        try:
            numberOfMovies = int(numberOfMovies)
        except ValueError:
            MovieMenu.__extra_text = "\nDatele introduse sunt invalide!"
            return

        result = movieService.generateRandomMovies(numberOfMovies)

        if result == True:
            MovieMenu.__extra_text = "\nFilmele au fost generate cu succes!"
        else:
            MovieMenu.__extra_text = "\nA aparut o eroare la generarea filmelor!"

    @staticmethod
    def run(movieService):
        """
        Functia principala a meniului gestionare lista filme care dirijeaza executia acestuia
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