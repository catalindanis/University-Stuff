from Controller.Test import runTests as run_service_tests
from Domain.Test import runTests as run_domain_tests
from Repository.Test import runTests as run_repo_tests


def test():
    """
    Functia apeleaza metodele de testare pentru fiecare functionalitate
    a aplicatiei
    :return:
    """
    run_repo_tests()
    run_service_tests()
    run_domain_tests()