#include "Book.h"
#include "Validator.h"
#include "Repository.h"
#include "Service.h"
#include "CartService.h"
#include "assert.h"
#include <iostream>

void runDomainTests() {
	Book book1{ "Book1", "Author1", "Type1", 2020 };
	Book test{ "Book1", "Author1", "Type1", 2020 };
	assert(book1 == test);

	assert(book1.getTitle().compare("Book1") == 0);
	assert(book1.getAuthor().compare("Author1") == 0);
	assert(book1.getType().compare("Type1") == 0);
	assert(book1.getReleaseYear() == 2020);

	book1.setTitle("Book");
	book1.setAuthor("Author");
	book1.setType("Type");
	book1.setReleaseYear(2000);

	assert(book1.getTitle().compare("Book") == 0);
	assert(book1.getAuthor().compare("Author") == 0);
	assert(book1.getType().compare("Type") == 0);
	assert(book1.getReleaseYear() == 2000);

	Book book2{ "Book", "Author", "Type", 2000 };

	assert(book1.equalTo(book2) == true);

	book2.setTitle("Book1");
	assert(book1.equalTo(book2) == false);

	book2.setTitle("Book1");
	assert(book1.compareTitle(book2) == -1);

	book2.setTitle("Book");
	assert(book1.compareTitle(book2) == 0);

	book2.setTitle("Boo");
	assert(book1.compareTitle(book2) == 1);

	book2.setAuthor("Author1");
	assert(book1.compareAuthor(book2) == -1);

	book2.setAuthor("Author");
	assert(book1.compareAuthor(book2) == 0);

	book2.setAuthor("Autho");
	assert(book1.compareAuthor(book2) == 1);

	book2.setType("Type1");
	assert(book1.compareType(book2) == -1);

	book2.setType("Type");
	assert(book1.compareType(book2) == 0);

	book2.setType("Typ");
	assert(book1.compareType(book2) == 1);

	book2.setReleaseYear(2021);
	assert(book1.compareReleaseYear(book2) == -1);

	book2.setReleaseYear(2000);
	assert(book1.compareReleaseYear(book2) == 0);

	book2.setReleaseYear(1999);
	assert(book1.compareReleaseYear(book2) == 1);
}

void runValidatorTests() {
	Validator validator;

	assert(validator.validateTitle("") == false);
	assert(validator.validateTitle("_") == false);
	assert(validator.validateTitle("A") == true);

	assert(validator.validateAuthor("") == false);
	assert(validator.validateAuthor("1") == false);
	assert(validator.validateAuthor("A") == true);

	assert(validator.validateType("") == false);
	assert(validator.validateType("1") == false);
	assert(validator.validateType("A") == true);

	assert(validator.validateReleaseYear(0) == false);
	assert(validator.validateReleaseYear(2026) == false);
	assert(validator.validateReleaseYear(2020) == true);
}

void runRepositoryTests() {
	FileRepository repository{ "test" };

	Book book1{0, "Book1", "Author1", "Type1", 2020 };

	try {
		repository.addBook(0, book1);
		assert(false);
	}
	catch (BookRepositoryException e) {
		assert(strcmp(e.what(), "Cartea exista deja!") == 0);
	}

	assert(repository.indexExists(0) == true);
	assert(repository.indexExists(1) == false);

	Book book2{1, "Book2", "Author2", "Type2", 2021 };

	try {
		repository.updateBook(1, book2);
		assert(false);
	}
	catch (BookRepositoryException e) {}

	repository.updateBook(0, book2);
	try {
		repository.updateBook(0, book2);
		assert(false);
	}
	catch (BookRepositoryException e) {}

	try {
		repository.addBook(book2);
		assert(false);
	}
	catch (BookRepositoryException e) {}

	try {
		repository.getBook(2);
		assert(false);
	}
	catch (BookRepositoryException e) {}

	assert(repository.bookExists(book1) == false);
	assert(repository.bookExists(book2) == true);

	try {
		repository.removeBook(2);
		assert(false);
	}
	catch (exception e) {}

	repository.removeBook(book2);

	try {
		repository.removeBook(book1);
		assert(false);
	}
	catch (exception e) {}

	assert(repository.getAllBooks().size() == 0);

	repository.addBook(book1);
	repository.removeBook(0);

	assert(repository.getAllBooks().size() == 0);

	repository.addBook(book1);
	try {
		repository.addBook(book1);
		assert(false);
	}
	catch (exception e) {

	}
}

