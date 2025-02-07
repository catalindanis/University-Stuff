from Domain.Client import ClientValidator
from Domain.Movie import MovieValidator


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

        self.__repository.removeRent(client_id, movie_id)
        return 1

    def __rentExists(self, movie_id, client_id):
        """
        Functia verifica daca un client a inchiriat un film
        :param movie_id: id-ul filmului (String)
        :param client_id: id-ul clientului (String)
        :return: True (clientul a inchiriat filmul) / False (in caz contrar)
        """
        if not client_id in self.__repository.getAllClientsMovies().keys():
            return False

        for movie in self.__repository.getAllClientsMovies()[client_id]:
            if movie == movie_id:
                return True
        return False
