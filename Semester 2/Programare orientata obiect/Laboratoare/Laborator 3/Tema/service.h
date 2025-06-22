/*
* Interfata ce defineste service-ul de cheltuieli
*/
#pragma once

#include "stdio.h"
#include "stdbool.h"
#include "expense.h"
#include "repository.h"

typedef struct {
	ExpensesRepository* repository;
}ExpensesService;

/*
Functia creeaza un service de cheltuieli
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: service-ul creat (ExpensesService)
*/
ExpensesService createExpensesService(ExpensesRepository* repository);

/*
Functia creeaza si adauga o cheltuiala in service-ul de cheltuieli
:param day: ziua cheltuielii (int) (1 <= day <= 31)
:param sum: suma cheltuielii (double) (0 < sum)
:param type: tipul cheltuielii (int) (1 <= type <= 5)
:param service: service-ul de cheltuieli (ExpensesService*)
:return: 0 (operatia s-a efectuat cu succes)
		 1 (ziua este invalida)
		 2 (suma este invalida)
		 3 (tipul este invalid)
		 4 (eroare la repository) (int)
*/
int addExpenseInService(int day, double sum, int type, ExpensesService* service);

/*
Functia returneaza numarul de cheltuieli din service-ul de cheltuieli
:param service: service-ul de cheltuieli (ExpensesService*)
:return: numarul de cheltuieli din service (int)
*/
int getNumberOfExpenses(ExpensesService* service);

/*
Functia actualizeaza cheltuiala de la indicele transmis din repository cu o noua cheltuiala
:param index: indicele cheltuielii de actualizat (int)
:param day: ziua cheltuielii (int) (1 <= day <= 31)
:param sum: suma cheltuielii (double) (0 < sum)
:param type: tipul cheltuielii (int) (1 <= type <= 5)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: 0 (daca operatia s-a efectuat cu succes)
		 1 (ziua este invalida)
		 2 (suma este invalida)
		 3 (tipul este invalid)
		 4 (nu exista o tranzactie cu acel index) (int)
*/
int updateExpenseFromServiceByIndex(int index, int day, double sum, int type, ExpensesService* service);

/*
Functia returneaza vectorul de cheltuieli din repository-ul de cheltuieli
:param result: vectorul de cheltuieli rezultat (Expense*)
:param resultLength: referinta catre lungimea vectorului de cheltuieli rezultat (int*)
:return: lista de cheltuieli / NULL (daca nu exista cheltuieli)
*/
Expense* getExpensesFromService(int* resultLength, ExpensesService* service);

/*
Functia returneaza referinta catre o cheltuiala din service-ul de cheltuieli
de la indicele specificat
:param index: indicele cheltuielii (int)
:param service: service-ul de cheltuieli (ExpensesService*)
:return: NULL (cheltuiala nu exista) / referinta cheltuialii (in caz contrar) (Expense*)
*/
Expense* getExpenseFromServiceByIndex(int index, ExpensesService* service);

/*
Functia sterge o cheltuiala de la indicele transmis din repository
:param index: indicele cheltuielii (int)
:param repository: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool deleteExpenseFromServiceByIndex(int index, ExpensesService* service);

/*
Functia returneaza lista de cheltuieli filtrata dupa ziua
:param day: ziua pentru filtrare (int) (1 <= day <= 31)
:param result: lista de cheltuieli rezultat (Expense*)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: service-ul aplicatiei (ExpensesService*)
*/
void getExpensesFromServiceFilteredByDay(int day, Expense* result, int* resultLength, ExpensesService* service);

/*
Functia returneaza lista de cheltuieli filtrata dupa suma
:param sum: suma pentru filtrare (double) (0 < sum)
:param result: lista de cheltuieli rezultat (Expense*)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void getExpensesFromServiceFilteredBySum(double sum, Expense* result, int* resultLength, ExpensesService* service);

/*
Functia returneaza lista de cheltuieli filtrata dupa tip
:param type: tipul pentru filtrare (int) (1 <= type <= 5)
:param result: lista de cheltuieli rezultat (Expense*)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void getExpensesFromServiceFilteredByType(int type, Expense* result, int* resultLength, ExpensesService* service);

/*
Functia returneaza o copie a listei de cheltuieli transmise ca parametru
:param newList: lista unde se va stoca copia (Expense*)
:param oldList: lista pentru care se face copia (Expense*)
:param length: lungimea listei (int)
*/
void copyList(Expense* newList, Expense* oldList, int length);

/*
Functia returneaza lista de cheltuieli sortata dupa suma
:param ascending: true (sortare crescatoare) / false (sortare desc)
:param result: lista de cheltuieli rezultat (Expense*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void getExpensesFromServiceSortedBySum(bool ascending, Expense* result, ExpensesService* service);

/*
Functia returneaza lista de cheltuieli sortata dupa tip
:param ascending: true (sortare crescatoare) / false (sortare desc)
:param result: lista de cheltuieli rezultat (Expense*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void getExpensesFromServiceSortedByType(bool ascending, Expense* result, ExpensesService* service);