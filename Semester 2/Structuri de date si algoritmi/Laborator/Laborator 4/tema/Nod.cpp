
#include "Nod.h"

//complexitate theta(1)
Nod::Nod(TElem element, Nod *urmator, Nod *anterior) :
    element(element),
    urmator(urmator),
    anterior(anterior) {}

//complexitate theta(1)
void Nod::setAnterior(Nod *anterior) {
    this->anterior = anterior;
}

//complexitate theta(1)
void Nod::setUrmator(Nod *urmator) {
    this->urmator = urmator;
}

//complexitate theta(1)
void Nod::setElement(TElem element) {
    this->element = element;
}

//complexitate theta(1)
TElem Nod::getElement() const {
    return this->element;
}

//complexitate theta(1)
Nod* Nod::getAnterior() const {
    return this->anterior;
}

//complexitate theta(1)
Nod* Nod::getUrmator() const {
    return this->urmator;
}

