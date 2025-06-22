#pragma once

#include "Service.h"

/*
Interfata pentru Ui-ul aplicatiei
*/
class ManagerUi {
private:
	/*
	Campurile clasei ui
	*/
	Service& service;
	bool exitRequested;
public:
	/*
	Constructorul ui-ului de carti
	@param service service-ul folosit de ui (Service)
	*/
	ManagerUi(Service& service);

	/*
	Functia principala a ui-ului care dirijeaza executia acestuia
	*/
	void runApplication();

	/*
	Functia interpreteaza input-ul introdus de utilizator
	*/
	void handleInput(string& input);

	/*
	Functia afiseaza meniul ui-ului
	*/
	void printMenu();

	/*
	Functia realizeaza operatia de adaugare a unei carti
	*/
	void add();

	/*
	Functia realizeaza operatia de stergere a unei carti
	*/
	void remove();

	/*
	Functia realizeaza operatia de modificare a unei carti
	*/
	void update();

	/*
	Functia afiseaza listei de carti
	*/
	void displayAll();

	/*
	Functia realizeaza operatia de cautare in lista de carti
	*/
	void search();

	/*
	Functia realizeaza operatia de filtrare a listei de carti
	*/
	void filter();

	/*
	Functia realizeaza operatia de sortare a listei de carti
	*/
	void sort();

	/*
	Functia afiseaza o carte
	@param book cartea de afisat (Book)
	*/
	void showBook(const Book& book);
};