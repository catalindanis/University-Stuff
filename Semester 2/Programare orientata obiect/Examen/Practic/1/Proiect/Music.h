#include <string>
using namespace std;

#pragma once

//Contine definitia clasei unei melodii

class Music {
private:
	//Proprietatile unei melodii
	int id;
	string titlu;
	string artist;
	int rank;

public:
	//Constructorul unei melodii
	Music(int id, string titlu, string artist, int rank);

	//Getteri pentru field-urile unei melodii
	int getId() const;
	string getTitlu() const;
	string getArtist() const;
	int getRank() const;

	//Setteri pentru field-urile unei melodii
	void setTitlu(string titlu);
	void setRank(int rank);
};
