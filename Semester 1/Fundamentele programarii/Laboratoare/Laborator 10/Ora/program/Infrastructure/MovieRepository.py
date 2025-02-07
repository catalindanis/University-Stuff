from Domain.Movie import MovieValidator, Movie


class MovieRepository:
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Movie
    """
    def __init__(self):
        """
        Constructorul clasei MovieRepository
        :return:
        """
        self._movies_list = []

    def getMovies(self):
        """
        Functia returneaza lista de filme
        :return: lista de filme (List of Movie)
        """
        return self._movies_list

    def addMovie(self, movie):
        """
        Functia adauga un film in lista de filme
        :param movie: filmul (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._movies_list.append(movie)
            return True
        except Exception:
            return False

    def removeMovie(self, movie):
        """
        Functia sterge un film din lista de filme
        :param movie: filmul (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._movies_list.remove(movie)
            return True
        except Exception:
            return False

    def updateMovie(self, movie, new_movie):
        """
        Functia actualizeaza un film din lista de filme
        :param movie: filmul vechi (Movie)
        :param new_movie: filmul nou (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._movies_list[self._movies_list.index(movie)] = new_movie
            return True
        except Exception:
            return False

class FileMovieRepository(MovieRepository):
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Movie care stocheaza datele in fisier
    """

    def __init__(self, file_path):
        """
        Constructorul clasei FileMovieRepository
        :param file_path: calea fisierului in care se va stoca lista de clienti (String)
        :return:
        """
        super().__init__()
        self.__file_path = file_path
        self.__readFromFile()

    def __readFromFile(self):
        """
        Functia citeste din fisier lista de filme
        :return:
        """
        with open(self.__file_path, 'r') as file:
            self._movies_list.clear()
            lines = file.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    line = line.split(",")
                    if len(line) == 4:
                        movie_id = line[0].strip()
                        movie_title = line[1].strip()
                        movie_description = line[2].strip()
                        movie_type = line[3].strip()
                        if MovieValidator.validateMovie(movie_id, movie_title, movie_description, movie_type):
                            self._movies_list.append(Movie(movie_id, movie_title, movie_description, movie_type))

    def __loadToFile(self):
        """
        Functia scrie in fisier lista de filme
        :return:
        """
        with open(self.__file_path, 'w') as file:
            for movie in self._movies_list:
                file.write(f"{movie.getId()},{movie.getTitle()},{movie.getDescription()},{movie.getType().value}\n")

    def getMovies(self):
        """
        Functia returneaza lista de filme citita din fisier
        :return: lista de filme (List of Movie)
        """
        self.__readFromFile()
        return self._movies_list

    def addMovie(self, movie):
        """
        Functia adauga un film in lista de filme si scrie in fisier lista de filme
        :param movie: filmul (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().addMovie(movie)
            self.__loadToFile()
            return True
        except Exception:
            return False

    def removeMovie(self, movie):
        """
        Functia sterge un film din lista de filme si scrie in fisier lista de filme
        :param movie: filmul (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().removeMovie(movie)
            self.__loadToFile()
            return True
        except Exception:
            return False

    def updateMovie(self, movie, new_movie):
        """
        Functia actualizeaza un film din lista de filme si scrie in fisier lista de filme
        :param movie: filmul vechi (Movie)
        :param new_movie: filmul nou (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().updateMovie(movie, new_movie)
            self.__loadToFile()
            return True
        except Exception:
            return False