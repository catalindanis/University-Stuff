from random import random, randrange

from Domain.Client import ClientValidator, Client


class ClientRepository:
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Client
    """
    def __init__(self):
        """
        Constructorul clasei ClientRepository
        :return:
        """
        self._clients_list = []

    def getClients(self):
        """
        Functia returneaza lista de clienti
        :return: lista de clienti (List of Client)
        """
        return self._clients_list

    def addClient(self, client):
        """
        Functia adauga un client in lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._clients_list.append(client)
            return True
        except Exception:
            return False

    def removeClient(self, client):
        """
        Functia sterge un client din lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._clients_list.remove(client)
            return True
        except Exception:
            return False

    def updateClient(self, client, new_client):
        """
        Functia actualizeaza un client din lista de cheltuieli
        :param client: clientul vechi (Client)
        :param new_client: clientul nou (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._clients_list[self._clients_list.index(client)] = new_client
            return True
        except Exception:
            return False

class FileClientRepository(ClientRepository):
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Client care stocheaza datele in fisier
    """
    def __init__(self, file_path):
        """
        Constructorul clasei FileClientRepository
        :param file_path: calea fisierului in care se va stoca lista de clienti (String)
        :return:
        """
        super().__init__()
        self.__file_path = file_path
        self._readFromFile()

    def _readFromFile(self):
        """
        Functia citeste din fisier lista de clienti
        :return:
        """
        with open(self.__file_path, 'r') as file:
            self._clients_list.clear()
            lines = file.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    line = line.split(",")
                    if len(line) == 3:
                        client_id = line[0].strip()
                        client_name = line[1].strip()
                        client_cnp = line[2].strip()
                        if (ClientValidator.validateClient(client_id, client_name, client_cnp)):
                            self._clients_list.append(Client(client_id, client_name, client_cnp))

    def _loadToFile(self):
        """
        Functia scrie in fisier lista de clienti
        :return:
        """
        with open(self.__file_path, 'w') as file:
            for client in self._clients_list:
                file.write(f"{client.getId()},{client.getName()},{client.getCnp()}\n")


    def getClients(self):
        """
        Functia returneaza lista de clienti citita din fisier
        :return: lista de clienti (List of Client)
        """
        self._readFromFile()
        return self._clients_list

    def addClient(self, client):
        """
        Functia adauga un client in lista de clienti si scrie in fisier lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._readFromFile()
            super().addClient(client)
            self._loadToFile()
            return True
        except Exception:
            return False

    def removeClient(self, client):
        """
        Functia sterge un client din lista de clienti si scrie in fisier lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._readFromFile()
            super().removeClient(client)
            self._loadToFile()
            return True
        except Exception:
            return False

    def updateClient(self, client, new_client):
        """
        Functia actualizeaza un client din lista de cheltuieli si scrie in fisier lista de clienti
        :param client: clientul vechi (Client)
        :param new_client: clientul nou (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self._readFromFile()
            super().updateClient(client, new_client)
            self._loadToFile()
            return True
        except Exception:
            return False

class CrashClientRepository(FileClientRepository):
    """
    Clasa reprezinta crash repository-ul folosit pentru obiectele de tip Client
    care stocheaza datele in fisier
    """
    def __init__(self, file_path, probability):
        """
        Constructorul clasei CrashClientRepository
        :param file_path: calea fisierului in care se va stoca lista de clienti (String)
        :param probability: probabilitatea ca programul sa crape (int)
        :return:
        """
        super().__init__(file_path)
        self.__crash_probability = probability

    def __readFromFile(self):
        """
        Functia citeste din fisier lista de clienti
        :return:
        """
        value = randrange(0, 100)
        if value < self.__crash_probability:
            raise CrashException()
        return super()._readFromFile()

    def __loadToFile(self):
        """
        Functia scrie in fisier lista de clienti
        :return:
        """
        value = randrange(0, 101)
        if value < self.__crash_probability:
            raise CrashException()
        return super()._loadToFile()

    def getClients(self):
        """
        Functia returneaza lista de clienti citita din fisier
        :return: lista de clienti (List of Client)
        """
        value = randrange(0, 101)
        if value < self.__crash_probability:
            raise CrashException()
        return super().getClients()

    def addClient(self, client):
        """
        Functia adauga un client in lista de clienti si scrie in fisier lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        value = randrange(0, 101)
        if value < self.__crash_probability:
            raise CrashException()
        return super().addClient(client)

    def removeClient(self, client):
        """
        Functia sterge un client din lista de clienti si scrie in fisier lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        value = randrange(0, 101)
        if value < self.__crash_probability:
            raise CrashException()
        return super().removeClient(client)

    def updateClient(self, client, new_client):
        """
        Functia actualizeaza un client din lista de cheltuieli si scrie in fisier lista de clienti
        :param client: clientul vechi (Client)
        :param new_client: clientul nou (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        value = randrange(0, 101)
        if value < self.__crash_probability:
            raise CrashException()
        return super().updateClient(client, new_client)

class CrashException(Exception):
    pass