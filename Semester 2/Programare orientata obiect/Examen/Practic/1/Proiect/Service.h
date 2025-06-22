#include "Repository.h"
#include "Observer.h"
#include "algorithm"

#pragma once

//Contine definitia service-ului aplicatiei

class Service : public Observable{
private:
	//Campurile service-ului
	FileRepository& repo;
public:
	//Constructorul service-ului
	Service(FileRepository& repo) : repo(repo) {}

	//Functia adauga o melodie cu parametrii transmisi in repository
	//:id: id-ul melodiei (int)
	//:titlu: titlul melodiei (string)
	//:artist: artistul melodiei (string)
	//:rank: rank-ul melodiei (int)
	void add(int id, string titlu, string artist, int rank);

	//Functia actualizeaza titlul si rank-ul melodiei cu id-ul transmis
	//:id: id-ul melodiei (int)
	//:titlu: titlul melodiei (string)
	//:rank: rank-ul melodiei (int)
	//:throws: exception (daca melodia nu exista)
	void update(int id, string titlu, int rank);

	//Functia sterge melodia cu id-ul transmis
	//:id: id-ul melodiei (int)
	//:throws: exception (daca melodia nu exista / este ultima melodie a artistului)
	void remove(int id);

	//Functia returneaza lista de melodii din repository
	//sortate dupa rank
	//:return: lista de melodii (vector<Music>&)
	vector<Music> getAll();

	//Functia returneaza numarul de melodii care au rank-ul transmis
	//:rank: rank-ul cautat (int)
	//:return: numarul de melodii cu acest rank (int)
	int getNumberOfMusicsByRank(int rank);
};