#include "Music.h"
using namespace std;

Music::Music(int id, string titlu, string artist, int rank) : 
	id(id), titlu(titlu), artist(artist), rank(rank) { }

int Music::getId() const { return this->id; }

string Music::getTitlu() const { return this->titlu; }

string Music::getArtist() const { return this->artist; }

int Music::getRank() const { return this->rank; }

void Music::setTitlu(string titlu) { this->titlu = titlu; }

void Music::setRank(int rank) { this->rank = rank; }