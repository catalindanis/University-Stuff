#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(){
	int n;
   	printf("N=");
	scanf("%d", &n);
	printf("(from parent) My PID: %d\n", getpid());
	for(int i=0;i<n;i++){
		int child_pid = fork();
		if(child_pid == 0){
			printf("(from child) Parent PID: %d, My PID: %d\n", getppid(), getpid());
			exit(0);
		}
		printf("(from parent) Child PID: %d\n", child_pid);
		wait(0);
	}	
	return 0;
}
