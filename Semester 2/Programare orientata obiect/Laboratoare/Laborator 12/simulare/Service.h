#include "Repository.h"

#pragma once

class Service {
private:
	//Proprietatile service-ului de produse
	Repository repository;
public:
	/*
	* Constructorul service - ului de produse
	* @repsitory : repository-ul de produse (Repository)
	*/
	Service(Repository repository);
	
	/*
	* Functia returneaza lista de produse din repository
	* @return : lista de produse (vector<Produs>)
	*/
	vector<Produs> getAll();

	/*
	* Functia returneaza lista de produse filtrata dupa brand
	* @return : lista de produse filtrata (vector<Produs>)
	*/
	vector<Produs> filtrareBrand(string brand);

	/*
	* Functia returneaza lista de produse filtrata dupa cod
	* @return : lista de produse filtrata (vector<Produs>)
	*/
	vector<Produs> filtrareCod(string cod);
};