#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

int* v;
pthread_t* t; 
int n;
pthread_mutex_t m;

void* f(void* a){
	int* arr = (int*)a;
	v[arr[0]] = v[arr[0]] + v[arr[1]];
	free(arr);
	return NULL;
}

int adunare(){
	int dr = n;
	int p = 1;
	while(dr > 1){	
		for(int i=0; i < dr; i += 2){
			int* v = malloc(2 * sizeof(int));
			v[0] = i * p;
			v[1] = (i + 1) * p;
			pthread_create(&t[i/2], NULL,
				f, v);
		}
		for(int i=0;i<dr;i+=2)
			pthread_join(t[i/2], NULL);
		dr /= 2;
		p *= 2;
	}
	return v[0];
}

int main(){
	printf("N = ");
	scanf("%d", &n);		
	v = malloc(n * sizeof(int));
	t = malloc(n / 2 * sizeof(pthread_t));
	for(int i=0;i<n;i++)
		v[i] = i+1;
	printf("Suma = %d\n", adunare());
	return 0;
}
