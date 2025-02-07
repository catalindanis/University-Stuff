class MovieRepository:
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Movie
    """
    def __init__(self):
        """
        Constructorul clasei MovieRepository
        :return:
        """
        self.__movies_list = []

    def getMovies(self):
        """
        Functia returneaza lista de filme
        :return: lista de filme (List of Movie)
        """
        return self.__movies_list

    def addMovie(self, movie):
        """
        Functia adauga un film in lista de filme
        :param movie: filmul (Movie)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__movies_list.append(movie)
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
            self.__movies_list.remove(movie)
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
            self.__movies_list[self.__movies_list.index(movie)] = new_movie
            return True
        except Exception:
            return False