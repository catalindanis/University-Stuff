/*
Implementarea metodelor pentru testele aplicatiei
*/

#include "expense.h"
#include <stdlib.h>
#include "repository.h"
#include <string.h>
#include "service.h"
#include "assert.h"

void runDomainTests();
void runRepositoryTests();
void runServiceTests();

/*
Functia ruleaza toate testele aplicatiei si asigura functionalitatea acesteia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runTests() {
	runDomainTests();
	runRepositoryTests();
	runServiceTests();
}

/*
Functia testeaza domeniul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runDomainTests() {
	Expense* expense;
	
	expense = createExpense(10, 150.2, 2);
	
	assert(getExpenseDay(expense) == 10);
	assert(getExpenseSum(expense) == 150.2);
	assert(getExpenseType(expense) == 2);
	assert(getExpenseTypeString(expense) == "Transport");

	setExpenseDay(expense, 5);
	assert(getExpenseDay(expense) == 5);

	setExpenseSum(expense, 45.3);
	assert(getExpenseSum(expense) == 45.3);

	setExpenseType(expense, 4);
	assert(getExpenseType(expense) == 4);
	assert(getExpenseTypeString(expense) == "Imbracaminte");
	
	setExpenseType(expense, 1);
	assert(getExpenseTypeString(expense) == "Mancare");

	setExpenseType(expense, 3);
	assert(getExpenseTypeString(expense) == "Telefon & internet");

	setExpenseType(expense, 5);
	assert(getExpenseTypeString(expense) == "Altele");

	setExpenseType(expense, 7);
	assert(getExpenseTypeString(expense) == "Unknown");

	Expense* e1,* e2;
	e1 = createExpense(5, 100, 2);
	e2 = createExpense(10, 25.2, 3);

	assert(expensesEqual(e1, e2) == false);
	assert(expensesEqual(e1, e1) == true);

	assert(validateExpenseDay(10) == true);
	assert(validateExpenseDay(32) == false);

	assert(validateExpenseSum(100.2) == true);
	assert(validateExpenseSum(-1) == false);
	
	assert(validateExpenseType(4) == true);
	assert(validateExpenseType(0) == false);
	
	assert(expensesEqual(e1, NULL) == false);

	assert(expensesEqual(NULL, NULL) == true);

	assert(getExpenseStringFormat(NULL) == "");

	assert(strcmp(getExpenseStringFormat(e1),
		"Ziua: 5, Suma: 100.00, Tipul: Transport") == 0);

	destroyExpense(expense);
	destroyExpense(e1);
	destroyExpense(e2);
}

/*
Functia testeaza repository-ul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runRepositoryTests() {
	/*ExpensesRepository* repository;
	repository = createExpensesRepository();

	assert(getExpensesFromRepository(repository) == NULL);

	Expense e1, e2, e3;
	e1 = createExpense(5, 100, 2);
	e2 = createExpense(10, 25.2, 3);
	e3 = createExpense(30, 250.25, 4);

	assert(addExpenseInRepository(e1, NULL) == false);
	assert(getRepositoryLength(NULL) == 0);

	assert(addExpenseInRepository(e1, &repository) == true);
	assert(getRepositoryLength(&repository) == 1);

	assert(addExpenseInRepository(e2, &repository) == true);
	assert(getRepositoryLength(&repository) == 2);

	assert(updateExpenseFromRepositoryByIndex(2, e3, &repository) == false);
	assert(updateExpenseFromRepositoryByIndex(1, e3, &repository) == true);

	assert(expensesEqual(getExpenseFromRepositoryByIndex(0, &repository), &e1) == true);
	assert(expensesEqual(getExpenseFromRepositoryByIndex(1, &repository), &e2) == false);
	assert(expensesEqual(getExpenseFromRepositoryByIndex(1, &repository), &e3) == true);

	assert(deleteExpenseFromRepositoryByIndex(0, &repository) == true);
	assert(expensesEqual(getExpenseFromRepositoryByIndex(0, &repository), &e1) == false);
	assert(getExpenseFromRepositoryByIndex(1, &repository) == NULL);
	assert(getRepositoryLength(&repository) == 1);

	assert(deleteExpenseFromRepositoryByIndex(0, &repository) == true);
	assert(getRepositoryLength(&repository) == 0);

	Expense test[100];

	Expense* result;

	e1 = createExpense(5, 100, 2);
	e2 = createExpense(3, 25.2, 3);

	addExpenseInRepository(e1, &repository);
	addExpenseInRepository(e2, &repository);

	result = getExpensesFromRepository(&repository);
	test[0] = e1;
	test[1] = e2;

	for (int i = 0; i < 2; i++)
		assert(expensesEqual(&test[i], &result[i]));*/

}

