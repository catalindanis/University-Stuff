from domeniu.animal import Animal
from exceptii.erori import RepoError


class RepoAnimale:

    def __init__(self):
        self._animale = {}

    def adauga_animal(self,animal):
        id_animal = animal.get_id_animal()
        if id_animal in self._animale:
            raise RepoError("id existent!")
        self._animale[id_animal] = animal

    def update_animal(self,animal):
        id_animal = animal.get_id_animal()
        if id_animal not in self._animale:
            raise RepoError("id inexistent!")
        self._animale[id_animal] = animal


    def cauta_dupa_id(self,id_animal):
        if id_animal not in self._animale:
            raise RepoError("id inexistent!")
        return self._animale[id_animal]

    def sterge_dupa_id(self, id_animal):
        if id_animal not in self._animale:
            raise RepoError("id inexistent!")
        del self._animale[id_animal]


    def get_all(self):
        return list(self._animale.values())

    def __len__(self):
        return len(self._animale)


class FileRepoAnimale(RepoAnimale):

    def __init__(self,cale_fisier):
        super().__init__()
        self.__cale_fisier = cale_fisier

    def __citeste_tot_din_fisier(self):
        with open(self.__cale_fisier,"r") as f:
            self._animale.clear()
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    line = line.split(",")
                    id_animal = int(line[0])
                    nume = line[1]
                    greutate = float(line[2])
                    animal = Animal(id_animal,nume,greutate)
                    self._animale[id_animal] = animal

    def __scrie_tot_in_fisier(self):
        with open(self.__cale_fisier,"w") as f:
            for animal in self._animale:
                f.write(f"{animal.get_id_animal()},{animal.get_nume()},{animal.get_greutate()}\n")

    def adauga_animal(self,animal):
        self.__citeste_tot_din_fisier()
        RepoAnimale.adauga_animal(self,animal)
        self.__scrie_tot_in_fisier()

    def update_animal(self,animal):
        self.__citeste_tot_din_fisier()
        RepoAnimale.update_animal(self,animal)
        self.__scrie_tot_in_fisier()

    def sterge_dupa_id(self, id_animal):
        self.__citeste_tot_din_fisier()
        RepoAnimale.sterge_dupa_id(self,id_animal)
        self.__scrie_tot_in_fisier()

    def cauta_dupa_id(self,id_animal):
        self.__citeste_tot_din_fisier()
        return RepoAnimale.cauta_dupa_id(self,id_animal)

    def get_all(self):
        self.__citeste_tot_din_fisier()
        return RepoAnimale.get_all(self)

    def __len__(self):
        self.__citeste_tot_din_fisier()
        return RepoAnimale.__len__(self)