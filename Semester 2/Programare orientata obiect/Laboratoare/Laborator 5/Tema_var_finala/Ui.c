#include "Service.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

/// Adauga un participant
/// @param li lista de participanti
void adauga(repository* li) {
    int scor, id_participant;
    char nume[51], prenume[51], sir[100];

    printf("Id-ul participantului: ");
    int ok = scanf_s("%d", &id_participant);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Id invalid!\n");
        return;
    }

    printf("Numele participantului: ");
    scanf_s("%s", &nume, 51);

    printf("Prenumele participantului: ");
    scanf_s("%s", &prenume, 51);
    printf("Scorul participantului: ");
    ok = scanf_s("%d", &scor);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Scor invalid!\n");
        return;
    }

    ok = creeaza_si_adauga_participant(id_participant, nume, prenume, scor, li);

    if (ok == 0)
        printf("Participantul a fost adaugat cu succes!\n");
    if (ok == -1)
        printf("Participantul exista deja!\n");
    if (ok == 1)
        printf("Participantul este invalid!\n");

}

void sterge(repository* li) {
    int id_participant;
    char sir[100];

    printf("Id-ul participantului: ");
    int ok = scanf_s("%d", &id_participant);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Id invalid!\n");
        return;
    }

    ok = sterge_participant_service(id_participant, li);
    if (!ok) printf("Participantul a fost sters!\n");
    else printf("Nu exista participantul!\n");
}

void modifica(repository* li) {
    int ok, scor, id_participant;
    char nume[51], prenume[51], sir[100];

    printf("Id-ul participantului: ");
    ok = scanf_s("%d", &id_participant);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Id invalid!\n");
        return;
    }

    printf("Numele participantului: ");
    scanf_s("%s", &nume, 51);

    printf("Prenumele participantului: ");
    scanf_s("%s", &prenume, 51);
    printf("Scorul participantului: ");
    ok = scanf_s("%d", &scor);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Scor invalid!\n");
        return;
    }

    ok = modifica_participant_service(id_participant, nume, prenume, scor, li);

    if (ok == 0)
        printf("Participantul a fost modificat cu succes!\n");
    if (ok == -1)
        printf("Participantul nu exista!\n");
    if (ok == 1)
        printf("Participantul este invalid!\n");
}

void afiseaza_participant(participant* p) {
    printf("Id: %d, Nume: %s, Prenume: %s, Scor: %d\n", get_id_participant(p), get_nume_participant(p), get_prenume_participant(p), get_scor_participant(p));
}

void afiseaza_toti_participantii(repository* li) {
    for (int i = 0; i < li->participanti->size; i++)
        afiseaza_participant(li->participanti->elements[i]);
}

void sorteaza(repository* li) {
    char optiune[101], sir[101];
    optiune[0] = '\0';
    int crescator;
    printf("Sorteaza dupa nume/scor: ");
    scanf_s("%s", &optiune, 101);
    printf("1 - crescator; 0 - descrescator: ");
    int ok = scanf_s("%d", &crescator);

    if (ok == 0) {
        scanf_s("%s", &sir, 100);
        printf("Optiune invalida!\n");
        return;
    }

    if (crescator == 1 && strcmp(optiune, "nume") == 0)
        sorteaza_dupa_nume(li, 1);
    else if (crescator == 0 && strcmp(optiune, "nume") == 0)
        sorteaza_dupa_nume(li, 0);
    else if (crescator == 1 && strcmp(optiune, "scor") == 0)
        sorteaza_dupa_scor(li, 1);
    else if (crescator == 0 && strcmp(optiune, "scor") == 0)
        sorteaza_dupa_scor(li, 0);
    else {
        printf("Optiune invalida!\n");
        return;
    }
    afiseaza_toti_participantii(li);

}

