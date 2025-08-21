#include <stdio.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>

struct p{
	int index;
	pthread_barrier_t* barrier;
	int* stop;
};

void* f(void* arg){
	struct p* a = (struct p*) arg;	
	pthread_barrier_wait(a->barrier);
	while(*(a->stop) == 0){
		int n = rand() % 111112;

		if(*(a->stop) == 0){
			printf("#%d a generat : %d\n", 
					a->index,
					n);
			if(n % 1001 == 0 && *(a->stop) == 0){
				*(a->stop) = 1;
				printf("#%d a oprit\n", a->index);
			}
		}
	}
	return NULL;
}

int main(int argc, char* argv[]){
	int n = atoi(argv[1]);
	
	srand(time(NULL));
	int* stop = malloc(sizeof(int));
	*stop = 0;
	pthread_t t[n];
	pthread_barrier_t* b = malloc(sizeof(pthread_barrier_t));
	pthread_barrier_init(b, NULL, n);

	for(int i=0;i<n;i++){
		struct p* arg = malloc(sizeof(struct p));
		arg->index = i;
		arg->barrier = b;	
		arg->stop = stop;
		pthread_create(&t[i], NULL, f, arg);
	}

	for(int i=0;i<n;i++)
		pthread_join(t[i], NULL);

	pthread_barrier_destroy(b);

	(void) argc;
	return 0;
}
