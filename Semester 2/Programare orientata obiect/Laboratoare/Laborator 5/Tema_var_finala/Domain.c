#include <string.h>
#include <stdlib.h>
#include "Domain.h"

int get_scor_participant(participant* p) {
    return p->scor;
}

void set_scor_participant(participant* p, int scor_nou) {
    p->scor = scor_nou;
}

int get_id_participant(participant* p) {
    return p->id;
}

void set_id_participant(participant* p, int id_nou) {
    p->id = id_nou;
}

char* get_nume_participant(participant* p) {
    return p->nume;
}

void set_nume_participant(participant* p, char* nou_nume) {
    unsigned long long lungime = strlen(nou_nume);
    strcpy_s(p->nume, lungime + 1, nou_nume);
}

char* get_prenume_participant(participant* p) {
    return p->prenume;
}

void set_prenume_participant(participant* p, char* nou_prenume) {
    unsigned long long lungime = strlen(nou_prenume);
    strcpy_s(p->prenume, lungime + 1, nou_prenume);
}

participant* initializeaza_participant() {
    participant* p = malloc(sizeof(participant));
    if (p) {
        p->nume = (char*)malloc(sizeof(char) * 51);
        if (p->nume)
            p->nume[0] = '\0';
        p->prenume = (char*)malloc(sizeof(char) * 51);
        if (p->prenume)
            p->prenume[0] = '\0';
        p->scor = 0;
    }
    return p;
}

participant* copyParticipant(participant* p) {
    participant* copy = initializeaza_participant();
    if (copy) {
        copy->id = p->id;
        copy->scor = p->scor;
        strcpy_s(copy->nume, strlen(p->nume) + 1, p->nume);
        strcpy_s(copy->prenume, strlen(p->prenume) + 1, p->prenume);
    }
    return copy;
}

void distruge_participant(participant* p) {
    free(p->nume);
    free(p->prenume);
    free(p);
}