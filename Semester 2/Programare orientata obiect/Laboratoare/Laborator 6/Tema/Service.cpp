#include "Service.h"
#include <algorithm>

Service::Service(Repository repository, Validator validator) : repository{ repository }, validator{ validator } {}


int Service::addBook(const string title, const string author, const string type, const int releaseYear) {
	if (this->validator.validateTitle(title) == false)
		return 2;
	if (this->validator.validateAuthor(author) == false)
		return 3;
	if (this->validator.validateType(type) == false)
		return 4;
	if (this->validator.validateReleaseYear(releaseYear) == false)
		return 5;

	Book book{ title, author, type, releaseYear };

	return this->repository.addBook(book);
}

int Service::removeBook(const int& index) {
	return this->repository.removeBook(index);
}

int Service::updateBook(const int& index, const string title, const string author, const string type, const int releaseYear) {
	if (this->validator.validateTitle(title) == false)
		return 2;
	if (this->validator.validateAuthor(author) == false)
		return 3;
	if (this->validator.validateType(type) == false)
		return 4;
	if (this->validator.validateReleaseYear(releaseYear) == false)
		return 5;

	Book book{ title, author, type, releaseYear };

	return this->repository.updateBook(index, book);
}

const vector<Book>& Service::getAllBooks() const {
	return this->repository.getAllBooks();
}

vector<Book> Service::searchBooksByTitle(const string& title) const {
	vector<Book> books;
	for (auto& book : this->repository.getAllBooks()) {
		if (book.getTitle().find(title) != string::npos)
			books.push_back(book);
	}

	return books;
}

vector<Book> Service::filterBooksByTitle(const string& title) const {
	vector<Book> books;
	for (auto& book : this->repository.getAllBooks()) {
		if (book.getTitle().compare(title) == 0)
			books.push_back(book);
	}

	return books;
}

vector<Book> Service::filterBooksByReleaseYear(const int& releaseYear) const {
	vector<Book> books;
	for (auto& book : this->repository.getAllBooks()) {
		if (book.getReleaseYear() == releaseYear)
			books.push_back(book);
	}

	return books;
}

vector<Book> Service::sortedBooksByTitle() const {
	vector<Book> books = this->repository.getAllBooks();

	for (unsigned int i = 0; i < books.size(); i++)
		for (unsigned int j = i + 1; j < books.size(); j++)
			if (books[i].compareTitle(books[j]) > 0)
				swap(books[i], books[j]);

	return books;
}

vector<Book> Service::sortedBooksByAuthor() const {
	vector<Book> books = this->repository.getAllBooks();

	for (unsigned int i = 0; i < books.size(); i++)
		for (unsigned int j = i + 1; j < books.size(); j++)
			if (books[i].compareAuthor(books[j]) > 0)
				swap(books[i], books[j]);

	return books;
}

vector<Book> Service::sortedBooksByReleaseDateAndType() const {
	vector<Book> books = this->repository.getAllBooks();

	for (unsigned int i = 0; i < books.size(); i++)
		for (unsigned int j = i + 1; j < books.size(); j++)
			if (books[i].compareReleaseYear(books[j]) > 0 || (
				(books[i].compareReleaseYear(books[j]) == 0 &&
					books[i].compareType(books[j]) > 0)))
				swap(books[i], books[j]);

	return books;
}








