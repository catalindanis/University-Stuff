#include "TestScurt.h"
#include <assert.h>
#include "Stiva.h"


void testAll() { //apelam fiecare functie sa vedem daca exista
	Stiva s;
	assert(s.vida() == true);
	s.adauga(5);
	s.adauga(1);
	s.adauga(10);
	assert(s.vida() == false);
	assert(s.element() == 10);
	assert(s.sterge() == 10);
	assert(s.element() == 1);
	assert(s.sterge() == 1);
	assert(s.element() == 5);
	assert(s.sterge() == 5);
	assert(s.vida() == true);
}
