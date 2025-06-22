#include "Task.h"

#pragma once

//Fisierul contine definitia clasei repository-ului de task-uri

class Repository {
private:
	//Campurile repository-ului de task-uri
	vector<Task> tasks;
	string filename;

	//Functia incarca din fisier toate datele in repository
	void load();

	//Functia salveaza in fisier toate datele din repository
	void save();
public:
	//Constructorul repository-ului de task-uri
	//filename : numele fisierului din care sunt preluate datele
	Repository(string filename) : filename(filename) { this->load(); }

	//Functia adauga un task in repository-ul de task-uri
	//t : task-ul care se va adauga
	void add(Task t);

	//Functia sterge din repository task-ul cu id-ul transmis
	// id : id-ul task-ului
	//throws: exception (daca un task cu acel id nu exista)
	void remove(int id);

	//Functia actualizeaza in repository task-ul cu id-ul transmis
	//cu noul task transmis
	// id : id-ul task-ului
	// t : noul task
	//throws: exception (daca un task cu acel id nu exista)
	void update(int id, Task t);

	//Functia returneaza lista de task-uri din repository
	//return : lista de task-uri
	vector<Task> getAll() const;

	//Functia returneaza un task dupa id-ul transmis
	//return : task-ul cu id-ul transmis
	//throws: exception (daca un task cu acel id nu exista)
	Task& getById(int id);
};


