import random
import string

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
        self.__current_id = 0

    def addMovie(self, title, description, type):
        """
        Functia realizeaza operatia de adaugare a unui film in lista de filme prin repository
        :param title: titlul filmului (String)
        :param description: descrierea filmului (String)
        :param type: tipul filmului (String)
        :return: -1 (datele nu sunt valide) / 0 (filmul exista deja) / 1 (operatia s-a efectuat cu succes)
        """
        id = self.__current_id

        if MovieValidator.validateMovie(id, title, description, type) == False:
            return -1

        movie = Movie(id, title, description, MovieType(int(type)))

        if self.alreadyExists(movie):
            return 0

        self.__repository.addMovie(movie)
        self.__current_id += 1
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

    def searchMovieByTitle(self, title):
        """
        Functia returneaza lista de filme care contin in titlu titlul transmis ca parametru
        :param title: titlul filmului (String)
        :return: lista de filme (List of Movie) / [] (daca nu exista astfel de filme / None (datele introduse sunt incorecte)
        """
        if MovieValidator.validateTitle(title) == False:
            return None

        result = []

        for movie in self.__repository.getMovies():
            movie_title = movie.getTitle().split()

            if title in movie_title:
                result.append(movie)

        return result

    def searchMovieByType(self, type):
        """
        Functia returneaza lista de filme de genul transmis ca parametru
        :param type: genul filmului (String)
        :return: lista de filme (List of Movie) / [] (daca nu exista astfel de filme / None (datele introduse sunt incorecte)
        """
        if MovieValidator.validateType(type) == False:
            return None

        result = []

        for movie in self.__repository.getMovies():
            if movie.getType() == MovieType(int(type)):
                result.append(movie)

        return result

    def generateRandomMovies(self, numberOfMovies):
        """
        Functia genereaza filme si le adauga prin repository in lista de filme
        :return: True (daca operatia s-a efectuat cu succes) / False (daca a aparut o eroare)
        """
        try:
            for i in range(numberOfMovies):
                movieName = ''.join(random.choices(string.ascii_letters,
                                                   k=random.randint(8, 20)))
                movieDescription = ''.join(random.choices(string.ascii_letters,
                                                   k=random.randint(15, 40)))
                movieType = str(random.randint(1, 4))

                if self.addMovie(movieName, movieDescription, movieType) == 1:
                    i -= 1

            return True
        except Exception:
            return False


    def idExists(self, id):
        """
        Functia returneaza daca exista un film cu un anumit id
        :param id: id-ul filmului (String)
        :return: -1 (id-ul este invalid) / 0 (nu exista un film cu acest id) /
                  1 (exista un film cu acest id)
        """
        try:
            id = int(id)
            for movie in self.__repository.getMovies():
                if movie.getId() == id:
                    return 1
            return 0
        except Exception:
            return -1

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