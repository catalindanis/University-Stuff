#include <stdio.h>
#include <pthread.h>
#include <string.h>

int ap[10];
pthread_mutex_t m[10];

void* f(void* arg){
	char* s = (char*) arg;

	for(unsigned int i=0;i<strlen(s);i++){
		int c = s[i] - '0';
		pthread_mutex_lock(&m[c]);
		ap[c] ++;
		pthread_mutex_unlock(&m[c]);
	}
	(void) arg;
	return NULL;
}

int main(int argc, char* argv[]){
	
	int n = argc;
	pthread_t t[n - 1];

	for(int i=0;i<10;i++)
		pthread_mutex_init(&m[i], NULL);

	for(int i=1;i<n;i++){
		pthread_create(&t[i-1], NULL, f, argv[i]);
	}

	for(int i=1;i<n;i++)
		pthread_join(t[i-1], NULL);

	for(int i=0;i<10;i++)
		pthread_mutex_destroy(&m[i]);
	
	for(int i=0;i<10;i++){
		printf("ap[%d] = %d\n", i, ap[i]);
	}

	return 0;
}
