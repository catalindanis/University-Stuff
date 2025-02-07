class Menu():
    """
    Clasa defineste proprietatile pe care le contine un meniu.
    Este folosita de catre mai multe clase copil care reprezinta meniurile utilizate in aplicatie.
    Fiecare clasa copil suprascrie si utilizeaza atributele acestei clase parinte.
    """
    title_message = "~~Cheltuieli de familie~~"
    options_message = ""
    extra_message = ""
    INVALID_VALUE_MESSAGE = "Valoarea introdusa este incorecta!"

    @staticmethod
    def __print():
        """
        Functia reprezinta partea de afisare a meniului
        :return:
        :raise: NotImplementedError (daca nu este implementata de clasa copil)
        """
        raise NotImplementedError

    @staticmethod
    def __handleInput(input, service):
        """
        Functia reprezinta partea de interpretare a input-ului a meniului
        :param input: input-ul introdus de utilizator (String)
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        :raise: NotImplementedError (daca nu este implementata de clasa copil)
        """
        raise NotImplementedError

    @staticmethod
    def run(service):
        """
        Functia principala a clasei care dirijeaza executia acesteia
        :param service: service-ul folosit in aplicatie (ExpensesService)
        :return:
        :raise: NotImplementedError (daca nu este implementata de clase)
        """
        raise NotImplementedError

