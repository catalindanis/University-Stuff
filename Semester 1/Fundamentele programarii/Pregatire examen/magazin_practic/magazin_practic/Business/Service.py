from Domain.Product import Product


class Service:

    def __init__(self, repository):
        """
        Constructorul clasei Service
        :param repository: repository-ul folosit in aplicatie (FileRepository)
        """
        self.__repository = repository
        self.__history_products_lists = []
        self.__filtru_pret = -1
        self.__filtru_denumire = ""

    def add_product(self, id, denumire, pret):
        """
        Functia realizeaza operatia de adaugare a unui produs
        cu id-ul id, denumirea denumire si pretul pret
        :param id: id-ul produsului (Integer)
        :param denumire: denumire (String)
        :param pret: pret (Float)
        :raises: Exception (daca parametrii transmisi nu sunt valizi /
                operatia nu s-a putut efectua)
        """
        errors = []
        try:
            id = int(id)
            if id < 0:
                raise ValueError()
        except ValueError:
            errors.append("Id-ul trebuie sa fie un numar natural!")
        try:
            if denumire == "":
                raise ValueError()
        except ValueError:
            errors.append("Denumirea nu poate fi goala!")
        try:
            pret = float(pret)
            if pret <= 0:
                raise ValueError()
        except ValueError:
            errors.append("Pretul trebuie sa fie un numar real > 0!")
        if len(errors) > 0:
            raise Exception("\n".join(errors))

        self.__repository.add_product(Product(id, denumire, pret))


    def remove_product(self, digit):
        """
        Functia realizeaza operatia de stergere a produselor
        ce contin in id cifra transmisa
        :param digit: cifra (Integer)
        """
        try:
            digit = int(digit)
            if digit < 0 or digit > 9:
                raise ValueError()
        except ValueError as e:
            raise Exception("Datele introduse sunt invalide!")

        self.__history_products_lists.append(self.__repository.get_all_products())
        number_of_products_deleted = 0
        for id in self.__repository.get_all_products():
            if str(digit) in str(id):
                self.__repository.remove_product_by_id(id)
                number_of_products_deleted += 1

        return number_of_products_deleted

    def filter_products(self, filtru_denumire, filtru_pret):
        """
        Functia realizeaza operatia de filtrare a unuor produse. Sunt incluse
        produsele care in denumire contin textul filtru_denumire si au pretul mai mic decat
        pretul filtru_pret
        :param filtru_denumire: filtru text (String)
        :param filtru_pret: filtru pret (Float)
        :raises: Exception (daca parametrii transmisi nu sunt valizi)
        """
        errors = []
        try:
            filtru_pret = float(filtru_pret)
            if filtru_pret <= 0 and filtru_pret != -1:
                raise ValueError()
        except ValueError:
            raise Exception("Filtrul de pret trebuie sa fie un numar real > 0!")

        self.__filtru_denumire = filtru_denumire
        self.__filtru_pret = filtru_pret

    def get_filters(self):
        """
        Functia returneaza filtrele aplicate pe lista de produse
        :return: lista de filtre (Dictionary)
        """
        filters = {}
        if self.__filtru_denumire != "":
            filters["filtru_denumire"] = self.__filtru_denumire
        if self.__filtru_pret != -1:
            filters["filtru_pret"] = self.__filtru_pret
        return filters

    def get_filtered_products(self):
        """
        Functia returneaza lista de produse cu filtrele aplicate pe ea
        :return: lista de produse (Dictionary of Product Objects)
        """
        if self.__filtru_denumire == "" and self.__filtru_pret == -1:
            return self.__repository.get_all_products()
        first_filter = {}
        if self.__filtru_denumire != "":
            for key, value in self.__repository.get_all_products().items():
                if self.__filtru_denumire.lower() in value.get_denumire().lower():
                    first_filter[key] = value
        else:
            first_filter = self.__repository.get_all_products()
        second_filter = {}
        if self.__filtru_pret != -1:
            for key, value in first_filter.items():
               if self.__filtru_pret > value.get_pret():
                    second_filter[key] = value
        else:
            return first_filter

        return second_filter

    def undo(self):
        """
        Functia realizeaza operatia de undo a ultimei operatii de stergere
        :raises: Exception (Daca nu exista operatii de stergeri in spate)
        """
        if len(self.__history_products_lists) == 0:
            raise Exception("Nu exista operatii de stergere efectuate!")
        self.__repository.set_products_list(self.__history_products_lists.pop())


