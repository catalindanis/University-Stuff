class Product:
    """
    Clasa ce defineste un produs
    """

    def __init__(self, id, denumire, pret):
        self.__id = id
        self.__denumire = denumire
        self.__pret = pret

    #Getteri si setteri pentru fiecare proprietate
    #a clasei Product
    def get_id(self):
        return self.__id

    def set_id(self, id):
        self.__id = id

    def get_denumire(self):
        return self.__denumire

    def set_denumire(self, denumire):
        self.__denumire = denumire

    def get_pret(self):
        return self.__pret

    def set_pret(self, pret):
        self.__pret = pret

    def __str__(self):
        return f"{self.__id}, {self.__denumire}, {self.__pret}"

    def __eq__(self, other):
        if isinstance(other, Product):
            return self.__id == other.__id and self.__pret == other.__pret and self.__denumire == other.__denumire
        raise Exception("Comparision of diferent data types!")