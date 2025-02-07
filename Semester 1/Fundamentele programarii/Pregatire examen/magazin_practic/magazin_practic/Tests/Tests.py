from Business.Service import Service
from Domain.Product import Product
from Infrastructure.Repository import InMemoryRepository, FileRepository


def runTests():
    """
    Functia apeleaza metodele care testeaza aplicatia
    """
    runDomainTests()
    runInfrastructureTests()
    runBusinessTests()

def runDomainTests():
    """
    Functia testeaza domeniul aplicatiei
    """
    product = Product(1, "Lapte", 10)
    assert product.get_id() == 1
    assert product.get_denumire() == "Lapte"
    assert product.get_pret() == 10

    product = Product(2, "Cereale", 6.99)
    assert product.get_id() == 2
    assert product.get_denumire() == "Cereale"
    assert product.get_pret() == 6.99

    assert str(product) == "2, Cereale, 6.99"

def runInfrastructureTests():
    """
    Functia testeaza infrastructura aplicatiei
    """
    repository = InMemoryRepository()

    repository.add_product(Product(1, "Lapte", 10))
    assert repository.get_product_by_id(1) == Product(1, "Lapte", 10)
    assert repository.get_all_products() == {1 : Product(1, "Lapte", 10)}
    try:
        repository.add_product(Product(1, "Lapte", 10))
        assert False
    except Exception:
        assert True
    try:
        repository.remove_product_by_id(2)
        assert False
    except Exception:
        assert True
    try:
        repository.update_product_by_id(2, "Test", 5)
        assert False
    except Exception:
        assert True
    repository.update_product_by_id(1, "Test", 10)
    assert repository.get_product_by_id(1) == Product(1, "Test", 10)
    assert repository.get_all_products() == {1: Product(1, "Test", 10)}
    repository.remove_product_by_id(1)
    assert repository.get_all_products() == {}
    try:
        repository.get_product_by_id(1)
        assert False
    except Exception:
        assert True

    repository = FileRepository("teste.txt")

    assert (repository.get_all_products() ==
            {1 : Product(1, "Lapte", 10),
             2 : Product(2, "Cereale", 6.99)})
    repository.remove_product_by_id(1)
    assert (repository.get_all_products() ==
            {2 : Product(2, "Cereale", 6.99)})
    repository.add_product(Product(1, "Lapte", 10))
    try:
        repository.get_product_by_id(5)
        assert False
    except Exception:
        assert True
    try:
        repository.update_product_by_id(9, "Hartie", 0.99)
        assert False
    except Exception:
        assert True

def runBusinessTests():
    """
    Functia testeaza business-ul aplicatiei
    """
    repository = InMemoryRepository()
    service = Service(repository)

    #service.add_product(Product(1, "Lapte", 10))


