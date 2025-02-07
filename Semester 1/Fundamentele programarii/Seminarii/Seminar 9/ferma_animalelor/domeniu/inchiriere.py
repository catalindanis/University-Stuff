class Inchiriere:

    def __init__(self,id_inchiriere,animal,client,pret):
        self.__id_inchiriere = id_inchiriere
        self.__animal = animal
        self.__client = client
        self.__pret = pret

    def get_id_inchiriere(self):
        return self.__id_inchiriere

    def get_animal(self):
        return self.__animal

    def get_client(self):
        return self.__client

    def get_pret(self):
        return self.__pret
