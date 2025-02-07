class Menu:

    def __init__(self, service, repository):
        """
        Constructorul clasei Menu
        :param service: service-ul folosit in aplicatie (Service)
        :param repository: repository-ul folosit in aplicatie (FileRepository)
        """
        self.__service = service
        self.__repository = repository

    def __print_menu(self):
        """
        Functia tipareste pe ecran meniul aplicatiei
        :return:
        """
        print("1.Adauga produs")
        print("2.Sterge produs")
        print("3.Filtreaza produse")
        print("4.Undo")
        print("5.Iesire")

    def __handle_input(self, input):
        """
        Functia apeleaza operatia aleasa in functie de input-ul utilizatorului
        si tipareste rezultatul
        :param input: input-ul utilizatorului (String)
        :return: False (daca a fost selectata operatia de Iesire)
        """
        try:
            if input == "5":
                return False
            if input == "1":
                self.__add_product()
            elif input == "2":
                self.__remove_product()
            elif input == "3":
                self.__filter_products()
            elif input == "4":
                self.__undo_operation()
            else:
                raise Exception("Optiune invalida!")
        except Exception as e:
            print(e)

    def __add_product(self):
        """
        Functia apeleaza operatia de adaugare a unui produs
        si afiseaza mesajele corespunzatoare
        :return:
        """
        id = input("Introduceti id-ul: ")
        denumire = input("Introduceti denumirea: ")
        pret = input("Introduceti pretul: ")
        try:
            self.__service.add_product(id, denumire, pret)
            print("Produsul a fost adaugat cu succes!")
            self.__display_filtered_products()
        except Exception as e:
            print(e)

    def __remove_product(self):
        """
        Functia apeleaza operatia de stergere a unor produs
        si afiseaza mesajele corespunzatoare
        :return:
        """
        digit = input("Introduceti o cifra: ")

        try:
            number_of_products = self.__service.remove_product(digit)
            print(f"Au fost sterse {number_of_products} produse!")
            self.__display_filtered_products()
        except Exception as e:
            print(e)

    def __filter_products(self):
        """
        Functia apeleaza operatia de filtrare a unor produse
        si afiseaza mesajele corespunzatoare
        :return:
        """
        filtru_denumire = input("Introduceti un filtru pentru denumire: ")
        filtru_pret = input("Introduceti un filtru pentru pret: ")

        try:
            self.__service.filter_products(filtru_denumire, filtru_pret)
            self.__display_filtered_products()
        except Exception as e:
            print(e)


    def __display_filtered_products(self):
        """
        Functia preia lista de produse filtrate din service si o afiseaza
        pe ecran
        :return:
        """
        filters = self.__service.get_filters()
        products_list = ""

        if filters == {}:
            print("Nu exista filtre!")
        else:
            if "filtru_denumire" in filters:
                print("Filtru denumire: " + filters["filtru_denumire"])
            if "filtru_pret" in filters:
                print("Filtru pret: " + str(filters["filtru_pret"]))

        for key, value in self.__service.get_filtered_products().items():
            products_list += str(value) + "\n"

        print(products_list)

    def __undo_operation(self):
        """
        Functia apeleaza operatia de undo a ultimei operatii de stergere
        si afiseaza mesajele corespunzatoare
        :return:
        """
        try:
            self.__service.undo()
            self.__display_filtered_products()
        except Exception as e:
            print(e)

    def run(self):
        """
        Functia principala a clasei Menu care dirijeaza executia acesteia
        """
        stop_requested = False
        while not stop_requested:
            self.__print_menu()
            user_input = input(">>")
            if self.__handle_input(user_input) == False:
                stop_requested = True
