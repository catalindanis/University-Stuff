
#include "Stiva.h"
#include <exception>
#include <iostream>

#include "Iterator.h"

#define INITIAL_CAPACITY 2
#define ALPHA 0.7
#define BETA 0.3

Stiva::Stiva() {
	// (*this).size = 0;  // (*this). este echivalent cu this->
	// size = 0;
	// in constructor, daca nu exista o variabila cu numele unui atribut, atunci nu e obligatoriu de folosit this
	this->size = 0;
	this->capacity = INITIAL_CAPACITY;
	this->elements = new TElem[this->capacity];
}

void Stiva::realloc(int capacity) {
	// realoca vectorul `elements` cu capacitatea `capacity`
	TElem *elements = new TElem[capacity];
	for (int i = 0; i < std::min(this->size, capacity); i++) {
		// min asigura ca nu suprascriem adrese de memorie nealocate
		elements[i] = this->elements[i];
	}
	delete [] this->elements;
	this->elements = elements;
	this->capacity = capacity;
}

// favorabil: Theta(1), defavorabil: Theta(n), mediu amortizat: Theta(1)
void Stiva::adauga(TElem elem) {
	if (this->size >= ALPHA * this->capacity) {
		this->realloc(this->capacity * 2);
	}
	this->elements[this->size] = elem;
	this->size++;
}

// favorabil, defavorabil, mediu: Theta(1)
//arunca exceptie daca coada e vida
TElem Stiva::element() const {
	if (this->size == 0) {
		throw std::exception();
	}
	return this->elements[this->size - 1];
}

// favorabil: Theta(1), defavorabil: Theta(n), mediu amortizat: Theta(1)
TElem Stiva::sterge() {
	TElem element = this->element();
	this->size--;
	if (this->size <= BETA * this->capacity) {
		this->realloc(this->capacity / 2);
	}
	return element;
}

// favorabil, defavorabil, mediu: Theta(1)
bool Stiva::vida() const {
	return this->size == 0;
}

// favorabil: Theta(1), defavorabil: Theta(n), mediu: Theta(n), overall: O(n)
bool Stiva::cautare(TElem elem) const {
	for (int i = 0; i < this->size; i++) {
		if (elem == this->elements[i]) {
			return true;
		}
	}
	return false;
}

Iterator Stiva::iterator() const {
	return  Iterator(*this);
}

Stiva::~Stiva() {
	delete [] this->elements;
	this->elements = nullptr; // previne dangling pointer (pointer care arata catre o adresa invalida)
}

