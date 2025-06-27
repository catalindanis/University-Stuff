#include <iostream>
#include <crtdbg.h>
#include "TestExtins.h"
#include "TestScurt.h"

using namespace std;

int main() {

	testAll();
	testAllExtins();

	if(_CrtDumpMemoryLeaks())
		cout<<"Exista memory leaks";
	else
		cout<<"Nu exista memory leaks";
}
