#include "Produs.h"
#include "Repository.h"
#include "Service.h"
#include <assert.h>

#pragma once

class Tests {
private:
	//Functia ce testeaza domeniul aplicatiei
	void runDomainTests();
	//Functia ce testeaza repository-ul de produse
	void runRepositoryTests();
	//Functia ce testeaza service-ul de produse
	void runServiceTests();
public:
	//Functia ce ruleaza testele intregii aplicatii
	void runAllTests();
};
