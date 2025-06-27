#include <assert.h>
#include "TestExtins.h"
#include <exception>

#include "Stiva.h"

void testCreeaza() {
	Stiva s;
	assert(s.vida() == true);
	try {
		s.element(); //daca e vida ar trebui sa arunce exceptie
		assert(false); //daca nu a aruncat exceptie, vrem sa avem un assert care da fail
	}
	catch (std::exception&) {
		assert(true);
	}
	try {
		s.sterge(); //daca e vida ar trebui sa arunce exceptie
		assert(false); //daca nu a aruncat exceptie, vrem sa avem un assert care da fail
	}
	catch (std::exception&) {
		assert(true);
	}
}

void testAdauga() {
	Stiva s;
	for (int i = 0; i < 10; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);
	for (int i = -10; i < 20; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);
	for (int i = -100; i < 100; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);

	for (int i = 10000; i > -10000; i--) {
		s.adauga(i);
	}
	assert(s.vida() == false);
	assert(s.element() == -9999);
	assert(s.element() != 0);

	assert(s.sterge() == -9999);
	assert(s.element() == -9998);
}

void testSterge() {
	Stiva s;
	for (int i = 0; i < 10; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);
	for (int i = -10; i < 20; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);
	for (int i = -100; i < 100; i++) {
		s.adauga(i);
	}
	assert(s.vida() == false);

	for (int i = 10000; i > -10000; i--) {
		s.adauga(i);
	}
	assert(s.vida() == false);

	// acum in ordine inversa ar trebui sa fie si sterse
	for (int i = -9999; i <= 10000; i++) {
		assert(s.sterge() == i);
	}
	assert(s.vida() == false);
	for (int i = 99; i >= -100; i--) {
		assert(s.sterge() == i);
	}
	assert(s.vida() == false);
	for (int i = 19; i >= -10; i--) {
		assert(s.sterge() == i);
	}
	assert(s.vida() == false);
	for (int i = 9; i >= 0; i--) {
		assert(s.sterge()==i);
	}

	assert(s.vida() == true);
}

void testQuantity() {//scopul e sa adaugam multe date 
	Stiva s;
	for (int i = 1; i <= 10; i++) {
		for (int j = 30000; j >= -3000; j--) {
			s.adauga(i+j);
		}
	}

	for (int i = 10; i >= 1; i--) {
		for (int j = -3000; j <= 30000; j++) {
			assert(s.sterge() == i+j);
		}
	}
}

void testCauta() {
	Stiva s;
	for (int i = 0; i < 100; i++) {
		s.adauga(i);
	}
	for (int i = 0; i < 100; i++) {
		assert(s.cautare(i));
	}
	for (int i = -100; i < 0; i++) {
		assert(!s.cautare(i));
	}
	for (int i = 100; i < 200; i++) {
		assert(!s.cautare(i));
	}
}

void testAllExtins() {
	testCreeaza();
	testAdauga();
	testSterge();
	testQuantity();
	testCauta();
}