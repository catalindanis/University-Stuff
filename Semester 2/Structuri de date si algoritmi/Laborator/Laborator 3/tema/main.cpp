#include <iostream>
#include <crtdbg.h>

#include "TestExtins.h"
#include "TestScurt.h"

int main() {

    //rulare teste
    testAll();
    testAllExtins();

    if(_CrtDumpMemoryLeaks())
        std::cout<<"Exista memory leaks";
    else
        std::cout<<"Nu exista memory leaks!";

    return 0;
}