void filtreaza(repository* li) {
    char optiune[101], sir[101], lit[2];
    optiune[0] = '\0';
    int scor;
    participant** lista_filtrata;
    printf("Filtreaza dupa nume/scor: ");
    scanf_s("%s", &optiune, 101);

    if (strcmp(optiune, "scor") == 0) {
        printf("Scorul mai mare/mic: ");
        scanf_s("%s", &optiune, 101);

        if (strcmp(optiune, "mic") == 0){
            printf("Scorul: ");
            int ok = scanf_s("%d", &scor);

            if (ok == 0) {
                scanf_s("%s", &sir, 100);
                printf("Scor invalid!\n");
                return;
            }

            lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);
            int len = filtreaza_scor_mai_mic(li, scor, lista_filtrata);
            if (lista_filtrata != NULL) {
                for (int i = 0; i < len; i++)
                    afiseaza_participant(lista_filtrata[i]);
                free(lista_filtrata);
            }
        }
        else if (strcmp(optiune, "mare") == 0) {
            printf("Scorul: ");
            int ok = scanf_s("%d", &scor);

            if (ok == 0) {
                scanf_s("%s", &sir, 100);
                printf("Scor invalid!\n");
                return;
            }

            lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);
            int len = filtreaza_scor_mai_mare(li, scor, lista_filtrata);
            if (lista_filtrata != NULL) {
                for (int i = 0; i < len; i++)
                    afiseaza_participant(lista_filtrata[i]);
                free(lista_filtrata);
            }
        }
        else printf("Optiune invalida!\n");

    }

    else if (strcmp(optiune, "nume") == 0) {
        printf("Litera: ");
        lit[0] = '\0';
        scanf_s("%s", &lit, 2);
        if (strlen(lit) != 1) {
            printf("Litera invalida!\n");
            return;
        }
        lista_filtrata = (participant**)malloc(sizeof(participant*) * li->participanti->size);
        int len = filtreaza_prima_litera(li, lit[0], lista_filtrata);

        if (lista_filtrata != NULL) {
            for (int i = 0; i < len; i++)
                afiseaza_participant(lista_filtrata[i]);
            free(lista_filtrata);
        }
    }
    else printf("Optiune invalida!\n");
}

void undo(repository* li) {
    int result = undo_ultima_operatie(li);
    if (result == -1)
        printf("Nu se mai poate face undo!\n");
}

void afisare_lista_stergeri(repository* repo) {
    for (int i = 0; i < repo->deletedParticipants->size; i++) {
        afiseaza_participant(repo->deletedParticipants->elements[i]);
    }
}

void printeaza_optiuni() {
    printf("------------------------------\n");
    printf("adauga - adauga participant\nmodifica - modifica un participant\nsterge - sterge un participant\nafiseaza - afiseaza toti perticipantii\nsorteaza - sorteaza participantii\nfiltreaza - filtreaza participantii\nundo - undo adaugare/modificare/stergere\nstergeri - afiseaza elementele sterse\nx - inchide\n");
}

void start_aplicatie(repository* li) {

    while (1) {
        char comanda[20];
        comanda[0] = '\0';
        printeaza_optiuni();
        printf(">> ");
        scanf_s("%s", &comanda, 20);

        if (strcmp(comanda, "adauga") == 0)
            adauga(li);

        else if (strcmp(comanda, "modifica") == 0)
            modifica(li);

        else if (strcmp(comanda, "sterge") == 0)
            sterge(li);

        else if (strcmp(comanda, "afiseaza") == 0)
            afiseaza_toti_participantii(li);

        else if (strcmp(comanda, "sorteaza") == 0)
            sorteaza(li);

        else if (strcmp(comanda, "filtreaza") == 0)
            filtreaza(li);

        else if (strcmp(comanda, "undo") == 0)
            undo(li);

        else if (strcmp(comanda, "stergeri") == 0)
            afisare_lista_stergeri(li);

        else if (strcmp(comanda, "x") == 0)
            break;

        else
            printf("Comanda nu exista!\n");

    }

    distruge_repo(li);
}