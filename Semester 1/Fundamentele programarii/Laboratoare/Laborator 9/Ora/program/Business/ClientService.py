from Domain.Client import ClientValidator, Client


class ClientService:
    """
    Clasa reprezinta service-ul folosit pentru obiectele de tip Client
    """

    def __init__(self, repository):
        """
        Constructorul clasei ClientService
        :param repository: repository-ul folosit pentru clasa Client (ClientRepository)
        """
        self.__repository = repository
        self.__current_id = 0

    def addClient(self, name, cnp):
        """
        Functia realizeaza operatia de adaugare a unui client in lista de clienti prin repository
        :param name: numele clientului (String)
        :param cnp: cnp-ul clientului (String)
        :return: -1 (datele nu sunt valide) / 0 (clientul exista deja) / 1 (operatia s-a efectuat cu succes)
        """
        id = self.__current_id

        if ClientValidator.validateClient(id, name, cnp) == False:
            return -1

        client = Client(id, name, int(cnp))

        if self.alreadyExists(client):
            return 0

        self.__repository.addClient(client)
        self.__current_id += 1
        return 1

    def deleteClientById(self, id):
        """
        Functia sterge un client prin repository dupa id-ul pe care il are
        :param id: id-ul clientului
        :return: -1 (daca id-ul este invalid) / 0 (daca nu exista un client cu acest id) /
                1 (daca operatia s-a efectuat cu succes)
        """
        try:
            id = int(id)
            if id < 0:
                return -1
        except ValueError:
            return -1
        for client in self.__repository.getClients():
            if client.getId() == id:
                self.__repository.removeClient(client)
                return 1

        return 0

    def updateClientById(self, id, name, cnp):
        """
        Functia actualizeaza un client prin repository dupa id-ul pe care il are
        :param id: id-ul clientului
        :param name: numele noului client (String)
        :param cnp: cnp-ul noului client (String)
        :return: -3 (daca exista deja un client cu acelasi cnp)
                -2 (daca datele nu sunt valide) / -1 (daca id-ul este invalid)
                0 (daca nu exista un client cu acest id) / 1 (daca operatia s-a efectuat cu succes)
        """
        try:
            id = int(id)
            if id < 0:
                return -1
        except ValueError:
            return -1

        if ClientValidator.validateClient(id, name, cnp) == False:
            return -2

        for client in self.__repository.getClients():
            if client.getCnp() == cnp and client.getId() != id:
                return -3

        new_client = Client(id, name, cnp)
        for client in self.__repository.getClients():
            if client.getId() == id:
                self.__repository.updateClient(client, new_client)
                return 1

        return 0

    def searchClientsByName(self, name):
        """
        Functia returneaza lista de clienti care contin in nume numele transmis ca parametru
        :param name: numele clientului (String)
        :return: lista de clienti (List of Client) / [] (daca nu exista astfel de clienti / None (datele introduse sunt incorecte)
        """
        if ClientValidator.validateName(name) == False:
            return None

        result = []

        for client in self.__repository.getClients():
            client_name = client.getName().split()

            if name in client_name:
                result.append(client)

        return result

    def searchClientsByCnp(self, cnp):
        """
        Functia returneaza lista de clienti care contin un anumit cnp
        :param cnp: cnp-ul clientului (String)
        :return: lista de clienti (List of Client) / [] (daca nu exista astfel de clienti / None (datele introduse sunt incorecte)
        """
        if ClientValidator.validateCnp(cnp) == False:
            return None

        result = []
        cnp = int(cnp)

        for client in self.__repository.getClients():
            if client.getCnp() == cnp:
                result.append(client)

        return result

    def idExists(self, id):
        """
        Functia returneaza daca exista un client cu un anumit id
        :param id: id-ul clientului (String)
        :return: -1 (id-ul este invalid) / 0 (nu exista un client cu acest id) /
                  1 (exista un client cu acest id)
        """
        try:
            id = int(id)
            for client in self.__repository.getClients():
                if client.getId() == id:
                    return 1
            return 0
        except Exception:
            return -1

    def alreadyExists(self, client):
        """
        Functia verifica daca un client exista deja in lista de clienti
        :param client: clientul cautat
        :return: True (daca exista deja) / False (in caz contrar)
        """
        for current_client in self.__repository.getClients():
            if current_client.getCnp() == client.getCnp():
                return True

        return False


    def getAllClients(self):
        """
        Functia returneaza lista de clienti
        :return: lista de clienti (List of Client)
        """
        return self.__repository.getClients()

    def getClientById(self, client_id):
        """
        Functia returneaza un client dupa id-ul lui
        :param client_id: id-ul clientului (Integer)
        :return: clientul (Client) / None (daca nu exista clientul cu acel id)
        """
        for client in self.getAllClients():
            if client.getId() == client_id:
                return client
        return None

    def formatClientsList(self, clientsList):
        """
        Functia formateaza lista de clienti intr-un mesaj pentru afisare
        :param clientsList: lista de clienti (List of Client)
        :return: mesajul formatat (String)
        """
        message = ""

        for i in range (len(clientsList)):
            message += str(clientsList[i])
            if i != len(clientsList) - 1:
                message += "\n"

        return message