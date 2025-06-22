
#ifndef VALIDATOR_H
#define VALIDATOR_H
#include "Domain.h"


/// Valideaza un participant
/// @param p Participant
/// @return 0 daca partcicipantul este valid:
///             numele si prenumele trebuie sa fie nevid
///             scorul trebuie sa fie in intervalul [0-100]
///             id-ul trebuie sa fie >0
///          1 daca nu e valid
int valideaza_participant(participant* p);


#endif //VALIDATOR_H
