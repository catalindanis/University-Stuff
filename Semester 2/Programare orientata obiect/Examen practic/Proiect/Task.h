#include <string>
#include <vector>
using namespace std;

#pragma once

//Fisierul contine definitia clasei unui Task

class Task {
private:
	//Campurile clasei Task
	int id;
	string descriere;
	vector<string> programatori;
	string stare;
public:
	//Constructorul unui task
	//id : id-ul task-ului
	//descriere : descrierea task-ului
	//programatori : lista de programatori
	//stare : starea task-ului
	Task(int id, string descriere, vector<string> programatori, string stare) :
		id(id), descriere(descriere), programatori(programatori), stare(stare) {
	}

	//Getteri pentru fiecare proprietate al clasei Task
	int getId() const;
	string getDescrere() const;
	vector<string> getProgramatori() const;
	string getStare() const;

	//Setter pentru field-ul de stare al unui task
	//stare : noua stare a task-ului
	void setStare(string stare);
};