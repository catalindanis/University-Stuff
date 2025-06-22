#pragma once
#include "CartService.h"

/*
Interfata pentru ClientUi-ul aplicatiei
*/
class ClientUi {
private:
	/*
	Campurile clasei ClientUi
	*/
	CartService& cartService;
	bool exitRequested;
public:
	/*
	Constructorul pentru ClientUi
	@param cartService : service-ul pentru cosul de carti (CartService)
	*/
	ClientUi(CartService& cartService);

	/*
	Functia adauga un produs in cos
	*/
	void add();

	/*
	Functia goleste cosul
	*/
	void empty();

	/*
	Functia afiseaza produsele din cos
	*/
	void display();

	/*
	Functia genereaza un cos
	*/
	void generate();

	/*
	Functia face export la cos 
	*/
	void save();

	/*
	Functia afiseaza raporturile cosului
	*/
	void report();

	/*
	Functia principala a client ui-ului care dirijeaza executia acestuia
	*/
	void runApplication();

	/*
	Functia afiseaza o carte printr-un mesaj formatat
	*/
	void showBook(const Book& book);

	/*
	Functia interpreteaza input-ul introdus de utilizator
	*/
	void handleInput(string& input);

	/*
	Functia afiseaza meniul client ui-ului
	*/
	void printMenu();

};