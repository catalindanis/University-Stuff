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
		cout << "Memory leaks found in tests!";

	Repository repository;
	Validator validator;

	Service service{ repository, validator };
	CartService cartService{ service };

	service.loadDefaultBooks();

	MainUi ui{ service, cartService };
	ui.runApplication();
}

int main() {
	start();

	if (_CrtDumpMemoryLeaks())
		cout << "Memory leaks found in application!";
	return 0;
}