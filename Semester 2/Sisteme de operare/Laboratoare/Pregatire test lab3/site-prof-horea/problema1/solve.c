/*

    RO: Copiati acest fisier intr-un alt fisier numit solve.c

    si rezolvati problemele compilare (erori si avertismente) si rulare (crashes, alte erori raportate de valgrind);

    Programul primeste un argument la linia de comanda ce reprezinta un nume de fisier.

    Programul citeste din fisier un singur numar intreg N apoi creeaza o matrice de dimensiune

    N x N dupa urmatoarea regula: matrix[i][j] = j - i. Matricea va fi salvata apoi intr-un fisier cu numele out.txt.



    EN: Copy this file into another file named solve.c and

    fix the compilation errors and warnings and any crashes or runtime problems reported by valgrind;

    The program will receive one filename as a command line argument.

    The program will read from the file a single integer N and then creates a matrix of size N x N

    using the following rule: matrix[i][j] = j - i. The matrix will be saved in a file with the name out.txt.



    Ex:

    Create a file named "in.txt" that contains the number 5.



    ------ This information will not be present for the actual test ------

    ----------------------------------------------------------------------



    Rezultat/Result:

    The file out.txt will be created if it does not exist and its content will be

     0   1   2   3   4

    -1   0   1   2   3

    -2  -1   0   1   2

    -3  -2  -1   0   1

    -4  -3  -2  -1   0



*/

#include <stdio.h>
#include <stdlib.h>



int** allocate_matrix(int size) {

    int **mat = (int**)malloc(sizeof(int*) * size);

    for(int i = 0; i < size; i++) {

        mat[i] = (int*)malloc(sizeof(int) * size);

    }

    return mat;

}



void print_matrix_to_fileptr(FILE *file, int **mat, int size) {

    for(int i = 0; i < size; i++) {

        for(int j = 0; j < size; j++) {

            fprintf(file, "%d", mat[i][j]);

        }

        fprintf(file, "\n");

    }

}



void deallocate_matrix(int **mat, int size) {

    for(int i = 0; i < size; i++) {

        free(mat[i]);

    }

    free(mat);

}





int main(int argc, char *argv[]) {

    if(argc != 2) {

        printf("The program requires exactly one argument\n");

        exit(1);

    }


    // open the file for reading

    FILE *in_file = fopen(argv[1], "r");

    if(NULL == in_file) {

        perror("Cannot open input file");

        exit(1);

    }



    int size = 0;

    fscanf(in_file, "%d", &size);



    fclose(in_file);

    if(0 >= size) {

        printf("Cannot create matrix with size %d since it is not positive.\n", size);

        exit(1);

    }



    int **mat = allocate_matrix(size);



    for(int i = 0; i < size; i++)

        for(int j = 0; j < size; j++)

            mat[i][j] = j - i;



    // open the file for writing

    FILE *out_file = fopen("out.txt", "w");

    if(NULL == out_file) {

        perror("Cannot open output file");

        exit(1);

    }



    print_matrix_to_fileptr(out_file, mat, size);



    fclose(out_file);



    deallocate_matrix(mat, size);

    return 0;

}
