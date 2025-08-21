/*

 * RO: Copiati acest fisier intr-un alt fisier numit solve.c

 * si rezolvati problemele compilare (erori si warning-uri) si rulare;

 * Programul trebuie sa calculeze si sa afiseze frecventa literelor mici

 * dintr-un fisier dat ca argument la linia de comanda.

 *

 * EN: Copy this file into a file named solve.c and

 * fix the compilation errors/warnings and the runtime problems;

 * The program  must calculate the frequency of lowercase letters

 * from a file given as an argument at the command line.

 *

 * Ex:

 * Place the following lines in a file named in.txt:



 A quick brown fox jumps over the lazy dog.

 Feel free to add random things like ,./;'[]\-=_+ or digits or other letters.



 *

 * Compile using:

 * gcc -Wall -g solve.c -o exe

 *

 * Run with:

 * valgrind ./exe in.txt

 *

 * Expected result:

Count = 3 character = a

Count = 1 character = b

Count = 1 character = c

Count = 5 character = d

Count = 10 character = e

Count = 2 character = f

Count = 3 character = g

Count = 3 character = h

Count = 5 character = i

Count = 1 character = j

Count = 2 character = k

Count = 4 character = l

Count = 2 character = m

Count = 3 character = n

Count = 9 character = o

Count = 1 character = p

Count = 1 character = q

Count = 8 character = r

Count = 4 character = s

Count = 7 character = t

Count = 2 character = u

Count = 1 character = v

Count = 1 character = w

Count = 1 character = x

Count = 1 character = y

Count = 1 character = z

 */
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>

void increment_frequency(int *frequency, char c) {

    if(c >= 'a' && c <= 'z') {

        frequency[c - 'a']++;

    }

}



int main(int argc, char *argv[]) {

    int* frequency;

    char* buffer;

    int i;

    int capacity = 26;



    buffer = (char*)malloc(1024*sizeof(char));
    frequency = (int*)malloc(sizeof(int) * capacity);


    for(i=0; i < capacity; i++) {

        frequency[i] = 0;

    }



    int fd = open(argv[1], O_RDONLY);

    if(fd < 0) {

        perror("Error opening the file");

        return 1;

    }



    while(1) {

        int k = read(fd, buffer, 1024);

        if(k <= 0) {

            break;

        }

        for(int i=0; i<k; i++) {

            increment_frequency(frequency, buffer[i]);

        }

    }



    close(fd);



    for(i=0; i < capacity; i++) {

        if(frequency[i] == 0) {

            continue;

        }

        printf("Count = %d character = %c\n", frequency[i], i + 'a');

    }



    free(buffer);
    free(frequency);

    (void) argc;
    return 0;

}

