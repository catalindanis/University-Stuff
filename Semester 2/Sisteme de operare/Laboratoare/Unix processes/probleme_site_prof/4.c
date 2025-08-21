#include <stdio.h>
#include <sys/time.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

int main(int argc, char* argv[]){
	struct timeval t1, t2;

	gettimeofday(&t1, NULL);
	if(fork() == 0){
		execvp("grep", argv);
		exit(0);
	}
	wait(0);
	gettimeofday(&t2, NULL);

	printf("Elapsed time: %lf\n",(double) (t2.tv_usec - t1.tv_usec) / 1000000 + (double) (t2.tv_sec - t1.tv_sec) );

	(void) argc;
	(void) argv;
	return 0;
}
