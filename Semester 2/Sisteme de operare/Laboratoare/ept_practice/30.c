#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>

pthread_barrier_t b;
int times[1000];

typedef struct arg{
        pthread_cond_t* c;
	pthread_mutex_t m;
	int* teamIndex;
}arg;

void* t0(void* argv){
	arg* p = (arg*) argv;
	pthread_barrier_wait(&b);
	//printf("Start\n");

	int n = rand() % 101 + 100;
	usleep(n * 1000);
	
	times[*p->teamIndex] += n;
	pthread_cond_signal(&p->c[0]);	
	return NULL;
}

void* t1(void* argv){
        arg* p = (arg*) argv;
        pthread_barrier_wait(&b);

	pthread_cond_wait(&p->c[0], &p->m);
	pthread_mutex_unlock(&p->m);
	//printf("1\n");

        int n = rand() % 101 + 100;
        usleep(n * 1000);

	times[*p->teamIndex] += n;
        pthread_cond_signal(&p->c[1]);
        return NULL;
}

void* t2(void* argv){
        arg* p = (arg*) argv;
        pthread_barrier_wait(&b);

	pthread_cond_wait(&p->c[1], &p->m);
	pthread_mutex_unlock(&p->m);
	//printf("2\n");

        int n = rand() % 101 + 100;
        usleep(n * 1000);

	times[*p->teamIndex] += n;
        pthread_cond_signal(&p->c[2]);
        return NULL;
}

void* t3(void* argv){
        arg* p = (arg*) argv;
        pthread_barrier_wait(&b);

        pthread_cond_wait(&p->c[2], &p->m);

        int n = rand() % 101 + 100;
        usleep(n * 1000);

	times[*p->teamIndex] += n;
	//printf("Finish\n");
        return NULL;
}

int main(){

	int n;
	printf("N=");
	scanf("%d", &n);

	pthread_t t[4 * n];
	pthread_barrier_init(&b, NULL, 4 * n);

	for(int i=0;i<n;i++){
		arg* a = malloc(sizeof(arg));
		a->c = malloc(3 * sizeof(pthread_cond_t));
		int* index = malloc(sizeof(int));
		*index = i;
		a->teamIndex = index;

		pthread_mutex_init(&a->m, NULL);

		pthread_cond_init(&a->c[0], NULL);
		pthread_cond_init(&a->c[1], NULL);
		pthread_cond_init(&a->c[2], NULL);		

		pthread_create(&t[4 * i], NULL, t0, a);
		pthread_create(&t[4 * i + 1], NULL, t1, a);
		pthread_create(&t[4 * i + 2], NULL, t2, a);
		pthread_create(&t[4 * i + 3], NULL, t3, a);
	}

	for(int i=0;i<n;i++){
		pthread_join(t[4 * i], NULL);
		pthread_join(t[4 * i + 1], NULL);
		pthread_join(t[4 * i + 2], NULL);
		pthread_join(t[4 * i + 3], NULL);
	}

	int minim = 0;
	for(int i=0;i<n;i++){
		if(times[i] < times[minim])
			minim = i;
		printf("Echipa #%d : %d\n", i, times[i]);
	}
	printf("%d a castigat!\n", minim); 
	pthread_barrier_destroy(&b);
	return 0;
}
