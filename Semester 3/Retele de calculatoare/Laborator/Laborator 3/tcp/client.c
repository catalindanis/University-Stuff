#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  int n;
  char sir[100], caracter;

  if(argc < 2) {
	printf("Ip-ul si port-ul trebuie speficiate ca argumente\n");
  	return 1;
  }

  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(atoi(argv[2]));
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr(argv[1]);
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }

  printf("Introduceti sirul de caractere: ");
  scanf("%s", sir);
  n = strlen(sir);
  
  printf("Introduceti caracterul cautat: ");
  scanf(" %c", &caracter);
 
  n = htonl(n);
  send(c, &n, sizeof(n), 0);
  n = ntohl(n);
  send(c, sir, sizeof(sir[0]) * n, 0);
  send(c, &caracter, sizeof(caracter), 0);
  
  int rez;
  int nRez;
  recv(c, &nRez, sizeof(nRez), MSG_WAITALL);
  nRez = ntohl(nRez);
  for(int i=0;i<nRez;i++) {
	recv(c, &rez, sizeof(rez), MSG_WAITALL);
        rez = ntohl(rez);
	printf("%d ", rez);	
  }
  
  printf("\n");
  close(c);
}
