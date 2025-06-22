#include "Produse.h"
#include <vector>
#include <fstream>
using namespace std;

#pragma once

class Repository {
private:
	vector<Produs> v;
	string filename;

	void load();

	void save() const;
public:
	Repository(string filename) : filename(filename) { load(); }

	void add(Produs p);

	vector<Produs> getAll() { return this->v; }
};