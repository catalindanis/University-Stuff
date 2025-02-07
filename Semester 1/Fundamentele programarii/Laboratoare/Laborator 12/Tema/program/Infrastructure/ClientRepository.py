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
        self.__readFromFile()

    def __readFromFile(self):
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

    def __loadToFile(self):
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
        self.__readFromFile()
        return self._clients_list

    def addClient(self, client):
        """
        Functia adauga un client in lista de clienti si scrie in fisier lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__readFromFile()
            super().addClient(client)
            self.__loadToFile()
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
            self.__readFromFile()
            super().removeClient(client)
            self.__loadToFile()
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
            self.__readFromFile()
            super().updateClient(client, new_client)
            self.__loadToFile()
            return True
        except Exception:
            return False