#include <stdio.h>

int main(){
	FILE* f = popen("less", "w");
	
	for(int i=99;i>=0;i--){
		fprintf(f, "%d bottles of beer!\n", i);
	}

	pclose(f);
	return 0;
}
