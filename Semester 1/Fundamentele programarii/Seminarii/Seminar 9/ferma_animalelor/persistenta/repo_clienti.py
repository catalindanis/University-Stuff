from domeniu.client import Client

class RepoCliente:

    def __init__(self):
        self._clienti = {}

    def adauga_client(self, client):
        id_client = client.get_id_client()
        if id_client in self._clienti:
            raise RepoError("id existent!")
        self._clienti[id_client] = client

    def update_client(self,client):
        id_client = client.get_id_client()
        if id_client not in self._clienti:
            raise RepoError("id inexistent!")
        self._clienti[id_client] = client


    def cauta_dupa_id(self,id_client):
        if id_client not in self._clienti:
            raise RepoError("id inexistent!")
        return self._clienti[id_client]

    def sterge_dupa_id(self, id_client):
        if id_client not in self._clienti:
            raise RepoError("id inexistent!")
        del self._clienti[id_client]


    def get_all(self):
        return list(self._clienti.values())

    def __len__(self):
        return len(self._clienti)


class FileRepoCliente(RepoCliente):

    def __init__(self,cale_fisier):
        super().__init__()
        self.__cale_fisier = cale_fisier

    def __citeste_tot_din_fisier(self):
        with open(self.__cale_fisier,"r") as f:
            self._clienti.clear()
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    line = line.split(",")
                    id_client = int(line[0])
                    nume = line[1]
                    client = Client(id_client,nume)
                    self._clienti[id_client] = client

    def __scrie_tot_in_fisier(self):
        with open(self.__cale_fisier,"w") as f:
            for client in self._clienti:
                f.write(f"{client.get_id_client()},{client.get_nume()}\n")

    def adauga_client(self, client):
        self.__citeste_tot_din_fisier()
        RepoCliente.adauga_client(self, client)
        self.__scrie_tot_in_fisier()

    def update_client(self,client):
        self.__citeste_tot_din_fisier()
        RepoCliente.update_client(self, client)
        self.__scrie_tot_in_fisier()

    def sterge_dupa_id(self, id_client):
        self.__citeste_tot_din_fisier()
        RepoCliente.sterge_dupa_id(self, id_client)
        self.__scrie_tot_in_fisier()

    def cauta_dupa_id(self,id_client):
        self.__citeste_tot_din_fisier()
        return RepoCliente.cauta_dupa_id(self, id_client)

    def get_all(self):
        self.__citeste_tot_din_fisier()
        return RepoCliente.get_all(self)

    def __len__(self):
        self.__citeste_tot_din_fisier()
        return RepoCliente.__len__(self)