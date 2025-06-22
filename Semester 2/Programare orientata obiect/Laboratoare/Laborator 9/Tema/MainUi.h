#pragma once
#include "Service.h"
#include "CartService.h"
#include "ManagerUi.h"
#include "ClientUi.h"

/*
Interfata pentru main Ui-ul aplicatiei
*/
class MainUi {
private:
	/*
	Campurile clasei MainUi
	*/
	Service& service;
	CartService& cartService;
	bool exitRequested;
public:
	/*
	Constructorul pentru MainUi
	@param service : service-ul de carti (Service)
	@param cartService : service-ul pentru cosul de carti (CartService)
	*/
	MainUi(Service& service, CartService& cartService);

	/*
	Functia principala a main ui-ului care dirijeaza executia acestuia
	*/
	void runApplication();

	/*
	Functia interpreteaza input-ul introdus de utilizator
	*/
	void handleInput(string& input);

	/*
	Functia afiseaza meniul main ui-ului
	*/
	void printMenu();

	/*
	Functia porneste meniul de manager
	*/
	void managerMode();

	/*
	Functia porneste meniul de client
	*/
	void clientMode();
};