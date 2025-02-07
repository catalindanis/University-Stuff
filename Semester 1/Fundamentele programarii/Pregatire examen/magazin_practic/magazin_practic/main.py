from Business.Service import Service
from Infrastructure.Repository import FileRepository
from Tests.Tests import runTests
from UserInterface.Menu import Menu

def main():
    runTests()

    repository = FileRepository("date.txt")
    service = Service(repository)
    menu = Menu(service, repository)
    menu.run()

if __name__ == '__main__':
    main()
