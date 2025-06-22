#ifndef REPO_H
#define REPO_H

#include "Domain.h"
#include "List.h"

///repository-ul de participanti
typedef struct {
    List* participanti;
    List* previousLists;
    List* deletedParticipants;
}repository;


/// Initializeaza un repository
/// @return repo-ul creat
repository* initializeaza_repo();

/// distruge repository
/// @param repo repository-ul
void distruge_repo(repository* repo);


/// Adauga un participant
/// @param p un participant
/// @param repo repository
/// @return 0, daca participantul a fost adaugat cu succes, 1 daca participantul exista deja
int adauga_participant(participant* p, repository* repo);

/// Modifica un participant p cu p_modificat din lista de participanti lista
/// @param p participantul care va fi modificat
/// @param repo repository
/// @return 0, daca participantul a fost modificat cu succes, 1 daca participantul nu exista
int modifica_participant(participant* p, repository* repo);

/// Cauta un participant dupa id-ul sau
/// @param id_participant int, id-ul participantului
/// @param repo repository
/// @return participant-ul cautat sau un participant cu id-ul -1 daca nu exista
participant* cauta_participant(int id_participant, repository* repo);

/// Sterge un participant p din lista de participanti lista
/// @param p participatul care va fi sters
/// @param repo repository
/// @return 0, daca partcicipantul a fost sters cu succes, 1 daca participantul nu exista
int sterge_participant(participant* p, repository* repo);


#endif //REPO_H
