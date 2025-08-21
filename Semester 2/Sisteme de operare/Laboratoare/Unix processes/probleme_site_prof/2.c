#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char* argv[]){
	if(argc != 2){
		printf("Invalid no of args\n");
		exit(1);
	}

	int n = atoi(argv[1]);
	printf("Parent, pid: %d\n", getpid());
	for(int i=0;i<n;i++){
		if(fork() != 0){
			wait(NULL);
			exit(0);
		}
		printf("Child #%d, pid: %d, parent pid: %d\n", i+1, getpid(), getppid());
	}
	return 0;
}
