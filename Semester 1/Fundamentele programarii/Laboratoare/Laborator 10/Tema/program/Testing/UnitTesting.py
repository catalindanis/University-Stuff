import unittest

from Business.ClientService import ClientService
from Business.MovieService import MovieService
from Business.RentService import RentService
from Domain.Client import Client, ClientValidator
from Domain.Movie import Movie, MovieType, MovieValidator
from Infrastructure.ClientRepository import ClientRepository
from Infrastructure.MovieRepository import MovieRepository
from Infrastructure.RentRepository import RentRepository

class TestClient(unittest.TestCase):
    """
    Clasa de test pentru clasa Client
    """
    def setUp(self):
        """
        Functia de initializare a clasei TestClient
        :return:
        """
        self.client = Client(1, "John Doe", 123)

    def test_client_initialization(self):
        """
        Testare initializare client
        :return:
        """
        self.assertEqual(self.client.getId(), 1)
        self.assertEqual(self.client.getName(), "John Doe")
        self.assertEqual(self.client.getCnp(), 123)

    def test_client_setters(self):
        """
        Testare functii setter client
        :return:
        """
        self.client.setId(2)
        self.client.setName("Jane Doe")
        self.client.setCnp(456)
        self.assertEqual(self.client.getId(), 2)
        self.assertEqual(self.client.getName(), "Jane Doe")
        self.assertEqual(self.client.getCnp(), 456)

    def test_client_equality(self):
        """
        Testare egalitate clienti
        :return:
        """
        other_client = Client(1, "John Doe", 123)
        self.assertEqual(self.client, other_client)

        different_client = Client(2, "Jane Doe", 456)
        self.assertNotEqual(self.client, different_client)

    def test_client_string_representation(self):
        """
        Testare reprezentare string client
        :return:
        """
        self.assertEqual(
            str(self.client),
            "Clientul cu id #1,\n\tnumele: John Doe,\n\tcnp: 123"
        )

class TestClientValidator(unittest.TestCase):
    """
    Clasa de test pentru ClientValidator
    """
    def test_validate_id(self):
        """
        Testare validator id client
        :return:
        """
        self.assertTrue(ClientValidator.validateId(1))
        self.assertTrue(ClientValidator.validateId("42"))
        self.assertFalse(ClientValidator.validateId(-1))
        self.assertFalse(ClientValidator.validateId("abc"))
        self.assertFalse(ClientValidator.validateId(None))

    def test_validate_name(self):
        """
        Testare validator nume client
        :return:
        """
        self.assertTrue(ClientValidator.validateName("John Doe"))
        self.assertTrue(ClientValidator.validateName("Anna Maria"))
        self.assertFalse(ClientValidator.validateName("John123"))
        self.assertFalse(ClientValidator.validateName(""))
        self.assertFalse(ClientValidator.validateName(123))

    def test_validate_cnp(self):
        """
        Testare validator cnp client
        :return:
        """
        self.assertTrue(ClientValidator.validateCnp(123))
        self.assertTrue(ClientValidator.validateCnp("456"))
        self.assertFalse(ClientValidator.validateCnp(99))
        self.assertFalse(ClientValidator.validateCnp(1000))
        self.assertFalse(ClientValidator.validateCnp("abc"))

    def test_validate_client(self):
        """
        Testare validator client
        :return:
        """
        self.assertTrue(ClientValidator.validateClient(1, "John Doe", 123))
        self.assertFalse(ClientValidator.validateClient(-1, "John Doe", 123))
        self.assertFalse(ClientValidator.validateClient(1, "John123", 123))
        self.assertFalse(ClientValidator.validateClient(1, "John Doe", 99))

