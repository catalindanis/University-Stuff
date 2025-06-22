#include "Music.h"

#pragma once

//Contine definitia clasei testelor aplicatiei

class Tests {
private:
	//Metodele private care ruleaza testele pentru fiecare sectiune a aplicatiei
	static void runDomain();
	static void runRepository();
	static void runService();

public:
	//Metoda publica care ruleaza tot setul de teste pentru intreaga aplicatie
	static void runAll();
};