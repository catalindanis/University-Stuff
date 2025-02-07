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
        self.__id = int(id)
        self.__name = name
        self.__cnp = int(cnp)

    """
    Getteri pentru atributele clasei Client
    """
    def getId(self):
        return self.__id

    def getName(self):
        return self.__name

    def getCnp(self):
        return self.__cnp

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
        :param id: id-ul clientului (Integer)
        :param name: numele clientului (String)
        :param cnp: cnp-ul clientului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        return (ClientValidator.validateId(id)
            and ClientValidator.validateName(name)
                and ClientValidator.validateCnp(cnp))

    @staticmethod
    def validateId(id):
        """
        Functia valideaza id-ul unui client
        :param id: id-ul clientului (Integer)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        try:
            id = int(id)
            if id < 0:
                return False
        except Exception:
            return False

        return True

    @staticmethod
    def validateName(name):
        """
        Functia valideaza numele unui client
        :param name: numele clientului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        if not type(name) == str:
            return False

        if name == "":
            return False

        name = name.split()

        for value in name:
            if not value.isalpha():
                return False

        return True

    @staticmethod
    def validateCnp(cnp):
        """
        Functia valideaza cnp-ul unui client
        :param id: cnp-ul clientului (String)
        :return: True (daca atributele sunt corecte) / False (in caz contrar)
        """
        try:
            cnp = int(cnp)
            if cnp < 100 or cnp > 999:
                return False
        except Exception:
            return False

        return True