class TestClientRepository(unittest.TestCase):
    def setUp(self):
        """
        Functia de initializare a clasei TestClientRepository
        :return:
        """
        self.repository = ClientRepository()

    def test_add_client(self):
        """
        Testare adaugare client
        :return:
        """
        client = Client(1, "John Doe", 123456)
        result = self.repository.addClient(client)
        self.assertTrue(result)
        self.assertIn(client, self.repository.getClients())

    def test_remove_client(self):
        """
        Testare stergere client
        :return:
        """
        client = Client(1, "John Doe", 123456)
        self.repository.addClient(client)
        result = self.repository.removeClient(client)
        self.assertTrue(result)
        self.assertNotIn(client, self.repository.getClients())

    def test_remove_non_existent_client(self):
        """
        Testare stergere client care nu exista
        :return:
        """
        client = Client(1, "John Doe", 123456)
        result = self.repository.removeClient(client)
        self.assertFalse(result)

    def test_update_client(self):
        """
        Testare actualizare client
        :return:
        """
        client = Client(1, "John Doe", 123456)
        new_client = Client(1, "John Smith", 654321)
        self.repository.addClient(client)
        result = self.repository.updateClient(client, new_client)
        self.assertTrue(result)
        self.assertIn(new_client, self.repository.getClients())
        self.assertNotIn(client, self.repository.getClients())

    def test_update_non_existent_client(self):
        """
        Testare actualizare client care nu exista
        :return:
        """
        client = Client(1, "John Doe", 123456)
        new_client = Client(1, "John Smith", 654321)
        result = self.repository.updateClient(client, new_client)
        self.assertFalse(result)

    def test_get_clients(self):
        """
        Testare obtinere lista clienti
        :return:
        """
        client1 = Client(1, "John Doe", 123456)
        client2 = Client(2, "Jane Doe", 654321)
        self.repository.addClient(client1)
        self.repository.addClient(client2)
        clients = self.repository.getClients()
        self.assertEqual(len(clients), 2)
        self.assertIn(client1, clients)
        self.assertIn(client2, clients)

class TestClientService(unittest.TestCase):
    """
    Clasa de test pentru serviciul ClientService
    """

    def setUp(self):
        """
        Functia de initializare a clasei TestClientService
        :return:
        """
        # Se creeaza un mock pentru repository-ul ClientRepository
        self.repository = ClientRepository()
        self.service = ClientService(self.repository)

    def test_add_client_valid(self):
        """
        Testare adaugare client valid
        :return:
        """
        result = self.service.addClient("John Doe", "123")
        self.assertEqual(result, 1)
        self.assertEqual(len(self.repository.getClients()), 1)

    def test_add_client_invalid(self):
        """
        Testare adaugare client cu date invalide
        :return:
        """
        result = self.service.addClient("", "123")  # Nume invalid
        self.assertEqual(result, -1)

    def test_add_client_already_exists(self):
        """
        Testare adaugare client care deja exista
        :return:
        """
        self.service.addClient("John Doe", "123")
        result = self.service.addClient("John Doe", "123")  # Clientul deja exista
        self.assertEqual(result, 0)

    def test_delete_client_valid(self):
        """
        Testare stergere client valid dupa id
        :return:
        """
        self.service.addClient("John Doe", "123")
        result = self.service.deleteClientById(0)
        self.assertEqual(result, 1)
        self.assertEqual(len(self.repository.getClients()), 0)

    def test_delete_client_invalid_id(self):
        """
        Testare stergere client cu id invalid
        :return:
        """
        result = self.service.deleteClientById(-1)
        self.assertEqual(result, -1)

    def test_update_client_valid(self):
        """
        Testare actualizare client valid
        :return:
        """
        self.service.addClient("John Doe", "123")
        result = self.service.updateClientById(0, "Jane Doe", "456")
        self.assertEqual(result, 1)
        updated_client = self.repository.getClients()[0]
        self.assertEqual(updated_client.getName(), "Jane Doe")
        self.assertEqual(updated_client.getCnp(), 456)

    def test_update_client_invalid_data(self):
        """
        Testare actualizare client cu date invalide
        :return:
        """
        result = self.service.updateClientById(1, "", "123")  # Nume invalid
        self.assertEqual(result, -2)

    def test_search_clients_by_name_valid(self):
        """
        Testare cautare clienti dupa nume valid
        :return:
        """
        self.service.addClient("John Doe", "123")
        clients = self.service.searchClientsByName("John")
        self.assertEqual(len(clients), 1)
        self.assertEqual(clients[0].getName(), "John Doe")

    def test_search_clients_by_name_invalid(self):
        """
        Testare cautare clienti dupa nume invalid
        :return:
        """
        clients = self.service.searchClientsByName("")
        self.assertEqual(clients, None)

    def test_search_clients_by_cnp_valid(self):
        """
        Testare cautare clienti dupa cnp valid
        :return:
        """
        self.service.addClient("John Doe", "123")
        clients = self.service.searchClientsByCnp("123")
        self.assertEqual(len(clients), 1)
        self.assertEqual(clients[0].getCnp(), 123)

    def test_search_clients_by_cnp_invalid(self):
        """
        Testare cautare clienti dupa cnp invalid
        :return:
        """
        clients = self.service.searchClientsByCnp("999")
        self.assertEqual(clients, [])

    def test_get_all_clients(self):
        """
        Testare obtinere toti clientii
        :return:
        """
        self.service.addClient("John Doe", "123")
        self.service.addClient("Jane Doe", "456")
        clients = self.service.getAllClients()
        self.assertEqual(len(clients), 2)

    def test_get_client_by_id(self):
        """
        Testare obtinere client dupa id
        :return:
        """
        self.service.addClient("John Doe", "123")
        client = self.service.getClientById(0)
        self.assertEqual(client.getName(), "John Doe")

    def test_get_client_by_invalid_id(self):
        """
        Testare obtinere client cu id invalid
        :return:
        """
        client = self.service.getClientById(99)
        self.assertEqual(client, None)

    def test_format_clients_list(self):
        """
        Testare formatare lista clienti
        :return:
        """
        self.service.addClient("John Doe", "123")
        self.service.addClient("Jane Doe", "456")
        formatted_list = self.service.formatClientsList(self.repository.getClients())
        self.assertIn("John Doe", formatted_list)
        self.assertIn("Jane Doe", formatted_list)

