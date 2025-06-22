#include "Produs.h"
#include <vector>
#include <fstream>
using namespace std;

#pragma once

class Repository {
private:
	//Proprietatile repository-ului de produse
	vector<Produs> produse;
	string fisier;

	//Functia incarca toate datele din fisier in repository
	void incarca();
public:
	/*
	Constructorul repository - ului de produse
	@fisier : numele fisierului din care se face citirea produselor
	*/
	Repository(string fisier);
	/*
	Functia realizeaza adaugarea unui produs in repository
	@produs : produsul adaugat
	*/
	void adauga(Produs produs);

	/*
	Functia returneaza lista de produse din repository
	@return : vectorul de produse(vector<Produs>)
	*/
	vector<Produs> getAll();
};