void runServiceTests() {
	Repository repository;
	Validator validator;
	Service service{ repository, validator };

	try {
		service.undoLastOperation();
		assert(false);
	}
	catch (exception e) {

	}

	try {
		service.addBook("", "a", "t", 1);
		assert(false);
	}
	catch (exception e) {}
	try {
		service.addBook("t", "", "t", 1);
		assert(false);

	}
	catch (exception e) {}
	try {
		service.addBook("t", "a", "", 1);
		assert(false);
	}
	catch (BookCreationException e) {
		assert(strcmp(e.what(), "Genul este invalid!") == 0);
	}
	try {
		service.addBook("t", "a", "t", 0);
		assert(false);

	}
	catch (exception e) {}
	service.addBook("t", "a", "t", 1);

	service.undoLastOperation();
	assert(service.getAllBooks().size() == 0);

	service.addBook("t", "a", "t", 1);

	try {
		service.addBook("t", "a", "t", 1);
		assert(false);
	}
	catch (exception e) {}
	assert(service.getAllBooks().size() == 1);

	try {
		service.updateBook(-1, "t", "a", "t", 1);
		assert(false);
	}
	catch (exception e) {}
	try {
		service.updateBook(0, "", "a", "t", 1);
		assert(false);
	}
	catch (exception e) {}
	try {
		service.updateBook(0, "t", "", "t", 1);
		assert(false);
	}
	catch (exception e) {}
	try {
		service.updateBook(0, "t", "a", "", 1);
		assert(false);
	}
	catch (exception e) {}
	try {
		service.updateBook(0, "t", "a", "t", 0);
		assert(false);
	}
	catch (exception e) {}

	Book book = service.getBookByIndex(1);
	service.updateBook(1, "title", "author", "type", 2000);

	assert(book.equalTo(service.getBookByIndex(1)) == false);

	service.undoLastOperation();
	assert(book.equalTo(service.getBookByIndex(1)));

	service.updateBook(1, "title", "author", "type", 2000);

	try {
		service.removeBook(2);
		assert(false);
	}
	catch (exception e) {}
	service.removeBook(1);

	service.undoLastOperation();
	assert(service.getAllBooks().size() == 1);

	service.removeBook(1);

	assert(service.getAllBooks().size() == 0);

	service.addBook("test", "author", "type", 1);
	service.addBook("testd", "author", "type", 2);
	service.addBook("testt", "author", "type", 3);
	assert(service.getAllBooks().size() == 3);

	assert(service.searchBooksByTitle("test").size() == 3);
	assert(service.searchBooksByTitle("testd").size() == 1);
	assert(service.searchBooksByTitle("testb").size() == 0);

	assert(service.filterBooksByTitle("tes").size() == 0);
	assert(service.filterBooksByTitle("testd").size() == 1);

	assert(service.filterBooksByReleaseYear(1).size() == 1);
	assert(service.filterBooksByReleaseYear(4).size() == 0);

	service.removeBook(3);
	service.removeBook(2);
	service.removeBook(1);
	try {
		service.removeBook(1);
		assert(false);
	}
	catch (exception e) {}
	assert(service.getAllBooks().size() == 0);

	service.addBook("testd", "auth", "typ", 4);
	service.addBook("testu", "autho", "ty", 3);
	service.addBook("testp", "authorr", "typee", 3);
	service.addBook("testt", "author", "type", 2);

	assert(service.getAllBooks().size() == 4);

	vector<Book> result = service.sortedBooksByTitle();

	for (unsigned int i = 0; i < result.size() - 1; i++)
		assert(result[i].compareTitle(result[i + 1]) <= 0);

	result = service.sortedBooksByAuthor();

	for (unsigned int i = 0; i < result.size() - 1; i++)
		assert(result[i].compareAuthor(result[i + 1]) <= 0);

	result = service.sortedBooksByReleaseDateAndType();

	for (unsigned int i = 0; i < result.size() - 1; i++)
		assert(result[i].compareReleaseYear(result[i + 1]) < 0 ||
			result[i].compareType(result[i + 1]) < 0);

	int oldSize = (int)service.getAllBooks().size();
	service.loadDefaultBooks();
	assert(service.getAllBooks().size() == oldSize + 20);
}

void runCartServiceTests() {
	Repository repository;
	Validator validator;
	Service service{ repository, validator };

	CartService cartService{ service };
	service.loadDefaultBooks();

	cartService.generateCart(21);
	assert(cartService.size() == 21);

	cartService.deleteAll();

	cartService.add("Quantum Drift");
	cartService.add("");
	assert(cartService.size() == 1);

	for (const auto& book : cartService.getAllBooks())
		assert(book.equalTo(service.getBookByIndex(2)));

	cartService.add("Quantum Drift");

	map<string, BookDTO> map = cartService.getReports();
	assert(map["Quantum Drift"].getQuantity() == 2);
	assert(map["Quantum Drift"].getType().compare("Science Fiction") == 0);

	map["Quantum Drift"].decreaseQuantity(1);

	cartService.deleteAll();
	assert(cartService.size() == 0);

	cartService.generateCart(5);
	assert(cartService.size() == 5);

	cartService.saveToFile("test");
}

void runAllTests() {
	runDomainTests();
	runValidatorTests();
	runRepositoryTests();
	runServiceTests();
	runCartServiceTests();
}
//
