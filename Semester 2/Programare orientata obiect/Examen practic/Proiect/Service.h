#include "Repository.h"
#include "Observer.h"

#pragma once

//Fisierul contine definitia service-ului de task-uri

class Service : public Observable{
private:
	//Campurile service-ului
	Repository& repository;
public:
	//Constructorul service-ului
	//repository : repository-ul folosit de service
	Service(Repository& repository) : repository(repository) {}

	//Functia adauga un nou task in repository daca datele introduse sunt valide
	//id : id-ul task-ului (unic)
	//descriere : descrierea task-ului (nevida)
	//programatori : lista de programatori (intre 1 si 4 programatori)
	//stare : starea task-ului (open | inprogress | closed)
	void add(int id, string descriere, vector<string> programatori, string stare);

	//Functia actualizeaza starea unui task cu id-ul transmis cu noua stare transmisa
	//id : id-ul task-ului
	//stare : noua stare a task-ului
	void updateStare(int id, string stare);

	//Functia returneaza lista de task-uri care contin in lista de programatori
	//numele transmis
	//nume : numele care trebuie sa fie continut
	//return : lista de task-uri
	vector<Task> search(string nume) const;

	//Functia returneaza lista de task-uri care au starea transmisa
	//stare : starea cautata
	//return : lista de task-uri
	vector<Task> getByStare(string stare) const;

	//Functia returneaza intreaga lista de task-uri
	//return : lista de task-uri
	vector<Task> getAll() const;

	//Functia returneaza daca exista deja un task cu id-ul transmis
	//return : true / false (exista / nu exista deja un task cu acel id)
	bool idExists(int id) const;
};