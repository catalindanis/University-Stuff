from Domain.Client import Client
from UserInterface.Generals import getUserInput, clearScreen


class ClientMenu:

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul client
        :return:
        """
        text = (""
                "~~~Gestiune lista clienti~~~\n"
                "1.Vizualizare lista clienti\n"
                "2.Adaugare client\n"
                "3.Stergere client\n"
                "4.Actualizare client\n"
                "5.Inapoi")

        print(text + (ClientMenu.__extra_text if ClientMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, clientService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul client
        :param input: input-ul introdus de utilizator
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        options = [ClientMenu.__showClients,
                   ClientMenu.__addClient,
                   ClientMenu.__deleteClient,
                   ClientMenu.__updateClient]

        ClientMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 5:
                raise ValueError

            if input == 5:
                return False

            options[input-1](clientService)
        except ValueError:
            ClientMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def __showClients(clientService):
        """
        Functia preia din service mesajul cu lista de clienti si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clients_formatted_list = clientService.formatClientsList(clientService.getAllClients())

        if clients_formatted_list == "":
            ClientMenu.__extra_text = "\nLista de clienti este goala!"
        else:
            ClientMenu.__extra_text = "\n" + clients_formatted_list

    @staticmethod
    def __addClient(clientService):
        """
        Functia apeleaza adaugarea unui client prin service si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clearScreen()
        name = getUserInput("Introduceti numele clientului: ")
        cnp = getUserInput("Introduceti cnp-ul clientului (3 cifre): ")

        result = clientService.addClient(name, cnp)
        if result == -1:
            ClientMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == 0:
            ClientMenu.__extra_text = "\nClientul exista deja!"
        else:
            ClientMenu.__extra_text = "\nClient adaugat cu succes!"

    @staticmethod
    def __deleteClient(clientService):
        """
        Functia apeleaza stergerea unui client prin service si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clearScreen()
        id = getUserInput("Introduceti id-ul clientului: ")

        result = clientService.deleteClientById(id)
        if result == -1:
            ClientMenu.__extra_text = "\nId-ul este invalid!"
        elif result == 0:
            ClientMenu.__extra_text = "\nClientul cu acest id nu exista!"
        else:
            ClientMenu.__extra_text = "\nClientul a fost sters cu succes!"

    @staticmethod
    def __updateClient(clientService):
        """
        Functia apeleaza actualizarea unei cheltuieli prin service si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clearScreen()
        id = getUserInput("Introduceti id-ul clientului: ")
        name = getUserInput("Introduceti noul nume al clientului: ")
        cnp = getUserInput("Introduceti noul cnp al clientului (3 cifre): ")

        result = clientService.updateClientById(id, name, cnp)
        if result == -3:
            ClientMenu.__extra_text = "\nExista deja un client cu acest cnp!"
        elif result == -2:
            ClientMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == -1:
            ClientMenu.__extra_text = "\nId-ul este invalid!"
        elif result == 0:
            ClientMenu.__extra_text = "\nClientul cu acest id nu exista!"
        else:
            ClientMenu.__extra_text = "\nClientul a fost actualizat cu succes!"

    @staticmethod
    def run(clientService):
        """
        Functia principala a meniului gestionare lista clienti care dirijeaza executia acestuia
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            ClientMenu.__print()
            input = getUserInput()
            if ClientMenu.__handleInput(input, clientService) == False:
                stop_requested = True