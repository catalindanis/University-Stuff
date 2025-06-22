/*
* Interfata ce defineste repository-ul de cheltuieli
*/
#pragma once

#include "expense.h"

typedef struct {
	Expense** expenses;
	int length;
	int capacity;
} ExpensesRepository;

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
ExpensesRepository* createExpensesRepository();

/*
Functia adauga o cheltuiala in repository-ul de cheltuieli
:param expense: referinta cheltuielii (Expense*)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca operatia s-a efectuat cu succes) /
		 false (in caz contrar) (bool)
*/
bool addExpenseInRepository(Expense* expense, ExpensesRepository* repository);

/*
Functia returneaza numarul de elemente din repository-ul de cheltuieli
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: lungimea vectorului de cheltuieli (int)
*/
int getRepositoryLength(ExpensesRepository* repository);

/*
Functia returneaza referinta catre o cheltuiala din repository-ul de cheltuieli
de la indicele specificat
:param index: indicele cheltuielii (int)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: NULL (cheltuiala nu exista) / referinta cheltuialii (in caz contrar) (Expense*)
*/
Expense* getExpenseFromRepositoryByIndex(int index, ExpensesRepository* repository);

/*
Functia actualizeaza o cheltuiala existenta din repository cu o noua cheltuiala
:param index: indicele cheltuielii de actualizat (int)
:param newExpense: referinta catre noua cheltuiala (Expense*)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool updateExpenseFromRepositoryByIndex(int index, Expense* newExpense, ExpensesRepository* repository);

/*
Functia returneaza referinta catre lista de cheltuieli din repository
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: referinta catre lista de cheltuieli (Expense**)
*/
Expense** getExpensesFromRepository(ExpensesRepository* repository);

/*
Functia sterge o cheltuiala de la indicele transmis din repository
:param index: indicele cheltuielii (int)
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool deleteExpenseFromRepositoryByIndex(int index, ExpensesRepository* repository);

/*
Functia dealoca spatiul alocat pentru repository-ul de cheltuieli si pentru cheltuielile din acesta
:param expense: referinta repository-ului (ExpensesRepository*)
*/
void destroyRepository(ExpensesRepository* repository);