#include "Iterator.h"
#include "Stiva.h"
#include <exception>

Iterator::Iterator(const Stiva& s): stiva(s) {
	this->position = this->stiva.size - 1;
}

void Iterator::prim(){
	this->position = this->stiva.size - 1;
}

void Iterator::urmator(){
	this->position--;
}

bool Iterator::valid() const{
	return this->position >= 0;
}

TElem Iterator::element() const{
	return this->stiva.elements[this->position];
}


