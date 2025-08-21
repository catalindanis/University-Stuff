#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

struct rezultat{
	int cifre;
	int litere;
	int speciale;
	pthread_mutex_t m;
};

struct arg{
	char* str;
	struct rezultat* rez;
};

void* procesare(void* arg){
	struct arg* a = (struct arg*) arg;
	
	int cntC = 0, cntL = 0, cntS = 0;
	for(unsigned int i=0; i < strlen(a->str); i++){
		if(isdigit(a->str[i]))
			cntC++;
		else if(isalpha(a->str[i]))
			cntL++;
		else
			cntS++;
	}
	
	pthread_mutex_lock(&(a->rez->m));

	a->rez->cifre += cntC;
	a->rez->litere += cntL;
	a->rez->speciale += cntS;

	pthread_mutex_unlock(&(a->rez->m));

	free(arg);
	return NULL;
}

int main(int argc, char* argv[]){

	int n = argc;
	pthread_t t[n - 1];

	pthread_mutex_t m;
	pthread_mutex_init(&m, NULL);

	struct rezultat* rez = malloc(sizeof(struct rezultat));
	rez->cifre = 0;
	rez->litere = 0;
	rez->speciale = 0;
	rez->m = m;
	
	for(int i=1;i<n;i++){
		struct arg* a = malloc(sizeof(struct arg));
		a->str = argv[i];
		a->rez = rez;
		pthread_create(&t[i], NULL, procesare, a);	
	}
	
	for(int i=1;i<n;i++)
		pthread_join(t[i], NULL);

	printf("Numar de litere: %d\n", rez->litere);
	printf("Numar de cifre: %d\n", rez->cifre);
	printf("Numar de speciale: %d\n", rez->speciale);

	pthread_mutex_destroy(&m);
	free(rez);
	
	(void) argv;
	(void) t;
	return 0;
}
