#include <stdio.h>
#include <stdbool.h>
#include "tests.h"
#include "repository.h"
#include "service.h"
#include "ui.h"
#include "assert.h"

/*
Functia principala a aplicatiei
:return: 0 (daca aplicatia a rulat cu succes) / alta valoare (in caz contrar)
*/
int main() {
	runTests();

	_CrtDumpMemoryLeaks();

	//ExpensesRepository repository;
	//repository = createExpensesRepository();

	//ExpensesService service;
	//service = createExpensesService(&repository);

	//runApplication(&service);

	return 0;
}