#include "Repository.h"
#include <optional>
using namespace std;

Repository::Repository() {
	this->books = vector<Book>();
}

int Repository::addBook(const Book& book) {
	if (this->bookExists(book))
		return 1;
	this->books.push_back(book);
	return 0;
}

int Repository::removeBook(const int& index) {
	if (!this->indexExists(index))
		return 1;
	this->books.erase(this->books.begin() + index);
	return 0;
}

int Repository::updateBook(const int& index, Book& book) {
	if (!this->indexExists(index))
		return 1;
	this->books[index] = book;
	return 0;
}

optional<Book> Repository::getBook(const int& index) const {
	if (!this->indexExists(index))
		return nullopt;
	return this->books[index];
}

const vector<Book>& Repository::getAllBooks() const {
	return this->books;
}

bool Repository::bookExists(const Book& searchedBook) const {
	for (auto book : this->books)
		if (searchedBook.equalTo(book))
			return true;
	return false;
}

bool Repository::indexExists(const unsigned int & index) const {
	return index >= 0 && index < this->books.size();
}





