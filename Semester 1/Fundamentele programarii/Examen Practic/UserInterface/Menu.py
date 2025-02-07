class Menu:
    """
    Clasa defineste meniul aplicatiei
    """

    def __init__(self, service):
        """
        Constructorul clasei Menu
        """
        self.__service = service

    def print_menu(self):
        """
        Functia afiseaza optiunile meniului
        """
        print("1.Adauga automobil")
        print("2.Sterge automobil")
        print("3.Filtrare automobil")
        print("4.Undo")
        print("5.Iesire")

    def handle_input(self, input):
        """
        Functia apeleaza o operatie in functie de input-ul introdus de utilizator
        :return: False (daca utilizatorul a selectat optiunea Iesire)
        """
        try:
            if input == "5":
                return False
            if input == "1":
                self.__add_vehicle()
            elif input == "2":
                self.__delete_vehicle()
            elif input == "3":
                self.__filter_vehicle()
            elif input == "4":
                self.__undo()
            else:
                raise Exception("Optiune invalida!")
        except Exception as e:
            print(e)

    def __add_vehicle(self):
        """
        Functia apeleaza prin service operatia de adaugare a unui automobil
        si afiseaza mesajele corespunzatoare
        """
        id = input("Introduceti id-ul: ")
        marca = input("Introduceti marca: ")
        pret = input("Introduceti pret: ")
        model = input("Introduceti model: ")
        data = input("Introduceti data (zi:luna:an): ")
        try:
            self.__service.add_vehicle(id, marca, pret, model, data)
            print("Vehiculul a fost adaugat cu succes!")
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            print(e)

    def __delete_vehicle(self):
        """
        Functia apeleaza prin service operatia de stergere a unui automobil
        si afiseaza mesajele corespunzatoare
        """
        digit = input("Introduceti o cifra: ")
        try:
            number_of_deleted_vehicles = self.__service.delete_vehicle(digit)
            print(f"Au fost sterse {number_of_deleted_vehicles} automobile!")
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            print(e)

    def __filter_vehicle(self):
        """
        Functia apeleaza prin service operatia de filtrare a listei de automobile
        si afiseaza mesajele corespunzatoare
        """
        marca_filter = input("Introduceti filtru pentru marca: ")
        pret_filter = input("Introduceti filtru pentru pret: ")
        try:
            self.__service.filter_vehicles(marca_filter, pret_filter)
            self.print_dictionary(self.__service.get_filtered_vehicles())
        except Exception as e:
            raise e

    def __undo(self):
        """
        Functia apeleaza prin service operatia de undo a ultimei operatii (adaugare / stergere)
        si afiseaza mesajele corespunzatoare
        """
        try:
            self.__service.undo()
        except Exception as e:
            print(e)

    def print_dictionary(self, dictionary):
        if "marca_filter" in dictionary:
            print("Filtru marca: " + dictionary["marca_filter"])
        if "pret_filter" in dictionary:
            print("Filtru pret: " + str((dictionary["pret_filter"])))
        for key, value in dictionary.items():
            if key != "marca_filter" and key != "pret_filter":
                print(f"{value}")

    def run(self):
        """
        Functia principala a clasei Menu care dirijeaza executia acesteia
        """
        exit_requested = False
        while not exit_requested:
            self.print_menu()
            if self.handle_input(input()) == False:
                exit_requested = True
        print("La revedere!")