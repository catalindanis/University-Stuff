from Business.ClientService import ClientService
from Business.MovieService import MovieService
from Business.RentService import RentService
from Infrastructure.ClientRepository import ClientRepository
from Infrastructure.MovieRepository import MovieRepository
from Infrastructure.RentRepository import RentRepository
from Testing.Tests import runTests
from UserInterface.MainMenu.Console import MainMenu


def main():
    """
    Functia principala a programului
    :return:
    """

    movieRepository = MovieRepository()
    clientRepository = ClientRepository()
    rentRepository = RentRepository()

    movieService = MovieService(movieRepository)
    clientService = ClientService(clientRepository)
    rentService = RentService(rentRepository, movieService, clientService)

    MainMenu.run(movieService, clientService, rentService)

if __name__ == '__main__':
    runTests()
    main()
