#include "Multime.h"

#include <exception>

//Complexitate theta(1)
bool rel(TElem elem1, TElem elem2) {
    return elem1 <= elem2;
}

//Complexitate theta(1)
Multime::Multime() {
    this->lista = new ListaInlantuita();
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
bool Multime::adauga(const TElem& elem) {
    IteratorListaInlantuita it = this->lista->iterator();
    Nod* previous = nullptr;

    while(it.valid() && rel(it.element(), elem)) {
        if(it.element() == elem)
            return false;

        previous = it.curent;
        it.urm();
    }

    Nod* element = new Nod(elem, it.curent);
    if(previous == nullptr)
        this->lista->prim = element;
    else {
        previous->setNext(element);
    }

    this->lista->lungime++;
    return true;
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
bool Multime::sterge(const TElem &elem) {
    return this->lista->sterge(elem);
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
bool Multime::cauta(const TElem &elem) const {
    return this->lista->cauta(elem);
}

//Complexitate theta(1)
int Multime::dim() const {
    return this->lista->dim();
}

//Complexitate theta(1)
bool Multime::vida() const{
    return this->dim() == 0;
}

//Complexitate theta(1)
IteratorMultime Multime::iterator() const {
    return IteratorMultime{*this};
}

//Complexitate theta(n)
Multime::~Multime() {
    delete this->lista;
}

//Complexitate theta(1)
IteratorMultime::IteratorMultime(const Multime &multime) : it(multime.lista->iterator()){}

//Complexitate theta(1)
void IteratorMultime::prim() {
    it.prim();
}

//Complexitate theta(1)
TElem IteratorMultime::element() const {
    return it.element();
}

//Complexitate theta(1)
void IteratorMultime::urmator() {
    it.urm();
}

//Complexitate theta(1)
void IteratorMultime::anterior() {
    it.ant();
    if(!it.valid())
        throw std::exception();
}

//Complexitate theta(1)
bool IteratorMultime::valid() const {
    return it.valid();
}






