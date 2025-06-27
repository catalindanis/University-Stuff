#include "Colectie.h"
#include "IteratorColectie.h"
#include <iostream>

using namespace std;

#define INITIAL_CAPACITY 1;

//Theta(1) (fav = defav = mediu)
bool rel(TElem e1, TElem e2) {
	return e1 <= e2;
}

//Theta(1) (fav = defav = mediu)
Colectie::Colectie() {
	size = 0;
	capacity = INITIAL_CAPACITY;
	elements = new TElem[capacity];
}

//fav: Theta(1)
//mediu: O(n)
//defav: Theta(n)
void Colectie::adauga(TElem e) {
	if(size == capacity) {
		capacity *= 2;
		TElem* elements = new TElem[capacity];
		for(int i=0;i<size;i++)
			elements[i] = this->elements[i];
		delete [] this->elements;
		this->elements = elements;
	}
	int pos = size - 1;
	while(pos >= 0 && rel(elements[pos], e) == false) {
		elements[pos+1] = elements[pos];
		pos--;
	}
	elements[pos+1] = e;
	size++;
}

//fav: O(n)
//mediu: O(n)
//defav: Theta(n)
bool Colectie::sterge(TElem e) {
	if(this->size < this->capacity / 2) {
		capacity /= 2;
		TElem* elements = new TElem[capacity];
		for(int i=0;i<size;i++)
			elements[i] = this->elements[i];
		delete [] this->elements;
		this->elements = elements;
	}

	for(int i=0; i < size; i++)
		if(elements[i] == e) {
			for(int j=i; j < size - 1; j++)
				elements[j] = elements[j+1];
			size--;
			return true;
		}
	return false;
}

//fav: Theta(1)
//mediu: O(n)
//defav: Theta(n)
bool Colectie::cauta(TElem elem) const {
	for(int i=0;i < size;i++)
		if(elements[i] == elem)
			return true;
	return false;
}

//Theta(n) (fav = defav = mediu)
int Colectie::nrAparitii(TElem elem) const {
	int count = 0;
	for(int i=0;i < size;i++)
		if(elements[i] == elem)
			count++;
	return count;
}

//Theta(1) (fav = defav = mediu)
int Colectie::dim() const {
	return size;
}

//Theta(1) (fav = defav = mediu)
bool Colectie::vida() const {
	return size == 0;
}

//Theta(1) (fav = defav = mediu)
IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}

//fav: nr * complexitate(adauga)[fav] = nr * Theta(1)
//mediu: nr * complexitate(adauga)[mediu] = nr * O(n)
//defav: nr * complexitate(adauga)[defav] = nr * Theta(n)
void Colectie::adaugaAparitiiMultiple(int nr, TElem elem) {
	if(nr < 0)
		throw runtime_error("Invalid number of occurrences");
	while(nr){
		nr--;
		adauga(elem);
	}
}

Colectie::~Colectie() {
	delete [] elements;
}
