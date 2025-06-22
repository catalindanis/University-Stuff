#include "Validator.h"
#include "Domain.h"
#include <string.h>

int valideaza_participant(participant* p) {

    int ok = 0;
    if (get_id_participant(p) <= 0)
        ok = 1;
    if (strlen(get_nume_participant(p)) == 0)
        ok = 1;
    if (strlen(get_prenume_participant(p)) == 0)
        ok = 1;
    if (get_scor_participant(p) > 100 || get_scor_participant(p) < 0)
        ok = 1;
    return ok;
}


