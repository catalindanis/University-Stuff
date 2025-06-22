
#ifndef SERVICE_H
#define SERVICE_H
#include "Repo.h"


/// Creeaza si adauga un participant
/// @param id_participant int
/// @param nume string
/// @param prenume string
/// @param scor int
/// @param repo repository
/// @return 0 daca participantul a fost adaugat cu succes,
///        -1 daca participantul exista deja,
///        1 daca participantul este invalid
int creeaza_si_adauga_participant(int id_participant, char* nume, char* prenume, int scor, repository* repo);

/// Modifica un participant
/// @param id_participant int
/// @param nume string
/// @param prenume string
/// @param scor int
/// @param repo repository
/// @return 0 daca participantul a fost modificat cu succes,
///        -1 daca participantul nu exista,
///        1 daca participantul este invalid
int modifica_participant_service(int id_participant, char* nume, char* prenume, int scor, repository* repo);


/// Sterge un participant cu un id dat
/// @param id_participant int
/// @param repo repository
/// @return 0 daca participantul a fost sters cu succes,
///         -1 daca nu exista participantul
int sterge_participant_service(int id_participant, repository* repo);

/// Sorteaza lista crescator sau descrescator dupa nume
/// @param repo repository
/// @param crescator int, 1 daca lista trebuie sortata crescator, 0 daca trebuie sortata descrescator
void sorteaza_dupa_nume(repository* repo, int crescator);

/// Sorteaza lista crescator sau descrescator dupa scor
/// @param repo repository
/// @param crescator int, 1 daca lista trebuie sortata crescator, 0 daca trebuie sortata descrescator
void sorteaza_dupa_scor(repository* repo, int crescator);

/// filtreaza lista de participanti cu un scor mai mic decat scor
/// @param repo repository
/// @param scor int
/// @param lista_filtrata lista filtrata de participanti
/// @return int, lungimea listei filtrate
int filtreaza_scor_mai_mic(repository* repo, int scor, participant** lista_filtrata);

/// filtreaza lista de participanti cu un scor mai mare decat scor
/// @param repo repository
/// @param scor int
/// @param lista_filtrata lista filtrata de participanti
/// @return int, lungimea listei filtrate
int filtreaza_scor_mai_mare(repository* repo, int scor, participant** lista_filtrata);

/// filtreaza lista de participanti cu o prima litera data din nume
/// @param repo repository
/// @param litera char, prima litera a numelui
/// @param lista_filtrata lista filtrata de participanti
/// @return int, lungimea listei filtrate
int filtreaza_prima_litera(repository* repo, char litera, participant** lista_filtrata);

/*
Functia face undo la ultima operatie de adaugare / stergere / modificare
@param repo repository-ul
@return 0 (operatia s-a efefctuat cu succes)
	   -1 (nu se mai poate realiza operatia de undo)
*/
int undo_ultima_operatie(repository* repo);


#endif //SERVICE_H
