#pragma once
#include <string>
using namespace std;

class Validator {
public:
	/*
	Functia valideaza titlul unei carti
	@param title : titlul cartii (string)
	@return true : titlul este valid
			false : in caz contrar
			(bool)
	*/
	bool validateTitle(const string& title) const;

	/*
	Functia valideaza autorul unei carti
	@param author : autorul cartii (string)
	@return true : autorul este valid
			false : in caz contrar
			(bool)
	*/
	bool validateAuthor(const string& author) const;

	/*
	Functia valideaza genul unei carti
	@param type : genul cartii (string)
	@return true : genul este valid
			false : in caz contrar
			(bool)
	*/
	bool validateType(const string& type) const;

	/*
	Functia valideaza anul publicatiei unei carti
	@param releaseYear : anul publicatiei cartii (int)
	@return true : anul publicatiei este valid
			false : in caz contrar
			(int)
	*/
	bool validateReleaseYear(const int& releaseYear) const;
};
