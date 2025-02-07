class Client:
    """
    Clasa reprezinta obiectul unui client
    """
    def __init__(self, id, name, cnp):
        """
        Constructorul clasei client
        :param id: id-ul clientului (Integer > 0)
        :param name: numele clientului (String)
        :param cnp: cnp-ul clientului (Integer of 3 digits)
        """
        self.__id = id
        self.__name = name
        self.__cnp = cnp

    """
    Getteri pentru atributele clasei Client
    """
    def getId(self):
        return self.__id

    def getName(self):
        return self.__name

    def getCnp(self):
        return self.__cnp

    def setId(self, id):
        self.__id = id

    """
    Setteri pentru atributele clasei Client
    """
    def setId(self, id):
        self.__id = id

    def setName(self, name):
        self.__name = name

    def setCnp(self, cnp):
        self.__cnp = cnp

    def __eq__(self, other):
        """
        Functia suprascrie metoda de verificare a egalitatii dintre 2 obiecte
        de tip Client
        :param other: clientul cu care se compara obiectul curent (Client)
        :return:
        """
        return (self.__id == other.getId()
                and self.__cnp == other.getCnp()
                and self.__name == other.getName())

    def __str__(self):
        """
        Functia suprascrie metoda folosita la afisarea unui obiect de tip client ca String
        :return: mesajul afisat (String)
        """
        return (f"Clientul cu id #{self.__id},\n"
                f"\tnumele: {self.__name},\n"
                f"\tcnp: {self.__cnp}")

class ClientValidator:
    """
    Clasa reprezinta un obiect ce valideaza un obiect de tip Client
    """
    @staticmethod
    def validateClient(id, name, cnp):
        """
        Functia valideaza atributele unui obiect de tip Client
        :param id: id-ul clientului (Integer >= 0)
        :param name: numele clientului (String)
        :param cnp: cnp-ul clientului (Integer of 3 digits)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        try:
            cnp = int(cnp)
        except ValueError:
            return False

        if not type(id) == int or not type(name) == str or not type(cnp) == int:
            return False

        if id < 0 or name == "" or cnp < 100 or cnp > 999:
            return False

        return True