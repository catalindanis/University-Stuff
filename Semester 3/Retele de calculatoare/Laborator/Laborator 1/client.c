#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

int main() {
  int c;
  struct sockaddr_in server;
  uint16_t n;
  char sir[100], caracter;
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }

  printf("Introduceti sirul de caractere: ");
  scanf("%s", sir);
  n = strlen(sir);
  
  printf("Introduceti caracterul cautat: ");
  scanf(" %c", &caracter);
 
  send(c, &n, sizeof(n), 0);
  send(c, sir, sizeof(sir[0]) * n, 0);
  send(c, &caracter, sizeof(caracter), 0);
  
  int rez[100];
  int nRez;
  recv(c, &nRez, sizeof(nRez), MSG_WAITALL);
  recv(c, rez, sizeof(rez[0]) * nRez, MSG_WAITALL);
  for(int i=0;i<nRez;i++)
	  printf("%d ", rez[i]);
  
  printf("\n");
  close(c);
}
