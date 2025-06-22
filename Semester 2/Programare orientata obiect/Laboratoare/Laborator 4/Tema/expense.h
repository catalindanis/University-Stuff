/*
Interfata ce defineste o cheltuiala
*/
#pragma once

#include "stdbool.h"

/*
Structura unei cheltuieli
day - ziua cheltuielii (int) (1 <= day <= 31)
sum - suma cheltuielii (double) (0 < sum)
type - tipul cheltuielii (int) (1 <= type <= 5)
		mancare = 1
		transport = 2
		telefon & internet = 3
		imbracaminte = 4
		altele = 5
stringFormat - un sir de caractere formatat pentru afisarea unei cheltuieli sub forma de string (char*)
*/
typedef struct {
	int day;
	double sum;
	int type;
	char* stringFormat;
} Expense;

/*
Functia creeaza o cheltuiala noua cu parametrii transmisi
:param day: ziua cheltuielii (int)
:param sum: suma cheltuielii (double)
:param type: tipul cheltuielii (int)
:return: referinta catre cheltuiala creata (Expense*) /  NULL (daca nu s-a putut crea)
*/
Expense* createExpense(int day, double sum, int type);

/*
Functia valideaza daca ziua transmisa este valida
:param day: ziua cheltuielii (int)
:return: true (daca ziua este valida) / false (in caz contrar) (bool)
*/
bool validateExpenseDay(int day);

/*
Functia valideaza daca suma transmisa este valida
:param sum: suma cheltuielii (double)
:return: true (daca suma este valida) / false (in caz contrar) (bool)
*/
bool validateExpenseSum(double sum);

/*
Functia valideaza daca ziua transmisa este valida
:param type: tipul cheltuielii (int)
:return: true (daca tipul este valid) / false (in caz contrar) (bool)
*/
bool validateExpenseType(int type);

/*
Functia returneaza ziua unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: ziua cheltuielii (int)
*/
int getExpenseDay(Expense* expense);

/*
Functia returneaza suma unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: suma cheltuielii (double)
*/
double getExpenseSum(Expense* expense);

/*
Functia returneaza tipul unei cheltuieli transmise
:param expense: referinta cheltuielii (Expense*)
:return: tipul cheltuielii (int)
*/
int getExpenseType(Expense* expense);

/*
Functia returneaza tipul unei cheltuielii sub forma de string (cu valoarea propriu-zisa)
:param expense: referinta cheltuielii (Expense*)
:return: tipul cheltuielii (char*)
		mancare = 1
		transport = 2
		telefon & internet = 3
		imbracaminte = 4
		altele = 5
*/
char* getExpenseTypeString(Expense* expense);

/*
Functia modifica ziua unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param day: noua zi a cheltuielii (int)
*/
void setExpenseDay(Expense* expense, int day);

/*
Functia modifica suma unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param sum: noua suma a cheltuielii (double)
*/
void setExpenseSum(Expense* expense, double sum);

/*
Functia modifica tipul unei cheltuieli cu cea transmisa ca parametru
:param expense: referinta cheltuielii (Expense*)
:param type: noul tip al cheltuielii (int)
*/
void setExpenseType(Expense* expense, int type);

/*
Functia verifica daca 2 cheltuieli sunt egale
:param e1: referinta catre prima cheltuiala (Expense*)
:param e2: referinta catre a doua cheltuiala (Expense*)
:return: true (daca cheltuielile sunt egale) / false (in caz contrar) (bool)
*/
bool expensesEqual(Expense* e1, Expense* e2);

/*
Functia returneaza o cheltuiala sub forma unui string formatat
:param e: referinta cheltuielii (Expense*)
:return: cheltuiala sub forma de string formatat (char*)
*/
char* getExpenseStringFormat(Expense* e);

/*
Functia dealoca spatiul alocat pentru o cheltuiala
:param expense: referinta cheltuielii (Expense*)
*/
void destroyExpense(Expense* expense);

/*
Functia compara sumele a 2 cheltuieli
:return: -1 (suma primei cheltuieli este mai mica)
		  0 (suma celor 2 cheltuieli este egala)
		  1 (suma primei cheltuieli este mai mare)
*/
int compareSum(Expense* e1, Expense* e2);

/*
Functia compara tipul a 2 cheltuieli
:return: -1 (tipul primei cheltuieli este mai mica)
		  0 (tipul celor 2 cheltuieli este egala)
		  1 (tipul primei cheltuieli este mai mare)
*/
int compareType(Expense* e1, Expense* e2);