class TestMovie(unittest.TestCase):
    """
    Clasa de test pentru Movie
    """

    def setUp(self):
        """
        Functia de initializare a clasei TestMovie
        :return:
        """
        self.movie = Movie(1, "Inception", "Un film despre vise in vise", 4)

    def test_movie_initialization(self):
        """
        Testare initializare film
        :return:
        """
        self.assertEqual(self.movie.getId(), 1)
        self.assertEqual(self.movie.getTitle(), "Inception")
        self.assertEqual(self.movie.getDescription(), "Un film despre vise in vise")
        self.assertEqual(self.movie.getType(), MovieType.ACTIUNE)

    def test_movie_setters(self):
        """
        Testare functii setter film
        :return:
        """
        self.movie.setId(2)
        self.movie.setTitle("Titanic")
        self.movie.setDescription("O poveste de dragoste pe un vapor scufundat")
        self.movie.setType(MovieType.DRAMA)
        self.assertEqual(self.movie.getId(), 2)
        self.assertEqual(self.movie.getTitle(), "Titanic")
        self.assertEqual(self.movie.getDescription(), "O poveste de dragoste pe un vapor scufundat")
        self.assertEqual(self.movie.getType(), MovieType.DRAMA)

    def test_movie_equality(self):
        """
        Testare egalitate filme
        :return:
        """
        other_movie = Movie(1, "Inception", "Un film despre vise in vise", 4)
        self.assertEqual(self.movie, other_movie)

        different_movie = Movie(2, "Titanic", "O poveste de dragoste pe un vapor scufundat", 3)
        self.assertNotEqual(self.movie, different_movie)

    def test_movie_string_representation(self):
        """
        Testare reprezentare string film
        :return:
        """
        self.assertEqual(
            str(self.movie),
            "Filmul cu id #1,\n\ttitlul: Inception,\n\tdescriere: \"Un film despre vise in vise\",\n\tgen: actiune"
        )

