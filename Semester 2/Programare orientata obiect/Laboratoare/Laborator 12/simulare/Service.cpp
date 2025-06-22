#include "Service.h"

Service::Service(Repository repository) : repository{ repository } {}

vector<Produs> Service::getAll() {
	return this->repository.getAll();
}

vector<Produs> Service::filtrareBrand(string brand) {
	vector<Produs> rezultat;
	for (Produs& produs : this->repository.getAll())
		if (produs.getBrand() == brand)
			rezultat.push_back(produs);
	return rezultat;
}

vector<Produs> Service::filtrareCod(string cod) {
	vector<Produs> rezultat;
	for (Produs& produs : this->repository.getAll())
		if (produs.getCod() == cod)
			rezultat.push_back(produs);
	return rezultat;
}


