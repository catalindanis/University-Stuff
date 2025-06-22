#include <string>
using namespace std;

#pragma once

class Produs {
private:
	int id;
	string nume;
	string tip;
	double pret;
public:
	Produs(int id, string nume, string tip, double pret) :
		id(id), nume(nume), tip(tip), pret(pret) { }
	int getId() const { return this->id; }
	string getNume() const { return this->nume; }
	string getTip() const { return this->tip; }
	double getPret() const { return this->pret; }
	int getNrVocale() const { 
		int cnt = 0; 
		string vocale = "aeiouAEIOU";
		for (const auto& c : this->nume) 
			if(vocale.find(c) != string::npos)
				cnt++;
		return cnt; 
	}
};
