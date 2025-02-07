import os


def clearScreen():
    os.system('cls' if os.name == 'nt' else 'clear')

def getUserInput(*args):
    return input((args[0] if len(args) > 0 else "") + (">>" if len(args) == 0 else ""))