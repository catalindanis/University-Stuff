#include "MainUi.h"

MainUi::MainUi(Service& service, CartService& cartService) : service{ service }, cartService{ cartService }, exitRequested { false } {}

void MainUi::managerMode() {
	ManagerUi managerUi{ this->service };
	managerUi.runApplication();
}

void MainUi::clientMode() {
	ClientUi clientUi{ this->cartService };
	clientUi.runApplication();
}

void MainUi::printMenu() {
	cout << "~Selecteaza modul~\n";
	cout << "1.Mod manager\n";
	cout << "2.Mod client\n";
	cout << "0.Iesire\n";
}

void MainUi::handleInput(string& input) {
	if (input.compare("0") == 0)
		this->exitRequested = true;
	else if (input.compare("1") == 0)
		managerMode();
	else if (input.compare("2") == 0)
		clientMode();
	else
		cout << "Comanda invalida!\n";
}

void MainUi::runApplication() {
	while (!this->exitRequested) {
		this->printMenu();
		string input;
		cout << ">>";
		getline(cin, input);
		handleInput(input);
	}
}