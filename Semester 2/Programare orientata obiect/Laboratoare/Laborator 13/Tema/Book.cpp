#include "Book.h"
using namespace std;

/*
Implementarile metodelor unei carti
*/

Book::Book(const string title, const string author, const string type, const int releaseYear) :
	id{ -1 }, title {title}, author{ author }, type{ type }, releaseYear{ releaseYear } {
}

Book::Book(const int& id, const string title, const string author, const string type, const int releaseYear) :
	id{ id }, title{ title }, author{ author }, type{ type }, releaseYear{ releaseYear } {
}


Book::Book(const Book& book){
	this->id = book.getId();
	this->title = book.getTitle();
	this->author = book.getAuthor();
	this->type = book.getType();
	this->releaseYear = book.getReleaseYear();
	//printf("copiere\n");
}

long Book::getId() const {
	return this->id;
}

string Book::getTitle() const {
	return this->title;
}

string Book::getAuthor() const {
	return this->author;
}

string Book::getType() const {
	return this->type;
}

int Book::getReleaseYear() const {
	return this->releaseYear;
}

void Book::setId(const int& id){
	this->id = id;
}

void Book::setTitle(const string newTitle) {
	this->title = newTitle;
}

void Book::setAuthor(const string newAuthor) {
	this->author = newAuthor;
}

void Book::setType(const string newType) {
	this->type = newType;
}

void Book::setReleaseYear(const int newReleaseYear) {
	this->releaseYear = newReleaseYear;
}

bool Book::equalTo(const Book& book) const{
	return
		this->title.compare(book.getTitle()) == 0 &&
		this->author.compare(book.getAuthor()) == 0 &&
		this->type.compare(book.getType()) == 0 &&
		this->releaseYear == book.getReleaseYear();
}

int Book::compareTitle(const Book& book) const {
	int returnValue = this->title.compare(book.getTitle());
	if (returnValue < 0)
		return -1;
	if (returnValue > 0)
		return 1;
	return returnValue;
}

int Book::compareAuthor(const Book& book) const {
	int returnValue = this->author.compare(book.getAuthor());
	if (returnValue < 0)
		return -1;
	if (returnValue > 0)
		return 1;
	return returnValue;
}

int Book::compareType(const Book& book) const {
	int returnValue = this->type.compare(book.getType());
	if (returnValue < 0)
		return -1;
	if (returnValue > 0)
		return 1;
	return returnValue;
}

int Book::compareReleaseYear(const Book& book) const {
	if (this->releaseYear < book.getReleaseYear())
		return -1;
	if (this->releaseYear > book.getReleaseYear())
		return 1;
	return 0;
}

bool Book::operator==(const Book& book) const {
	return this->equalTo(book);
}

