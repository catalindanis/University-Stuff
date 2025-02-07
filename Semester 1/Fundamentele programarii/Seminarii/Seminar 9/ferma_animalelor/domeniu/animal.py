class Animal:

    def __init__(self,id_animal,nume,greutate):
        self.__id_animal = id_animal
        self.__nume = nume
        self.__greutate = greutate

    def get_id_animal(self):
        return self.__id_animal

    def get_nume(self):
        return self.__nume

    def get_greutate(self):
        return self.__greutate
