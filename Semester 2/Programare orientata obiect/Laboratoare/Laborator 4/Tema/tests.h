/*
Interfata ce defineste testele aplicatiei
*/
#pragma once

/*
Functia ruleaza toate testele aplicatiei si asigura functionalitatea acesteia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runTests();

/*
Functia testeaza domeniul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runDomainTests();

/*
Functia testeaza repository-ul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runRepositoryTests();

/*
Functia testeaza service-ul aplicatiei si asigura functionalitatea acestuia
:throws: assertion error (daca cel putin un test nu a rulat cu succes)
*/
void runServiceTests();
