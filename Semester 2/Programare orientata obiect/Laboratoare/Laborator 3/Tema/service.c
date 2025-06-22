///*
//Implementarea metodelor service-ului de cheltuieli
//*/
//
//#include "stdio.h"
//#include "stdbool.h"
//#include <stdlib.h>
//#include "expense.h"
//#include "repository.h"
//#include "service.h"
//
///*
//Functia creeaza un service de cheltuieli
//:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
//:return: service-ul creat (ExpensesService)
//*/
//ExpensesService createExpensesService(ExpensesRepository* repository) {
//	ExpensesService service;
//
//	service.repository = repository;
//	return service;
//}
//
///*
//Functia creeaza si adauga o cheltuiala in repository-ul de cheltuieli
//:param day: ziua cheltuielii (int) (1 <= day <= 31)
//:param sum: suma cheltuielii (double) (0 < sum)
//:param type: tipul cheltuielii (int) (1 <= type <= 5)
//:param service: service-ul de cheltuieli (ExpensesService*)
//:return: 0 (operatia s-a efectuat cu succes)
//		 1 (ziua este invalida)
//		 2 (suma este invalida)
//		 3 (tipul este invalid) 
//		 4 (eroare la repository / service) (int)
//*/
//int addExpenseInService(int day, double sum, int type, ExpensesService* service) {
//	if (!validateExpenseDay(day))
//		return 1;
//	if (!validateExpenseSum(sum))
//		return 2;
//	if (!validateExpenseType(type))
//		return 3;
//	
//	Expense* expense;
//	expense = createExpense(day, sum, type);
//
//	if (service == NULL || service->repository == NULL || 
//		addExpenseInRepository(*expense, service->repository) == false)
//		return 4;
//
//	return 0;
//}
//
///*
//Functia returneaza numarul de cheltuieli din repository-ul de cheltuieli
//:param service: service-ul de cheltuieli (ExpensesService*)
//:return: numarul de cheltuieli din service (int)
//*/
//int getNumberOfExpenses(ExpensesService* service) {
//	return getRepositoryLength(service->repository);
//}
//
///*
//Functia actualizeaza cheltuiala de la indicele transmis din repository cu o noua cheltuiala
//:param index: indicele cheltuielii de actualizat (int)
//:param day: ziua cheltuielii (int) (1 <= day <= 31)
//:param sum: suma cheltuielii (double) (0 < sum)
//:param type: tipul cheltuielii (int) (1 <= type <= 5)
//:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
//:return: 0 (daca operatia s-a efectuat cu succes)
//		 1 (ziua este invalida)
//		 2 (suma este invalida)
//		 3 (tipul este invalid)
//		 4 (nu exista o tranzactie cu acel index) (int)
//*/
//int updateExpenseFromServiceByIndex(int index, int day, double sum, int type, ExpensesService* service) {
//	if (!validateExpenseDay(day))
//		return 1;
//	if (!validateExpenseSum(sum))
//		return 2;
//	if (!validateExpenseType(type))
//		return 3;
//	
//	Expense* newExpense;
//	newExpense = createExpense(day, sum, type);
//
//	if (updateExpenseFromRepositoryByIndex(index, *newExpense, service->repository) == false)
//		return 4;
//
//	return 0;
//}
//
///*
//Functia returneaza vectorul de cheltuieli din repository-ul de cheltuieli
//:param result: vectorul de cheltuieli rezultat (Expense*)
//:param resultLength: referinta catre lungimea vectorului de cheltuieli rezultat (int*)
//:param service: service-ul de cheltuieli (ExpensesService*)
//*/
//Expense* getExpensesFromService(int* resultLength, ExpensesService* service) {
//	*resultLength = getNumberOfExpenses(service);
//	return getExpensesFromRepository(service->repository);	
//}
//
///*
//Functia returneaza referinta catre o cheltuiala din repository-ul de cheltuieli
//de la indicele specificat
//:param index: indicele cheltuielii (int)
//:param service: service-ul de cheltuieli (ExpensesService*)
//:return: NULL (cheltuiala nu exista) / referinta cheltuialii (in caz contrar) (Expense*)
//*/
//Expense* getExpenseFromServiceByIndex(int index, ExpensesService* service) {
//	return getExpenseFromRepositoryByIndex(index, service->repository);
//}
//
///*
//Functia sterge o cheltuiala de la indicele transmis din repository
//:param index: indicele cheltuielii (int)
//:param repository: referinta catre service-ul de cheltuieli (ExpensesService*)
//:return: true (daca cheltuiala exista) /
//		 false (in caz contrar)
//*/
//bool deleteExpenseFromServiceByIndex(int index, ExpensesService* service) {
//	return deleteExpenseFromRepositoryByIndex(index, service->repository);
//}
//
///*
//Functia returneaza o copie a listei de cheltuieli transmise ca parametru
//:param newList: lista unde se va stoca copia (Expense*)
//:param oldList: lista pentru care se face copia (Expense*)
//:param length: lungimea listei (int)
//*/
//void copyList(Expense newList[], Expense oldList[], int length) {
//	for (int i = 0; i < length; i++)
//		newList[i] = oldList[i];
//}
//
///*
//Functia returneaza lista de cheltuieli filtrata dupa ziua
//:param day: ziua pentru filtrare (int) (1 <= day <= 31)
//:param result: lista de cheltuieli rezultat (Expense*)
//:param resultLength: lungimea listei de cheltuieli rezultat (int*)
//:param service: service-ul aplicatiei (ExpensesService*)
//*/
//void getExpensesFromServiceFilteredByDay(int day, Expense* result, int* resultLength, ExpensesService* service) {
//	if (validateExpenseDay(day) == false || *resultLength == 0) {
//		*resultLength = 0;
//		return;
//	}
//
//	copyList(result, getExpensesFromService(resultLength, service), *resultLength);
//
//	for (int i = 0; i < *resultLength; i++)
//		if (result[i].day != day) {
//			for (int j = i; j < *resultLength - 1; j++)
//				result[j] = result[j + 1];
//			(*resultLength)--;
//			i--;
//		}
//}
//
///*
//Functia returneaza lista de cheltuieli filtrata dupa suma
//:param sum: suma pentru filtrare (double) (0 < sum)
//:param result: lista de cheltuieli rezultat (Expense*)
//:param resultLength: lungimea listei de cheltuieli rezultat (int*)
//:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
//*/
//void getExpensesFromServiceFilteredBySum(double sum, Expense* result, int* resultLength, ExpensesService* service) {
//	if (validateExpenseSum(sum) == false || *resultLength == 0) {
//		*resultLength = 0;
//		return;
//	}
//
//	copyList(result, getExpensesFromService(resultLength, service), *resultLength);
//
//	for (int i = 0; i < *resultLength; i++)
//		if (result[i].sum != sum) {
//			for (int j = i; j < *resultLength - 1; j++)
//				result[j] = result[j + 1];
//			(*resultLength)--;
//			i--;
//		}
//}
//
///*
//Functia returneaza lista de cheltuieli filtrata dupa tip
//:param type: tipul pentru filtrare (int) (1 <= type <= 5)
//:param result: lista de cheltuieli rezultat (Expense*)
//:param resultLength: lungimea listei de cheltuieli rezultat (int*)
//:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
//*/
//void getExpensesFromServiceFilteredByType(int type,Expense* result,  int* resultLength, ExpensesService* service) {
//	if (validateExpenseType(type) == false || *resultLength == 0) {
//		*resultLength = 0;
//		return;
//	}
//
//	copyList(result, getExpensesFromService(resultLength, service), *resultLength);
//
//	for (int i = 0; i < *resultLength; i++)
//		if (result[i].type != type) {
//			for (int j = i; j < *resultLength - 1; j++)
//				result[j] = result[j + 1];
//			(*resultLength)--;
//			i--;
//		}
//}
//
//
///*
//Functia returneaza lista de cheltuieli sortata dupa suma
//:param ascending: true (sortare crescatoare) / false (sortare desc)
//:param result: lista de cheltuieli rezultat (Expense*)
//:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
//*/
//void getExpensesFromServiceSortedBySum(bool ascending, Expense* result, ExpensesService* service) {
//	int length = 0;
//
//	copyList(result, getExpensesFromService(&length, service), length);
//
//	if (length == 0) {
//		result = NULL;
//		return;
//	}
//
//	Expense aux;
//
//	if (ascending) {
//		for (int i = 0; i < length; i++)
//			for (int j = i + 1; j < length; j++)
//				if (result[i].sum > result[j].sum) {
//					aux = result[i];
//					result[i] = result[j];
//					result[j] = aux;
//				}
//	}
//	else {
//		for (int i = 0; i < length; i++)
//			for (int j = i + 1; j < length; j++)
//				if (result[i].sum <= result[j].sum) {
//					aux = result[i];
//					result[i] = result[j];
//					result[j] = aux;
//				}
//	}
//}
//
///*
//Functia returneaza lista de cheltuieli sortata dupa tip
//:param ascending: true (sortare crescatoare) / false (sortare desc)
//:param result: lista de cheltuieli rezultat (Expense*)
//:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
//*/
//void getExpensesFromServiceSortedByType(bool ascending, Expense* result, ExpensesService* service) {
//	int length = 0;
//	
//	copyList(result, getExpensesFromService(&length, service), length);
//
//	if (length == 0) {
//		result = NULL;
//		return;
//	}
//
//	Expense aux;
//
//	if (ascending) {
//		for (int i = 0; i < length; i++)
//			for (int j = i + 1; j < length; j++)
//				if (result[i].type > result[j].type) {
//					aux = result[i];
//					result[i] = result[j];
//					result[j] = aux;
//				}
//	}
//	else {
//		for (int i = 0; i < length; i++)
//			for (int j = i + 1; j < length; j++)
//				if (result[i].type < result[j].type) {
//					aux = result[i];
//					result[i] = result[j];
//					result[j] = aux;
//				}
//	}
//}
//
//
