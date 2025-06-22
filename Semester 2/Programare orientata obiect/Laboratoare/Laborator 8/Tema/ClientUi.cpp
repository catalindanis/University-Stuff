#include "ClientUi.h"

ClientUi::ClientUi(CartService& service) : cartService{ service }, exitRequested{ false } {}

void ClientUi::add() {
	if (this->cartService.booksService.getAllBooks().size() == 0) {
		cout << "Nu exista carti!\n";
		return;
	}

	int index = 0;
	for (const auto& book : this->cartService.booksService.getAllBooks()) {
		cout << "#" + to_string(index++) + " | ";
		showBook((Book&)book);
	}

	cout << "Introduceti titlul cartii: ";

	string input;
	getline(cin, input);

	this->cartService.add(input);
	this->display();
}

void ClientUi::empty() {
	this->cartService.deleteAll();
	cout << "Cosul a fost golit cu succes!\n";
}

void ClientUi::display() {
	if (this->cartService.getAllBooks().size() == 0){
		cout << "Cosul este gol!\n";
		return;
	}

	cout << "Produse: " << this->cartService.getAllBooks().size() << "\n";
	int index = 0;
	for (const auto& book : this->cartService.getAllBooks()) {
		cout << "#" + to_string(index++) + " | ";
		showBook((Book&)book);
	}
}

void ClientUi::generate() {
	cout << "Introduceti numarul de carti: ";

	string input;
	getline(cin, input);

	int numberOfProducts;

	try {
		numberOfProducts = stoi(input);
		if (numberOfProducts <= 0)
			throw exception("");
	}
	catch (exception e) {
		cout << "Nu ati introdus un numar valid!\n";
		return;
	}

	this->cartService.generateCart(numberOfProducts);
	this->display();
}

void ClientUi::save() {
	cout << "Introduceti numele fisierului: ";

	string input;
	getline(cin, input);

	if (input.compare("") == 0) {
		cout << "Numele fisierului nu poate fi gol!";
		return;
	}

	for (const auto& ch : input) {
		if (!isalpha(ch)) {
			cout << "Numele fisierului poate contine doar litere!";
			return;
		}
	}

	this->cartService.saveToFile(input);
	cout << "Cosul a fost salvat cu succes!\n";
}

void ClientUi::report() {
	for (const auto& pair : this->cartService.getReports()) {
		cout << pair.first << " : " << pair.second.getQuantity() << '\n';
	}
}

void ClientUi::showBook(const Book& book) {
	cout << "Titlu: " + book.getTitle()
		+ " | Autor: " + book.getAuthor()
		+ " | Gen: " + book.getType()
		+ " | Anul publicatiei: " + to_string(book.getReleaseYear())
		+ "\n";
}

void ClientUi::runApplication() {
	while (!this->exitRequested) {
		this->printMenu();
		string input;
		cout << ">>";
		getline(cin, input);
		handleInput(input);
	}
}

void ClientUi::handleInput(string& input) {
	if (input.compare("0") == 0)
		this->exitRequested = true;
	else if (input.compare("1") == 0)
		add();
	else if (input.compare("2") == 0)
		empty();
	else if (input.compare("3") == 0)
		display();
	else if (input.compare("4") == 0)
		generate();
	else if (input.compare("5") == 0)
		save();
	else if (input.compare("6") == 0)
		report();
	else
		cout << "Comanda invalida!\n";
}

void ClientUi::printMenu() {
	cout << "~Biblioteca~\n";
	cout << "1.Adauga produs in cos\n";
	cout << "2.Goleste cosul\n";
	cout << "3.Afiseaza cosul\n";
	cout << "4.Genereaza cos\n";
	cout << "5.Export cos\n";
	cout << "6.Rapoarte\n";
	cout << "0.Iesire\n";
}