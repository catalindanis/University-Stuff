#include "IteratorMD.h"
#include "MD.h"

using namespace std;

//Complexitate
//Best case: theta(1)
//Average case: theta(1) (complexitate amortizata)
//Worst case: O(n) (n este numarul de chei posibile)
IteratorMD::IteratorMD(const MD& _md): md(_md) {
	int pozitie = 0;
	primul = curent = nullptr;
	while(md.elemente[pozitie] == nullptr && pozitie < md.capacitate)
		pozitie++;
	if(pozitie < md.capacitate) {
		primul = md.elemente[pozitie];
		curent = primul;
	}
}

//Complexitate
//theta(1)
TElem IteratorMD::element() const{
	if(!valid())
		throw exception();
	return curent->getElement();
}

//Complexitate
//theta(1)
bool IteratorMD::valid() const {
	return curent != nullptr;
}

//Complexitate
//Best case: theta(1)
//Average case: theta(1) (complexitate amortizata)
//Worst case: O(n) (n este numarul de chei posibile)
void IteratorMD::urmator() {
	if(!valid())
		throw new exception();
	if(this->curent->getUrmator() != nullptr)
		this->curent = this->curent->getUrmator();
	else {
		int pozitieCurenta = this->md.d(this->curent->getElement().first) + 1;
		while(this->md.elemente[pozitieCurenta] == nullptr &&
			pozitieCurenta < this->md.capacitate)
			pozitieCurenta++;
		if(pozitieCurenta == this->md.capacitate)
			this->curent = nullptr;
		else
			this->curent = this->md.elemente[pozitieCurenta];
	}
}

//Complexitate
//theta(1)
void IteratorMD::prim() {
	curent = primul;
}

