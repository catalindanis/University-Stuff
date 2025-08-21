#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <string.h>

int main(){
	int p[2];
	pipe(p);
	
	//p[0] - read
	//p[1] - write

	if(fork() == 0){
		//child
		close(p[1]);
		int n;
		char input[100];

		while(1){
			read(p[0], &n, sizeof(int));
			read(p[0], input, n);
			printf("Am primit: %s\n", input);	
		}

		close(p[0]);
		exit(0);
	}
	
	close(p[0]);

	int n;
	char input[100];
	while(1){
		fgets(input, sizeof(input), stdin);
		n = strlen(input) + 1;
		write(p[1], &n, sizeof(int));
		write(p[1], input, n);
		//printf("Am trimis: %s de lungime %d\n", input, n);
	}

	close(p[1]);
	wait(0);
	return 0;
}
