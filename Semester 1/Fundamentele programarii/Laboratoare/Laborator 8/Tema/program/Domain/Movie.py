from enum import Enum


class Movie:
    """
    Clasa reprezinta obiectul unui film
    """
    def __init__(self, id, title, description, type):
        """
        Constructorul clasei film
        :param id: id-ul filmului (Integer > 0)
        :param title: titlul filmului (String)
        :param description: descrierea filmului (String)
        :param type: genul filmului (Integer > 0, < 5)
        """
        self.__id = int(id)
        self.__title = title
        self.__description = description
        self.__type = MovieType(type)

    """
    Getteri pentru atributele clasei Movie
    """
    def getId(self):
        return self.__id

    def getTitle(self):
        return self.__title

    def getDescription(self):
        return self.__description

    def getType(self):
        return self.__type

    """
    Setteri pentru atributele clasei Movie
    """
    def setId(self, id):
        self.__id = id

    def setTitle(self, title):
        self.__title = title

    def setDescription(self, description):
        self.__description = description

    def setType(self, type):
        self.__type = type

    def __eq__(self, other):
        """
        Functia suprascrie metoda de verificare a egalitatii dintre 2 obiecte
        de tip Movie
        :param other: filmul cu care se compara obiectul curent (Movie)
        :return:
        """
        return (self.__id == other.getId()
                and self.__title == other.getTitle()
                and self.__description == other.getDescription()
                and self.__type == other.getType())

    def __str__(self):
        """
        Functia suprascrie metoda folosita la afisarea unui obiect de tip film ca String
        :return:
        """
        return (f"Filmul cu id #{self.__id},\n"
                f"\ttitlul: {self.__title},\n"
                f"\tdescriere: \"{self.__description}\",\n"
                f"\tgen: {self.__type.name.lower()}")

class MovieType(Enum):
    """
    Clasa reprezinta tipul unui film
    Foloseste clasa parinte Enum de la care mosteneste functionalitatile si atributele
    """
    COMEDIE = 1
    HORROR = 2
    DRAMA = 3
    ACTIUNE = 4

class MovieValidator:
    """
    Clasa reprezinta un obiect ce valideaza un obiect de tip Movie
    """
    @staticmethod
    def validateMovie(id, title, description, movie_type):
        """
        Functia valideaza atributele unui obiect de tip Movie
        :param id: id-ul filmului (Integer)
        :param title: titlul filmului (String)
        :param description: descrierea filmului (String)
        :param movie_type: genul filmului (String)
        :return: True (daca atributele sunt corecti) / False (in caz contrar)
        """
        return (MovieValidator.validateId(id)
                and MovieValidator.validateTitle(title)
                and MovieValidator.validateDescription(description)
                and MovieValidator.validateType(movie_type))

    @staticmethod
    def validateId(id):
        """
        Functia valideaza id-ul unui film
        :param id: id-ul filmului (Integer)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        try:
            id = int(id)
            if id < 0:
                return False
        except ValueError:
            return False

        return True

    @staticmethod
    def validateTitle(title):
        """
        Functia valideaza titlul unui film
        :param title: titlul filmului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        if not type(title) == str:
            return False

        if title == "":
            return False

        title = title.split()

        for value in title:
            if not value.isalnum():
                return False

        return True

    @staticmethod
    def validateDescription(description):
        """
        Functia valideaza descrierea unui film
        :param title: descrierea filmului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        if not type(description) == str:
            return False

        if description == "":
            return False

        return True

    @staticmethod
    def validateType(type):
        """
        Functia valideaza genul unui film
        :param title: genul filmului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        try:
            type = int(type)

            if type < 1 or type > 4:
                return False
        except ValueError:
            return False

        return True