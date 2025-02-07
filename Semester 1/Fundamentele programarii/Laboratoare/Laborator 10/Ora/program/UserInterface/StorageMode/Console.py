from UserInterface.Generals import clearScreen, getUserInput


class StorageMode():

    __extra_text = ""

    @staticmethod
    def __print():
        """
        Functia afiseaza textul din meniul de selectare al modului de stocare
        :return:
        """
        text = (""
                "~~~Alege modul de stocare~~~\n"
                "1.Fisier\n"
                "2.Memorie\n"
                "3.Iesire")

        print(text + (StorageMode.__extra_text if StorageMode.__extra_text != "" else ""))

    @staticmethod
    def __handleInput(input):
        """
        Functia interpreteaza input-ul introdus de utilizator in meniul principal
        :param input: input-ul introdus de utilizator
        :return:
        """

        StorageMode.__extra_text = ""

        try:
            input = int(input)

            if input < 1 or input > 3:
                raise ValueError

            if input == 3:
                return False

            if input == 1:
                return "files"
            elif input == 2:
                return "memory"
        except ValueError:
            StorageMode.__extra_text = "\nOptiune invalida!"
        return True

    @staticmethod
    def run():
        """
        Functia principala a clasei StorageMode care dirijeaza executia acesteia
        :return:
        """
        stop_requested = False
        while not stop_requested:
            clearScreen()
            StorageMode.__print()
            input = getUserInput()
            result = StorageMode.__handleInput(input)
            if result == False:
                stop_requested = True
            elif result != True:
                return result

        print("La revedere!")
