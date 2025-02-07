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
        self.__data = {"id" : id, "name" : name, "cnp" : cnp}

    """
    Getteri pentru atributele clasei Client
    """
    def getId(self):
        return self.__data["id"]

    def getName(self):
        return self.__data["name"]

    def getCnp(self):
        return self.__data["cnp"]

    """
    Setteri pentru atributele clasei Client
    """
    def setId(self, id):
        self.__data["id"] = id

    def setName(self, name):
        self.__data["name"] = name

    def setCnp(self, cnp):
        self.__data["cnp"] = cnp

    def __eq__(self, other):
        """
        Functia suprascrie metoda de verificare a egalitatii dintre 2 obiecte
        de tip Client
        :param other: clientul cu care se compara obiectul curent (Client)
        :return:
        """
        return (self.getId() == other.getId()
                and self.getCnp() == other.getCnp()
                and self.getName() == other.getName())

    def __str__(self):
        """
        Functia suprascrie metoda folosita la afisarea unui obiect de tip client ca String
        :return: mesajul afisat (String)
        """
        return (f"Clientul cu id #{self.getId()},\n"
                f"\tnumele: {self.getName()},\n"
                f"\tcnp: {self.getCnp()}")

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