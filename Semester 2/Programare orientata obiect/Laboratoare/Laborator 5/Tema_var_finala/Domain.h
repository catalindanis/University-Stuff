#ifndef DOMAIN_H
#define DOMAIN_H


/// Participant, cu nume string, prenume string si scor int
typedef struct {
    char* nume, * prenume;
    int scor;
    int id;
}participant;


/// Initializeaza un participant
/// @return Participant
participant* initializeaza_participant();

/// distruge un participant
/// @param p participantul
void distruge_participant(participant* p);

/// Returneaza scorul participantului
/// @param p participant
/// @return int, scor
int get_scor_participant(participant* p);

/// Seteaza scorul participantului
/// @param p participant
/// @param scor_nou int noul scor
void set_scor_participant(participant* p, int scor_nou);

/// Returneaza numele participantului
/// @param p participant
/// @return string, numele participantului
char* get_nume_participant(participant* p);

/// Returneaza id-ul participantului
/// @param p participant
/// @return int, id
int get_id_participant(participant* p);

/// Seteaza id-ul participantului
/// @param p participant
/// @param id_nou int, noul id
void set_id_participant(participant* p, int id_nou);

/// Seteaza numele participantului
/// @param p participant
/// @param nou_nume string, nou nume al participantului
void set_nume_participant(participant* p, char* nou_nume);

/// Returneaza prenumele participantului
/// @param p participant
/// @return string, prenumele participantului
char* get_prenume_participant(participant* p);

/// Seteaza prenumele participantului
/// @param p participant
/// @param nou_prenume string, noul prenume al participantului
void set_prenume_participant(participant* p, char* nou_prenume);

/*
Functia returneaza o copie a unui participant
@param p participantul
@return copia participantului
*/
participant* copyParticipant(participant* p);

#endif //DOMAIN_H
