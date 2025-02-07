from Domain.Client import ClientValidator
from Domain.Movie import MovieValidator
from Generals.Sorting import bubbleSort, shellSort
from Infrastructure.RentRepository import FileRentRepository


class RentService:
    """
    Clasa reprezinta service-ul folosit pentru inchirieri
    """

    def __init__(self, rentRepository, movieService, clientService):
        """
        Constructorul clasei RentService
        :param rentRepository: reprezinta repository-ul folosit pentru inchirieri (RentRepository)
        :param movieService: service-ul folosit pentru clasa Movie (MovieService)
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        """
        self.__repository = rentRepository
        self.__movieService = movieService
        self.__clientService = clientService

    def rentMovie(self, movie_id, client_id):
        """
        Functia realizeaza operatia de inchiriere a unui film prin repository
        :param movie_id: id-ul filmului (String)
        :param client_id: id-ul filmului (String)
        :return: -2 (datele sunt invalide) / -1 (nu exista filmul) / 0 (nu exista clientul) /
                1 (operatia s-a realizat cu succes) / 2 (clientul a inchiriat deja filmul)
        """
        if (MovieValidator.validateId(movie_id) == False or
            ClientValidator.validateId(client_id) == False):
                return -2

        if self.__movieService.idExists(movie_id) == 0:
            return -1

        if self.__clientService.idExists(client_id) == 0:
            return 0

        if self.__rentExists(movie_id, client_id):
            return 2

        self.__repository.addRent(client_id, movie_id)
        return 1


    def unrentMovie(self, movie_id, client_id):
        """
        Functia realizeaza operatia de returnare a unui film prin repository
        :param movie_id: id-ul filmului (String)
        :param client_id: id-ul clientului (String)
        :return: -2 (datele sunt invalide) / -1 (nu exista filmul) / 0 (nu exista clientul) /
                1 (operatia s-a realizat cu succes) / 2 (clientul nu a inchiriat filmul)
        """
        if (MovieValidator.validateId(movie_id) == False or
                ClientValidator.validateId(client_id) == False):
            return -2

        if self.__movieService.idExists(movie_id) == 0:
            return -1

        if self.__clientService.idExists(client_id) == 0:
            return 0

        if not self.__rentExists(movie_id, client_id):
            return 2

        self.__repository.removeRent(int(client_id), int(movie_id))
        return 1

    def getTopRentedMovies(self):
        """
        Functia returneaza dictionarul de inchirieri care contine doar filmele cu numar maxim
        de inchirieri
        :return: dictionarul cu cheia movie_id si valori lista de client_id
                (Dictionary)
        """
        initial = self.__repository.getAllMoviesClients().items()
        new_dict = {}

        deleted_movies_id = []
        deleted_clients_id = []

        for item in initial:
            if (self.__movieService.idExists(item[0])):
                for id in item[1]:
                    if (self.__clientService.idExists(id)):
                        if item[0] not in new_dict:
                            new_dict[item[0]] = [id]
                        else:
                            new_dict[item[0]].append(id)
                    elif not id in deleted_clients_id:
                        deleted_clients_id.append(id)
            elif not item[0] in deleted_movies_id:
                deleted_movies_id.append(item[0])

        self.__clearDeletedMovies(deleted_movies_id)
        self.__clearDeletedClients(deleted_clients_id)

        sortedDict = dict(shellSort(new_dict.items(),
                                 key=lambda item: len(item[1]),
                                 reverse=True))

        if len(sortedDict) > 0:
            maxValue = len(sortedDict[list(sortedDict.keys())[0]])

        return dict(filter(lambda item: len(item[1]) == maxValue, sortedDict.items()))

    def sortClientsByName(self):
        """
        Functia returneaza dictionarul de inchirieri ordonat dupa nume
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        initial = self.__repository.getAllClientsMovies().items()
        new_dict = {}

        deleted_movies_id = []
        deleted_clients_id = []

        for item in initial:
            if (self.__clientService.idExists(item[0])):
                for id in item[1]:
                    if (self.__movieService.idExists(id)):
                        if item[0] not in new_dict:
                            new_dict[item[0]] = [id]
                        else:
                            new_dict[item[0]].append(id)
                    else:
                        deleted_movies_id.append(id)
            else:
                deleted_clients_id.append(item[0])

        sortedDict = dict(bubbleSort(new_dict.items(),
                                 key=lambda item: self.__clientService.getClientByIdRecursive(len(self.__clientService.getAllClients()),
                                                                                              self.__clientService.getAllClients(),
                                                                                              int(item[0]))
                                                    .getName().lower()))

        self.__clearDeletedMovies(deleted_movies_id)
        self.__clearDeletedClients(deleted_clients_id)

        return sortedDict

    def sortClientsByNumberOfMoviesRented(self):
        """
        Functia returneaza dictionarul de inchirieri ordonat dupa numarul de filme inchiriate
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        initial = self.__repository.getAllClientsMovies().items()
        new_dict = {}

        deleted_movies_id = []
        deleted_clients_id = []

        for item in initial:
            if (self.__clientService.idExists(item[0])):
                for id in item[1]:
                    if (self.__movieService.idExists(id)):
                        if item[0] not in new_dict:
                            new_dict[item[0]] = [id]
                        else:
                            new_dict[item[0]].append(id)
                    else:
                        deleted_movies_id.append(id)
            else:
                deleted_clients_id.append(item[0])

        self.__clearDeletedMovies(deleted_movies_id)
        self.__clearDeletedClients(deleted_clients_id)

        sortedDict = dict(shellSort(new_dict.items(),
                                 key=lambda item: len(item[1]),
                                 reverse=True))

        return sortedDict

    def top30PercentClients(self):
        """
        Functia returneaza dictionarul de inchirieri ce contine primii 30% clienti dupa
        numarul de filme inchiriate
        :return: dictionarul cu cheia client_id si valori lista de movie_id
                (Dictionary)
        """
        numberOfClients = len(self.__repository.getAllClientsMovies())
        numberOfClients = round(0.3 * numberOfClients)

        if numberOfClients == 0:
            numberOfClients = 1

        sortedDict = dict(
            shellSort(
                dict(bubbleSort(self.__repository.getAllClientsMovies().items(), key = lambda item: item[0], reverse = True)).items(),
            key = lambda item: len(item[1]), reverse=True))

        deleted_movies_id = []
        deleted_clients_id = []

        dictKeys = list(sortedDict.keys())
        dictValues = list(sortedDict.values())
        currentIndex = 0
        sortedDict = {}
        while currentIndex < numberOfClients and currentIndex < len(dictKeys):
            if (self.__clientService.idExists(dictKeys[currentIndex])):
                for id in dictValues[currentIndex]:
                    if (self.__movieService.idExists(id)):
                        if dictKeys[currentIndex] not in sortedDict:
                            sortedDict[dictKeys[currentIndex]] = [id]
                        else:
                            sortedDict[dictKeys[currentIndex]].append(id)
                    else:
                        deleted_movies_id.append(id)
            else:
                deleted_clients_id.append(dictKeys[currentIndex])

            currentIndex += 1

        self.__clearDeletedMovies(deleted_movies_id)
        self.__clearDeletedClients(deleted_clients_id)

        return sortedDict

    def formatMoviesDict(self, dict):
        """
        Functia formateaza dictionarul de clienti ale filmelor intr-un mesaj pentru afisare
        :param dict: dictionarul cu cheia movie_id si valori lista de client_id
                    (Dictionary)
        :return: mesajul formatat (String)
        """
        message = ""

        for id in dict:
            movie = self.__movieService.getMovieById(int(id))

            message += str(movie) + (f"\n\tnumar de inchirieri: {len(dict[id])}\n")

        return message.strip()

    def formatClientsDict(self, dict):
        """
        Functia formateaza dictionarul de filme ale clientilor intr-un mesaj pentru afisare
        :param dict: dictionarul cu cheia client_id si valori lista de movie_id
                    (Dictionary)
        :return: mesajul formatat (String)
        """
        message = ""

        for id in dict:
            client = self.__clientService.getClientByIdRecursive(len(self.__clientService.getAllClients()),
                                                                                              self.__clientService.getAllClients(),
                                                                                              int(id))

            message += str(client) + (f"\n\tnumar de inchirieri: {len(dict[id])}\n")

        return message.strip()

    def formatClientsDictShort(self, dict):
        """
        Functia formateaza dictionarul de filme ale clientilor intr-un mesaj scurt pentru afisare
        :param dict: dictionarul cu cheia client_id si valori lista de movie_id
                    (Dictionary)
        :return: mesajul formatat (String)
        """
        message = ""

        for id in dict:
            client = self.__clientService.getClientByIdRecursive(len(self.__clientService.getAllClients()),
                                                                                              self.__clientService.getAllClients(),
                                                                                              int(id))

            message += client.getName() + " " + (f"\tnumar de inchirieri: {len(dict[id])}\n")

        return message.strip()

    def __rentExists(self, movie_id, client_id):
        """
        Functia verifica daca un client a inchiriat un film
        :param movie_id: id-ul filmului (String)
        :param client_id: id-ul clientului (String)
        :return: True (clientul a inchiriat filmul) / False (in caz contrar)
        """
        try:
            movie_id = int(movie_id)
            client_id = int(client_id)
        except Exception:
            pass
        if not client_id in self.__repository.getAllClientsMovies().keys():
            return False

        for movie in self.__repository.getAllClientsMovies()[client_id]:
            if movie == movie_id:
                return True
        return False

    def __clearDeletedMovies(self, deleted_movies_id):
        for movie_id in deleted_movies_id:
            del self.__repository.getAllMoviesClients()[movie_id]
            for key, value in self.__repository.getAllClientsMovies().items():
                if movie_id in value:
                    value.remove(movie_id)
        if type(self.__repository) is FileRentRepository:
            self.__repository.reload()

    def __clearDeletedClients(self, deleted_clients_id):
        for client_id in deleted_clients_id:
            del self.__repository.getAllClientsMovies()[client_id]
            for key, value in self.__repository.getAllMoviesClients().items():
                if client_id in value:
                    value.remove(client_id)
        if type(self.__repository) is FileRentRepository:
            self.__repository.reload()
