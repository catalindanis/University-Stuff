#define _CRTDBG_MAP_ALLOC
#include <stdio.h>
#include <stdlib.h>
#include "Teste.h"
#include "Ui.h"
#include <crtdbg.h>

int main() {
    printf("Se ruleaza testele...\n");
    ruleaza_toate_testele();
    printf("S-au rulat toate testele!\n\n\n");

    repository* repo = initializeaza_repo();
    start_aplicatie(repo);

    _CrtDumpMemoryLeaks();
    return 0;
}