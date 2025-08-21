#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main(){
	int a2b[2];
	int b2a[2];

	pipe(a2b);
	pipe(b2a);

	int n;
	
	if(fork() == 0){
		close(a2b[1]);
		close(b2a[0]);
	  	
		srandom(getpid());
		while(1){

			if(0 > read(a2b[0], &n, sizeof(int)))
				break;

			printf("[B] Received: %d\n", n);

			if(n == 10){
				break;
			}

			n = random() % 10 + 1;

			printf("[B] Sending: %d\n", n);
			write(b2a[1], &n, sizeof(int));

		}
		printf("[B] Stopping...\n");

		close(a2b[0]);
		close(b2a[1]);

		exit(0);
	}

	close(a2b[0]);
	close(b2a[1]);
	
	srandom(getpid());
	while(1){
		n = random() % 10 + 1;

		printf("[A] Sending: %d\n", n);
		write(a2b[1], &n, sizeof(int));

		if(0 > read(b2a[0], &n, sizeof(int)))
			break;
		
		printf("[A] Received: %d\n", n);

		if(n == 10){
			break;
		}

	}
	printf("[A] Stopping...\n");
	
	close(a2b[1]);
	close(b2a[0]);
	
	wait(0);
	return 0;
}
