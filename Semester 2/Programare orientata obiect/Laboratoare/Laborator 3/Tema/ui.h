/*
Interfata ce defineste ui-ul aplicatiei
*/
#pragma once

/*
Functia principala a ui-ului care dirijeaza intreaga executie
a acestuia
:param service: service-ul cheltuielilor (ExpensesService*)
*/
void runApplication(ExpensesService* service);

/*
Functia afiseaza meniul principal al aplicatiei
*/
void showMainMenu();

/*
Functia citeste input-ul introdus de utilizator de la tastatura
si il transforma in numar natural
:return: -1 (daca input-ul nu este un numar intreg)
			/ valoarea citita (in caz contrar)
*/
int getIntegerFromUser();

/*
Functia citeste input-ul introdus de utilizator de la tastatura
si il transforma in numar real
:return: -1 (daca input-ul nu este un numar real)
			/ valoarea citita (in caz contrar)
*/
int getFloatFromUser();

/*
Functia preia de la utilizator tipul unei cheltuieli
:param type: referinta catre tipul cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readTypeFromUser(int* type);

/*
Functia preia de la utilizator id-ul unei cheltuieli
:param id: referinta catre id-ul cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readIdFromUser(int* id);

/*
Functia preia de la utilizator suma unei cheltuieli
:param suma: referinta catre suma cheltuielii (double*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readSumFromUser(double* sum);

/*
Functia preia de la utilizator ziua unei cheltuieli
:param day: referinta catre ziua cheltuielii (int*)
:return: true (operatia s-a efectuat cu succes) /
		 false (citirea a esuat)
*/
bool readDayFromUser(int* day);

/*
Functia preia de la utilizator proprietatile unei cheltuieli
si o adauga prin service in repository-ul de cheltuieli si
afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void addExpense(ExpensesService* service);

/*
Functia preia de la utilizator indicele unei cheltuieli si
proprietatile unei cheltuieli si actualizeaza prin service
cheltuiala cu indicele specificat cu noile proprietati in
repository si afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void updateExpense(ExpensesService* service);

/*
Functia preia de la utilizator indicele unei cheltuieli si
si sterge prin service cheltuiala cu indicele specificat din
repository si afiseaza mesaje corespunzatoare
:param service: service-ul de cheltuieli (ExpenesesService*)
*/
void deleteExpense(ExpensesService* service);

/*
Functia afiseaza prin service lista de cheltuieli
*/
void viewExpenses(ExpensesService* service);

/*
Functia preia de la utilizator proprietatea filtrarii si afiseaza
prin service lista de cheltuieli filtrata dupa acea proprietate
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void viewExpensesFiltered(ExpensesService* service);

/*
Functia preia de la utilizator proprietatea sortarii si modul
de sortare iar apoi afiseaza prin service lista de cheltuieli
sortata dupa acea proprietate
:param service: referinta catre service-ul de cheltuieli (ExpensesService*)
*/
void viewExpensesSorted(ExpensesService* service);

/*
Functia executa o actiune in functie de input-ul introdus de la tastatura
*/
void handleInput(int command, ExpensesService* service);
