#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include "Domain.h"
#include "Repo.h"
#include "Validator.h"
#include "Service.h"
#include <stdio.h>


void test_domain() {

    participant* p = initializeaza_participant();

    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);

    assert(1 == get_id_participant(p));
    assert(strcmp("Maria", get_nume_participant(p)) == 0);
    assert(strcmp("Laura", get_prenume_participant(p)) == 0);
    assert(10 == get_scor_participant(p));

    set_nume_participant(p, "Paula");
    set_prenume_participant(p, "Rebeca");
    set_scor_participant(p, 2);
    assert(strcmp("Paula", get_nume_participant(p)) == 0);
    assert(strcmp("Rebeca", get_prenume_participant(p)) == 0);
    assert(2 == get_scor_participant(p));

    participant* copy = copyParticipant(p);
    assert(copy->id == p->id);
    assert(copy->scor == p->scor);
    assert(strcmp(copy->nume, p->nume) == 0);
    assert(strcmp(copy->prenume, p->prenume) == 0);
    set_nume_participant(copy, "test");
    set_prenume_participant(copy, "test");
    assert(strcmp(copy->nume, p->nume) != 0);
    assert(strcmp(copy->prenume, p->prenume) != 0);

    distruge_participant(p);
    distruge_participant(copy);
}

void test_adauga_participant() {
    repository* li = initializeaza_repo();
    participant* p = initializeaza_participant();

    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);

    assert(li->participanti->size == 0);
    int ok = adauga_participant(p, li);
    assert(li->participanti->size == 1);
    assert(ok == 0);

    ok = adauga_participant(p, li);
    assert(ok == 1);
    assert(li->participanti->size == 1);

    participant* p1 = initializeaza_participant();
    set_nume_participant(p1, "Maria");
    set_prenume_participant(p1, "Laura");
    set_scor_participant(p1, 10);
    set_id_participant(p1, 2);

    participant* p2 = initializeaza_participant();
    set_nume_participant(p2, "Maria");
    set_prenume_participant(p2, "Laura");
    set_scor_participant(p2, 10);
    set_id_participant(p2, 2);

    adauga_participant(p1, li);
    adauga_participant(p2, li);

    distruge_participant(p2);
    distruge_repo(li);
}

void test_modifica_participant() {
    participant* p = initializeaza_participant();
    repository* li = initializeaza_repo();

    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);

    adauga_participant(p, li);

    participant* p1 = initializeaza_participant();
    set_nume_participant(p1, "M");
    set_prenume_participant(p1, "L");
    set_scor_participant(p1, 5);
    set_id_participant(p1, 1);

    int ok = modifica_participant(p1, li);
    assert(ok == 0);
    participant* aux = cauta_participant(1, li);
    assert(strcmp(get_nume_participant(aux), "M") == 0);
    assert(strcmp(get_prenume_participant(aux), "L") == 0);
    assert(get_scor_participant(aux) == 5);

    participant* p2 = initializeaza_participant();
    set_id_participant(p2, 5);
    ok = modifica_participant(p2, li);
    assert(ok == 1);

    distruge_participant(p2);
    distruge_repo(li);
}

void test_sterge_participant() {
    repository* li = initializeaza_repo();

    participant* p = initializeaza_participant();
    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);

    participant* p1 = initializeaza_participant();
    set_nume_participant(p1, "Maria");
    set_prenume_participant(p1, "Laura");
    set_scor_participant(p1, 10);
    set_id_participant(p1, 2);

    participant* p2 = initializeaza_participant();
    set_nume_participant(p2, "Maria");
    set_prenume_participant(p2, "Laura");
    set_scor_participant(p2, 10);
    set_id_participant(p2, 3);

    adauga_participant(p, li);
    adauga_participant(p1, li);
    adauga_participant(p2, li);
    assert(li->participanti->size == 3);

    int ok = sterge_participant(p, li);
    assert(ok == 0);
    assert(li->participanti->size == 2);

    ok = sterge_participant(p, li);
    assert(ok == 1);
    assert(li->participanti->size == 2);

    distruge_repo(li);
}

void test_cauta_participant() {
    repository* li = initializeaza_repo();

    participant* p = initializeaza_participant();
    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);

    participant* p1 = initializeaza_participant();
    set_nume_participant(p1, "M");
    set_prenume_participant(p1, "L");
    set_scor_participant(p1, 5);
    set_id_participant(p1, 2);

    adauga_participant(p, li);
    adauga_participant(p1, li);

    participant* par = cauta_participant(2, li);
    assert(strcmp(get_nume_participant(par), "M") == 0);
    assert(strcmp(get_prenume_participant(par), "L") == 0);
    assert(get_scor_participant(par) == 5);

    participant* pi = cauta_participant(89, li);
    assert(get_id_participant(pi) == -1);

    distruge_participant(pi);
    distruge_repo(li);
}

void test_validator() {
    participant* p = initializeaza_participant();

    set_nume_participant(p, "Maria");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 10);
    set_id_participant(p, 1);
    assert(valideaza_participant(p) == 0);

    set_nume_participant(p, "");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, 101);
    set_id_participant(p, 1);
    assert(valideaza_participant(p) == 1);


    set_nume_participant(p, "");
    set_prenume_participant(p, "Laura");
    set_scor_participant(p, -12);
    set_id_participant(p, 1);
    assert(valideaza_participant(p) == 1);
    distruge_participant(p);
}

