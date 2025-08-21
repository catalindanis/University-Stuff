#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

int number_of_elements = 10;
int v[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
int number_of_threads = 30;
pthread_t t[30];
pthread_mutex_t m[10];
pthread_mutex_t print;

void display(){
	pthread_mutex_lock(&print);
	for(int i=0;i<number_of_elements;i++)
		printf("%d, ", v[i]);
	printf("\n");
	pthread_mutex_unlock(&print);
}

void* f(void* a){
	int i = random() % 10;
	int j = random() % 10;
	int st = i < j ? i : j, dr = i < j ? j : i;

	pthread_mutex_lock(&m[st]);
	pthread_mutex_lock(&m[dr]);
	 
	int aux = v[i];
	v[i] = v[j];
	v[j] = aux;
	//display();
	
	pthread_mutex_unlock(&m[st]);
	pthread_mutex_unlock(&m[dr]);
	return NULL;
	(void) a;
}

int main(){
	srand(time(NULL));
	
	pthread_mutex_init(&print, NULL);
	for(int i=0;i<number_of_threads;i++)
		pthread_mutex_init(&m[i], NULL);
	
	for(int i=0;i<number_of_threads;i++){
		pthread_create(&t[i], NULL, f, NULL);
	}

	for(int i=0;i<number_of_threads;i++)
		pthread_join(t[i], NULL);

	pthread_mutex_destroy(&print);
	for(int i=0;i<number_of_threads;i++)
		pthread_mutex_destroy(&m[i]);
	return 0;
}
