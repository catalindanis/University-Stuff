/*
Implementarea metodelor service-ului de cheltuieli
*/

#include "stdio.h"
#include "stdbool.h"
#include <stdlib.h>
#include "expense.h"
#include "repository.h"
#include "service.h"

/*
Functia creeaza un service de cheltuieli
:param repository: referinta catre repository-ul de cheltuieli (ExpensesRepository*)
:return: referinta catre service-ul creat (ExpensesService*) / NULL (daca nu s-a putut crea)
*/
ExpensesService* createExpensesService(ExpensesRepository* repository) {
	ExpensesService* service = malloc(sizeof(ExpensesRepository));

	if (service != NULL && repository != NULL) {
		service->repository = repository;
	}
	return service;
}

/*
Functia creeaza si adauga o cheltuiala in repository-ul de cheltuieli
:param day: ziua cheltuielii (int) (1 <= day <= 31)
:param sum: suma cheltuielii (double) (0 < sum)
:param type: tipul cheltuielii (int) (1 <= type <= 5)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: 0 (operatia s-a efectuat cu succes)
		 1 (ziua este invalida)
		 2 (suma este invalida)
		 3 (tipul este invalid) 
		 4 (eroare la repository / service) (int)
*/
int addExpenseInService(int day, double sum, int type, ExpensesService* service) {
	if (!validateExpenseDay(day))
		return 1;
	if (!validateExpenseSum(sum))
		return 2;
	if (!validateExpenseType(type))
		return 3;
	
	Expense* expense;
	expense = createExpense(day, sum, type);

	if (service == NULL || service->repository == NULL ||
		addExpenseInRepository(expense, service->repository) == false) {
		destroyExpense(expense);
		return 4;
	}

	return 0;
}

/*
Functia returneaza numarul de cheltuieli din repository-ul de cheltuieli
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: numarul de cheltuieli din service (int)
*/
int getNumberOfExpenses(ExpensesService* service) {
	return getRepositoryLength(service->repository);
}

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
		 4 (nu exista o tranzactie cu acel index)
		 5 (eroare la repository / service) (int)
*/
int updateExpenseFromServiceByIndex(int index, int day, double sum, int type, ExpensesService* service) {
	if (!validateExpenseDay(day))
		return 1;
	if (!validateExpenseSum(sum))
		return 2;
	if (!validateExpenseType(type))
		return 3;
	
	Expense* newExpense;
	newExpense = createExpense(day, sum, type);

	if (service == NULL || service->repository == NULL || newExpense == NULL) {
		destroyExpense(newExpense);
		return 5;
	}

	if (updateExpenseFromRepositoryByIndex(index, newExpense, service->repository) == false) {
		destroyExpense(newExpense);
		return 4;
	}

	return 0;
}

/*
Functia returneaza vectorul de cheltuieli din repository-ul de cheltuieli
:param resultLength: referinta catre lungimea vectorului de cheltuieli rezultat (int*)
:param service: service-ul de cheltuieli (ExpensesService*)
:return: lista de cheltuieli din repository (Expenses**) / NULL (daca nu exista cheltuieli)
*/
Expense** getExpensesFromService(int* resultLength, ExpensesService* service) {
	*resultLength = getNumberOfExpenses(service);
	if (*resultLength == 0)
		return NULL;
	return getExpensesFromRepository(service->repository);	
}

/*
Functia returneaza referinta catre o cheltuiala din repository-ul de cheltuieli
de la indicele specificat
:param index: indicele cheltuielii (int)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: NULL (cheltuiala nu exista) / referinta cheltuialii (in caz contrar) (Expense*)
*/
Expense* getExpenseFromServiceByIndex(int index, ExpensesService* service) {
	return getExpenseFromRepositoryByIndex(index, service->repository);
}

/*
Functia sterge o cheltuiala de la indicele transmis din repository
:param index: indicele cheltuielii (int)
:param repository: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: true (daca cheltuiala exista) /
		 false (in caz contrar)
*/
bool deleteExpenseFromServiceByIndex(int index, ExpensesService* service) {
	return deleteExpenseFromRepositoryByIndex(index, service->repository);
}

/*
Functia returneaza o copie a listei de cheltuieli transmise ca parametru
:param oldList: lista pentru care se face copia (Expense*)
:param length: lungimea listei de copiat (int)
:return: copia listei (Expenses**) / NULL (daca nu s-a putut copia lista / lista e goala)
*/
Expense** copyList(Expense** oldList, int length) {
	if (length == 0)
		return NULL;

	Expense** newList = malloc(length * sizeof(Expense*));
	
	if (newList != NULL) {

		for (int i = 0; i < length; i++) {
			Expense* element = createExpense(oldList[i]->day, oldList[i]->sum, oldList[i]->type);

			if (element != NULL) {
				newList[i] = element;
			}
		}
	}

	return newList;
}

