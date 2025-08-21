#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

int main(){

	int rd = open("a2b", O_RDONLY);
	int wd = open("b2a", O_WRONLY);
	
	while(1){
		int len;
		int n = read(rd, &len, sizeof(int));
		
		if(n == 0)
			break;
		
		char arg[100];
		arg[len] = '\0';
		read(rd, arg, len);
	
		printf("[B] Am primit argumentul %s, de lungime %d\n", arg, len);
		for(int i=0;i<len;i++)
			if(arg[i] >= 'a' && arg[i] <= 'z')
				arg[i] = arg[i] - ('a' - 'A');
		
		printf("[B] Trimit argumentul %s, de lungime %d\n", arg, len);
		write(wd, arg, len);
	}

	close(rd);
	close(wd);

	return 0;
}
