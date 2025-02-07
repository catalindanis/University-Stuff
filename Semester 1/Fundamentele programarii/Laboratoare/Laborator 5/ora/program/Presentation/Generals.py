import os

def clearScreen():
    """
    Functia curata consola programului
    input  -
    output -
    """
    os.system('cls' if os.name == 'nt' else 'clear')