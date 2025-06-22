#pragma once

#include <string>
using namespace std;

class Produs {
private:
	//Proprietatile unui produs
	string cod;
	string brand;
	string tip;
	string consumEnergetic;
public:
	/*
	Constructorul unui produs
	@cod : codul produsului (string)
	@tip : tipul produsului (string)
	@brand : brandul produsului (string)
	@consumEnergetic : consumul energetic al produsului (string)
	*/
	Produs(string cod, string tip, string brand, string consumEnergetic);
	
	//Getteri si setteri pentru proprietatile unui produs
	string getCod();
	void setCod(string cod);
	string getBrand();
	void setBrand(string brand);
	string getTip();
	void setTip(string tip);
	string getConsum();
	void setConsum(string consumEnergetic);
};