/*
Functia testeaza service-ul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runServiceTests() {
	/*ExpensesRepository repository;
	repository = createExpensesRepository();

	ExpensesService service;
	service = createExpensesService(&repository);
	int resultLength = 0;

	Expense result[100];

	getExpensesFromServiceFilteredByDay(0, result, &resultLength, &service);
	assert(resultLength == 0);
	getExpensesFromServiceFilteredBySum(0, result, &resultLength, &service);
	assert(resultLength == 0);
	getExpensesFromServiceFilteredByType(0, result, &resultLength, &service);
	assert(resultLength == 0);
	getExpensesFromServiceSortedBySum(true, result, &service);
	assert(resultLength == 0);
	getExpensesFromServiceSortedByType(false, result, &service);
	assert(resultLength == 0);

	assert(addExpenseInService(-1, 100, 2, &service) == 1);
	assert(addExpenseInService(5, 0, 2, &service) == 2);
	assert(addExpenseInService(5, 100, 0, &service) == 3);
	assert(addExpenseInService(5, 100, 2, NULL) == 4);
	assert(addExpenseInService(5, 100, 2, &service) == 0);

	assert(getNumberOfExpenses(&service) == 1);

	Expense e1;
	e1 = createExpense(5, 100, 2);
	assert(expensesEqual(getExpenseFromServiceByIndex(0, &service), &e1) == true);

	assert(updateExpenseFromServiceByIndex(1, 2, 100.2, 3, &service) == 4);
	assert(updateExpenseFromServiceByIndex(0, 32, 100.2, 3, &service) == 1);
	assert(updateExpenseFromServiceByIndex(0, 15, -1, 3, &service) == 2);
	assert(updateExpenseFromServiceByIndex(0, 15, 100.2, 7, &service) == 3);
	assert(updateExpenseFromServiceByIndex(0, 15, 100.2, 3, &service) == 0);
	
	e1 = createExpense(15, 100.2, 3);
	assert(getExpenseFromServiceByIndex(2, &service) == NULL);
	assert(expensesEqual(getExpenseFromServiceByIndex(0, &service), &e1) == true);

	assert(deleteExpenseFromServiceByIndex(1, &service) == false);
	assert(deleteExpenseFromServiceByIndex(0, &service) == true);

	assert(getNumberOfExpenses(&service) == 0);

	addExpenseInService(5, 100, 2, &service);
	addExpenseInService(3, 25.2, 3, &service);
	addExpenseInService(3, 35.4, 3, &service);
	addExpenseInService(5, 50, 1, &service);
	addExpenseInService(8, 35.4, 3, &service);

	Expense e2, e3, e4, e5;
	e1 = createExpense(5, 100, 2);
	e2 = createExpense(3, 25.2, 3);
	e3 = createExpense(3, 35.4, 3);
	e4 = createExpense(5, 50, 1);
	e5 = createExpense(8, 35.4, 3);

	Expense test[100];

	Expense* result1;

	result1 = getExpensesFromService(&resultLength, &service);
	test[0] = e1;
	test[1] = e2;
	test[2] = e3;
	test[3] = e4;
	test[4] = e5;

	for (int i = 0; i < resultLength; i++)
		assert(expensesEqual(&test[i], &result1[i]));
	
	getExpensesFromServiceFilteredByDay(3, result, &resultLength, &service);
	test[0] = e2;
	test[1] = e3;

	for (int i = 0; i < 2; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceFilteredByDay(5, result, &resultLength, &service);
	test[0] = e1;
	test[1] = e4;

	for (int i = 0; i < 2; i++)
		assert(expensesEqual(&test[i], &result[i]));
	
	getExpensesFromServiceFilteredBySum(100, result, &resultLength, &service);
	test[0] = e1;

	for (int i = 0; i < 1; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceFilteredBySum(35.4, result, &resultLength, &service);
	test[0] = e3;
	test[1] = e5;

	for (int i = 0; i < 2; i++)
		assert(expensesEqual(&test[i], &result[i]));
	
	getExpensesFromServiceFilteredByType(3, result, &resultLength, &service);
	test[0] = e2;
	test[1] = e3;
	test[2] = e5;

	for (int i = 0; i < 3; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceFilteredByType(1, result, &resultLength, &service);
	test[0] = e4;

	for (int i = 0; i < 1; i++)
		assert(expensesEqual(&test[i], &result[i]));
	  
	test[0] = e2;
	test[1] = e3;
	test[2] = e5;
	
	copyList(result, test, 3);

	for (int i = 0; i < 3; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceFilteredByDay(0, result, &resultLength, &service);
	assert(resultLength == 0);

	getExpensesFromServiceFilteredBySum(0, result, &resultLength, &service);
	assert(resultLength == 0);

	getExpensesFromServiceFilteredByType(0, result, &resultLength, &service);
	assert(resultLength == 0);

	test[0] = e2;
	test[1] = e3;
	test[2] = e5;
	test[3] = e4;
	test[4] = e1;
	getExpensesFromServiceSortedBySum(true, result, &service);

	for (int i = 0; i < 5; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceSortedBySum(false, result, &service);

	for (int i = 0; i < 5; i++)
		assert(expensesEqual(&test[5 - i - 1], &result[i]));

	test[0] = e4;
	test[1] = e1;
	test[2] = e3;
	test[3] = e2;
	test[4] = e5;
	getExpensesFromServiceSortedByType(true, result, &service);

	for (int i = 0; i < 5; i++)
		assert(expensesEqual(&test[i], &result[i]));

	getExpensesFromServiceSortedByType(false, result, &service);

	test[0] = e4;
	test[1] = e1;
	test[2] = e5;
	test[3] = e3;
	test[4] = e2;
	for (int i = 0; i < 5; i++)
		assert(expensesEqual(&test[5 - i - 1], &result[i]));*/
}