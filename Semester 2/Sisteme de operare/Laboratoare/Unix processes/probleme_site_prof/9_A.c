#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(){
	char* command = malloc(100);

	fgets(command, 100, stdin);	
	while(strcmp(command, "stop\n") != 0){
		int length = strlen(command);
		command[length-1] = '\0';
		length--;
		printf("%s\n", command);
		fgets(command, 100, stdin);
	}
	return 0;
}
