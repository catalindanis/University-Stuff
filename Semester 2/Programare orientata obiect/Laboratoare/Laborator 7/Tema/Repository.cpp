#include "Repository.h"
#include <optional>
using namespace std;

Repository::Repository() {
	this->books = LinkedList<Book>();
}

void Repository::addBook(const Book& book) {
	if (this->bookExists(book))
		throw exception("Cartea exista deja!");
	this->books.add(book);
}

void Repository::removeBook(const int& index) {
	if (!this->indexExists(index))
		throw exception("Cartea nu exista!");
	this->books.remove(this->getAllBooks()[index]);
}

void Repository::updateBook(const int& index, Book& book) {
	if (!this->indexExists(index))
		throw exception("Cartea nu exista!");
	this->books.update(this->books.getAll()[index], book);
}

optional<Book> Repository::getBook(const int& index) const {
	if (!this->indexExists(index))
		return nullopt;
	return this->getAllBooks()[index];
}

const vector<Book> Repository::getAllBooks() const {
	return this->books.getAll();
}

bool Repository::bookExists(const Book& searchedBook) const {
	for (const auto& book : this->getAllBooks())
		if (searchedBook.equalTo(book))
			return true;
	return false;
}

bool Repository::indexExists(const unsigned int & index) const {
	return index >= 0 && index < this->books.getSize();
}





