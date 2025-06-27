#pragma once
#include "ListaInlantuita.h"

typedef int TElem;

bool rel(TElem, TElem);

class IteratorMultime;

class Multime {
private:
    ListaInlantuita* lista;
    friend class IteratorMultime;
public:
    //constructorul multimii
    Multime();

    //adauga elementul elem in multime
    //returneaza true daca elementul a fost adaugat
    bool adauga(const TElem& elem);

    //sterge elementul elem
    //returneaza true daca elementul a existat in multime si a fost sters
    bool sterge(const TElem& elem);

    //returneaza true daca elementul elem apare in multime
    bool cauta(const TElem& elem) const;

    //returneaza dimensiunea multimii
    int dim() const;

    //returneaza true daca multimea este goala
    bool vida() const;

    //returneaza un iterator pe multime
    IteratorMultime iterator() const;

    //destructorul multimii
    ~Multime();
};

class IteratorMultime {
private:
    IteratorListaInlantuita it;
public:
    IteratorMultime(const Multime& multime);

    //reseteaza iteratorul pe primul element din lista
    void prim();

    //seteaza iteratorul pe urmatorul element din lista
    void urmator();

    //seteaza iteratorul pe elementul anterior din lista
    void anterior();

    //returneaza true daca elementul curent este valid
    bool valid() const;

    //returneaza elementul curent
    TElem element() const;
};