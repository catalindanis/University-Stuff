#include <stdio.h>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

int n, m, stop = 0;
pthread_t* t;
pthread_barrier_t* b;
sem_t* s;
pthread_mutex_t mutex;

void* f(void* arg){
	int index = *((int*) arg);
	pthread_barrier_wait(b);
	for(int i=0;i<m;i++){
		sem_wait(&s[i]);

		usleep(10000);
		printf("thread-ul %d a trecut\n", index);

		sem_post(&s[i]);
	}
	pthread_mutex_lock(&mutex);

	if(stop == 0){
		stop = 1;
		//printf("thread-ul %d a castigat!\n", index);
	}

	pthread_mutex_unlock(&mutex);
	return NULL;
}

int main(int argc, char* argv[]){
	
	printf("%d\n", argc);

	if(argc < 3 || argc > 3){
		printf("Please provide exactly 2 args!\n");
		exit(1);
	}
	
	n = atoi(argv[1]);
	m = atoi(argv[2]);

	t = malloc(sizeof(pthread_t) * n);
	s = malloc(sizeof(sem_t) * m);
	b = malloc(sizeof(pthread_barrier_t));

	pthread_mutex_init(&mutex, NULL);
	pthread_barrier_init(b, NULL, n);

	
	for(int i=0;i<m;i++)
		sem_init(&s[i], 0, 1);

	for(int i=0;i<n;i++){
		int* index = malloc(sizeof(int));
		*index = i;
		pthread_create(&t[i], NULL, f, index);
	}

	for(int i=0;i<n;i++)
		pthread_join(t[i], NULL);

	for(int i=0;i<m;i++)
		sem_destroy(&s[i]);

	pthread_barrier_destroy(b);
	pthread_mutex_destroy(&mutex);
	return 0;
}
