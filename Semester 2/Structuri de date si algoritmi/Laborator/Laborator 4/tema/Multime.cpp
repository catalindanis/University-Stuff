#include "Multime.h"

#include <iostream>

//complexitate
//defav = fav = mediu = theta(1)
Multime::Multime() : lungime(0) {
    this->cp = 1;
    this->e = new TElem[cp];
    this->next = new int[cp];
    this->previous = new int[cp];
    this->first = -1;
    this->last = -1;
    this->firstFree = 0;
    for(int i=0;i < cp - 1;i++)
        this->next[i] = i + 1;
    for(int i=1;i < cp;i++)
        this->previous[i] = i - 1;

    this->previous[0] = -1;
    this->next[cp - 1] = -1;
}

//complexitate
//defav: theta(n)
//mediu: O(n)
//fav: theta(1)
bool Multime::adauga(TElem element) {
    if(this->cauta(element))
        return false;

    int i = aloca();

    e[i] = element;
	this->next[i] = -1;
	this->previous[i] = last;

	if (this->last != -1)
		this->next[last] = i;
	last = i;

	if (first == -1)
		first = i;

    this->lungime++;
    return true;
}

//complexitate
//fav: theta(1)
//mediu: O(n)
//defav: theta(n)
int Multime::aloca() {
    if(this->firstFree == -1)
        redimensionare();
    int i = this->firstFree;
    this->firstFree = this->next[i];
    return i;
}

//complexitate
//defav = mediu = fav = theta(n)
void Multime::redimensionare() {
    int oldCp = this->cp;
    this->cp *= 2;
    TElem* newE = new TElem[this->cp];
    int* newNext = new int[this->cp];
    int* newPrevious = new int[this->cp];

    for(int i=0;i<oldCp;i++) {
        newE[i] = this->e[i];
        newNext[i] = this->next[i];
        newPrevious[i] = this->previous[i];
    }

    for(int i = oldCp - 1; i < cp; i++)
        newNext[i] = i+1;
    newNext[cp-1] = -1;

    delete [] e;
    delete [] next;
    delete [] previous;

    this->e = newE;
    this->next = newNext;
    this->previous = newPrevious;
    this->firstFree = oldCp;
}

//complexitate
//defav = mediu = fav = theta(1)
void Multime::dealoca(int i) {
    this->next[i] = firstFree;
    this->firstFree = i;
}

//complexitate
//defav: theta(n)
//mediu: O(n)
//fav: theta(1)
bool Multime::sterge(TElem element) {
    int curent = this->first;
    int anterior = -1;
    while(curent != -1 && this->e[curent] != element) {
        anterior = curent;
        curent = this->next[curent];
    }
    if(curent == -1)
        return false;
    if(anterior == -1)
        this->first = this->next[curent];
    else {
        this->next[anterior] = this->next[curent];
        if(this->next[curent] != -1) {
            this->previous[this->next[curent]] = anterior;
        }
    }

    if(curent == this->last) {
        this->last = anterior;
    }

    this->lungime--;
    dealoca(curent);
    return true;
}

//complexitate
//defav: theta(n)
//mediu: O(n)
//fav: theta(1)
bool Multime::cauta(TElem element) const {
    int curent = this->first;
    while(curent != -1) {
        if(this->e[curent] == element)
            return true;
        curent = this->next[curent];
    }
    return false;
}

//complexitate
//defav = mediu = fav = theta(1)
bool Multime::vida() const {
    return this->lungime == 0;
}

//complexitate
//defav = mediu = fav = theta(1)
int Multime::dim() const {
    return this->lungime;
}

//complexitate
//fav = theta(m), m - dim(b)
//mediu = O(m * n), m - dim(b), n - dim(multimii noastre)
//defav = theta(m * n), m - dim(b), n - dim(multimii noastre)
int Multime::diferenta(const Multime& b) {
    IteratorMultime it = b.iterator();
    int numberOfDeletedElements = 0;
    while(it.valid()) {
        if(this->sterge(it.element()))
            numberOfDeletedElements++;
        it.urmator();
    }
    return numberOfDeletedElements;
}

//complexitate
//defav = mediu = fav = theta(1)
IteratorMultime Multime::iterator() const {
    return IteratorMultime(*this);
}

//complexitate
//defav = mediu = fav = theta(n)
Multime::~Multime() {
    delete [] this->e;
    delete [] this->next;
    delete [] this->previous;
}






