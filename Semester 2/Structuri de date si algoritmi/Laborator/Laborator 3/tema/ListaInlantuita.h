#pragma once

typedef int TElem;

class IteratorListaInlantuita;

class Nod {
private:
    TElem value;
    Nod* next;
    Nod* previous;
public:
    //constructorul nodului
    Nod(const TElem& value, Nod* const next, Nod* const previous);

    //second constructor
    Nod(const TElem& value, Nod* const next);

    //setter-ul pentru value
    void setValue(const TElem& value);

    //setter-ul pentru next
    void setNext(Nod* next);

    //setter-ul pentru previous
    void setPrevious(Nod* previous);

    //getter-ul pentru value
    TElem getValue() const;

    //getter-ul pentru next
    Nod* getNext() const;

    //getter-ul pentru previous
    Nod* getPrevious() const;
};

class ListaInlantuita {
private:
    Nod* prim;
    int lungime;
    friend class IteratorListaInlantuita;
    friend class Multime;
public:
    //constructorul listei inlantuite
    ListaInlantuita();

    //adauga un element in lista
    void adauga(const TElem& value);

    //sterge un element din lista
    bool sterge(const TElem& value);

    //returneaza true daca elementul exista deja in lista
    bool cauta(const TElem& value) const;

    //returneaza lungimea listei
    int dim() const;

    //returneaza true daca lista este vida
    bool vida() const;

    //returneaza un iterator pe lista
    IteratorListaInlantuita iterator() const;

    //destructorul listei
    ~ListaInlantuita();
};

class IteratorListaInlantuita {
private:
    const ListaInlantuita& lista;
    Nod* curent;
    friend class Multime;
public:
    //constructorul iteratorului pe lista
    IteratorListaInlantuita(const ListaInlantuita& lista);

    //reseteaza iteratorul pe primul element din lista
    void prim();

    //seteaza iteratorul pe urmatorul element din lista
    void urm();

    //seteaza iteratorul pe elementul anterior din lista
    void ant();

    //returneaza true daca elementul curent este valid
    bool valid() const;

    //returneaza elementul curent
    TElem element() const;

};
