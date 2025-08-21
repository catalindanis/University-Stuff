#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char* argv[]){

	mkfifo("a2b", 0600);
	mkfifo("b2a", 0600);

	int wd = open("a2b", O_WRONLY);
	int rd = open("b2a", O_RDONLY);

	int result_max_len = 1000;
	char* result = malloc(result_max_len);
	for(int i=0;i<result_max_len;i++)
		result[i] = '\0';

	int length = 0;
	for(int i=1;i<argc;i++){
		int arg_len = strlen(argv[i]);
		
		write(wd, &arg_len, sizeof(int));
		write(wd, argv[i], arg_len);
		
		read(rd, argv[i], arg_len);	
      		argv[i][arg_len] = '\0';	

		printf("[A] Am primit %s, de lungime %d\n", argv[i], arg_len);

		strcat(result, argv[i]);
		length += arg_len;
	}

	result[length] = '\0';

	printf("%s\n", result);

	free(result);

	close(wd);
	close(rd);

	unlink("a2b");
	unlink("b2a");

	return 0;
}