class TestMovieValidator(unittest.TestCase):
    """
    Clasa de test pentru MovieValidator
    """
    def test_validate_id(self):
        """
        Testare validator id film
        :return:
        """
        self.assertTrue(MovieValidator.validateId(1))
        self.assertTrue(MovieValidator.validateId("42"))
        self.assertFalse(MovieValidator.validateId(-1))
        self.assertFalse(MovieValidator.validateId("abc"))
        self.assertFalse(MovieValidator.validateId(None))

    def test_validate_title(self):
        """
        Testare validator titlu film
        :return:
        """
        self.assertTrue(MovieValidator.validateTitle("Inception"))
        self.assertTrue(MovieValidator.validateTitle("The Matrix"))
        self.assertFalse(MovieValidator.validateTitle(""))
        self.assertFalse(MovieValidator.validateTitle(123))
        self.assertFalse(MovieValidator.validateTitle("Spiderman!"))

    def test_validate_description(self):
        """
        Testare validator descriere film
        :return:
        """
        self.assertTrue(MovieValidator.validateDescription("Un film interesant"))
        self.assertFalse(MovieValidator.validateDescription(""))
        self.assertFalse(MovieValidator.validateDescription(123))

    def test_validate_type(self):
        """
        Testare validator gen film
        :return:
        """
        self.assertTrue(MovieValidator.validateType(1))
        self.assertTrue(MovieValidator.validateType(4))
        self.assertFalse(MovieValidator.validateType(0))
        self.assertFalse(MovieValidator.validateType(5))
        self.assertFalse(MovieValidator.validateType("abc"))

    def test_validate_movie(self):
        """
        Testare validator film
        :return:
        """
        self.assertTrue(MovieValidator.validateMovie(1, "Inception", "Un film interesant", 4))
        self.assertFalse(MovieValidator.validateMovie(-1, "Inception", "Un film interesant", 4))
        self.assertFalse(MovieValidator.validateMovie(1, "", "Un film interesant", 4))
        self.assertFalse(MovieValidator.validateMovie(1, "Inception", "", 4))
        self.assertFalse(MovieValidator.validateMovie(1, "Inception", "Un film interesant", 5))

class TestMovieRepository(unittest.TestCase):
    """
    Clasa de test pentru MovieRepository
    """
    def setUp(self):
        """
        Set up the test environment
        """
        self.repository = MovieRepository()

        # Creating sample movies for testing
        self.movie1 = Movie(1, "Movie 1", "Description of Movie 1", 4)
        self.movie2 = Movie(2, "Movie 2", "Description of Movie 2", 1)
        self.movie3 = Movie(3, "Movie 3", "Description of Movie 3", 3)

    def test_add_movie(self):
        """
        Test adding a movie to the repository
        """
        self.assertTrue(self.repository.addMovie(self.movie1))
        self.assertEqual(len(self.repository.getMovies()), 1)
        self.assertEqual(self.repository.getMovies()[0], self.movie1)

    def test_remove_movie(self):
        """
        Test removing a movie from the repository
        """
        self.repository.addMovie(self.movie1)
        self.assertTrue(self.repository.removeMovie(self.movie1))
        self.assertEqual(len(self.repository.getMovies()), 0)

    def test_remove_movie_not_found(self):
        """
        Test removing a movie that doesn't exist
        """
        self.repository.addMovie(self.movie1)
        self.assertFalse(self.repository.removeMovie(self.movie2))

    def test_update_movie(self):
        """
        Test updating an existing movie in the repository
        """
        self.repository.addMovie(self.movie1)
        updated_movie = Movie(1, "Updated Movie 1", "Updated Description", 1)
        self.assertTrue(self.repository.updateMovie(self.movie1, updated_movie))
        self.assertEqual(self.repository.getMovies()[0], updated_movie)

    def test_update_movie_not_found(self):
        """
        Test updating a movie that doesn't exist
        """
        self.repository.addMovie(self.movie1)
        updated_movie = Movie(1, "Updated Movie 1", "Updated Description", 3)
        self.assertFalse(self.repository.updateMovie(self.movie2, updated_movie))

    def test_get_movies_empty(self):
        """
        Test getting movies from an empty repository
        """
        self.assertEqual(self.repository.getMovies(), [])

    def test_get_movies_after_addition(self):
        """
        Test getting movies after adding some movies
        """
        self.repository.addMovie(self.movie1)
        self.repository.addMovie(self.movie2)
        self.assertEqual(len(self.repository.getMovies()), 2)

    def test_add_multiple_movies(self):
        """
        Test adding multiple movies to the repository
        """
        self.repository.addMovie(self.movie1)
        self.repository.addMovie(self.movie2)
        self.repository.addMovie(self.movie3)
        self.assertEqual(len(self.repository.getMovies()), 3)
        self.assertIn(self.movie1, self.repository.getMovies())
        self.assertIn(self.movie2, self.repository.getMovies())
        self.assertIn(self.movie3, self.repository.getMovies())

    def test_remove_movie_from_empty_repository(self):
        """
        Test removing a movie from an empty repository
        """
        self.assertFalse(self.repository.removeMovie(self.movie1))

