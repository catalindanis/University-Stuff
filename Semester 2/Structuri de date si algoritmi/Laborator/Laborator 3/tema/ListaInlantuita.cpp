#include "ListaInlantuita.h"

//Complexitate theta(1)
Nod::Nod(const TElem& value, Nod* const next, Nod* const previous) {
    this->value = value;
    this->next = next;
    this->previous = previous;
}

//Complexitate theta(1)
Nod::Nod(const TElem& value, Nod* const next) {
    this->value = value;
    this->next = next;
    this->previous = nullptr;
}

//Complexitate theta(1)
void Nod::setValue(const TElem &value) {
    this->value = value;
}

//Complexitate theta(1)
void Nod::setNext(Nod* next) {
    this->next = next;
}

//Complexitate theta(1)
TElem Nod::getValue() const {
    return this->value;
}

//Complexitate theta(1)
Nod* Nod::getNext() const {
    return this->next;
}

Nod* Nod::getPrevious() const {
    return this->previous;
}

void Nod::setPrevious(Nod* previous) {
    this->previous = previous;
}

//Complexitate theta(1)
ListaInlantuita::ListaInlantuita() {
    this->prim = nullptr;
    this->lungime = 0;
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
void ListaInlantuita::adauga(const TElem &value) {
    Nod* elem = new Nod(value, nullptr, nullptr);

    if(this->prim == nullptr)
        this->prim = elem;
    else {
        Nod* curent = this->prim;

        while(curent->getNext() != nullptr)
            curent = curent->getNext();

        curent->setNext(elem);
        elem->setPrevious(curent);
    }

    this->lungime++;
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
bool ListaInlantuita::sterge(const TElem &value) {
    Nod* previous = nullptr;
    Nod* curent = this->prim;

    while(curent != nullptr) {
        if(curent->getValue() == value) {
            if(previous == nullptr) {
                this->prim = this->prim->getNext();
                if(dim() > 1) {
                    this->prim->setPrevious(nullptr);
                }
            }
            else {
                previous->setNext(curent->getNext());
                if(curent->getNext())
                    curent->getNext()->setPrevious(previous);
            }

            this->lungime--;
            delete curent;
            return true;
        }

        previous = curent;
        curent = curent->getNext();
    }

    return false;
}

//Complexitate
//caz favorabil: theta(1)
//caz mediu: O(n)
//caz defavorabil: theta(n)
bool ListaInlantuita::cauta(const TElem &value) const {
    Nod* curent = this->prim;

    while(curent != nullptr) {
        if(curent->getValue() == value)
            return true;

        curent = curent->getNext();
    }

    return false;
}

//Complexitate theta(1)
int ListaInlantuita::dim() const {
    return this->lungime;
}

//Complexitate theta(1)
bool ListaInlantuita::vida() const {
    return this->lungime == 0;
}

//Complexitate theta(n)
ListaInlantuita::~ListaInlantuita() {
    Nod* curent = this->prim;

    while(curent != nullptr) {
        Nod* next = curent->getNext();
        delete curent;
        curent = next;
    }
}

//Complexitate theta(1)
IteratorListaInlantuita ListaInlantuita::iterator() const {
    return IteratorListaInlantuita{*this};
}

//Complexitate theta(1)
IteratorListaInlantuita::IteratorListaInlantuita(const ListaInlantuita& lista) : lista(lista){
    this->curent = this->lista.prim;
}

//Complexitate theta(1)
void IteratorListaInlantuita::prim() {
    this->curent = this->lista.prim;
}

//Complexitate theta(1)
TElem IteratorListaInlantuita::element() const {
    return this->curent->getValue();
}

//Complexitate theta(1)
void IteratorListaInlantuita::urm() {
    this->curent = this->curent->getNext();
}

//Complexitate theta(1)
void IteratorListaInlantuita::ant() {
    this->curent = this->curent->getPrevious();
}

//Complexitate theta(1)
bool IteratorListaInlantuita::valid() const {
    return this->curent != nullptr;
}

















