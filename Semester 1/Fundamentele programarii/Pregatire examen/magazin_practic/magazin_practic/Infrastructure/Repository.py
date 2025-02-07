from Domain.Product import Product


class InMemoryRepository:
    """
    Repository-ul care stocheaza produse in memorie
    """

    def __init__(self):
        """
        Constructorul clasei InMemoryRepository
        """
        self._products = {}

    def add_product(self, product):
        """
        Functia realizeaza adaugarea unui produs in lista de produse
        :param product: produsul (Product)
        :raises: Exception (daca produsul exista deja)
        """
        if product.get_id() not in self._products:
            self._products[product.get_id()] = product
        else: raise Exception("Produsul exista deja!")

    def remove_product_by_id(self, id):
        """
        Functia realizeaza stergerea unui produs din lista de produse dupa id
        :param id: id-ul produsului (Integer)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        if id in self._products:
            del self._products[id]
        else: raise Exception("Produsul nu exista!")

    def update_product_by_id(self, id, denumire, pret):
        """
        Functia realizeaza actualizarea unui produs din lista de produse dupa id
        :param id: id-ul produsului (Integer)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        if id in self._products:
            self._products[id].set_denumire(denumire)
            self._products[id].set_pret(pret)
        else: raise Exception("Produsul nu exista!")

    def get_product_by_id(self, id):
        """
        Functia returneaza un produs din lista de produse dupa id
        :param id: id-ul produsului (Integer)
        :return: produsul (Product)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        if id in self._products:
            return self._products[id]
        else: raise Exception("Produsul nu exista!")

    def get_all_products(self):
        """
        Functia returneaza lista de produse
        :return: lista de produse (Dictionar de (Integer, Product))
        """
        return dict(self._products)

    def set_products_list(self, products_list):
        """
        Functia seteaza lista de produse la lista de produse transmisa ca parametru
        :param products_list: lista de produse
        """
        self._products = products_list

class FileRepository(InMemoryRepository):
    """
    Repository-ul care stocheaza produse in fisier
    Mosteneste clasa parinte InMemoryRepository
    """

    def __init__(self, file_name):
        """
        Constructorul clasei FileRepository
        """
        super().__init__()
        self.__file_name = file_name
        self.__read_from_file()

    def __read_from_file(self):
        """
        Functia care citeste produsele din fisier
        """
        self._products = {}
        with open(self.__file_name, mode='r') as file:
            for line in file.readlines():
                if line != "":
                    data = line.split(',')
                    id = int(data[0].strip())
                    denumire = data[1].strip()
                    pret = float(data[2].strip())
                    super().add_product(Product(id, denumire, pret))

    def __write_to_file(self):
        """
        Functia care scrie produsele din fisier
        """
        with open(self.__file_name, mode='w') as file:
            for id in self._products:
                file.write(str(self._products[id]) + '\n')

    def add_product(self, product):
        """
        Functia realizeaza adaugarea unui produs in lista de produse
        si actualizarea fisierului in mod corespunzator
        :param product: produsul (Product)
        :raises: Exception (daca produsul exista deja)
        """
        self.__read_from_file()
        super().add_product(product)
        self.__write_to_file()

    def remove_product_by_id(self, id):
        """
        Functia realizeaza stergerea unui produs din lista de produse dupa id
        si actualizarea fisierului in mod corespunzator
        :param id: id-ul produsului (Integer)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        self.__read_from_file()
        super().remove_product_by_id(id)
        self.__write_to_file()

    def update_product_by_id(self, id, product):
        """
        Functia realizeaza actualizarea unui produs din lista de produse dupa id
        si actualizarea fisierului in mod corespunzator
        :param id: id-ul produsului (Integer)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        self.__read_from_file()
        super().update_product_by_id(id, product)
        self.__write_to_file()

    def get_product_by_id(self, id):
        """
        Functia returneaza un produs din lista de produse dupa id
        :param id: id-ul produsului (Integer)
        :return: produsul (Product)
        :raises: Exception (daca un produs cu id-ul respectiv nu exista)
        """
        self.__read_from_file()
        return super().get_product_by_id(id)

    def get_all_products(self):
        """
        Functia returneaza lista de produse
        :return: lista de produse (Dictionar de (Integer, Product))
        """
        self.__read_from_file()
        return super().get_all_products()

    def set_products_list(self, products_list):
        """
        Functia seteaza lista de produse la lista de produse transmisa ca parametru
        si actualizeaza fisierul in mod corespunzator
        :param products_list: lista de produse
        """
        super().set_products_list(products_list)
        self.__write_to_file()