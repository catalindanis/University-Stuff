#pragma once
#include "Repository.h"
#include "Validator.h"
#include <functional>
#include <string>

/*
Interfata pentru service-ul de carti
*/
class Service {
private:
	/*
	Campurile service-ului de carti
	*/
	Repository repository;
	Validator validator;
public:

	/*
	Constructorul service-ului de carti
	*/
	Service(Repository repository, Validator validator);

	/*
	Functia adauga o carte in service-ul de carti
	@param title titlul cartii (string)
	@param author autorul cartii (string)
	@param type genul cartii (string)
	@param releaseYear anul publicatiei cartii (int)
	@return 0 : operatia s-a efectuat cu succes
			1 : cartea exista deja
			2 : titlul nu este valid
			3 : autorul nu este valid
			4 : genul nu este valid
			5 : anul publicatiei nu este valid
			(int)
	*/
	int addBook(const string title, const string author, const string type, const int releaseYear);

	/*
	Functia sterge o carte din service-ul de carti
	@param index : indicele cheltuielii (int)
	@return 0 : operatia s-a efectuat cu succes
			1 : cartea nu exista
			(int)
	*/
	int removeBook(const int& index);

	/*
	Functia actualizeaza o carte din service-ul de carti
	@param index : indicele cheltuielii (int)
	@param title titlul cartii (string)
	@param author autorul cartii (string)
	@param type genul cartii (string)
	@param releaseYear anul publicatiei cartii (int)
	@return 0 : operatia s-a efectuat cu succes
			1 : cartea nu exista
			2 : titlul nu este valid
			3 : autorul nu este valid
			4 : genul nu este valid
			5 : anul publicatiei nu este valid
			(int)
	*/
	int updateBook(const int& index, const string title, const string author, const string type, const int releaseYear);

	/*
	Functia returneaza lista de carti
	@return lista de carti (vector<Book>)
	*/
    const vector<Book>& getAllBooks() const;

	/*
	Functia returneaza lista de carti ce contin titlul transmis ca parametru
	@param title titlul cartii (string)
	@return lista de carti (vector<Book>)
	*/
	vector<Book> searchBooksByTitle(const string& title) const;

	/*
	Functia returneaza lista de carti filtrata dupa titlul transmis ca parametru
	@param title titlul cartii (string)
	@return lista de carti (vector<Book>)
	*/
	vector<Book> filterBooksByTitle(const string& title) const;

	/*
	Functia returneaza lista de carti filtrata dupa anul aparitiei transmis ca parametru
	@param releaseYear anul aparitiei cartii (string)
	@return lista de carti (vector<Book>)
	*/
	vector<Book> filterBooksByReleaseYear(const int& releaseYear) const;

	/*
	Functia returneaza lista de carti sortata dupa titlu
	@return lista de carti (vector<Book>)
	*/
	vector<Book> sortedBooksByTitle() const;

	/*
	Functia returneaza lista de carti sortata dupa autor
	@return lista de carti (vector<Book>)
	*/
	vector<Book> sortedBooksByAuthor() const;

	/*
	Functia returneaza lista de carti sortata dupa anul aparitiei + gen
	@return lista de carti (vector<Book>)
	*/
	vector<Book> sortedBooksByReleaseDateAndType() const;
};
