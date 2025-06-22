/*
Implementarea metodelor unei cheltuieli
*/

#include "expense.h"
#include "stdbool.h"
#include <stdio.h>
#include <stdlib.h>

/*
Functia creeaza o cheltuiala noua cu parametrii transmisi
:param day: ziua cheltuielii (int)
:param sum: suma cheltuielii (double)
:param type: tipul cheltuielii (int)
:return: referinta catre cheltuiala creata (Expense*) /  NULL (daca nu s-a putut crea)
*/
Expense* createExpense(int day, double sum, int type){
	Expense* expense = malloc(sizeof(Expense));
	if (expense != NULL) {

		expense->stringFormat = malloc(60 * sizeof(char));
		if (expense->stringFormat != NULL) {
			expense->day = day;
			expense->sum = sum;
			expense->type = type;
		}
	}
	return expense;
}

/*
Functia valideaza daca ziua transmisa este valida
:param day: ziua cheltuielii (int)
:return: true (daca ziua este valida) / false (in caz contrar) (bool)
*/
bool validateExpenseDay(int day) {
	return day >= 1 && day <= 31;
}

/*
Functia valideaza daca suma transmisa este valida
:param sum: suma cheltuielii (double)
:return: true (daca suma este valida) / false (in caz contrar) (bool)
*/
bool validateExpenseSum(double sum) {
	return sum > 0;
}

/*
Functia valideaza daca ziua transmisa este valida
:param type: tipul cheltuielii (int)
:return: true (daca tipul este valid) / false (in caz contrar) (bool)
*/
bool validateExpenseType(int type) {
	return type >= 1 && type <= 5;
}

/*
Functia returneaza ziua unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: ziua cheltuielii (int)
*/
int getExpenseDay(Expense* expense) {
	return expense->day;
}

/*
Functia returneaza suma unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: suma cheltuielii (double)
*/
double getExpenseSum(Expense* expense) {
	return expense->sum;
}

/*
Functia returneaza tipul unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: tipul cheltuielii (int)
*/
int getExpenseType(Expense* expense) {
	return expense->type;
}

/*
Functia returneaza tipul unei cheltuielii sub forma de string (cu valoarea propriu-zisa)
:param expense: referinta cheltuielii (Expense*)
:return: tipul cheltuielii (int)
		mancare = 1
		transport = 2
		telefon & internet = 3
		imbracaminte = 4
		altele = 5
*/
char* getExpenseTypeString(Expense* expense) {
	switch (expense->type) {
	case 1: return "Mancare";
	case 2: return "Transport";
	case 3: return "Telefon & internet";
	case 4: return "Imbracaminte";
	case 5: return "Altele";
	default: return "Unknown";
	}
}

/*
Functia modifica ziua unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param day: noua zi a cheltuielii (int)
*/
void setExpenseDay(Expense* expense, int day) {
	expense->day = day;
}

/*
Functia modifica suma unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param sum: noua suma a cheltuielii (double)
*/
void setExpenseSum(Expense* expense, double sum) {
	expense->sum = sum;
}

/*
Functia modifica tipul unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param type: noul tip al cheltuielii (int)
*/
void setExpenseType(Expense* expense, int type) {
	expense->type = type;
}

/*
Functia verifica daca 2 cheltuieli sunt egale
:param e1: referinta catre prima cheltuiala (Expense*)
:param e2: referinta catre a doua cheltuiala (Expense*)
:return: true (daca cheltuielile sunt egale) / false (in caz contrar) (bool)
*/
bool expensesEqual(Expense* e1, Expense* e2) {
	if (e1 == NULL && e2 == NULL)
		return true;
	if (e1 == NULL || e2 == NULL)
		return false;
	return e1->day == e2->day && e1->sum == e2->sum && e1->type == e2->type;
}

/*
Functia returneaza o cheltuiala sub forma unui string formatat
:param e: referinta cheltuielii (Expense*)
:return: cheltuiala sub forma de string formatat (char*)
*/
char* getExpenseStringFormat(Expense* e) {
	if (e == NULL)
		return "";

	snprintf(e->stringFormat, 60, "Ziua: %d, Suma: %.2f, Tipul: %s",
		e->day, e->sum, getExpenseTypeString(e));
	return e->stringFormat;
}

/*
Functia dealoca spatiul alocat pentru o cheltuiala
:param expense: referinta cheltuielii (Expense*)
*/
void destroyExpense(Expense* expense) {
	free(expense->stringFormat);
	free(expense);
}

/*
Functia compara sumele a 2 cheltuieli
:return: -1 (suma primei cheltuieli este mai mica)
		  0 (suma celor 2 cheltuieli este egala)
		  1 (suma primei cheltuieli este mai mare)
*/
int compareSum(Expense* e1, Expense* e2) {
	if (e1->sum <= e2->sum)
		return -1;
	else
		return 1;
}

/*
Functia compara tipul a 2 cheltuieli
:return: -1 (tipul primei cheltuieli este mai mica)
		  0 (tipul celor 2 cheltuieli este egala)
		  1 (tipul primei cheltuieli este mai mare)
*/
int compareType(Expense* e1, Expense* e2) {
	if (e1->type < e2->type)
		return -1;
	else
		return 1;
}
