#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char* argv[]){
	int n = atoi(argv[1]);
	
	if(n == 0){
		printf("Invalid value");
		exit(1);
	}

	int v[n];

	printf("[P] Generated numbers: ");
	for(int i=0;i<n;i++){
		v[i] = random() % 100;	
		printf("%d ", v[i]);
	}

	printf("\n");

	int ptc[2];
	int ctp[2];
	pipe(ptc);
	pipe(ctp);

	int sum;
	if(fork() == 0){
		close(ptc[1]);
		close(ctp[0]);
		sum = 0;
		int val;
		for(int i=0;i<n;i++){
			read(ptc[0], &val, sizeof(int));		
			sum += val;
		}

		close(ptc[0]);
		sleep(2);
		write(ctp[1], &sum, sizeof(int));
		close(ctp[1]);

		exit(0);
	}

	close(ptc[0]);
	close(ctp[1]);

	for(int i=0;i<n;i++)
		write(ptc[1], &v[i], sizeof(int));
	
	close(ptc[1]);
	read(ctp[0], &sum, sizeof(int));
	close(ctp[0]);

	printf("[P] The sum is: %d\n", sum);

	wait(0);
	(void) argc;
	return 0;
}
