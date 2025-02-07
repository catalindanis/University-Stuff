from Business.ClientService import ClientService
from Business.MovieService import MovieService
from Business.RentService import RentService
from Infrastructure.ClientRepository import ClientRepository, FileClientRepository, CrashClientRepository, \
    CrashException
from Infrastructure.MovieRepository import MovieRepository, FileMovieRepository
from Infrastructure.RentRepository import RentRepository, FileRentRepository
from Testing.UnitTesting import runTests
from UserInterface.MainMenu.Console import MainMenu
from UserInterface.StorageMode.Console import StorageMode


def main():
    """
    Functia principala a programului
    :return:
    """
    mode = StorageMode.run()
    if mode != "memory" and mode != "files":
        return

    movieRepository = MovieRepository()
    clientRepository = ClientRepository()
    rentRepository = RentRepository()

    if mode == "files":
        probability = 50
        movieRepository = FileMovieRepository("movies.txt")
        clientRepository = CrashClientRepository("clients.txt", probability)
        rentRepository = FileRentRepository("rents.txt")

    movieService = MovieService(movieRepository)
    clientService = ClientService(clientRepository)
    rentService = RentService(rentRepository, movieService, clientService)

    MainMenu.run(movieService, clientService, rentService)

if __name__ == '__main__':
    main()
