#include <vector>
using namespace std;

#pragma once

//Fisierul contine definitia claselor de Observer si Observable

class Observer {
public:
	//Functia care se apeleaza atunci cand un observer este notificat
	virtual void update() = 0;
};

class Observable {
private:
	vector<Observer*> obs;
public:
	//Functia prin care adaugam un observer la obiectul nostru observabil
	//obs : observer-ul adaugat
	void addObserver(Observer* obs);

	//Functia prin care notificam toti observerii ca s-a produs o modificare
	void notify();
};