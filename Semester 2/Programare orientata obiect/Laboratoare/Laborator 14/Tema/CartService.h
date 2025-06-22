#pragma once
#include "Book.h"
#include "Service.h"
#include "Observable.h"
#include <vector>
#include <string.h>
#include <fstream>
#include <map>
#include "BookDTO.h"
using std::vector;
using std::string;

class CartService : public Observable{
private:
	vector<Book> cart;
	Service& booksService;
	map<string, BookDTO> report;
	friend class ClientUi;
public:

	/*
	Constructorul cosului de carti
	*/
	CartService(Service& service);

	/*
	Functia adauga o carte in cosul de carti dupa titlul cartii
	*/
	void add(string title);

	/*
	Functia sterge toate produsele din cosul de cumparaturi
	*/
	void deleteAll();

	/*
	Functia genereaza un cos populat cu numarul de produse transmis ca parametru
	*/
	void generateCart(int size);

	/*
	Functia returneaza dimensiunea cosului de carti
	*/
	int size() const;

	/*
	Functia returneaza lista de carti din cosul de cumparaturi
	*/
	const vector<Book>& getAllBooks() const;

	/*
	Functia salveaza continutul cosului intr-un fisier HTML
	*/
	void saveToFile(string fileName) const;

	/*
	Functia returneaza raporturile produselor
	*/
	map<string, BookDTO> getReports() const;
};