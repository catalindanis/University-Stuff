#include <stdio.h>
#include <stdlib.h>

int main(){
	FILE* f = popen("ls", "r");
	char* input = (char*)malloc(100);
	fscanf(f, "%s", input);
	printf("%s", input);
	pclose(f);
	return 0;
}
