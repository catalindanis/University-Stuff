#ifndef LIST_H
#define LIST_H

typedef struct {
	void** elements;
	int size;
	int capacity;
}List;

typedef void (*DestroyFct)();
typedef void* (*CopyElement)();

/*
Functia creeaza o lista generica goala
@return lista creata
*/
List* createEmptyList();

/*
Functia dubleaza dimensiunea unei liste
@param list lista
*/
void resizeList(List* list);

/*
Functia adauga un element la finalul listei
@param lista lista
@param element elementul
*/
void addElement(List* list, void* element);

/*
Functia sterge elementul de pe pozitia index din lista
@param list lista
@param index indicele
@param destroyElement functia de distrugere
*/
void removeElement(List* list, int index, DestroyFct destroyElement);

/*
Functia actualizeaza elementul de pe pozitia index din lista
@param list lista
@param newElement noul element
@param destroyElement functia de distrugere
*/
void updateElement(List* list, int index, void* newElement, DestroyFct destroyElement);

/*
Functia returneaza elementul de pe pozitia index
@param list lista
@param index indicele
*/
void* getElement(List* list, int index);

/*
Functia returneaza o copie a unei liste
@param l lista
@param copyElement functia de copiere
@return copia listei
*/
List* copyList(List* l, CopyElement copyElement);

/*
Functia distruge o lista generica
@param list lista
@param destroyElement functia de distrugere
*/
void destroyList(List* list, DestroyFct destroyElement);

/*
Functia distruge o lista de liste
@param lista lista
@param destroyElement functia de distrugere
*/
void destroyDoubleList(List* list, DestroyFct destroyElement);


#endif