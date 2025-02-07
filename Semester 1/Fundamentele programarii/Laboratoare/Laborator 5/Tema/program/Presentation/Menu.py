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
        Functia reprezinta partea de afisare.
        Arunca o eroare in cazul in care nu este implementata de clasa copil.
        input  -
        output -
        """
        raise NotImplementedError

    @staticmethod
    def __handleInput(input):
        """
        Functia reprezinta partea de interpretare a input-ului.
        Arunca o eroare in cazul in care nu este implementata de clasa copil.
        input  - datele introduse de utilizator (string)
        output -
        """
        raise NotImplementedError

    @staticmethod
    def run():
        """
        Functia principala a clasei care dirijeaza executia acesteia.
        Arunca o eroare in cazul in care nu este implementata de clasa copil.
        input  -
        output -
        """
        raise NotImplementedError

