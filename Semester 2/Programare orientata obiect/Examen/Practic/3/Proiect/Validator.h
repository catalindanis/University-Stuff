#include "Produse.h"

#pragma once

class Validator {
public:
	static bool validareNume(string nume) {
		return nume.length() != 0;
	}

	static bool validarePret(double pret) {
		return pret >= 1.0 && pret <= 100.0;
	}
};