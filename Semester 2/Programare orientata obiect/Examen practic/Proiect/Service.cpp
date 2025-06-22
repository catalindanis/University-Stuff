#include "Service.h"
#include <algorithm>

void Service::add(int id, string descriere, vector<string> programatori, string stare) {
	if (idExists(id))
		return;

	if (descriere.empty())
		return;

	if (programatori.size() < 1 || programatori.size() > 4)
		return;

	if (stare != "open" && stare != "inprogress" && stare != "closed")
		return;

	this->repository.add(
		Task(id, descriere, programatori, stare)
	);
	this->notify();
}

void Service::updateStare(int id, string stare) {
	if (!this->idExists(id))
		return;

	if (stare != "open" && stare != "inprogress" && stare != "closed")
		return;

	Task t = this->repository.getById(id);
	this->repository.update(id, Task(
		id, t.getDescrere(), t.getProgramatori(), stare
	));
	this->notify();
}

vector<Task> Service::search(string nume) const {
	vector<Task> rez;

	for (const auto& t : this->getAll()) {
		bool found = false;

		for (const auto& p : t.getProgramatori())
			if (p == nume)
				found = true;

		if (found)
			rez.push_back(t);
	}

	return rez;
}

vector<Task> Service::getAll() const {
	vector<Task> rez = this->repository.getAll();

	sort(rez.begin(), rez.end(), [](Task& t1, Task& t2) {
		return t1.getStare() < t2.getStare();
	});

	return rez;
}

vector<Task> Service::getByStare(string stare) const {
	vector<Task> rez;

	for (const auto& t : this->getAll()) {
		if(t.getStare() == stare)
			rez.push_back(t);
	}

	return rez;
}

bool Service::idExists(int id) const {
	for (const auto& t : this->repository.getAll())
		if (t.getId() == id)
			return true;

	return false;
}