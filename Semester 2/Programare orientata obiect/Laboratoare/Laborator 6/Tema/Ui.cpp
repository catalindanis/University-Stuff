#include "Ui.h"
#include "Service.h"
#include <iostream>
#include <exception>


Ui::Ui(Service service) : service{ service } {}

void Ui::add() {
	string title;
	string author;
	string type;
	string year;
	cout << "Introduceti titlul: ";
	getline(cin, title);
	cout << "Introduceti autorul: ";
	getline(cin, author);
	cout << "Introduceti genul: ";
	getline(cin, type);
	cout << "Introduceti anul aparitiei: ";
	getline(cin, year);
	
	int releaseYear;
	
	try {
		releaseYear = stoi(year);
	}
	catch (exception e) {
		cout << "Nu ati introdus un numar valid!\n";
		return;
	}

	int resultCode = this->service.addBook(title, author, type, releaseYear);
	switch (resultCode) {
		case 0:
			cout << "Cartea a fost adaugata cu succes!\n";
			break;
		case 1:
			cout << "Cartea exista deja!\n";
			break;
		case 2:
			cout << "Titlul este invalid!\n";
			break;
		case 3:
			cout << "Autorul este invalid!\n";
			break;
		case 4:
			cout << "Genul este invalid!\n";
			break;
		case 5:
			cout << "Anul publicatiei este invalid!\n";
			break;
	}
}

void Ui::remove() {
	string input;

	cout << "Introduceti indexul cartii: ";
	getline(cin, input);

	int index;
	try {
		index = stoi(input);
	}
	catch (exception e) {
		cout << "Nu ati introdus un numar valid!\n";
		return;
	}

	int resultCode = this->service.removeBook(index);
	switch (resultCode)
	{
		case 0:
			cout << "Cartea a fost stearsa cu succes!\n";
			break;
		case 1:
			cout << "Cartea cu acest index nu exista!\n";
			break;
	}
}

void Ui::update() {
	string input;

	cout << "Introduceti indexul cartii: ";
	getline(cin, input);

	int index;
	try {
		index = stoi(input);
	}
	catch (exception e) {
		cout << "Nu ati introdus un numar valid!\n";
		return;
	}

	string title;
	string author;
	string type;
	string year;
	cout << "Introduceti titlul: ";
	getline(cin, title);
	cout << "Introduceti autorul: ";
	getline(cin, author);
	cout << "Introduceti genul: ";
	getline(cin, type);
	cout << "Introduceti anul aparitiei: ";
	getline(cin, year);

	int releaseYear;

	try {
		releaseYear = stoi(year);
	}
	catch (exception e) {
		cout << "Nu ati introdus un numar valid!\n";
		return;
	}

	int resultCode = this->service.updateBook(index, title, author, type, releaseYear);
	switch (resultCode) {
	case 0:
		cout << "Cartea a fost actualizata cu succes!\n";
		break;
	case 1:
		cout << "Cartea nu exista!\n";
		break;
	case 2:
		cout << "Titlul este invalid!\n";
		break;
	case 3:
		cout << "Autorul este invalid!\n";
		break;
	case 4:
		cout << "Genul este invalid!\n";
		break;
	case 5:
		cout << "Anul publicatiei este invalid!\n";
		break;
	}
}

void Ui::displayAll() {
	if (this->service.getAllBooks().size() == 0)
		cout << "Nu exista carti!\n";
	int index = 0;
	for (auto& book : this->service.getAllBooks()) {
		cout << "#" + to_string(index++) + " | ";
		showBook((Book&)book);
	}
}

void Ui::search() {
	string title;
	cout << "Introduceti titlul: ";
	getline(cin, title);

	if (this->service.searchBooksByTitle(title).size() == 0)
		cout << "Nu exista carti!\n";
	int index = 0;
	for (auto& book : this->service.searchBooksByTitle(title)) {
		cout << "#" + to_string(index++) + " | ";
		this->showBook((Book&) book);
	}
}

