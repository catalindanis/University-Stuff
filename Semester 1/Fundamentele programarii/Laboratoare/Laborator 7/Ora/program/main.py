from Business.ClientService import ClientService
from Business.MovieService import MovieService
from Infrastructure.ClientRepository import ClientRepository
from Infrastructure.MovieRepository import MovieRepository
from Testing.Tests import runTests
from UserInterface.Console import MainMenu


def main():
    """
    Functia principala a programului
    :return:
    """

    movieRepository = MovieRepository()
    clientRepository = ClientRepository()

    movieService = MovieService(movieRepository)
    clientService = ClientService(clientRepository)

    MainMenu.run(movieService, clientService)

if __name__ == '__main__':
    runTests()
    main()
