#include "Repository.h"
#include <optional>
using namespace std;

Repository::Repository() {
	this->books = vector<Book>();
}

void Repository::addBook(const Book& book) {
	if (this->bookExists(book))
		throw BookRepositoryException("Cartea exista deja!");
	this->books.emplace_back(book);
}

void Repository::removeBook(const int& index) {
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	this->books.erase(this->books.begin() + index);
}

void Repository::updateBook(const int& index, const Book& book) {
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	this->books[index] = book;
}

const Book& Repository::getBook(const int& index) const {
	if (!this->indexExists(index)) 
		throw BookRepositoryException("Cartea nu exista!");
	return this->books[index];
}

const vector<Book>& Repository::getAllBooks() const {
	return this->books;
}

bool Repository::bookExists(const Book& searchedBook) const {
	for (const auto& book : this->books)
		if (searchedBook.equalTo(book))
			return true;
	return false;
}

bool Repository::indexExists(const unsigned int & index) const {
	return index >= 0 && index < this->books.size();
}





