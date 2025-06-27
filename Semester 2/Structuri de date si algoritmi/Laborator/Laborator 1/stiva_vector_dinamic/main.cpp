#include <iostream>

#include "Iterator.h"
#include "Stiva.h"
#include "TestScurt.h"
#include "TestExtins.h"


//operatie in plus: tipareste elementele stivei in aceeasi ordine in care au fost adaugate.
// La sfarsitul operatiei, stiva va contine toate elementele pe care le continea inainte.
void tiparesteStiva(Stiva& s) {
	Stiva aux;
	// punem toate datele intr-o stiva auxiliara pentru a le inversa ordinea
	while (!s.vida()) {
		aux.adauga(s.sterge());
	}
	// iteram noua stiva
	while (!aux.vida()) {
		std::cout << aux.element() << " ";
		s.adauga(aux.sterge());
	}
}


int main() {
	testAll();
	testAllExtins();
	std::cout << "End\n";
	Stiva s;
	for (int i = 0; i < 10; i++) {
		s.adauga(i);
	}
	tiparesteStiva(s);
	std::cout << "\nFolosind iteratorul:\n";
	// iterarea prin elemente
	Iterator iterator = s.iterator();
	iterator.prim();
	while (iterator.valid()) {
		std::cout << iterator.element() << " ";
		iterator.urmator();
	}
	return 0;
}
