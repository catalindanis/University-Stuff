from Presentation.MainMenu import *
from Tests.Tests import *

def main():
    """
    Functia principala a programului
    :return:
    """
    expenses = {}
    MainMenu.run(expenses)

if __name__ == '__main__':
    test()
    main()