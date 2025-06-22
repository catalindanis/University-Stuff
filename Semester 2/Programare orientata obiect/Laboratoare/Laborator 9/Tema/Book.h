#pragma once
#include <string>
#include <iostream>
using namespace std;

/*
Interfata pentru o carte
*/
class Book {
private:
	//Campurile unei carti
	string title;
	string author;
	string type;
	int releaseYear;
public:
	//Metodele unei carti

	/*
	Constructorul unui obiect de tip carte
	@param title : titlul cartii (string)
	@param author : autorul cartii (string)
	@param type : tipul cartii (string)
	@param releaseYear : anul aparitiei cartii (int)
	*/
	Book(const string title, const string author, const string type, const int releaseYear);
	
	/*
	Copy constructor
	*/
	Book(const Book& book);

	/*
	Functia returneaza titlul unei carti
	@return titlul unei carti (string)
	*/
	string getTitle() const;

	/*
	Functia returneaza autorul unei carti
	@return autorul unei carti (string)
	*/
	string getAuthor() const;

	/*
	Functia returneaza genul unei carti
	@return genul unei carti (string)
	*/
	string getType() const;

	/*
	Functia returneaza anul aparitiei unei carti
	@return anul aparitiei unei carti (int)
	*/
	int getReleaseYear() const;

	/*
	Functia seteaza titlul unei carti
	@param newTitle : titlul cartii (string)
	*/
	void setTitle(const string newTitle);

	/*
	Functia seteaza autorul unei carti
	@param newAuthor : autorul cartii (string)
	*/
	void setAuthor(const string newAuthor);

	/*
	Functia seteaza genul unei carti
	@param newType : genul cartii (string)
	*/
	void setType(const string newType);

	/*
	Functia returneaza anul aparitiei unei carti
	@param newReleaseYear : anul de aparitiei al cartii (int)
	*/
	void setReleaseYear(const int newReleaseYear);

	/*
	Functia verifica daca 2 carti sunt egale
	@param book : cartea cu care se compara (Book)
	@return true : cartile sunt egale 
			false : in caz contrar
	*/
	bool equalTo(const Book& book) const;

	/*
	Functia compara titlul a 2 carti
	@param book : cartea cu care se compara (Book)
	@return -1 : titlul cartii este mai mic decat titlul cartii transmise
			 0 : titlul cartilor este egal
			 1 : titlul cartii este mai mare decat titlul cartii transmise
	*/
	int compareTitle(const Book& book) const;

	/*
	Functia compara autorul a 2 carti
	@param book : cartea cu care se compara (Book)
	@return -1 : autorul cartii este mai mic decat autorul cartii transmise
			 0 : autorul cartilor este egal
			 1 : autorul cartii este mai mare decat autorul cartii transmise
	*/
	int compareAuthor(const Book& book) const;

	/*
	Functia compara genul a 2 carti
	@param book : cartea cu care se compara (Book)
	@return -1 : genul cartii este mai mic decat genul cartii transmise
			 0 : genul cartilor este egal
			 1 : genul cartii este mai mare decat genul cartii transmise
	*/
	int compareType(const Book& book) const;

	/*
	Functia compara anul publicatiei a 2 carti
	@param book : cartea cu care se compara (Book)
	@return -1 : anul publicatiei cartii este mai mic decat anul publicatiei cartii transmise
			 0 : anul publicatiei cartilor este egal
			 1 : anul publicatiei cartii este mai mare decat anul publicatiei cartii transmise
	*/
	int compareReleaseYear(const Book& book) const;

	/*
	Functia suprascrie operatorul ==
	@param book : cartea cu care se compara
	@return : true (cartile sunt egale)
			  false (in caz contrar)
			  (bool)
	*/
	bool operator==(const Book& book) const;
};
