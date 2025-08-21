#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(){
	
	int rd = open("a2b", O_RDONLY);
	int wd = open("b2a", O_WRONLY);

	srandom(getpid());
	while(1){
		int number;
		
		read(rd, &number, sizeof(int));
		
		printf("B received : %d\n", number);

		if(number == 10)
			break;

		number = random() % 10 + 1;

		write(wd, &number, sizeof(int));

		printf("B sent : %d\n", number);

		if(number == 10)
			break;

	}

	close(wd);
	close(rd);

	return 0;
}
