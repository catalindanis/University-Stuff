#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

FILE* file;
pthread_mutex_t m;

void* f(void* a){
	int index = *((int*) a);
	char* rez = malloc(1000 * sizeof(char));
	rez[0] = '\0';
	char* word = malloc(21 * sizeof(char));

	int stop = 0;
	while(!stop){
		int n = rand() % 3 + 1;
		pthread_mutex_lock(&m);
		printf("#%d va citi : %d\n", index, n);
		for(int i=0;i<n;i++){
			if(fscanf(file, "%s", word) == EOF){
				stop = 1;
			       	break;
			}
			printf("#%d a citit : %s\n", index, word);
			strcat(rez, word);	
		}
		pthread_mutex_unlock(&m);
		sleep(0.2);
	}
	return rez;
}
int main(int argc, char* argv[]){
	int n = atoi(argv[1]);
	pthread_t t[n];

	file = fopen("32.txt", "r");

	pthread_mutex_init(&m, NULL);
	for(int i=0;i<n;i++){
		int* p = malloc(sizeof(int));
		*p = i;
		pthread_create(&t[i], NULL, f, p);
	}

	for(int i=0;i<n;i++){
		char* rez = NULL;
		pthread_join(t[i], (void**)&rez);
		printf("#%d : %s\n", i, rez);
		free(rez);
	}
	pthread_mutex_destroy(&m);

	(void) argc;
	return 0;
}
