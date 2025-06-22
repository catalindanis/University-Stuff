#pragma once
#include <vector>
#include <optional>
#include "Book.h"
using namespace std;

/*
Interfata pentru repository-ul de carti
*/
class Repository {
private:
	//Campurile repository-ului de carti
	vector<Book> books;
public:
	//Metodele repository-ului de carti

	/*
	Constructorul repository-ului de carti
	*/
	Repository();

	/*
	Functia adauga o carte in lista de carti
	@param book : cartea de adaugat (Book)
	@return 0 : operatia s-a efectuat cu succes
		    1 : cartea deja exista
			(int)
	*/
	int addBook(const Book& book);

	/*
	Functia sterge o carte din lista de carti dupa indicele din vector
	@param index : indicele cartii (int)
	@return 0 : operatia s-a efectuat cu succes
			1 : cartea nu exista
			(int)
	*/
	int removeBook(const int& index);

	/*
	Functia actualizeaza o carte din lista de carti de la indicele specificat
	@param index : indicele cartii (int)
	@param book : noua carte (Book)
	@return 0 : operatia s-a efectuat cu succes
			1 : cartea nu exista
			(int)
	*/
	int updateBook(const int& index, Book& book);

	/*
	Functia returneaza o copie a cartii de pe indicele specificat
	@return cartea (Book)
	*/
	optional<Book> getBook(const int& index) const;

	/*
	Functia returneaza o copie a listei de carti
	@return lista de carti (vector<Book>)
	*/
    const vector<Book>& getAllBooks() const;

	/*
	Functia returneaza daca cartea exista in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	bool bookExists(const Book& searchedBook) const;

	/*
	Functia returneaza daca exista o carte cu indicele transmis in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	bool indexExists(const unsigned int& index) const;
};
