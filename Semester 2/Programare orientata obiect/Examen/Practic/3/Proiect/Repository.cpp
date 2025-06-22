#include "Repository.h"
#include "assert.h"

void Repository::load() {
	ifstream in(this->filename);
	
	if (!in.good()) {
		ofstream tmp(this->filename);
		tmp.close();
	}

	string id;
	string nume, tip;
	string pret;

	while (getline(in, id)) {
		getline(in, nume);
		getline(in, tip);
		getline(in, pret);
		
		this->v.push_back(Produs(stoi(id), nume, tip, stod(pret)));
	}

	in.close();
}

void Repository::save() const {
	ofstream out(this->filename);

	for (const auto& p : this->v)
		out << p.getId() << '\n'
		<< p.getNume() << '\n'
		<< p.getTip() << '\n'
		<< p.getPret() << '\n';

	out.close();
}

void Repository::add(Produs p) {
	this->v.push_back(p);
	save();
}

