#include <stdio.h>
#include <crtdbg.h>
#include <stdbool.h>
#include "tests.h"
#include "repository.h"
#include "service.h"
#include "ui.h"

/*
Functia principala a aplicatiei
:return: 0 (daca aplicatia a rulat cu succes) / alta valoare (in caz contrar)
*/
int main() {
	runTests();

	ExpensesRepository* repository;
	repository = createExpensesRepository();

	ExpensesService* service;
	service = createExpensesService(repository);

	runApplication(service);

	_CrtDumpMemoryLeaks();
	return 0;
}