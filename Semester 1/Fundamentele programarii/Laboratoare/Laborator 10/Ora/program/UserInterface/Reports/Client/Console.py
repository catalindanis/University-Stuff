from Infrastructure.ClientRepository import CrashException
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
                "~~~Rapoarte pentru clienti~~~\n"
                "1.Ordonare dupa nume\n"
                "2.Ordonare dupa numarul de filme inchiriate\n"
                "3.Primii 30% clienti cu cele mai multe filme inchiriate\n"
                "4.Inapoi")

        print(text + (ClientMenu.__extra_text if ClientMenu.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input, rentService):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul client
        :param input: input-ul introdus de utilizator
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        options = [ClientMenu.__sortClientsByName,
                   ClientMenu.__sortClientsByNumberOfMoviesRented,
                   ClientMenu.__top30PercentClients]

        ClientMenu.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 4:
                raise ValueError

            if input == 4:
                return False

            try:
                options[input-1](rentService)
            except CrashException:
                ClientMenu.__extra_text = "\nEroare la repository!"
        except ValueError:
            ClientMenu.__extra_text = "\nOptiune invalida!"

    @staticmethod
    def __sortClientsByName(rentService):
        """
        Functia preia din service mesajul cu lista de clienti ordonati dupa nume
        si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        sortedDict = rentService.sortClientsByName()

        clients_formatted_dict = rentService.formatClientsDict(sortedDict)

        if clients_formatted_dict == "":
            ClientMenu.__extra_text = "\nLista de inchirieri este goala!"
        else:
            ClientMenu.__extra_text = "\n" + clients_formatted_dict

    @staticmethod
    def __sortClientsByNumberOfMoviesRented(rentService):
        """
        Functia preia din service mesajul cu lista de clienti ordonati dupa numarul
        de filme inchiriate si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        sortedDict = rentService.sortClientsByNumberOfMoviesRented()

        clients_formatted_dict = rentService.formatClientsDict(sortedDict)

        if clients_formatted_dict == "":
            ClientMenu.__extra_text = "\nLista de inchirieri este goala!"
        else:
            ClientMenu.__extra_text = "\n" + clients_formatted_dict

    @staticmethod
    def __top30PercentClients(rentService):
        """
        Functia preia din service mesajul cu primii 30% clienti ordonati dupa numarul de
        filme inchiriate si afiseaza mesajele corespunzatoare
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        sortedDict = rentService.top30PercentClients()

        clients_formatted_dict = rentService.formatClientsDictShort(sortedDict)

        if clients_formatted_dict == "":
            ClientMenu.__extra_text = "\nLista de inchirieri este goala!"
        else:
            ClientMenu.__extra_text = "\n" + clients_formatted_dict

    @staticmethod
    def run(rentService):
        """
        Functia principala a meniului rapoarte pentru clienti care dirijeaza executia acestuia
        :param rentService: service-ul folosit pentru inchirieri
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            ClientMenu.__print()
            input = getUserInput()
            if ClientMenu.__handleInput(input, rentService) == False:
                stop_requested = True