#include "BookDTO.h"

BookDTO::BookDTO(const Book& book) : type(book.getType()), quantity(1) {}

BookDTO::BookDTO() {
	this->quantity = 0;
}

void BookDTO::increaseQuantity(const int& size) {
	this->quantity += size;
}

void BookDTO::decreaseQuantity(const int& size) {
	this->quantity -= size;
}

string BookDTO::getType() const {
	return this->type;
}

int BookDTO::getQuantity() const {
	return this->quantity;
}

BookDTO::~BookDTO() {}