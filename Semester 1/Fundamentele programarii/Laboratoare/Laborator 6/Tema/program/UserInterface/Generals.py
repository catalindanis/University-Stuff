import os

def clearScreen():
    """
    Functia curata consola programului
    :return:
    """
    os.system('cls' if os.name == 'nt' else 'clear')