class TestMovieService(unittest.TestCase):

    def setUp(self):
        """
        Creează un repository mock pentru testare
        """
        self.mock_repository = MovieRepository()
        self.movie_service = MovieService(self.mock_repository)

    def test_add_movie_valid(self):
        """
        Testează adăugarea unui film cu date valide.
        """
        movie = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        #self.mock_repository.getMovies() == []
        #self.mock_repository.addMovie.return_value = None  # Simulează comportamentul repository-ului

        result = self.movie_service.addMovie("Inception", "A mind-bending thriller", "1")

        self.assertEqual(result, 1)
        self.assertTrue(self.mock_repository.addMovie(movie), 1)

    def test_add_movie_invalid_data(self):
        """
        Testează adăugarea unui film cu date invalide.
        """
        self.mock_repository.getMovies() == []
        result = self.movie_service.addMovie("", "", "1")  # Titlu și descriere invalide
        self.assertEqual(result, -1)

    def test_add_movie_already_exists(self):
        """
        Testează adăugarea unui film care există deja.
        """
        existing_movie = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        self.mock_repository.addMovie(existing_movie) == False

        result = self.movie_service.addMovie("Inception", "A mind-bending thriller", "1")

        self.assertEqual(result, 0)  # Filmul există deja

    def test_delete_movie_by_id_valid(self):
        """
        Testează ștergerea unui film după ID valid.
        """
        self.movie_service.addMovie("Inception", "A mind-bending thriller", 1)

        result = self.movie_service.deleteMovieById(0)

        self.assertEqual(result, 1)

    def test_delete_movie_by_id_invalid(self):
        """
        Testează ștergerea unui film cu ID invalid.
        """
        result = self.movie_service.deleteMovieById(-1)
        self.assertEqual(result, -1)  # ID invalid

    def test_delete_movie_by_id_not_found(self):
        """
        Testează ștergerea unui film care nu există.
        """
        result = self.movie_service.deleteMovieById(99)  # ID-ul filmului nu există
        self.assertEqual(result, 0)

    def test_update_movie_valid(self):
        """
        Testează actualizarea unui film cu un ID valid.
        """
        old_movie = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        new_movie = Movie(1, "Inception 2", "A mind-bending thriller sequel", MovieType(1))

        self.movie_service.addMovie("Inception", "A mind-bending thriller", "1")
        self.movie_service.addMovie("Inception 2", "A mind-bending thriller sequel", 1)

        result = self.movie_service.updateMovieById(1, "Inception 2", "A mind-bending thriller sequel", "1")

        self.assertEqual(result, 1)

    def test_update_movie_invalid_data(self):
        """
        Testează actualizarea unui film cu date invalide.
        """
        result = self.movie_service.updateMovieById(1, "", "", "1")
        self.assertEqual(result, -2)  # Date invalide

    def test_search_movie_by_title_valid(self):
        """
        Testează căutarea filmelor după titlu.
        """
        movie1 = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        movie2 = Movie(2, "Inception 2", "A mind-bending thriller sequel", MovieType(1))

        self.movie_service.addMovie("Inception", "A mind-bending thriller", "1")
        self.movie_service.addMovie("Inception 2", "A mind-bending thriller sequel", "1")

        result = self.movie_service.searchMovieByTitle("Inception")
        self.assertEqual(len(result), 2)

    def test_search_movie_by_title_invalid(self):
        """
        Testează căutarea filmelor cu un titlu invalid.
        """
        result = self.movie_service.searchMovieByTitle("")
        self.assertEqual(result, None)  # Titlu invalid

    def test_search_movie_by_type(self):
        """
        Testează căutarea filmelor după tip.
        """
        movie1 = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        movie2 = Movie(2, "Titanic", "A romantic drama", MovieType(2))

        self.movie_service.addMovie("Inception", "A mind-bending thriller", "1")
        self.movie_service.addMovie("Titanic", "A romantic drama", 2)

        result = self.movie_service.searchMovieByType("1")
        self.assertEqual(len(result), 1)

    def test_id_exists(self):
        """
        Testează verificarea existenței unui film după ID.
        """
        self.movie_service.addMovie("Inception", "A mind-bending thriller", 1)

        result = self.movie_service.idExists(0)
        self.assertEqual(result, 1)

    def test_id_exists_not_found(self):
        """
        Testează verificarea existenței unui film cu un ID inexistent.
        """
        result = self.movie_service.idExists(99)
        self.assertEqual(result, 0)

    def test_get_all_movies(self):
        """
        Testează obținerea tuturor filmelor din repository.
        """

        self.movie_service.addMovie("Inception", "A mind-bending thriller", 1)
        self.movie_service.addMovie("Titanic", "A romantic drama", 2)

        result = self.movie_service.getAllMovies()
        self.assertEqual(len(result), 2)

    def test_get_movie_by_id(self):
        """
        Testează obținerea unui film după ID.
        """
        self.movie_service.addMovie("Inception", "A mind-bending thriller", 1)

        result = self.movie_service.getMovieById(0)
        self.assertEqual(result, Movie(0, "Inception", "A mind-bending thriller", 1))

    def test_get_movie_by_id_not_found(self):
        """
        Testează obținerea unui film cu un ID inexistent.
        """
        result = self.movie_service.getMovieById(99)
        self.assertEqual(result, None)

    def test_format_movies_list(self):
        """
        Testează formatarea unei liste de filme într-un mesaj de afișare.
        """
        movie1 = Movie(1, "Inception", "A mind-bending thriller", MovieType(1))
        movie2 = Movie(2, "Titanic", "A romantic drama", MovieType(2))
        movie_list = [movie1, movie2]

        result = self.movie_service.formatMoviesList(movie_list)
        self.assertIn("Inception", result)
        self.assertIn("Titanic", result)

