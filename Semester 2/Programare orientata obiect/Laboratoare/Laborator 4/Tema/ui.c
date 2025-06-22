#define _CRT_SECURE_NO_WARNINGS

/*
Implementarea pentru ui-ul aplicatiei
*/
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "service.h"

/*
Functia afiseaza meniul principal al aplicatiei
*/
void showMainMenu() {
	printf("~Buget de familie~\n");
	printf("1.Adauga o cheltuiala\n");
	printf("2.Modifica o cheltuiala\n");
	printf("3.Sterge o cheltuiala\n");
	printf("4.Vizualizare lista de cheltuieli\n");
	printf("5.Vizualizare lista cheltuieli filtrat dupa proprietate\n");
	printf("6.Vizualizare lista cheltuieli ordonat dupa suma sau tip\n");
	printf("7.Iesire\n");
	printf(">>");
}

/*
Functia citeste input-ul introdus de utilizator de la tastatura
si il transforma in numar natural
:return: -1 (daca input-ul nu este un numar intreg)
			/ valoarea citita (in caz contrar)
*/
int getIntegerFromUser() {
	char input[100] = "";

	if (scanf("%s", input) == 0)
		return -1;

	char* errptr;

	long value = strtol(input, &errptr, 10);

	if (*errptr != '\0' && *errptr != '\n')
		return -1;

	if (value < 0)
		return -1;

	return value;
}

/*
Functia citeste input-ul introdus de utilizator de la tastatura
si il transforma in numar real
:return: -1 (daca input-ul nu este un numar real)
			/ valoarea citita (in caz contrar)
*/
double getFloatFromUser() {
	char input[100] = "";

	if (scanf("%s", input) == 0)
		return -1;

	char* errptr;

	double value = strtod(input, &errptr);

	if (*errptr != '\0' && *errptr != '\n')
		return -1;

	if (value < 0)
		return -1;

	return value;
}

/*
Functia preia de la utilizator tipul unei cheltuieli
:param type: referinta catre tipul cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readTypeFromUser(int* type) {
	printf("Introduceti tipul cheltuielii: \n");
	printf("1.Mancare\n");
	printf("2.Transport\n");
	printf("3.Telefon & internet\n");
	printf("4.Imbracaminte\n");
	printf("5.Altele\n");
	printf(">>");
	*type = getIntegerFromUser();
	if(*type == -1)
		printf("Valoarea introdusa nu este numar natural!\n");
	return *type != -1;
}

/*
Functia preia de la utilizator id-ul unei cheltuieli
:param id: referinta catre id-ul cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readIdFromUser(int* id) {
	printf("Introduceti id-ul cheltuielii: ");
	*id = getIntegerFromUser();
	if (*id == -1)
		printf("Valoarea introdusa nu este numar natural!\n");
	return *id != -1;
}

/*
Functia preia de la utilizator suma unei cheltuieli
:param suma: referinta catre suma cheltuielii (double*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readSumFromUser(double* sum) {
	printf("Introduceti suma cheltuielii: ");
	*sum = getFloatFromUser();
	if(*sum == -1)
		printf("Numarul introdus nu este real!\n");
	return *sum != -1;
}

/*
Functia preia de la utilizator ziua unei cheltuieli
:param day: referinta catre ziua cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readDayFromUser(int* day) {
	printf("Introduceti ziua cheltuielii: ");
	*day = getIntegerFromUser();
	if(*day == -1)
		printf("Valoarea introdusa nu este numar natural!\n");
	return *day != -1;
}

/*
Functia preia de la utilizator proprietatile unei cheltuieli
si o adauga prin service in repository-ul de cheltuieli si 
afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void addExpense(ExpensesService* service) {
	int day, type;
	double sum;

	if (readDayFromUser(&day) == false)
		return;

	if (readSumFromUser(&sum) == false)
		return;

	if (readTypeFromUser(&type) == false)
		return;

	int resultCode = addExpenseInService(day, sum, type, service);
	
	switch (resultCode) {
	case 0:
		printf("Cheltuiala a fost adaugata cu succes!\n");
		return;
	case 1:
		printf("Ziua este invalida!\n");
		return;
	case 2:
		printf("Suma este invalida!\n");
		return;
	case 3:
		printf("Tipul este invalid!\n");
		return;
	case 4:
		printf("Eroare la repository!\n");
		return;
	default:
		break;
	}
}

/*
Functia preia de la utilizator indicele unei cheltuieli si
proprietatile unei cheltuieli si actualizeaza prin service
cheltuiala cu indicele specificat cu noile proprietati in 
repository si afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void updateExpense(ExpensesService* service) {
	int id;
	int day, type;
	double sum;

	if (readIdFromUser(&id) == false)
		return;

	if (readDayFromUser(&day) == false)
		return;

	if (readSumFromUser(&sum) == false)
		return;

	if (readTypeFromUser(&type) == false)
		return;

	int resultCode = updateExpenseFromServiceByIndex(id, day, sum, type, service);

	switch (resultCode) {
	case 0:
		printf("Cheltuiala a fost actualizata cu succes!\n");
		return;
	case 1:
		printf("Ziua este invalida!\n");
		return;
	case 2:
		printf("Suma este invalida!\n");
		return;
	case 3:
		printf("Tipul este invalid!\n");
		return;
	case 4:
		printf("Nu exista o cheltuiala cu acel id!\n");
		return;
	default:
		break;
	}
}

/*
Functia preia de la utilizator indicele unei cheltuieli si
si sterge prin service cheltuiala cu indicele specificat din
repository si afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void deleteExpense(ExpensesService* service) {
	int id;

	if (readIdFromUser(&id) == false)
		return;

	bool resultCode = deleteExpenseFromServiceByIndex(id, service);

	if (resultCode == true)
		printf("Cheltuiala a fost stearsa cu succes!\n");
	else
		printf("Nu exista o cheltuiala cu acel id!\n");
}

/*
Functia afiseaza prin service lista de cheltuieli
*/
void viewExpenses(ExpensesService* service) {
	int length;
	Expense** expenses;

	expenses = getExpensesFromService(&length, service);

	if (expenses) {
		for (int i = 0; i < length; i++) {
			printf("Cheltuiala #%d: %s", i, getExpenseStringFormat(expenses[i]));
			printf("\n");
		}
	}
	else printf("Nu exista cheltuieli!\n");
}

