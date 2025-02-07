class RentRepository:
    """
    Clasa reprezinta repository-ul folosit pentru a face legatura intre obiectele
    de tip Movie si cele de tip Client
    """
    def __init__(self):
        """
        Constructorul clasei MovieClientRepository
        :return:
        """
        self.__client_movies = {}
        self.__movie_clients = {}

    def addRent(self, client_id, movie_id):
        """
        Functia atribuie un film unui client si unui client un film
        :param client_id: id-ul clientului (Integer)
        :param movie_id: id-ul filmului (Integer)
        :return: True (daca operatia s-a efectuat cu succes) / False (in caz contrar)
        """
        try:
            if client_id in self.__client_movies.keys():
                self.__client_movies[client_id].append(movie_id)
            else:
                self.__client_movies[client_id] = [movie_id]

            if movie_id in self.__movie_clients.keys():
                self.__movie_clients[movie_id].append(client_id)
            else:
                self.__movie_clients[movie_id] = [client_id]
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
            self.__client_movies[client_id].remove(movie_id)
            self.__movie_clients[movie_id].remove(client_id)
        except Exception:
            return False

        return True

    def getAllClientsMovies(self):
        """
        Functia returneaza filmele tuturor clientilor
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        return self.__client_movies

    def getAllMoviesClients(self):
        """
        Functia returneaza clientii tuturor filmelor
        :return: dictionarul cu cheia movie_id si valori lista de client_id
                (Dictionary)
        """
        return self.__movie_clients