void Ui::filter() {
	string filterType;
	cout << "Introduceti tipul filtrarii: \n1.Titlu\n2.Anul aparitiei\n>>";
	getline(cin, filterType);

	vector<Book> books;
	if (filterType.compare("1") == 0) {
		string title;
		cout << "Introduceti titlul: ";
		getline(cin, title);
	
		if (this->service.filterBooksByTitle(title).size() == 0)
			cout << "Nu exista carti!\n";
		int index = 0;
		for (auto& book : this->service.filterBooksByTitle(title)) {
			cout << "#" + to_string(index++) + " | ";
			showBook(book);
		}
	}
	else if (filterType.compare("2") == 0) {
		string year;
		cout << "Introduceti anul aparitiei: ";
		getline(cin, year);

		int releaseYear;

		try {
			releaseYear = stoi(year);
		}
		catch (exception e) {
			cout << "Nu ati introdus un numar valid!\n";
			return;
		}
		if (this->service.filterBooksByReleaseYear(releaseYear).size() == 0)
			cout << "Nu exista carti!\n";
		int index = 0;
		for (auto& book : this->service.filterBooksByReleaseYear(releaseYear)) {
			cout << "#" + to_string(index++) + " | ";
			showBook(book);
		}
	}
	else {
		cout << "Comanda invalida!\n";
		return;
	}
}

void Ui::sort() {
	string sortType;
	cout << "Introduceti campul sortarii: \n1.Titlu\n2.Autor\n3.Anul aparitiei + gen\n>>";
	getline(cin, sortType);

	if (sortType.compare("1") == 0) {
		if (this->service.sortedBooksByTitle().size() == 0)
			cout << "Nu exista carti!\n";
		int index = 0;
		for (auto& book : this->service.sortedBooksByTitle()) {
			cout << "#" + to_string(index++) + " | ";
			showBook(book);
		}
	}
	else if (sortType.compare("2") == 0) {
		if (this->service.sortedBooksByAuthor().size() == 0)
			cout << "Nu exista carti!\n";
		int index = 0;
		for (auto& book : this->service.sortedBooksByAuthor()) {
			cout << "#" + to_string(index++) + " | ";
			showBook(book);
		}
	}
	else if (sortType.compare("3") == 0) {
		if (this->service.sortedBooksByReleaseDateAndType().size() == 0)
			cout << "Nu exista carti!\n";
		int index = 0;
		for (auto& book : this->service.sortedBooksByReleaseDateAndType()) {
			cout << "#" + to_string(index++) + " | ";
			showBook(book);
		}
	}
	else {
		cout << "Comanda invalida!\n";
		return;
	}
}

void Ui::showBook(Book& book) {
	cout << "Titlu: " + book.getTitle()
		+ " | Autor: " + book.getAuthor()
		+ " | Gen: " + book.getType()
		+ " | Anul publicatiei: " + to_string(book.getReleaseYear())
		+ "\n";
}


void Ui::printMenu() {
	cout << "~Biblioteca~\n";
	cout << "1.Adaugare\n";
	cout << "2.Stergere\n";
	cout << "3.Modificare\n";
	cout << "4.Afisare\n";
	cout << "5.Cautare\n";
	cout << "6.Filtrare\n";
	cout << "7.Sortare\n";
	cout << "0.Iesire\n";
}


void Ui::handleInput(string& input) {
	if (input.compare("0") == 0)
		exit(0);
	else if (input.compare("1") == 0)
		add();
	else if (input.compare("2") == 0)
		remove();
	else if (input.compare("3") == 0)
		update();
	else if (input.compare("4") == 0)
		displayAll();
	else if (input.compare("5") == 0)
		search();
	else if (input.compare("6") == 0)
		filter();
	else if (input.compare("7") == 0)
		sort();
	else
		cout << "Comanda invalida!\n";
}


void Ui::runApplication() {
	while (true) {
		this->printMenu();
		string input;
		cout << ">>";
		getline(cin, input);
		handleInput(input);
	}
}
