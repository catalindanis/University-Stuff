#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char** argv){
	int r, c, fd;
	
	FILE* file;
	file = fopen(argv[1], "r");

	fscanf(file, "%d", &r);
	fscanf(file, "%d", &c);

	int** m;
	m = (int**) malloc(r * sizeof(int*));
	for(int i=0;i<r;i++)
		m[i] = (int*) malloc(c * sizeof(int));	
	
	for(int i=0;i<r;i++)
		for(int j=0;j<c;j++)
			fscanf(file, "%d", &m[i][j]);
	fclose(file);

	printf("%d %d\n", r, c);
	for(int i=0;i<r;i++){
		for(int j=0;j<c;j++)
			printf("%d ", m[i][j]);
		printf("\n");
	}

	fd = open(argv[2], O_CREAT | O_WRONLY, 00600);
 	write(fd, &r, sizeof(int));
 	write(fd, &c, sizeof(int));
 	for(int i=0; i<r; i++) {
 		for(int j=0; j<c; j++) {
 			write(fd, &m[i][j], sizeof(int));
 		}
 	}
 	close(fd);

	fd = open(argv[2], O_RDONLY);
	read(fd, &r, sizeof(int));
	read(fd, &c, sizeof(int));


	close(fd);

	for(int i=0;i<r;i++)
		free(m[i]);	

	free(m);
	return 0;
	(void) argc;
}
