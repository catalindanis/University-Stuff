#pragma once

#include <vector>

#include "IteratorMultime.h"
#include "Nod.h"
#include "VectorDinamic.h"

typedef int TElem;

class Multime {
private:
    int lungime;

    int cp = 1;
    int first, last, firstFree;
    TElem* e;
    int* next;
    int* previous;

    friend class IteratorMultime;

    int aloca();
    void dealoca(int i);
    void redimensionare();
public:
    //constructorul multimii
    Multime();

    //functia adauga elementul transmis in multime
    //daca elementul exista deja, returneaza false
    bool adauga(TElem element);

    //functia sterge elementul transmis din multime
    //daca elementul nu exista, returneaza false
    bool sterge(TElem element);

    //functia cauta elementul transmis in multime
    //daca elementul nu exista, returneaza false
    bool cauta(TElem element) const;

    //functia verifica daca multimea e vida sau nu
    bool vida() const;

    //functia returneaza numarul de elemente din multime
    int dim() const;

    //functia returneaza un iterator pe multime
    IteratorMultime iterator() const;

    //functia pastreaza in multime doar elementele care nu apar in b
    //si returneaza numarul de elemente eliminate
    int diferenta(const Multime& b);

    //destructorul multimii
    ~Multime();
};