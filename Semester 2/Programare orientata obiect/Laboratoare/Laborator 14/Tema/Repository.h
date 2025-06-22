#pragma once
#include <fstream>
#include <sstream>
#include <vector>
#include <optional>
#include <cstring>
#include "Book.h"
#include "Exceptions.h"
using namespace std;

class AbstractRepository {
private:
	vector<Book> books;
public:
	virtual void addBook(const Book& book) = 0;
	virtual void addBook(int index, const Book& book) = 0;
	virtual void removeBook(const int& index) = 0;
	virtual void removeBook(const Book& book) = 0;
	virtual void updateBook(const int& index, const Book& book) = 0;
	virtual const Book& getBook(const int& index) const = 0;
	virtual const vector<Book>& getAllBooks() const = 0;
	virtual bool bookExists(const Book& searchedBook) const = 0;
	virtual bool indexExists(const unsigned int& index) const = 0;
};

//class RandomRepository : public AbstractRepository {
//private:
//	//Campurile repository-ului de carti
//	vector<Book> books;
//public:
//	//Metodele repository-ului de carti
//
//	/*
//	Constructorul repository-ului de carti
//	*/
//	RandomRepository();
//
//	/*
//	Functia adauga o carte in lista de carti
//	@param book : cartea de adaugat (Book)
//	@throws : BookRepositoryException (cartea exista deja)
//	*/
//	virtual void addBook(const Book& book) override;
//
//	/*
//	Functia adauga o carte in lista de carti pe pozitia specificata
//	@param index : pozitia (int)
//	@param book : cartea de adaugat (Book)
//	@throws : BookRepositoryException (cartea exista deja)
//	*/
//	virtual void addBook(int index, const Book& book) override;
//
//	/*
//	Functia sterge o carte din lista de carti dupa indicele din vector
//	@param index : indicele cartii (int)
//	@throws : BookRepositoryException (cartea nu exista)
//	*/
//	virtual void removeBook(const int& index) override;
//
//	/*
//	Functia sterge o carte din lista de carti
//	@param book : cartea (Book)
//	@throws : BookRepositoryException (cartea nu exista)
//	*/
//	virtual void removeBook(const Book& book) override;
//
//	/*
//	Functia actualizeaza o carte din lista de carti de la indicele specificat
//	@param index : indicele cartii (int)
//	@param book : noua carte (Book)
//	@throws : BookRepositoryException (cartea nu exista)
//	*/
//	virtual void updateBook(const int& index, const Book& book) override;
//
//	/*
//	Functia returneaza o copie a cartii de pe indicele specificat
//	@return cartea (Book)
//	@throws : BookRepositoryException("Cartea nu exista!");
//	*/
//	virtual const Book& getBook(const int& index) const override;
//
//	/*
//	Functia returneaza lista de carti
//	@return lista de carti (vector<Book>&)
//	*/
//	virtual const vector<Book>& getAllBooks() const override;
//
//	/*
//	Functia returneaza daca cartea exista in lista de carti
//	@return true : cartea exista
//			false : cartea nu exista
//			(bool)
//	*/
//	virtual bool bookExists(const Book& searchedBook) const override;
//
//	/*
//	Functia returneaza daca exista o carte cu indicele transmis in lista de carti
//	@return true : cartea exista
//			false : cartea nu exista
//			(bool)
//	*/
//	virtual bool indexExists(const unsigned int& index) const override;
//
//	/*
//	Functia reseteaza vectorul din clasa Repository
//	*/
//	void clear();
//
//	/*
//	Functia genereaza o eroare cateodata
//	*/
//	void checkIfError() const;
//};

/*
Interfata pentru repository-ul de carti
*/
class Repository : public AbstractRepository {
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
	@throws : BookRepositoryException (cartea exista deja)
	*/
	virtual void addBook(const Book& book) override;

	/*
	Functia adauga o carte in lista de carti pe pozitia specificata
	@param index : pozitia (int)
	@param book : cartea de adaugat (Book)
	@throws : BookRepositoryException (cartea exista deja)
	*/
	virtual void addBook(int index, const Book& book) override;

	/*
	Functia sterge o carte din lista de carti dupa indicele din vector
	@param index : indicele cartii (int)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	virtual void removeBook(const int& index) override;

	/*
	Functia sterge o carte din lista de carti
	@param book : cartea (Book)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	virtual void removeBook(const Book& book) override;

	/*
	Functia actualizeaza o carte din lista de carti de la indicele specificat
	@param index : indicele cartii (int)
	@param book : noua carte (Book)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	virtual void updateBook(const int& index, const Book& book) override;

	/*
	Functia returneaza o copie a cartii de pe indicele specificat
	@return cartea (Book)
	@throws : BookRepositoryException("Cartea nu exista!");
	*/
	virtual const Book& getBook(const int& index) const override;

	/*
	Functia returneaza lista de carti
	@return lista de carti (vector<Book>&)
	*/
	virtual const vector<Book>& getAllBooks() const override;

	/*
	Functia returneaza daca cartea exista in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	virtual bool bookExists(const Book& searchedBook) const override;

	/*
	Functia returneaza daca exista o carte cu indicele transmis in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	virtual bool indexExists(const unsigned int& index) const override;

	/*
	Functia reseteaza vectorul din clasa Repository
	*/
	void clear();
};

class FileRepository : public Repository {
private:
	string filename;

	/*
	Functia salveaza continutul din repository in fisier
	*/
	void save() const;

	/*
	Functia incarca continutul din fisier in repository
	*/
	void load();
public:

	/*
	Constructorul clasei FileRepository
	@param filename : numele fisierului in care se salveaza datele (string)
	*/
	FileRepository(string filename);

	/*
	Functia adauga o carte in lista de carti si salveaza in fisier
	@param book : cartea de adaugat (Book)
	@throws : BookRepositoryException (cartea exista deja)
	*/
	void addBook(const Book& book) override;

	/*
	Functia adauga o carte in lista de carti pe pozitia specificata
	@param index : pozitia (int)
	@param book : cartea de adaugat (Book)
	@throws : BookRepositoryException (cartea exista deja)
	*/
	void addBook(int index, const Book& book) override;

	/*
	Functia sterge o carte din lista de carti dupa indicele din vector si salveaza in fisier
	@param index : indicele cartii (int)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	void removeBook(const int& index) override;

	/*
	Functia sterge o carte din lista de carti
	@param book : cartea (Book)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	void removeBook(const Book& book) override;

	/*
	Functia actualizeaza o carte din lista de carti de la indicele specificat si salveaza in fisier
	@param index : indicele cartii (int)
	@param book : noua carte (Book)
	@throws : BookRepositoryException (cartea nu exista)
	*/
	void updateBook(const int& index, const Book& book) override;

	/*
	Functia returneaza o copie a cartii de pe indicele specificat
	@return cartea (Book)
	@throws : BookRepositoryException("Cartea nu exista!");
	*/
	const Book& getBook(const int& index) const override;

	/*
	Functia returneaza o copie a listei de carti
	@return lista de carti (vector<Book>)
	*/
	const vector<Book>& getAllBooks() const override;

	/*
	Functia returneaza daca cartea exista in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	bool bookExists(const Book& searchedBook) const override;

	/*
	Functia returneaza daca exista o carte cu indicele transmis in lista de carti
	@return true : cartea exista
			false : cartea nu exista
			(bool)
	*/
	bool indexExists(const unsigned int& index) const override;
};
