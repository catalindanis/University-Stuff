#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int POTATO;
pthread_mutex_t m;

void* f(void* a){
	while(1){
		int c = rand() % 91 + 10;
		pthread_mutex_lock(&m);
		
		if(POTATO < 0)
			break;

		printf("#%d scade : %d\n", *((int*) a), c);
		POTATO -= c;

		printf("Potato = %d\n", POTATO);
		if(POTATO < 0)
			printf("Am inchis : %d\n", *((int*) a));
		pthread_mutex_unlock(&m);
		
		sleep( (rand() % 101 + 100) * 1.0 / 10);
	}	
	free(a);
	pthread_mutex_unlock(&m);
	return NULL;
}

int main(int argc, char* argv[]){
	int n = atoi(argv[1]);
	POTATO = rand() % 9001 + 1000;
	printf("Potato = %d", POTATO);
	pthread_t t[n];

	pthread_mutex_init(&m, NULL);
	for(int i=0;i<n;i++){
		int* p = malloc(sizeof(int));
		*p = i;
		
		pthread_create(&t[i], NULL, f, p);
	}

	for(int i=0;i<n;i++)
		pthread_join(t[i], NULL);
	pthread_mutex_destroy(&m);
	(void) argc;
	return 0;
}
