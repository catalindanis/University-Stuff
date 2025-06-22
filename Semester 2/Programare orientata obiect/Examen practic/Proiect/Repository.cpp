#include "Repository.h"
#include <fstream>

void Repository::load() {
	ifstream in(this->filename);

	if (!in.good()) {
		ofstream tmp(this->filename);
		tmp.close();
	}

	string id;
	string descriere;
	vector<string> programatori;
	string stare;

	while (getline(in, id)) {
		getline(in, descriere);
		
		string input;
		getline(in, input);

		int nrProgramatori = stoi(input);

		for (int i = 0; i < nrProgramatori; i++) {
			getline(in, input);
			programatori.push_back(input);
		}

		getline(in, stare);

		this->tasks.push_back(Task(
			stoi(id),
			descriere,
			programatori,
			stare));

		programatori.clear();
	}

	in.close();
}

void Repository::save() {
	ofstream out(this->filename);

	for (const auto& t : this->tasks) {
		out << t.getId() << '\n'
			<< t.getDescrere() << '\n'
			<< t.getProgramatori().size() << '\n';
		for (const auto& p : t.getProgramatori())
			out << p << '\n';
		out << t.getStare() << '\n';
	}

	out.close();
}

void Repository::add(Task t) {
	this->tasks.push_back(t);
	this->save();
}

vector<Task> Repository::getAll() const { return this->tasks; }

Task& Repository::getById(int id) {
	for (auto& t : this->tasks)
		if (t.getId() == id)
			return t;
	throw exception("Nu exista un task cu acest id!");
}

void Repository::remove(int id) {
	this->getById(id);
	int p = -1;

	for (int i = 0; i < this->tasks.size(); i++)
		if (this->tasks[i].getId() == id)
			p = i;

	this->tasks.erase(this->tasks.begin() + p);
	this->save();
}

void Repository::update(int id, Task newT) {
	Task& t = this->getById(id);
	t = newT;
	this->save();
}
