from Business.ClientService import ClientService
from Business.MovieService import MovieService
from Domain.Client import Client, ClientValidator
from Domain.Movie import Movie, MovieValidator, MovieType
from Infrastructure.ClientRepository import ClientRepository
from Infrastructure.MovieRepository import MovieRepository


def runTests():
    """
    Functia apeleaza toate testele create in aplicatie
    :return:
    """
    testMovie()
    testClient()
    testClientRepository()
    testClientService()
    testMovieRepository()
    testMovieService()

def testMovie():
    """
    Functia testeaza clasa Movie, MovieValidator
    :return:
    """
    assert (MovieValidator.validateMovie
            (1, "Spiderman", "This is the description", 2) == True)
    assert (MovieValidator.validateMovie
            (-1, "Spiderman", "This is the description", 2) == False)
    assert (MovieValidator.validateMovie
            (1, "", "This is the description", 2) == False)
    assert (MovieValidator.validateMovie
            (1, "Spiderman", "", 2) == False)
    assert (MovieValidator.validateMovie
            (1, "Spiderman", "This is the description", 0) == False)
    movie1 = Movie(1, "Spiderman", "This is the description", 2)
    assert (movie1.getId() == 1 and movie1.getTitle() == "Spiderman"
            and movie1.getDescription() == "This is the description" and movie1.getType() == MovieType(2))
    movie2 = Movie(2, "Spiderman", "This is the description", 2)
    assert movie1 != movie2
    assert movie2 == Movie(2, "Spiderman", "This is the description", 2)

def testClient():
    """
    Functia testeaza clasa Client, ClientValidator
    :return:
    """
    assert ClientValidator.validateClient(1, "Andrei", 123) == True
    assert ClientValidator.validateClient(-1, "Andrei", 123) == False
    assert ClientValidator.validateClient(1, "", 123) == False
    assert ClientValidator.validateClient(1, "Andrei", 99) == False
    client1 = Client(1, "Andrei", 123)
    assert client1.getId() == 1 and client1.getName() == "Andrei" and client1.getCnp() == 123
    client2 = Client(1, "Andrei", 100)
    assert client1 != client2
    assert client2 == Client(1, "Andrei", 100)

def testClientRepository():
    """
    Functia testeaza clasa ClientRepository
    :return:
    """
    repository = ClientRepository()

    assert repository.addClient(Client(0, "Andrei", 123)) == True
    assert repository.getClients() == [Client(0, "Andrei", 123)]
    assert repository.updateClient(Client(0, "Andrei", 123),
                            Client(0, "Daniel", 109)) == True
    assert repository.getClients() == [Client(0, "Daniel", 109)]
    assert repository.removeClient(Client(0, "Daniel", 109)) == True
    assert repository.getClients() == []



def testClientService():
    """
    Functia testeaza clasa ClientService
    :return:
    """
    repository = ClientRepository()
    service = ClientService(repository)

    assert service.addClient("", 123) == -1
    assert service.addClient("Alex", 99) == -1
    assert service.addClient("Alex", 123) == 1
    assert service.addClient("Alex", 123) == 0
    assert service.addClient("Alex", 100) == 1
    assert service.addClient("Alex", 101) == 1

    assert service.deleteClientById(0) == 1
    assert service.deleteClientById(-1) == -1
    assert service.deleteClientById("") == -1

    assert service.updateClientById(0, "", 123) == -2
    assert service.updateClientById(0, "Alex", 99) == -2
    assert service.updateClientById(-1, "", 123) == -1
    assert service.updateClientById(0, "Alex", 101) == -3
    assert service.updateClientById(0, "Jack", 199) == 1
    assert service.updateClientById(3, "Jack", 201) == 0

    assert service.getAllClients() == [Client(0, "Jack", 199),
                                       Client(1, "Alex", 101)]
    assert service.getAllClients() != [Client(0, "Jack1", 199),
                                       Client(1, "Alex", 101)]
    assert service.getAllClients() != [Client(0, "Jack", 101),
                                       Client(1, "Alex", 101)]
    assert service.getAllClients() != [Client(0, "Jack", 199),
                                       Client(1, "", 101)]


def testMovieRepository():
    """
    Functia testeaza clasa MovieRepository
    :return:
    """
    repository = MovieRepository()

    assert repository.addMovie(Movie(0, "t", "d", 1)) == True
    assert repository.getMovies() == [Movie(0, "t", "d", 1)]
    assert repository.updateMovie(Movie(0, "t", "d", 1),
                            Movie(0, "t1", "d1", 2)) == True
    assert repository.getMovies() == [Movie(0, "t1", "d1", 2)]
    assert repository.removeMovie(Movie(0, "t1", "d1", 2)) == True
    assert repository.getMovies() == []



def testMovieService():
    """
    Functia testeaza clasa MovieService
    :return:
    """
    repository = MovieRepository()
    service = MovieService(repository)

    assert service.addMovie("", "d", 1) == -1
    assert service.addMovie("d", "", 1) == -1
    assert service.addMovie("d", "t", 0) == -1
    assert service.addMovie("t", "d", 1) == 1
    assert service.addMovie("t", "d", 1) == 0
    assert service.addMovie("t", "d", 2) == 1
    assert service.addMovie("t", "d", 3) == 1

    assert service.deleteMovieById(0) == 1
    assert service.deleteMovieById(-1) == -1
    assert service.deleteMovieById("") == -1

    assert service.updateMovieById(0, "", "d1", 4) == -2
    assert service.updateMovieById(0, "t", "", 2) == -2
    assert service.updateMovieById(0, "t1", "d2", 5) == -2
    assert service.updateMovieById(-1, "", 123, 4) == -1
    assert service.updateMovieById("", "", 123, 4) == -1
    #print(service.formatMoviesList(service.getAllMovies()))
    assert service.updateMovieById(0, "t", "d", 3) == -3
    assert service.updateMovieById(0, "Spiderman", "A movie", 4) == 1
    assert service.updateMovieById(3, "Jack", "test", 2) == 0

    assert service.getAllMovies() == [Movie(0, "Spiderman", "A movie", 4),
                                      Movie(1, "t", "d", 3)]
    assert service.getAllMovies() != [Movie(0, "Spiderman1", "A movie", 4),
                                      Movie(1, "t", "d", 3)]
    assert service.getAllMovies() != [Movie(0, "Spiderman1", "A movie", 4),
                                      Movie(1, "t", "d3", 3)]
    assert service.getAllMovies() != [Movie(0, "Spiderman1", "A movie", 4),
                                      Movie(2, "t2", "d", 3)]



