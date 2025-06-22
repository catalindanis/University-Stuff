#include "Repository.h"

Repository::Repository(string fisier) : fisier(fisier) { 
	this->produse.clear(); 
	this->incarca();
}

void Repository::adauga(Produs produs) {
	this->produse.push_back(produs);
}

void Repository::incarca() {
	ifstream fin(this->fisier);
	char input[100];
	while (fin.getline(input, 100)) {
		string id = input;
		fin.getline(input, 100);
		string tip = input;
		fin.getline(input, 100);
		string brand = input;
		fin.getline(input, 100);
		string consumEnergetic = input;
		this->adauga(
			Produs{ id, tip, brand, consumEnergetic });
	}
	fin.close();
}

vector<Produs> Repository::getAll() {
	return this->produse;
}