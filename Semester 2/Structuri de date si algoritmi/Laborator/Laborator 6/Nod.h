//
// Created by catal on 12/05/2025.
//

#pragma once
#include <utility>

typedef int TCheie;
typedef int TValoare;

typedef std::pair<TCheie, TValoare> TElem;

class Nod {
private:
    TElem element;
    Nod* urmator;
public:
    Nod(Nod* urmator, TElem element) : urmator(urmator), element(element) {}

    TElem getElement() {
        return this->element;
    }

    Nod* getUrmator() {
        return this->urmator;
    }

    void setElement(TElem element) {
        this->element = element;
    }

    void setUrmator(Nod* urmator) {
        this->urmator = urmator;
    }
};
