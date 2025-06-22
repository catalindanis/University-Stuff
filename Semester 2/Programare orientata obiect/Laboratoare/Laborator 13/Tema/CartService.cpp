#include "CartService.h"
#include <random>
using std::mt19937;
using std::uniform_int_distribution;

CartService::CartService(Service& service) : cart(), booksService(service), report(map<string, BookDTO>()) {}

void CartService::add(string title) {
	for (const auto& book : this->booksService.getAllBooks()) {
		if (title.compare(book.getTitle()) == 0) {
			if (this->report.count(title) == 0)
				this->report[title] = BookDTO(book);
			else
				this->report[title].increaseQuantity(1);
			this->cart.emplace_back(book);
		}
	}
	this->notify();
}

void CartService::deleteAll() {
	this->report.clear();
	this->cart.clear();
	this->notify();
}

void CartService::generateCart(int size) {
	std::mt19937 mt{ std::random_device{}() };
	std::uniform_int_distribution<> dist(1, (int)this->booksService.getAllBooks().size());
	while (size > 0) {
		int rndNr = dist(mt);
		int position = 1;
		for (const auto& book : this->booksService.getAllBooks())
			if (position == rndNr) {
				if (this->report.count(book.getTitle()) == 0)
					this->report[book.getTitle()] = BookDTO(book);
				else
					this->report[book.getTitle()].increaseQuantity(1);
				this->cart.emplace_back(book);
				break;
			}
			else position++;

		size--;
	}
	this->notify();
}

int CartService::size() const {
	return (int)this->cart.size();
}


const vector<Book>& CartService::getAllBooks() const {
	return this->cart;
}

map<string, BookDTO> CartService::getReports() const{
	return this->report;
}

void CartService::saveToFile(string fileName) const {
	fileName = fileName + ".html";
	ofstream fout(fileName);

	fout << "<h2>" << "Title | Author | Type | Release year" << "</h2>";

	for (const auto& book : this->cart) {
		fout << "<h2>" << book.getTitle() << " | " << book.getAuthor() << " | " << book.getType() << " | " << book.getReleaseYear() << "</h2>";
	}

	fout.close();
}


