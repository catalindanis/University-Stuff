#include <iostream>
#include "Tests.h"
#include "Repository.h"
#include "Validator.h"
#include "Service.h"
#include "CartService.h"
#include "MainUi.h"
#include <crtdbg.h>
using namespace std;

/*
Functia principala a programului
*/
void start() {
	/*std::vector<int> v;
	for (int i = 0; i < 20; ++i) {
		v.push_back(i);
		std::cout << "Size: " << v.size() << ", Capacity: " << v.capacity() << "\n";
	}*/

	runAllTests();

	if (_CrtDumpMemoryLeaks())
		cout << "Memory leaks found in tests!\n";

	RandomRepository repository;
	//Repository repository;
	//FileRepository repository{ "database" };
	Validator validator;

	Service service{ repository, validator };
	CartService cartService{ service };

	MainUi ui{ service, cartService };
	ui.runApplication();
}

int main() {
	start();

	if (_CrtDumpMemoryLeaks())
		cout << "Memory leaks found in application!\n";
	return 0;
}