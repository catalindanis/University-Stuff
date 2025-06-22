#pragma once
#include <string>
#include "Book.h"
using std::string;

class BookDTO {
private:
	string type;
	int quantity;
public:
	/*
	Constructorul clasei BookDTO
	*/
	BookDTO(const Book& book);

	BookDTO();
	
	/*
	Functia creste cantitatea obiectului
	*/
	void increaseQuantity(const int& size);

	/*
	Functia scade cantitatea obiectului
	*/
	void decreaseQuantity(const int& size);

	/*
	Functia returneaza tipul obiectului
	*/
	string getType() const;

	/*
	Functia returneaza cantitatea obiectului
	*/
	int getQuantity() const;

	~BookDTO();
};