#include <stdio.h>
#include <unistd.h>

int main(){
	int n;
	printf("N=");
	scanf("%d", &n);

	int p[2];

	for(int i=0;i<n;i++){
		p = pipe();
		if(fork() == 0)
			break;
	}

	wait(0);	
	return 0;
}
