#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <sys/wait.h>

int pid;

void child(int sgn){
	printf("Copilul a primit semnalul!");
	    fflush(stdout);  
	(void) sgn;
	exit(0);
}

void parent(int sgn){
	printf("Parintele a primit semnalul!");
	    fflush(stdout);
	 kill(pid, SIGUSR1);
	(void) sgn;
	wait(0);
	exit(0);
}

void zombie(int sgn){
	wait(0);
	(void) sgn;
}

int main(){
	pid = fork();
	if(pid == 0){
		signal(SIGUSR1, child);
		while(1);
		exit(0);	
	}
	signal(SIGUSR1, parent);
	signal(SIGCHLD, zombie);
	while(1);
	return 0;
}
