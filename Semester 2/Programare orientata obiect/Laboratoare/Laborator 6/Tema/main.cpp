#include <iostream>
#include "Tests.h"
#include "Repository.h"
#include "Validator.h"
#include "Service.h"
#include "Ui.h"
#include <crtdbg.h>
using namespace std;

/*
Functia principala a programului
@return 0 : programul s-a efectuat cu succes
*/
int main() {
	runAllTests();

	if (_CrtDumpMemoryLeaks())
		cout << "Memory leaks found in tests!";

	Repository repository;
	Validator validator;
	Service service{ repository, validator };
	Ui ui{ service };
	ui.runApplication();

	if (_CrtDumpMemoryLeaks())
		cout << "Memory leaks found in application!";
	return 0;
}