class TestRentRepository(unittest.TestCase):

    def setUp(self):
        """
        Create instances of RentRepository for testing
        """
        self.rent_repo = RentRepository()

    def test_add_rent(self):
        """
        Test adding a rent (assign a movie to a client and vice versa)
        """
        client = Client(1, "John Doe", "12345")
        movie = Movie(1, "Inception", "A mind-bending thriller", 2)
        result = self.rent_repo.addRent(client.getId(), movie.getId())
        self.assertTrue(result)
        self.assertIn(client.getId(), self.rent_repo.getAllClientsMovies())
        self.assertIn(movie.getId(), self.rent_repo.getAllMoviesClients())

    def test_remove_rent(self):
        """
        Test removing a rent (remove the association between a client and a movie)
        """
        client = Client(1, "John Doe", "12345")
        movie = Movie(1, "Inception", "A mind-bending thriller", 2)
        self.rent_repo.addRent(client.getId(), movie.getId())
        result = self.rent_repo.removeRent(client.getId(), movie.getId())
        self.assertTrue(result)

    def test_get_all_clients_movies(self):
        """
        Test retrieving all clients and their rented movies
        """
        client = Client(1, "John Doe", "12345")
        movie1 = Movie(1, "Inception", "A mind-bending thriller", 3)
        movie2 = Movie(2, "The Matrix", "Another mind-bending movie", 4)
        self.rent_repo.addRent(client.getId(), movie1.getId())
        self.rent_repo.addRent(client.getId(), movie2.getId())
        all_movies = self.rent_repo.getAllClientsMovies()
        self.assertIn(client.getId(), all_movies)
        self.assertEqual(len(all_movies[client.getId()]), 2)

    def test_get_all_movies_clients(self):
        """
        Test retrieving all movies and their clients
        """
        client1 = Client(1, "John Doe", "12345")
        client2 = Client(2, "Jane Doe", "67890")
        movie = Movie(1, "Inception", "A mind-bending thriller", 1)
        self.rent_repo.addRent(client1.getId(), movie.getId())
        self.rent_repo.addRent(client2.getId(), movie.getId())
        all_clients = self.rent_repo.getAllMoviesClients()
        self.assertIn(movie.getId(), all_clients)
        self.assertEqual(len(all_clients[movie.getId()]), 2)

