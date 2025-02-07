from Domain.Movie import MovieValidator, Movie, MovieType


class MovieService:
    """
    Clasa reprezinta service-ul folosit pentru obiectele de tip Movie
    """
    def __init__(self, repository):
        """
        Constructorul clasei MovieService
        :param repository: repository-ul folosit pentru clasa Movie (MovieRepository)
        """
        self.__repository = repository

    def addMovie(self, title, description, type):
        """
        Functia realizeaza operatia de adaugare a unui film in lista de filme prin repository
        :param title: titlul filmului (String)
        :param description: descrierea filmului (String)
        :param type: tipul filmului (String)
        :return: -1 (datele nu sunt valide) / 0 (filmul exista deja) / 1 (operatia s-a efectuat cu succes)
        """
        id = self.__generateId()

        if MovieValidator.validateMovie(id, title, description, type) == False:
            return -1

        movie = Movie(id, title, description, MovieType(int(type)))

        if self.alreadyExists(movie):
            return 0

        self.__repository.addMovie(movie)
        return 1


    def deleteMovieById(self, id):
        """
        Functia sterge un film prin repository dupa id-ul pe care il are
        :param id: id-ul filmului
        :return: -1 (daca id-ul este invalid) / 0 (daca nu exista un film cu acest id) /
                1 (daca operatia s-a efectuat cu succes)
        """
        try:
            id = int(id)
            if id < 0:
                return -1
        except ValueError:
            return -1
        for movie in self.__repository.getMovies():
            if movie.getId() == id:
                self.__repository.removeMovie(movie)
                self.__updateMoviesId()
                return 1

        return 0

    def updateMovieById(self, id, title, description, type):
        """
        Functia actualizeaza un film prin repository dupa id-ul pe care il are
        :param id: id-ul filmului
        :param title: titlul filmului (String)
        :param description: descrierea filmului (String)
        :param type: tipul filmului (String)
        :return: -3 (daca exista deja acest film)
                -2 (daca datele nu sunt valide) / -1 (daca id-ul este invalid)
                0 (daca nu exista un film cu acest id) / 1 (daca operatia s-a efectuat cu succes)
        """
        try:
            id = int(id)
            if id < 0:
                return -1
        except ValueError:
            return -1

        if MovieValidator.validateMovie(id, title, description, type) == False:
            return -2

        for movie in self.__repository.getMovies():
            if (movie.getTitle() == title
                    and movie.getDescription() == description
                    and movie.getType() == MovieType(int(type))
                    and movie.getId() != id):
                return -3

        new_movie = Movie(id, title, description, MovieType(int(type)))
        for movie in self.__repository.getMovies():
            if movie.getId() == id:
                self.__repository.updateMovie(movie, new_movie)
                return 1

        return 0

    def alreadyExists(self, movie):
        """
        Functia verifica daca un film exista deja in lista de filme
        :param movie: filmul cautat
        :return: True (daca exista deja) / False (in caz contrar)
        """
        for current_movie in self.__repository.getMovies():
            if current_movie.getTitle() == movie.getTitle()\
                    and current_movie.getDescription() == movie.getDescription()\
                    and current_movie.getType() == movie.getType():
                return True

        return False

    def __updateMoviesId(self):
        """
        Functia actualizeaza id-urile din lista de filme in urma unei modificari in lista
        :return:
        """
        current_id = 0
        for movie in self.__repository.getMovies():
            movie.setId(current_id)
            current_id += 1

    def __generateId(self):
        """
        Functia genereaza un id pe baza lungimii listei de filme
        :return: id-ul (Integer)
        """
        return len(self.__repository.getMovies())

    def getAllMovies(self):
        """
        Functia returneaza lista de filme
        :return: lista de filme (List of Movie)
        """
        return self.__repository.getMovies()

    def formatMoviesList(self, moviesList):
        """
        Functia formateaza lista de filme intr-un mesaj pentru afisare
        :param moviesList: lista de filme (List of Movie)
        :return: mesajul formatat (String)
        """
        message = ""

        for i in range(len(moviesList)):
            message += str(moviesList[i])
            if i != len(moviesList) - 1:
                message += "\n"

        return message