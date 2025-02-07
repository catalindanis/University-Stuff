class ClientRepository:
    """
    Clasa reprezinta repository-ul folosit pentru obiectele de tip Client
    """
    def __init__(self):
        """
        Constructorul clasei ClientRepository
        :return:
        """
        self.__clients_list = []

    def getClients(self):
        """
        Functia returneaza lista de clienti
        :return: lista de clienti (List of Client)
        """
        return self.__clients_list

    def addClient(self, client):
        """
        Functia adauga un client in lista de clienti
        :param client: clientul (Client)
        :return: True (daca operatia s-a efectuat) / False (in caz contrar)
        """
        try:
            self.__clients_list.append(client)
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
            self.__clients_list.remove(client)
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
            self.__clients_list[self.__clients_list.index(client)] = new_client
            return True
        except Exception:
            return False