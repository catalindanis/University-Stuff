from Controller.Service import ExpensesService
from Repository.Repository import ExpensesRepository
from UserInterface.MainMenu import *
from Tests.Tests import *

def main():
    """
    Functia principala a programului
    :return:
    """

    repo = ExpensesRepository()
    service = ExpensesService(repo)

    MainMenu.run(service)

if __name__ == '__main__':
    test()
    main()