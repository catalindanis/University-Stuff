#include "Repository.h"
#include "Observer.h"
#include <map>
using namespace std;

#pragma once

class Service : public Observable {
private:
	Repository& repo;
public:
	Service(Repository& repo) : repo(repo) {}

	vector<Produs> getAll() const;

	void add(int id, string nume, string tip, double pret);

	map<string, int> getNumberOfTypes() const;

	int getNumberOfType(string type) const;

	bool idExists(int id) const;
};