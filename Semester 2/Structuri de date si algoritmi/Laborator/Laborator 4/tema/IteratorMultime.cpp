
#include "IteratorMultime.h"

#include <exception>

#include "Multime.h"

//complexitate theta(1)
IteratorMultime::IteratorMultime(const Multime &multime)  :
    multime(multime),
    curent(multime.first)
{}

//complexitate theta(1)
TElem IteratorMultime::element() const {
    if(!this->valid())
        throw std::exception();
    return this->multime.e[curent];
}

//complexitate theta(1)
void IteratorMultime::prim() {
    this->curent = this->multime.first;
}

//complexitate theta(1)
void IteratorMultime::urmator() {
    this->curent = this->multime.next[this->curent];
}

//complexitate theta(1)
bool IteratorMultime::valid() const {
    return this->curent != -1;
}