/*
Functia preia de la utilizator proprietatea filtrarii si afiseaza
prin service lista de cheltuieli filtrata dupa acea proprietate
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void viewExpensesFiltered(ExpensesService* service) {
	printf("Alegeti filtrul: \n");
	printf("1.Ziua\n");
	printf("2.Suma\n");
	printf("3.Tipul\n");
	printf(">>");
	int filterType = getIntegerFromUser();

	int day, type;
	double sum;
	int resultLength;
	Expense** result;
	
	switch (filterType)
	{
	case 1:
		if (readDayFromUser(&day) == false)
			return;
		result = getExpensesFromServiceFilteredByDay(day, &resultLength, service);	
		break;
	case 2:
		if (readSumFromUser(&sum) == false)
			return;
		result = getExpensesFromServiceFilteredBySum(sum, &resultLength, service);
		break;
	case 3:
		if (readTypeFromUser(&type) == false)
			return;
		result = getExpensesFromServiceFilteredByType(type, &resultLength, service);
		break;
	default:
		if (filterType == -1)
			printf("Valoarea introdusa nu este numar natural!\n");
		else
			printf("Optiunea este invalida!\n");
		return;
	}

	if (result) {
		for (int i = 0; i < resultLength; i++) {
			printf("Cheltuiala #%d: %s", i, getExpenseStringFormat(result[i]));
			printf("\n");
			destroyExpense(result[i]);
		}
		free(result);
	}
	else printf("Nu exista astfel de cheltuieli!\n");
}

/*
Functia preia de la utilizator proprietatea sortarii si modul
de sortare iar apoi afiseaza prin service lista de cheltuieli
sortata dupa acea proprietate
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void viewExpensesSorted(ExpensesService* service) {
	printf("Alegeti proprietatea: \n");
	printf("1.Suma\n");
	printf("2.Tipul\n");
	printf(">>");
	int sortProperty = getIntegerFromUser();
	int sortType;
	int resultLength;

	Expense** result;

	switch (sortProperty)
	{
	case 1:
		printf("Alegeti modul de sortare: \n");
		printf("1.Crescator\n");
		printf("2.Descrescator\n");
		printf(">>");
		sortType = getIntegerFromUser();

		switch (sortType) {
			case 1:
			case 2:
				result = getExpensesFromServiceSortedBySum(sortType == 1, compareSum, &resultLength, service);
				break;
			default:
				if (sortType == -1)
					printf("Valoarea introdusa nu este numar natural!\n");
				else
					printf("Optiunea este invalida!\n");
				return;
		}
		break;
	case 2:
		printf("Alegeti modul de sortare: \n");
		printf("1.Crescator\n");
		printf("2.Descrescator\n");
		printf(">>");
		sortType = getIntegerFromUser();

		switch (sortType) {
			case 1:
			case 2:
				result = getExpensesFromServiceSortedByType(sortType == 1, *compareType, &resultLength, service);
				break;
			default:
				if (sortType == -1)
					printf("Valoarea introdusa nu este numar natural!\n");
				else
					printf("Optiunea este invalida!\n");
				return;
		}
		break;
	default:
		if (sortProperty == -1)
			printf("Valoarea introdusa nu este numar natural!\n");
		else
			printf("Optiunea este invalida!\n");
		return;
	}

	if (result) {
		for (int i = 0; i < resultLength; i++) {
			printf("Cheltuiala #%d: %s", i, getExpenseStringFormat(result[i]));
			printf("\n");
			destroyExpense(result[i]);
		}

		free(result);
	}
	else printf("Nu exista cheltuieli!\n");
}

/*
Functia executa o actiune in functie de input-ul introdus de la tastatura
*/
void handleInput(int command, ExpensesService* service) {
	switch (command) {
		case 1:
			addExpense(service);
			break;
		case 2:
			updateExpense(service);
			break;
		case 3:
			deleteExpense(service);
			break;
		case 4:
			viewExpenses(service);
			break;
		case 5:
			viewExpensesFiltered(service);
			break;
		case 6:
			viewExpensesSorted(service);
			break;
		case 7:
			exit(0);
		default: 
			printf("Optiune invalida!\n");
	}
	printf("\n");
}

/*
Functia principala a ui-ului care dirijeaza intreaga executie
a acestuia
:param service: service-ul cheltuielilor (ExpensesService*)
*/
void runApplication(ExpensesService* service) {
	while (true) {
		showMainMenu();
		int command = getIntegerFromUser();
		handleInput(command, service);
	}
}
