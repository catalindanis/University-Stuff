#include "Service.h"
#include "Validator.h"
#include <algorithm>

void Service::add(int id, string nume, string tip, double pret) {
	string err = "";

	if (this->idExists(id))
		err += "Id-ul exista deja!\n";
	if (Validator::validareNume(nume) == false)
		err += "Numele nu poate fi vid!\n";
	if (Validator::validarePret(pret) == false)
		err += "Pretul trebuie sa fie intre 1.0 si 100.0!\n";

	if (err.length() > 0)
		throw err;

	this->repo.add(Produs(id, nume, tip, pret));
	this->notify();
}

map<string, int> Service::getNumberOfTypes() const {
	map<string, int> rez;
	for (const auto& p : this->repo.getAll())
		rez[p.getTip()] = this->getNumberOfType(p.getTip());
	return rez;
}

int Service::getNumberOfType(string type) const {
	int cnt = 0;
	for (const auto& p : this->repo.getAll())
		if (p.getTip() == type)
			cnt++;
	return cnt;
}


bool Service::idExists(int id) const { 
	for (const auto& p : this->repo.getAll())
		if (p.getId() == id)
			return true;
	return false;
}

vector<Produs> Service::getAll() const { 
	vector<Produs> rez = this->repo.getAll();
	sort(rez.begin(), rez.end(), [](const auto& p1, const auto& p2) {
		return p1.getPret() < p2.getPret();
	});
	return rez;
}