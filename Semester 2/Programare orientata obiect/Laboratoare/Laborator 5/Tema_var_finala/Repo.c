#include "Repo.h"
#include <stdlib.h>


repository* initializeaza_repo() {
    repository* repo = malloc(sizeof(repository));
    if (repo) {
        repo->participanti = createEmptyList();
        repo->previousLists = createEmptyList();
        repo->deletedParticipants = createEmptyList();
    }
    return repo;
}

void distruge_repo(repository* repo) {
    destroyDoubleList(repo->previousLists, distruge_participant);
    destroyList(repo->participanti, distruge_participant);
    destroyList(repo->deletedParticipants, distruge_participant);
    free(repo);
}

int adauga_participant(participant* p, repository* repo) {
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_id_participant(repo->participanti->elements[i]) == get_id_participant(p))
            return 1;
    }

    addElement(repo->participanti, p);
    return 0;
}

int modifica_participant(participant* p, repository* repo) {
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_id_participant(repo->participanti->elements[i]) == get_id_participant(p)) {
            updateElement(repo->participanti, i, p, distruge_participant);
            return 0;
        }
    }
    return 1;
}

int sterge_participant(participant* p, repository* repo) {
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_id_participant(repo->participanti->elements[i]) == get_id_participant(p)) {
            addElement(repo->deletedParticipants, copyParticipant(repo->participanti->elements[i]));
            removeElement(repo->participanti, i, distruge_participant);
            return 0;
        }
    }
    return 1;
}

participant* cauta_participant(int id_participant, repository* repo) {
    for (int i = 0; i < repo->participanti->size; i++) {
        if (get_id_participant(repo->participanti->elements[i]) == id_participant) {
            return getElement(repo->participanti, i);
        }
    }

    participant* p = initializeaza_participant();
    set_id_participant(p, -1);
    return p;
}



