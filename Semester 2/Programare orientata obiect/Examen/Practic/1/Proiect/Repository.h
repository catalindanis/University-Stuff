#include "Music.h"
#include <fstream>
#include <vector>
using namespace std;

#pragma once

//Contine definitia repository-ului cu fisiere al aplicatiei

class FileRepository {
private:
	//Campurile clasei
	vector<Music> v;
	string filename;

	//Functia incarca datele din fisier
	void load();

	//Functia salveaza datele curente pe fisier
	void save() const;
public:
	//Constructorii repository-ului cu fisiere
	FileRepository(string filename) : filename(filename) { this->load(); }
	FileRepository() : filename("default.txt") { this->load(); }

	//Functia adauga o noua melodie in repository
	//:music: melodia de adaugat (Music&)
	void add(Music music);

	//Functia actualizeaza vechea melodie de la id-ul transmis cu noua melodie transmisa
	//:id: id-ul vechii melodii (int)
	//:music: noua melodie (Music)
	//:throw: exception (daca nu exista o melodie cu acest id)
	void update(const int id, Music music);

	//Functia sterge melodia de la id-ul transmis
	//:id: id-ul melodiei (int)
	//:throw: exception (daca nu exista o melodie cu acest id)
	void remove(const int id);
	
	//Functia returneaza melodia cu id-ul transmis ca parametru
	//:id: id-ul melodiei (int)
	//:return: melodia cu id-ul respectiv (Music&)
	//:throw: exception (daca nu exista o melodie cu acest id)
	Music& getById(const int id);

	//Functia returneaza lista de melodii din repository
	//:return: lista de melodii (vector<Music>&)
	vector<Music> getAll();
};