#include <stdio.h>

void creare_sir_pare(int n, int sir[], int *m, int sir_pare[]);

void creare_sir_impare(int n, int sir[], int *m, int sir_impare[]);

void citire_sir(int *n, int sir[]){
    FILE *file;
    *n = 0;

    file = fopen("numere.txt", "r");
    if (file != NULL) {
        while (fscanf(file, "%d", &sir[*n]) == 1) {
            *n = *n + 1;
        }
        fclose(file);
    }
}

void afisare_sir(int n, int sir[]){
    printf("%d\n", n);
    for (int i = 0; i < n; i++) 
        printf("%d ", sir[i]);
    printf("\n");
}

int main(void){
    int n = 0, sir[100];
    citire_sir(&n, sir);
    afisare_sir(n, sir);
    
    int n_sir_pare = 0, sir_pare[100];
    construire_sir_pare(n, sir, &n_sir_pare, sir_pare);
    afisare_sir(n_sir_pare, sir_pare);
    
    int n_sir_impare = 0, sir_impare[100];
    construire_sir_impare(n, sir, &n_sir_impare, sir_impare);
    afisare_sir(n_sir_impare, sir_impare);
    return 0;
}