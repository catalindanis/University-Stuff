#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
int n;
int m[100][100];
int sum[100];

void* func(void* a){
	int index = *((int*) a);
	for(int i=0;i<n;i++)
		sum[index] += m[index][i];
	free(a);
	return NULL;
}

int main(){
 	FILE* f = fopen("12a.txt", "r");
	fscanf(f, "%d", &n);
	for(int i=0;i<n;i++)
		for(int j=0;j<n;j++)
			fscanf(f, "%d", &m[i][j]);
	pthread_t t[100];
	for(int i=0;i<n;i++){
		int* p = malloc(sizeof(int));
		*p = i;
		pthread_create(&t[i], NULL, func, p);
	}
	for(int i=0;i<n;i++)
		pthread_join(t[i], NULL);

	for(int i=0;i<n;i++)
		printf("%d\n", sum[i]);
	return 0;
}
