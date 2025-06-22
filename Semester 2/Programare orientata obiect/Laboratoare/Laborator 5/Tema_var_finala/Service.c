#include "Domain.h"
#include "Repo.h"
#include "Service.h"
#include "Validator.h"
#include <string.h>

int creeaza_si_adauga_participant(int id_participant, char* nume, char* prenume, int scor, repository* repo) {
    participant* p = initializeaza_participant();
    set_id_participant(p, id_participant);
    set_nume_participant(p, nume);
    set_prenume_participant(p, prenume);
    set_scor_participant(p, scor);

    int ok = valideaza_participant(p);

    if (ok == 1) {
        distruge_participant(p); // ca a fost creat participantul
        return 1;
    }

    List* previous = copyList(repo->participanti, copyParticipant);
    ok = adauga_participant(p, repo);

    if (ok == 1) {
        distruge_participant(p);
        destroyList(previous, distruge_participant);
        return -1;

    }

    addElement(repo->previousLists, previous);
    return 0;
}

int sterge_participant_service(int id_participant, repository* repo) {
    participant* p = cauta_participant(id_participant, repo);
    if (get_id_participant(p) == -1) {
        distruge_participant(p);
        return 1;
    }

    List* previous = copyList(repo->participanti, copyParticipant);
    sterge_participant(p, repo);

    addElement(repo->previousLists, previous);
    return 0;
}

int modifica_participant_service(int id_participant, char* nume, char* prenume, int scor, repository* repo) {
    participant* p = initializeaza_participant();
    set_id_participant(p, id_participant);
    set_nume_participant(p, nume);
    set_prenume_participant(p, prenume);
    set_scor_participant(p, scor);
    int ok = valideaza_participant(p);
    if (ok == 1) {
        distruge_participant(p);
        return 1;
    }

    List* previous = copyList(repo->participanti, copyParticipant);
    ok = modifica_participant(p, repo);
    if (ok == 1) {
        distruge_participant(p);
        destroyList(previous, distruge_participant);
        return -1;
    }

    addElement(repo->previousLists, previous);
    return 0;
}


/// Sorteaza lista de participanti crescator
/// @param l lista de participanti
/// @param nr_elems int, nr de elemente
/// @param func_comp fct de comparare
void selection_sort_crescator(repository* repo, int nr_elems, int (*func_comp) (participant* a, participant* b)) {
    for (int i = 0; i < nr_elems - 1; i++) {
        for (int j = i + 1; j < nr_elems; j++) {
            if (func_comp(repo->participanti->elements[i], repo->participanti->elements[j]) > 0) {
                participant* p = repo->participanti->elements[i];
                repo->participanti->elements[i] = repo->participanti->elements[j];
                repo->participanti->elements[j] = p;
            }
        }
    }
}

/// Sorteaza lista de participanti descrescator
/// @param l lista de participanti
/// @param nr_elems int, nr de elemente
/// @param func_comp fct de comparare
void selection_sort_descrescator(repository* repo, int nr_elems, int (*func_comp) (participant* a, participant* b)) {
    for (int i = 0; i < nr_elems - 1; i++) {
        for (int j = i + 1; j < nr_elems; j++) {
            if (func_comp(repo->participanti->elements[i], repo->participanti->elements[j]) < 0) {
                participant* p = repo->participanti->elements[i];
                repo->participanti->elements[i] = repo->participanti->elements[j];
                repo->participanti->elements[j] = p;
            }
        }
    }
}

/// Functie de comparare a numelor a 2 participanti
/// @param a participant
/// @param b participant
/// @return int, <0 daca numele lui a este inaintea lui b alfabetic, =0 daca sunt la fel, >0 altfel
int compara_nume(participant* a, participant* b) {
    int rez = strcmp(get_nume_participant(a), get_nume_participant(b));
    return rez;
}
/// Functie de comparare a scorurilor a 2 participanti
/// @param a participant
/// @param b participant
/// @return int, <0 daca scor a> scor b, =0 daca sunt la fel, >0 altfel
int compara_scor(participant* a, participant* b) {
    int rez = -(get_scor_participant(b) - get_scor_participant(a));
    return rez;
}

void sorteaza_dupa_scor(repository* repo, int crescator) {

    if (crescator) {
        selection_sort_crescator(repo, repo->participanti->size, &compara_scor);
    }
    else {
        selection_sort_descrescator(repo, repo->participanti->size, &compara_scor);
    }
}


void sorteaza_dupa_nume(repository* repo, int crescator) {

    if (crescator) {
        selection_sort_crescator(repo, repo->participanti->size, &compara_nume);
    }
    else {
        selection_sort_descrescator(repo, repo->participanti->size, &compara_nume);
    }
}



/// Functie de append
/// @param lista lista unde se va face append
/// @param p participant
/// @param lungime lungimea listei
void append_participant(participant** lista, participant* p, int lungime) {
    lista[lungime] = p;
}

int filtreaza_scor_mai_mic(repository* repo, int scor, participant** lista_filtrata) {
    int lungime = 0;
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_scor_participant(repo->participanti->elements[i]) < scor) {
            append_participant(lista_filtrata, repo->participanti->elements[i], lungime);
            lungime++;
        }
    }
    return lungime;
}

int filtreaza_scor_mai_mare(repository* repo, int scor, participant** lista_filtrata) {
    int lungime = 0;
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_scor_participant(repo->participanti->elements[i]) > scor) {
            append_participant(lista_filtrata, repo->participanti->elements[i], lungime);
            lungime++;
        }
    }
    return lungime;
}

int filtreaza_prima_litera(repository* repo, char litera, participant** lista_filtrata) {
    int lungime = 0;
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_nume_participant(repo->participanti->elements[i])[0] == litera) {
            append_participant(lista_filtrata, repo->participanti->elements[i], lungime);
            lungime++;
        }
    }
    return lungime;
}

int undo_ultima_operatie(repository* repo) {
    if (repo->previousLists->size > 0) {
        destroyList(repo->participanti, distruge_participant);
        repo->participanti =
            repo->previousLists->elements[repo->previousLists->size - 1];
        repo->previousLists->size--;
        return 0;
   }
    return -1;
}

