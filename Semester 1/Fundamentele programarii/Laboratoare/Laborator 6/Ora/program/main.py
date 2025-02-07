from Controller.Service import ExpensesService
from Repository.Repository import ExpensesRepository
from UserInterface.Clasic.MainMenu import *
from Tests.Tests import *
from UserInterface.MenuSelector import MenuSelector


def main():
    """
    Functia principala a programului
    :return:
    """

    repo = ExpensesRepository()
    service = ExpensesService(repo)

    MenuSelector.run(service)

if __name__ == '__main__':
    test()
    main()