/*
Implementarea metodelor repository-ului de cheltuieli
*/

#include <stdlib.h>
#include <stdbool.h>
#include "repository.h"

/*
Functia creeaza un repository de cheltuieli
:return: referinta catre repository-ul creat (ExpensesRepository*) /  NULL (daca nu s-a putut crea)
*/
ExpensesRepository* createExpensesRepository() {
	ExpensesRepository* repository = malloc(sizeof(ExpensesRepository));

	if (repository == NULL)
		return NULL;

	repository->capacity = 1;
	repository->length = 0;
	repository->expenses = malloc(repository->capacity * sizeof(Expense*));
	return repository;
}


/*
Functia adauga o cheltuiala in repository-ul de cheltuieli
:param expense: referinta cheltuielii (Expense*)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca operatia s-a efectuat cu succes) /
		 false (in caz contrar) (bool)
*/
bool addExpenseInRepository(Expense* expense, ExpensesRepository* repository) {
	if (repository == NULL || expense == NULL)
		return false;

	repository->length++;
	if (repository->length == repository->capacity) {
		repository->capacity *= 2;
		repository->expenses = realloc(repository->expenses, repository->capacity * sizeof(Expense*));
	}

	repository->expenses[repository->length] = expense;
	return true;
}

/*
Functia returneaza numarul de elemente din repository-ul de cheltuieli
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: lungimea vectorului de cheltuieli (int)
*/
int getRepositoryLength(ExpensesRepository* repository) {
	if (repository == NULL)
		return 0;
	return repository->length;
}

/*
Functia returneaza referinta catre o cheltuiala din repository-ul de cheltuieli
de la indicele specificat
:param index: indicele cheltuielii (int)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: NULL (cheltuiala nu exista) / referinta cheltuialii (in caz contrar) (Expense*)
*/
Expense* getExpenseFromRepositoryByIndex(int index, ExpensesRepository* repository) {
	if (index < 0 || index >= repository->length)
		return NULL;
	return repository->expenses[index];
}

/*
Functia actualizeaza o cheltuiala existenta din repository cu o noua cheltuiala
:param index: indicele cheltuielii de actualizat (int)
:param newExpense: referinta catre noua cheltuiala (Expense*)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool updateExpenseFromRepositoryByIndex(int index, Expense* newExpense, ExpensesRepository* repository) {
	if (index < 0 || index >= repository->length)
		return false;

	repository->expenses[index] = newExpense;
	return true;
}

/*
Functia returneaza referinta catre lista de cheltuieli din repository
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: referinta catre lista de cheltuieli (Expense**)
*/
Expense** getExpensesFromRepository(ExpensesRepository* repository) {
	return repository->expenses;
}

/*
Functia sterge o cheltuiala de la indicele transmis din repository
:param index: indicele cheltuielii (int)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool deleteExpenseFromRepositoryByIndex(int index, ExpensesRepository* repository) {
	int noOfElements = getRepositoryLength(repository);
	if (index < 0 || index >= noOfElements)
		return false;

	for (int i = index; i < noOfElements; i++)
		repository->expenses[i] = repository->expenses[i + 1];
	repository->length--;
	return true;
}

