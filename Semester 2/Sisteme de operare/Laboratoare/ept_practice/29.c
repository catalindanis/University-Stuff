#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int v[100];
pthread_mutex_t m;

int main(){
	int n;
	scanf("%d", &n);
	
	pthread_mutex_init(&m, NULL);
	pthread_t t[n];

	for(int i=0;i<n;i++){
		:wq

	}

	pthread_mutex_destroy(&m);
	return 0;

}
