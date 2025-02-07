from UserInterface.Generals import getUserInput, clearScreen


class ClientMenu:

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul cautare client
        :return:
        """
        text = ("~~~Cautare clienti~~~\n"
                "1.Dupa nume\n"
                "2.Dupa cnp\n"
                "3.Inapoi")

        print(text + (ClientMenu.__extra_text if ClientMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, clientService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul cautare client
        :param input: input-ul introdus de utilizator
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        options = [ClientMenu.searchClientByName,
                   ClientMenu.searchClientByCnp]

        ClientMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 5:
                raise ValueError

            if input == 3:
                return False

            options[input-1](clientService)
        except ValueError:
            ClientMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def searchClientByName(clientService):
        """
        Functia apeleaza cautarea unui client dupa nume prin service si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clearScreen()
        name = getUserInput("Introduceti numele clientului: ")

        result = clientService.searchClientsByName(name)

        if result == None:
            ClientMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == []:
            ClientMenu.__extra_text = "\nNu exista astfel de clienti!"
        else:
            clients_formatted_list = clientService.formatClientsList(result)
            ClientMenu.__extra_text = "\n" + clients_formatted_list

    @staticmethod
    def searchClientByCnp(clientService):
        """
        Functia apeleaza cautarea unui client dupa cnp prin service si afiseaza mesajele corespunzatoare
        :param clientService: service-ul folosit pentru clasa Client (ClientService)
        :return:
        """
        clearScreen()
        cnp = getUserInput("Introduceti cnp-ul clientului (3 cifre): ")

        result = clientService.searchClientsByCnp(cnp)

        if result == None:
            ClientMenu.__extra_text = "\nDatele introduse sunt invalide!"
        elif result == []:
            ClientMenu.__extra_text = "\nNu exista astfel de clienti!"
        else:
            clients_formatted_list = clientService.formatClientsList(result)
            ClientMenu.__extra_text = "\n" + clients_formatted_list

    @staticmethod
    def run(clientService):
        """
        Functia principala a meniului cautare clienti care dirijeaza executia acestuia
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