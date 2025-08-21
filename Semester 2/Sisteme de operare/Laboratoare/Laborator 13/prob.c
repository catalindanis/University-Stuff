#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <pthread.h>
#include <sys/wait.h>
#include <string.h>

void* f(void* arg){
	char* file = (char*) arg;
	int fd = open(file, O_RDONLY);
	char buffer[4096] = "";

	int s = 0;

	while(read(fd, buffer, 4095) > 0){
		for(unsigned int i=0;i<strlen(buffer);i++)
			s += buffer[i];		
	}
	
	printf("Numar bytes in %s : %d\n", file, s);

	return NULL;
}

int main(int argc, char* argv[]){

	int noChilds = 0, noThreads = 0;

	pthread_t t[argc - 1];

	for(int i=1;i<argc;i++){
		if(access(argv[i], X_OK) == 0){
			printf("Executabil : %s\n", argv[i]);
			if(fork() == 0)
				execl(argv[i], argv[i], NULL);
		}
		else if(access(argv[i], F_OK) == 0){
			printf("Fisier : %s\n", argv[i]);	
			pthread_create(&t[noThreads++], NULL, f, argv[i]);
		}
		else printf("Ignor : %s\n", argv[i]);
	}	
	
	for(int i=0;i<noChilds;i++)
		wait(0);

	for(int i=0;i<noThreads;i++)
		pthread_join(t[i], NULL);

	return 0;
}
