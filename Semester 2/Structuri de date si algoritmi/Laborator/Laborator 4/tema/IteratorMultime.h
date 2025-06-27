#pragma once
#include "Nod.h"

typedef int TElem;

class Multime;

class IteratorMultime {
private:
    friend class Multime;

    //constructorul iteratorului pe multime
    IteratorMultime(const Multime& multime);

    const Multime& multime;
    int curent;
public:

    //seteaza iteratorul pe primul element din multime
    void prim();

    //seteaza iteratorul pe urmatorul element din multime
    void urmator();

    //verifica daca iteratorul este valid (pointeaza pe un element
    //valid din multime)
    bool valid() const;

    //returneaza elementul pe care pointeaza iteratorul
    TElem element() const;
};
