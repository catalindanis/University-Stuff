#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>

int main(){
	char* s = malloc(100 * sizeof(char));

	scanf("%s", s);
	
	int p2a[2], p2b[2];
	int a2p[2], b2p[2];

	pipe(p2a);
	pipe(p2b);
	pipe(a2p);
	pipe(b2p);

	if(fork() == 0){
		close(p2b[0]);
		close(p2b[1]);
		close(b2p[0]);
		close(b2p[1]);

		close(p2a[1]);
		close(a2p[0]);

		char* rez = malloc(100 * sizeof(char));
		char* val = malloc(100 * sizeof(char));
		int n, lRez = 0;

		read(p2a[0], &n, sizeof(int));
	       	read(p2a[0], val, n);
		close(p2a[0]);

		for(int i=0;i<n;i++)
			if(tolower(val[i]) >= 'a' && tolower(val[i]) <= 'e')
				rez[lRez++] = val[i];
		rez[lRez++] = '\0';
		write(a2p[1], &lRez, sizeof(int));
		write(a2p[1], rez, lRez);
		close(a2p[1]);

		free(rez);
		free(val);
		exit(0);
	}
	if(fork() == 0){
		close(p2a[0]);
		close(p2a[1]);
		close(a2p[0]);
		close(a2p[1]);

		close(p2b[1]);
		close(b2p[0]);
		
		char* rez = malloc(100 * sizeof(char));
                char* val = malloc(100 * sizeof(char));
                int n, lRez = 0;

                read(p2b[0], &n, sizeof(int));
                read(p2b[0], val, n);
                close(p2b[0]);

                for(int i=0;i<n;i++)
                        if(tolower(val[i]) >= 'v' && tolower(val[i]) <= 'z')
                                rez[lRez++] = val[i];
                rez[lRez++] = '\0';
		write(b2p[1], &lRez, sizeof(int));
                write(b2p[1], rez, lRez);
                close(b2p[1]);

                free(rez);
                free(val);

		exit(0);
	}

	close(p2a[0]);
	close(p2b[0]);
	close(a2p[1]);
	close(b2p[1]);
	
	int n = strlen(s);
	
	write(p2a[1], &n, sizeof(int));
	write(p2a[1], s, n);
	close(p2a[1]);

	write(p2b[1], &n, sizeof(int));
	write(p2b[1], s, n);
	close(p2b[1]);

	read(a2p[0], &n, sizeof(int));
	read(a2p[0], s, n);
	close(a2p[0]);
	printf("A : %s\n", s);

	read(b2p[0], &n, sizeof(int));
	read(b2p[0], s, n);
	close(b2p[0]);
	printf("B : %s\n", s);

	free(s);
	wait(0);
	wait(0);
	return 0;
}
