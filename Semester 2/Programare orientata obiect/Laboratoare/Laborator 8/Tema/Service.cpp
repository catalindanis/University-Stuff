#include "Service.h"
#include <algorithm>
#include <iterator>
using std::copy_if;
using std::back_inserter;

Service::Service(Repository repository, Validator validator) : repository{ repository }, validator{ validator } {}

void Service::addBook(const string title, const string author, const string type, const int releaseYear) {
	if (this->validator.validateTitle(title) == false)
		throw BookCreationException("Titlul este invalid!");
	if (this->validator.validateAuthor(author) == false)
		throw BookCreationException("Autorul este invalid!");
	if (this->validator.validateType(type) == false)
		throw BookCreationException("Genul este invalid!");
	if (this->validator.validateReleaseYear(releaseYear) == false)
		throw BookCreationException("Anul publicatiei este invalid!");

	Book book{ title, author, type, releaseYear };
	return this->repository.addBook(book);
}

void Service::removeBook(const int& index) {
	this->repository.removeBook(index);
}

void Service::updateBook(const int& index, const string title, const string author, const string type, const int releaseYear) {
	if (this->validator.validateTitle(title) == false)
		throw BookCreationException("Titlul este invalid!");
	if (this->validator.validateAuthor(author) == false)
		throw BookCreationException("Autorul este invalid!");
	if (this->validator.validateType(type) == false)
		throw BookCreationException("Genul este invalid!");
	if (this->validator.validateReleaseYear(releaseYear) == false)
		throw BookCreationException("Anul publicatiei este invalid!");

	this->repository.updateBook(index, Book{ title, author, type, releaseYear });
}

const vector<Book>& Service::getAllBooks() const {
	return this->repository.getAllBooks();
}

const Book& Service::getBookByIndex(const int& index) const {
	return this->repository.getBook(index);
}


vector<Book> Service::searchBooksByTitle(const string& title) const {
	vector<Book> books;

	copy_if(
		this->repository.getAllBooks().begin(),
		this->repository.getAllBooks().end(),
		back_inserter(books),
		[title](const auto& book) {
			return book.getTitle().find(title) != string::npos;
		}
	);

	return books;
}

vector<Book> Service::filterBooksByTitle(const string& title) const {
	vector<Book> books;
	
	copy_if(
		this->repository.getAllBooks().begin(),
		this->repository.getAllBooks().end(),
		back_inserter(books),
		[title](const auto& book) {
			return book.getTitle().compare(title) == 0;
		}
	);

	return books;
}

vector<Book> Service::filterBooksByReleaseYear(const int& releaseYear) const {
	vector<Book> books;

	copy_if(
		this->repository.getAllBooks().begin(),
		this->repository.getAllBooks().end(),
		back_inserter(books),
		[releaseYear](const auto& book) {
			return book.getReleaseYear() == releaseYear;
		}
	);

	return books;
}

vector<Book> Service::sortedBooksByTitle() const {
	vector<Book> books = this->repository.getAllBooks();

	sort(books.begin(), books.end(), [](const auto& b1, const auto& b2) {
		return b1.compareTitle(b2) <= 0;
	});

	return books;
}

vector<Book> Service::sortedBooksByAuthor() const {
	vector<Book> books = this->repository.getAllBooks();

	sort(books.begin(), books.end(), [](const auto& b1, const auto& b2) {
		return b1.compareAuthor(b2) <= 0;
	});

	return books;
}

vector<Book> Service::sortedBooksByReleaseDateAndType() const {
	vector<Book> books = this->repository.getAllBooks();

	sort(books.begin(), books.end(), [](const auto& b1, const auto& b2) {
		if (b1.compareReleaseYear(b2) == 0)
			return b1.compareType(b2) <= 0;
		return b1.compareReleaseYear(b2) < 0;
	});

	return books;
}

void Service::loadDefaultBooks() {
	this->repository.addBook(Book{ "Whispers in the Fog", "Alice Cartwright", "Mystery", 2018 });
	this->repository.addBook(Book{ "Quantum Drift", "Nathan O’Connell", "Science Fiction", 2020 });
	this->repository.addBook(Book{ "The Ashen Grove", "Miriam Blackwell", "Horror", 2016 });
	this->repository.addBook(Book{ "Code & Consequence", "Rishi Talwar", "Tech Thriller", 2022 });
	this->repository.addBook(Book{ "Beneath the Crimson Sky", "Sofia Andros", "Historical Fiction", 2019 });
	this->repository.addBook(Book{ "A Symphony of Stars", "Lin Zhang", "Romance", 2021 });
	this->repository.addBook(Book{ "The Fractal Mind", "Tobias R. Milton", "Psychological Thriller", 2023 });
	this->repository.addBook(Book{ "Dreamwalker’s Descent", "Zara Delaney", "Fantasy", 2017 });
	this->repository.addBook(Book{ "Broken Algorithms", "Keenan Wright", "Cyberpunk", 2024 });
	this->repository.addBook(Book{ "Paper Feathers", "Anika Sorensen", "Young Adult", 2015 });
	this->repository.addBook(Book{ "The Edge of Silence", "Elias Monroe", "Drama", 2018 });
	this->repository.addBook(Book{ "Viral Minds", "Dr. Samantha Beck", "Medical Fiction", 2021 });
	this->repository.addBook(Book{ "The Eclipsed Heir", "Julianne Holloway", "Fantasy", 2020 });
	this->repository.addBook(Book{ "Shadows of the Republic", "Marcus D’Angelo", "Political Fiction", 2023 });
	this->repository.addBook(Book{ "Orbit’s End", "Elio Navarro", "Sci-Fi Thriller", 2024 });
	this->repository.addBook(Book{ "Baking with Babushka", "Tatyana Lebedev", "Cookbook", 2016 });
	this->repository.addBook(Book{ "The Philosopher’s Shadow", "Omar al-Sayed", "Philosophical Fiction", 2019 });
	this->repository.addBook(Book{ "From Circuit to Soul", "Ada R. Jensen", "Tech Memoir", 2022 });
	this->repository.addBook(Book{ "Canvas of Echoes", "Giulia Ventresca", "Art History", 2020 });
	this->repository.addBook(Book{ "The Hiker’s Journal", "Caleb Frost", "Adventure", 2017 });

}