void test_creeaza_si_adauga() {

    repository* li = initializeaza_repo();
    assert(li->participanti->size == 0);
    int ok = creeaza_si_adauga_participant(1, "Maria", "Laura", 30, li);
    assert(ok == 0);
    assert(li->participanti->size == 1);

    ok = creeaza_si_adauga_participant(1, "das", "asda", 20, li);
    assert(ok == -1);

    ok = creeaza_si_adauga_participant(-1, "", "", 101, li);
    assert(ok == 1);

    assert(undo_ultima_operatie(li) == 0);
    assert(undo_ultima_operatie(li) == -1);
    assert(li->participanti->size == 0);

    distruge_repo(li);
}

void test_modifica() {
    repository* li = initializeaza_repo();
    creeaza_si_adauga_participant(1, "Maria", "Laura", 30, li);
    int ok = modifica_participant_service(1, "M", "L", 20, li);
    assert(ok == 0);

    assert(strcmp(cauta_participant(1, li)->nume, "M") == 0);
    assert(undo_ultima_operatie(li) == 0);
    assert(strcmp(cauta_participant(1, li)->nume, "Maria") == 0);

    modifica_participant_service(1, "M", "L", 20, li);

    List* lista = copyList(li->participanti, copyParticipant);
    assert(lista->size == li->participanti->size);
    destroyList(lista, distruge_participant);

    participant* p = cauta_participant(1, li);
    assert(get_scor_participant(p) == 20);
    ok = modifica_participant_service(2, "asd", "asds", 2, li);
    assert(ok == -1);

    ok = modifica_participant_service(1, "", "", 102, li);
    assert(ok == 1);

    distruge_repo(li);
}

void test_sterge() {
    repository* li = initializeaza_repo();
    creeaza_si_adauga_participant(1, "Maria", "Laura", 30, li);
    creeaza_si_adauga_participant(2, "Maria", "Laura", 30, li);
    assert(li->participanti->size == 2);
    assert(undo_ultima_operatie(li) == 0);

    assert(li->participanti->size == 1);
    assert(undo_ultima_operatie(li) == 0);

    assert(li->participanti->size == 0);
    assert(undo_ultima_operatie(li) == -1);


    creeaza_si_adauga_participant(1, "Maria", "Laura", 30, li);
    creeaza_si_adauga_participant(2, "Maria", "Laura", 30, li);

    List* lista = copyList(li->participanti, copyParticipant);
    assert(lista->size == li->participanti->size);
    destroyList(lista, distruge_participant);

    int ok = sterge_participant_service(1, li);
    assert(li->participanti->size == 1);
    assert(ok == 0);

    ok = sterge_participant_service(1, li);
    assert(ok == 1);
    assert(li->participanti->size == 1);

    assert(lista->size != li->participanti->size);

    distruge_repo(li);
}

void test_sorteaza_dupa_scor() {
    repository* li = initializeaza_repo();

    creeaza_si_adauga_participant(1, "Maria", "Laura", 5, li);

    creeaza_si_adauga_participant(2, "das", "asda", 2, li);

    creeaza_si_adauga_participant(3, "asd", "asd", 7, li);

    participant* p = li->participanti->elements[0] ;
    assert(get_id_participant(p) == 1);

    sorteaza_dupa_scor(li, 1);
    p = li->participanti->elements[0];
    assert(get_id_participant(p) == 2);

    sorteaza_dupa_scor(li, 0);
    p = li->participanti->elements[0];
    assert(get_id_participant(p) == 3);

    distruge_repo(li);
}

void test_sorteaza_dupa_nume() {
    repository* li = initializeaza_repo();
    creeaza_si_adauga_participant(1, "B", "Laura", 5, li);
    creeaza_si_adauga_participant(2, "C", "asda", 2, li);
    creeaza_si_adauga_participant(3, "A", "asd", 7, li);

    participant* p = li->participanti->elements[0];
    assert(get_id_participant(p) == 1);

    sorteaza_dupa_nume(li, 1);

    p = li->participanti->elements[0];
    assert(get_id_participant(p) == 3);

    sorteaza_dupa_nume(li, 0);

    p = li->participanti->elements[0];
    assert(get_id_participant(p) == 2);

    distruge_repo(li);
}

void filtreaza_scor() {
    repository* li = initializeaza_repo();
    creeaza_si_adauga_participant(1, "Maria", "Laura", 5, li);
    creeaza_si_adauga_participant(2, "das", "asda", 2, li);
    creeaza_si_adauga_participant(3, "asd", "asd", 7, li);

    participant** lista_filtrata;
    lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);

    int lungime = filtreaza_scor_mai_mic(li, 6, lista_filtrata);
    assert(lungime == 2);

    free(lista_filtrata);

    lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);

    lungime = filtreaza_scor_mai_mare(li, 4, lista_filtrata);
    assert(lungime == 2);

    free(lista_filtrata);

    distruge_repo(li);

}

void filtreaza_nume() {
    repository* li = initializeaza_repo();
    creeaza_si_adauga_participant(1, "maria", "Laura", 5, li);
    creeaza_si_adauga_participant(2, "mas", "asda", 2, li);
    creeaza_si_adauga_participant(3, "asd", "asd", 7, li);

    participant** lista_filtrata;
    lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);

    int lungime = filtreaza_prima_litera(li, 'm', lista_filtrata);
    assert(lungime == 2);

    free(lista_filtrata);
    distruge_repo(li);
}

void ruleaza_toate_testele() {
    test_domain();
    test_adauga_participant();
    test_modifica_participant();
    test_sterge_participant();
    test_cauta_participant();
    test_validator();

    test_creeaza_si_adauga();
    test_modifica();
    test_sterge();
    test_sorteaza_dupa_scor();
    test_sorteaza_dupa_nume();
    filtreaza_scor();
    filtreaza_nume();
}
