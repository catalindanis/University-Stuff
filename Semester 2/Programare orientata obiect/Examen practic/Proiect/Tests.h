#pragma once

//Fisierul contine clasa de teste pentru intreaga aplicatie

class Test {
private:
	//Functia ruleaza testele pentru domeniul aplicatiei
	static void runDomainTests();

	//Functia ruleaza testele pentru repository-ul aplicatiei
	static void runRepositoryTests();

	//Functia ruleaza testele pentru service-ul aplicatiei
	static void runServiceTests();
public:
	//Functia ruleaza toate testele aplicatiei
	static void runAllTests();
};