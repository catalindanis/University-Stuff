#include "Repository.h"
#include <optional>
#include <cstdlib>
#include <ctime> 
using namespace std;

Repository::Repository() {
	this->books = vector<Book>();
}

void Repository::addBook(const Book& book) {
	if (this->bookExists(book))
		throw BookRepositoryException("Cartea exista deja!");
	this->books.emplace_back(book);
}

void Repository::addBook(int index, const Book& book) {
	if (this->bookExists(book))
		throw BookRepositoryException("Cartea exista deja!");
	this->books.insert(this->books.begin() + index, book);
}


void Repository::removeBook(const int& index) {
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	this->books.erase(this->books.begin() + index);
}

void Repository::removeBook(const Book& book) {
	if(!this->bookExists(book))
		throw BookRepositoryException("Cartea nu exista!");
	this->books.erase(
		remove(this->books.begin(), this->books.end(), book),
		this->books.end()
	);
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

void Repository::clear() {
	this->books = vector<Book>();
}

FileRepository::FileRepository(string filename) : Repository::Repository() {
	this->filename = filename;
	this->load();
}

void FileRepository::addBook(const Book& book) {
	this->load();
	Repository::addBook(book);
	this->save();
}

void FileRepository::addBook(int index, const Book& book) {
	this->load();
	Repository::addBook(index, book);
	this->save();
}

void FileRepository::removeBook(const int& index) {
	this->load();
	Repository::removeBook(index);
	this->save();
}

void FileRepository::removeBook(const Book& book) {
	this->load();
	Repository::removeBook(book);
	this->save();
}

void FileRepository::updateBook(const int& index, const Book& book) {
	this->load();
	Repository::updateBook(index, book);
	this->save();
}

const Book& FileRepository::getBook(const int& index) const {
	return Repository::getBook(index);
}

const vector<Book>& FileRepository::getAllBooks() const {
	return Repository::getAllBooks();
}

bool FileRepository::bookExists(const Book& searchedBook) const {
	return Repository::bookExists(searchedBook);
}

bool FileRepository::indexExists(const unsigned int& index) const {
	return Repository::indexExists(index);
}

void FileRepository::save() const {
	ofstream fout(this->filename);
	for (const auto& book : this->getAllBooks()) {
		fout << book.getTitle() << "," << book.getAuthor() << "," << book.getType() << "," << book.getReleaseYear() << '\n';
	}
	fout.close();
}

vector<string> splitByComma(const string& input) {
	vector<string> result;
	stringstream ss(input);
	string item;

	while (getline(ss, item, ',')) {
		result.push_back(item);
	}

	return result;
}

void FileRepository::load() {
	Repository::clear();

	ifstream fin(this->filename);
	if (!fin.is_open()) {
		ofstream fout(filename);
		fout.close();
	}

	char line[1001];
	while (fin.getline(line, 1000)) {
		vector<string> values = splitByComma(line);
		const Book book{values[0], values[1], values[2], atoi(values[3].c_str())};
		Repository::addBook(book);
	}
	fin.close();
}


RandomRepository::RandomRepository() {
	checkIfError();
	this->books = vector<Book>();
}

void RandomRepository::addBook(const Book& book) {
	checkIfError();
	if (this->bookExists(book))
		throw BookRepositoryException("Cartea exista deja!");
	this->books.emplace_back(book);
}

void RandomRepository::addBook(int index, const Book& book) {
	checkIfError();
	if (this->bookExists(book))
		throw BookRepositoryException("Cartea exista deja!");
	this->books.insert(this->books.begin() + index, book);
}


void RandomRepository::removeBook(const int& index) {
	checkIfError();
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	this->books.erase(this->books.begin() + index);
}

void RandomRepository::removeBook(const Book& book) {
	checkIfError();
	if (!this->bookExists(book))
		throw BookRepositoryException("Cartea nu exista!");
	this->books.erase(
		remove(this->books.begin(), this->books.end(), book),
		this->books.end()
	);
}

void RandomRepository::updateBook(const int& index, const Book& book) {
	checkIfError();
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	this->books[index] = book;
}

const Book& RandomRepository::getBook(const int& index) const {
	checkIfError();
	if (!this->indexExists(index))
		throw BookRepositoryException("Cartea nu exista!");
	return this->books[index];
}

const vector<Book>& RandomRepository::getAllBooks() const {
	checkIfError();
	return this->books;
}

bool RandomRepository::bookExists(const Book& searchedBook) const {
	checkIfError();
	for (const auto& book : this->books)
		if (searchedBook.equalTo(book))
			return true;
	return false;
}

bool RandomRepository::indexExists(const unsigned int& index) const {
	checkIfError();
	return index >= 0 && index < this->books.size();
}

void RandomRepository::clear() {
	this->checkIfError();
	this->books = vector<Book>();
}

void RandomRepository::checkIfError() const {
	srand((unsigned int) std::time(nullptr));
	int random_number = std::rand() % 2 + 1;
	if (random_number == 1)
		throw exception("Random repository failed!");
}