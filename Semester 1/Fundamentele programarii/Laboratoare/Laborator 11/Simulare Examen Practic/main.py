from Application.TicTacToeUI import TicTacToeUI
from Controller.TicTacToeService import TicTacToeService
from Repository.TicTacToeRepository import TicTacToeRepository
from Testing.Tests import Tests

def start():
    """
    Functia principala a aplicatiei
    :return:
    """
    tests = Tests()
    tests.runTests()

    repository = TicTacToeRepository("date.txt")
    service = TicTacToeService(repository)
    ui = TicTacToeUI(service, repository)

    ui.run()

if __name__ == '__main__':
    start()