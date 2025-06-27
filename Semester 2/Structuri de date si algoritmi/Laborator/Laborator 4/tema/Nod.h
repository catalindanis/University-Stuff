#pragma once

typedef int TElem;

class Nod {
private:
    TElem element;
    Nod* urmator;
    Nod* anterior;
public:
    //constructorul clasei Nod
    Nod(TElem element, Nod* urmator, Nod* anterior);

    //getter pentru element
    TElem getElement() const;

    // getter pentru urmator
    Nod* getUrmator() const;

    //getter pentru anterior
    Nod* getAnterior() const;

    //setter pentru element
    void setElement(TElem element);

    //setter pentru urmator
    void setUrmator(Nod* urmator);

    //setter pentru anterior
    void setAnterior(Nod* anterior);
};