class TestRentService(unittest.TestCase):
    """
    Clasa de test pentru serviciul RentService
    """

    def setUp(self):
        """
        Funcția de inițializare a clasei TestRentService
        Se creează mock-uri pentru serviciile dependente
        :return:
        """
        # Mock pentru serviciile dependente
        self.client_repo = ClientRepository()
        self.movie_repo = MovieRepository()

        self.mock_movie_service = MovieService(self.movie_repo)
        self.mock_client_service = ClientService(self.client_repo)
        self.mock_rent_repository = RentRepository()

        # Crearea obiectului RentService cu dependințele mock
        self.rent_service = RentService(
            self.mock_rent_repository,
            self.mock_movie_service,
            self.mock_client_service
        )

    def test_rent_movie_invalid_data(self):
        """
        Testare închiriere film cu date invalide
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        rezultat = self.rent_service.rentMovie("invalid_movie_id", "invalid_client_id")

        self.assertEqual(rezultat, -2)  # Date invalide

    def test_rent_movie_movie_not_found(self):
        """
        Testare închiriere film care nu există
        :return:
        """
        self.mock_movie_service.addMovie("test", "test", 2)
        self.mock_client_service.addClient("test", 123)

        rezultat = self.rent_service.rentMovie(3, 0)

        self.assertEqual(rezultat, -1)  # Filmul nu există

    def test_rent_movie_success(self):
        """
        Testare închiriere film cu succes
        :return:
        """
        self.mock_movie_service.addMovie("test", "test", 2)
        self.mock_client_service.addClient("test", 123)


        rezultat = self.rent_service.rentMovie(0, 0)

        self.assertEqual(rezultat, 1)  # Operația a avut succes

    def test_rent_movie_already_rented(self):
        """
        Testare închiriere film deja închiriat de client
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        self.rent_service.rentMovie(0, 0)
        rezultat = self.rent_service.rentMovie(0, 0)

        self.assertEqual(rezultat, 2)

    def test_unrent_movie_invalid_data(self):
        """
        Testare returnare film cu date invalide
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        rezultat = self.rent_service.unrentMovie("invalid_movie_id", "invalid_client_id")

        self.assertEqual(rezultat, -2)  # Date invalide

    def test_unrent_movie_movie_not_found(self):
        """
        Testare returnare film care nu există
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        rezultat = self.rent_service.unrentMovie(4, 0)

        self.assertEqual(rezultat, -1)

    def test_unrent_movie_client_not_found(self):
        """
        Testare returnare film când clientul nu există
        :return:
        """
        # Simulăm că clientul nu există
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        rezultat = self.rent_service.unrentMovie(0, 4)

        self.assertEqual(rezultat, 0)  # Clientul nu există

    def test_unrent_movie_success(self):
        """
        Testare returnare film cu succes
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        self.rent_service.rentMovie(0, 0)
        rezultat = self.rent_service.unrentMovie(0, 0)

        self.assertEqual(rezultat, 1)  # Operația a avut succes

    def test_unrent_movie_not_rented(self):
        """
        Testare returnare film care nu a fost închiriat
        :return:
        """
        self.mock_client_service.addClient("test", 123)
        self.mock_movie_service.addMovie("test", "test", 2)

        rezultat = self.rent_service.unrentMovie(0, 0)

        self.assertEqual(rezultat, 2)

def runTests():
    unittest.main()
