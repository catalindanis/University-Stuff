import datetime


class Vehicle:
    """
    Clasa care defineste tipul automobil
    """

    def __init__(self, id, marca, pret, model, data):
        """
        Constructorul clasei Vehicle
        :param id: id-ul automobilului
        :param marca: marca automobilului
        :param pret: pretul automobilului
        :param model: modelul automobilului
        :param data: data de expirare itp a automobilului
        """
        self.__id = id
        self.__marca = marca
        self.__pret = pret
        self.__model = model
        self.__data = data

    """
    Getteri si setteri pentru proprietatile clasei Vehicle
    """
    def get_id(self):
        return self.__id

    def get_marca(self):
        return self.__marca

    def get_pret(self):
        return self.__pret

    def get_model(self):
        return self.__model

    def get_data(self):
        return self.__data

    def set_id(self, id):
        self.__id = id

    def set_marca(self, marca):
        self.__marca = marca

    def set_pret(self, pret):
        self.__pret = pret

    def set_model(self, model):
        self.__model = model

    def set_data(self, data):
        self.__data = data

    def __eq__(self, other):
        """
        Functia folosita pentru verificarea egalitatii dintre 2 obiecte
        de tip Vehicle
        :param other: obiectul cu care se compara (Vehicle)
        :return: True (daca obiectele sunt egale) / False (in caz contrar)
        """
        if not isinstance(other, Vehicle):
            return False
        return (self.get_id() == other.get_id() and
                self.get_marca() == other.get_marca() and
                self.get_pret() == other.get_pret() and
                self.get_model() == other.get_model() and
                self.get_data() == other.get_data())

    def __str__(self):
        """
        Functia folosita pentru transformarea unui obiect Vehicle in String
        :return: string-ul generat (String)
        """
        return f"{self.get_id()}, {self.get_marca()}, {self.get_pret()}, {self.get_model()}, {self.get_data().strftime('%d:%m:%Y')}"

class VehicleValidator:
    """
    Clasa defineste validatorul unui obiect de tip Vehicle
    """

    def __init__(self):
        """
        Constructorul clasei VehicleValidator
        """

    def validate_id(self, id):
        """
        Functia valideaza un id de automobil
        :param id: id-ul (Integer)
        :return: True (daca id-ul este valid) / False (in caz contrar)
        """
        try:
            id = int(id)
            if id < 0:
                return False
            return True
        except ValueError:
            return False

    def validate_marca(self, marca):
        """
        Functia valideaza o marca de automobil
        :param marca: marca (String)
        :return: True (daca marca este valida) / False (in caz contrar)
        """
        if not isinstance(marca, str):
            return False
        return marca != ""

    def validate_pret(self, pret):
        """
        Functia valideaza un pret de automobil
        :param pret: pretul (Float)
        :return: True (daca pretul este valid) / False (in caz contrar)
        """
        try:
            pret = float(pret)
            if pret <= 0:
                return False
            return True
        except ValueError:
            return False

    def validate_model(self, model):
        """
        Functia valideaza un model de automobil
        :param model: model (String)
        :return: True (daca modelul este valid) / False (in caz contrar)
        """
        if not isinstance(model, str):
            return False
        return model != ""

    def validate_data(self, data):
        """
        Functia valideaza o data de automobil
        :param data: data (String) (zi:luna:an)
        :return: True (daca data este valida) / False (in caz contrar)
        """
        data = data.split(":")
        try:
            datetime.datetime(int(data[2]), int(data[1]), int(data[0]))
            return True
        except Exception:
            return False
