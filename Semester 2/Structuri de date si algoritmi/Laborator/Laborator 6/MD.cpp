#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;

//Complexitate
//theta(1)
MD::MD() {
	this->capacitate = 1000;
	this->dimensiune = 0;
	this->elemente = new Nod*[this->capacitate];
	for(int i=0;i<this->capacitate;i++)
		this->elemente[i] = nullptr;
}

//Complexitate
//Best case: theta(1)
//Average case: theta(1) (complexitate amortizata)
//Worst case: O(n) (unde n este numarul de elemente cu aceeasi cheie)
void MD::adauga(TCheie c, TValoare v) {
	int pozitie = this->d(c);
	Nod* curent = this->elemente[pozitie];
	Nod* anterior = nullptr;
	while(curent != nullptr) {
		anterior = curent;
		curent = curent->getUrmator();
	}
	if(anterior == nullptr)
		this->elemente[pozitie] = new Nod(nullptr, make_pair(c, v));
	else {
		curent = new Nod(nullptr, make_pair(c, v));
		anterior->setUrmator(curent);
	}
	this->dimensiune++;
}

//Complexitate
//Best case: theta(1)
//Average case: theta(1) (complexitate amortizata)
//Worst case: O(n) (unde n este numarul de elemente cu aceeasi cheie)
bool MD::sterge(TCheie c, TValoare v) {
	int pozitie = this->d(c);
	Nod* curent = this->elemente[pozitie];
	Nod* anterior = nullptr;
	TElem cautat = make_pair(c, v);
	while(curent != nullptr &&
		curent->getElement() != cautat) {
		anterior = curent;
		curent = curent->getUrmator();
	}
	if(curent == nullptr)
		return false;
	if(anterior != nullptr)
		anterior->setUrmator(curent->getUrmator());
	else
		this->elemente[pozitie] = curent->getUrmator();
	delete curent;
	this->dimensiune--;
	return true;
}

//Complexitate
//Best case: theta(1)
//Average case: theta(1) (complexitate amortizata)
//Worst case: O(n) (unde n este numarul de elemente cu aceeasi cheie)
vector<TValoare> MD::cauta(TCheie c) const {
	int pozitie = this->d(c);
	Nod* curent = this->elemente[pozitie];
	vector<TValoare> rezultat;
	while(curent != nullptr) {
		if(curent->getElement().first == c)
			rezultat.push_back(curent->getElement().second);
		curent = curent->getUrmator();
	}
	return rezultat;
}

//Complexitate
//theta(1)
int MD::dim() const {
	return this->dimensiune;
	return 0;
}

//Complexitate
//theta(1)
bool MD::vid() const {
	return this->dimensiune == 0;
	return true;
}

//Complexitate
//theta(1)
IteratorMD MD::iterator() const {
	return IteratorMD(*this);
}

//Complexitate
//theta(1)
MD::~MD() {
	delete [] this->elemente;
}

//Complexitate
//theta(1)
int MD::d(TCheie cheie) const {
	return abs(cheie) % 1000;
}

//Complexitate
//Best case : theta(n) (n lungimea lui md)
//Average case : theta(n) (n lungimea lui md, elementele dispersate uniform)
//Worst case : theta(n) * m
//(n lungimea lui md, m nr de elemente cu aceeasi cheie)
int MD::adaugaInexistente(const MD& md) {
	int count = 0;
	IteratorMD it = md.iterator();
	while(it.valid()) {
		TElem elem = it.element();
		vector<TValoare> v = this->cauta(elem.first);

		bool found = false;
		for(const auto& val : v)
			if(val == elem.second)
				found = true;

		if(!found) {
			this->adauga(elem.first, elem.second);
			count++;
		}
		it.urmator();
	}
	return count;
}



