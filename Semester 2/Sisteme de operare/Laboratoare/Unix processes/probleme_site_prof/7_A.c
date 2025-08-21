#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(){
	
	mkfifo("a2b", 0600);
	mkfifo("b2a", 0600);

	int wd = open("a2b", O_WRONLY);
	int rd = open("b2a", O_RDONLY);

	srandom(getpid());
	while(1){
		int number = random() % 10 + 1;

		write(wd, &number, sizeof(int));
				
		printf("A sent : %d\n", number);

		if(number == 10)
			break;

		read(rd, &number, sizeof(int));

		printf("A received : %d\n", number);
		
		if(number == 10)
			break;
	}

	close(wd);
	close(rd);

	unlink("a2b");
	unlink("b2a");
	return 0;
}