/*
Functia returneaza lista de cheltuieli filtrata dupa ziua
:param day: ziua pentru filtrare (int) (1 <= day <= 31)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: service-ul aplicatiei (ExpensesService*)
:return: lista de cheltuieli rezultat (Expense**) / NULL (ziua este invalida sau eroare la service)
*/
Expense** getExpensesFromServiceFilteredByDay(int day, int* resultLength, ExpensesService* service) {
	if (validateExpenseDay(day) == false || service == NULL) {
		*resultLength = 0;
		return NULL;
	}

	Expense** result = copyList(getExpensesFromService(resultLength, service), *resultLength);

	for (int i = 0; i < *resultLength; i++)
		if (result[i]->day != day) {
			destroyExpense(result[i]);
			for (int j = i; j < *resultLength - 1; j++)
				result[j] = result[j + 1];
			(*resultLength)--;
			i--;
		}

	if (*resultLength == 0) {
		free(result);
		return NULL;
	}

	return result;
}

/*
Functia returneaza lista de cheltuieli filtrata dupa suma
:param suma: suma pentru filtrare (double) (0 < sum)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: service-ul aplicatiei (ExpensesService*)
:return: lista de cheltuieli rezultat (Expense**) / NULL (suma este invalida sau eroare la service)
*/
Expense** getExpensesFromServiceFilteredBySum(double sum, int* resultLength, ExpensesService* service) {
	if (validateExpenseSum(sum) == false || service == NULL) {
		*resultLength = 0;
		return NULL;
	}

	Expense** result = copyList(getExpensesFromService(resultLength, service), *resultLength);

	for (int i = 0; i < *resultLength; i++)
		if (result[i]->sum != sum) {
			destroyExpense(result[i]);
			for (int j = i; j < *resultLength - 1; j++)
				result[j] = result[j + 1];
			(*resultLength)--;
			i--;
		}
	if (*resultLength == 0) {
		free(result);
		return NULL;
	}

	return result;
}

/*
Functia returneaza lista de cheltuieli filtrata dupa tip
:param type: tipul pentru filtrare (int) (1 <= type <= 5)
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: service-ul aplicatiei (ExpensesService*)
:return: lista de cheltuieli rezultat (Expense**) / NULL (ziua este invalida sau eroare la service)
*/
Expense** getExpensesFromServiceFilteredByType(int type,  int* resultLength, ExpensesService* service) {
	if (validateExpenseType(type) == false || service == NULL) {
		*resultLength = 0;
		return NULL;
	}

	Expense** result = copyList(getExpensesFromService(resultLength, service), *resultLength);

	for (int i = 0; i < *resultLength; i++)
		if (result[i]->type != type) {
			destroyExpense(result[i]);
			for (int j = i; j < *resultLength - 1; j++)
				result[j] = result[j + 1];
			(*resultLength)--;
			i--;
		}
	if (*resultLength == 0) {
		free(result);
		return NULL;
	}

	return result;
}


/*
Functia returneaza lista de cheltuieli sortata dupa suma
:param ascending: true (sortare crescatoare) / false (sortare desc)
:param compare: functia de comparare *(int f(Expense* e1, Expense* e2))
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: lista de cheltuieli rezultat (Expense**) / (NULL daca memoria nu a fost alocata cu succes)
*/
Expense** getExpensesFromServiceSortedBySum(bool ascending, Compare compare, int* resultLength, ExpensesService* service) {
	Expense** result = copyList(getExpensesFromService(resultLength, service), *resultLength);

	if (result == NULL)
		return NULL;

	Expense* aux;

	if (ascending) {
		for (int i = 0; i < *resultLength; i++)
			for (int j = i + 1; j < *resultLength; j++)
				if (compare(result[i], result[j]) == 1) {
					aux = result[i];
					result[i] = result[j];
					result[j] = aux;
				}
	}
	else {
		for (int i = 0; i < *resultLength; i++)
			for (int j = i + 1; j < *resultLength; j++)
				if (compare(result[i], result[j]) == -1) {
					aux = result[i];
					result[i] = result[j];
					result[j] = aux;
				}
	}
	
	return result;
}

/*
Functia returneaza lista de cheltuieli sortata dupa tip
:param ascending: true (sortare crescatoare) / false (sortare desc)
:param compare: functia de comparare *(int f(Expense* e1, Expense* e2))
:param resultLength: lungimea listei de cheltuieli rezultat (int*)
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
:return: lista de cheltuieli rezultat (Expense**) / (NULL daca memoria nu a fost alocata cu succes)
*/
Expense** getExpensesFromServiceSortedByType(bool ascending, Compare compare, int* resultLength, ExpensesService* service) {
	Expense** result = copyList(getExpensesFromService(resultLength, service), *resultLength);

	if (result != NULL) {

		Expense* aux;

		if (ascending) {
			for (int i = 0; i < *resultLength; i++)
				for (int j = i + 1; j < *resultLength; j++)
					if (compare(result[i], result[j]) == 1) {
						aux = result[i];
						result[i] = result[j];
						result[j] = aux;
					}
		}
		else {
			for (int i = 0; i < *resultLength; i++)
				for (int j = i + 1; j < *resultLength; j++)
					if (compare(result[i], result[j]) == -1) {
						aux = result[i];
						result[i] = result[j];
						result[j] = aux;
					}
		}
	}

	return result;
}

/*
Functia dealoca spatiul alocat pentru service-ul de cheltuieli si pentru cheltuielile din acesta
:param service: referinta service-ului (ExpensesService*)
*/
void destroyService(ExpensesService* service) {
	destroyRepository(service->repository);
	free(service);
}

