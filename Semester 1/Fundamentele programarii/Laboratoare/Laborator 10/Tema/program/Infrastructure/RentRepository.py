from Domain.Client import ClientValidator
from Domain.Movie import MovieValidator


class RentRepository:
    """
    Clasa reprezinta repository-ul folosit pentru a face legatura intre obiectele
    de tip Movie si cele de tip Client
    """
    def __init__(self):
        """
        Constructorul clasei RentRepository
        :return:
        """
        self._client_movies = {}
        self._movie_clients = {}


    def addRent(self, client_id, movie_id):
        """
        Functia atribuie un film unui client si unui client un film
        :param client_id: id-ul clientului (Integer)
        :param movie_id: id-ul filmului (Integer)
        :return: True (daca operatia s-a efectuat cu succes) / False (in caz contrar)
        """
        try:
            client_id = int(client_id)
            movie_id = int(movie_id)
        except Exception:
            pass
        try:
            if client_id in self._client_movies.keys():
                self._client_movies[client_id].append(movie_id)
            else:
                self._client_movies[client_id] = [movie_id]

            if movie_id in self._movie_clients.keys():
                self._movie_clients[movie_id].append(client_id)
            else:
                self._movie_clients[movie_id] = [client_id]
        except Exception:
            return False

        return True

    def removeRent(self, client_id, movie_id):
        """
        Functia sterge filmul unui client si clientul un film
        :param client_id: id-ul clientului (Integer)
        :param movie_id: id-ul filmului (Integer)
        :return: True (daca operatia s-a efectuat cu succes) / False (in caz contrar)
        """
        try:
            self._client_movies[client_id].remove(movie_id)
            self._movie_clients[movie_id].remove(client_id)
        except Exception:
            return False

        return True

    def getAllClientsMovies(self):
        """
        Functia returneaza filmele tuturor clientilor
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        return self._client_movies

    def getAllMoviesClients(self):
        """
        Functia returneaza clientii tuturor filmelor
        :return: dictionarul cu cheia movie_id si valori lista de client_id
                (Dictionary)
        """
        return self._movie_clients

class FileRentRepository(RentRepository):
    """
    Clasa reprezinta repository-ul folosit pentru a face legatura intre obiectele
    de tip Movie si cele de tip Client care stocheaza datele in fisier
    """

    def __init__(self, file_path):
        """
        Constructorul clasei FileRentRepository
        :param file_path: calea fisierului in care se vor stoca dictionarele (String)
        :return:
        """
        super().__init__()
        self.__file_path = file_path
        self.__readFromFile()

    def __readFromFile(self):
        """
        Functia citeste din fisier dictionarul cu cheia client_id si valori lista de movie_id
        :return:
        """
        with open(self.__file_path, 'r') as file:
            self._client_movies = {}
            self._movie_clients = {}
            lines = file.readlines()
            for line in lines:
                line = line.split(":")
                client_id = line[0]
                movies_id_list = eval(line[1].strip())
                if ClientValidator.validateId(client_id):
                    for movie_id in movies_id_list:
                        if MovieValidator.validateId(movie_id):
                            super().addRent(client_id, movie_id)

    def __loadToFile(self):
        """
        Functia scrie in fisier dictionarul cu cheia client_id si valori lista de movie_id
        :return:
        """
        with open(self.__file_path, 'w') as file:
            for key, value in self._client_movies.items():
                file.write(f"{key}:{value}\n")

    def addRent(self, client_id, movie_id):
        """
        Functia atribuie un film unui client si unui client un film si scrie in fisier
        :param client_id: id-ul clientului (Integer)
        :param movie_id: id-ul filmului (Integer)
        :return: True (daca operatia s-a efectuat cu succes) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().addRent(client_id, movie_id)
            self.__loadToFile()
        except Exception:
            return False

        return True

    def removeRent(self, client_id, movie_id):
        """
        Functia sterge filmul unui client si clientul un film si scrie in fisier
        :param client_id: id-ul clientului (Integer)
        :param movie_id: id-ul filmului (Integer)
        :return: True (daca operatia s-a efectuat cu succes) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().removeRent(client_id, movie_id)
            self.__loadToFile()
        except Exception:
            return False

        return True

    def getAllClientsMovies(self):
        """
        Functia returneaza filmele tuturor clientilor citita din fisier
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        self.__readFromFile()
        return self._client_movies

    def getAllMoviesClients(self):
        """
        Functia returneaza clientii tuturor filmelor citita din fisier
        :return: dictionarul cu cheia movie_id si valori lista de client_id
                (Dictionary)
        """
        self.__readFromFile()
        return self._movie_clients

    def reload(self):
        """
        Functia reseteaza continutul din dictionare
        :return:
        """
        self.__loadToFile()
        self.__readFromFile()