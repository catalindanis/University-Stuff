#include "IteratorColectie.h"
#include "Colectie.h"

//Theta(1) (fav = defav = mediu)
IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	current = c.elements;
}

//Theta(1) (fav = defav = mediu)
TElem IteratorColectie::element() const{
	return *current;
}

//Theta(1) (fav = defav = mediu)
bool IteratorColectie::valid() const {
	return current - col.elements < col.size;
}

//Theta(1) (fav = defav = mediu)
void IteratorColectie::urmator() {
	current += sizeof(TElem);
}

//Theta(1) (fav = defav = mediu)
void IteratorColectie::prim() {
	current = col.elements;
}
