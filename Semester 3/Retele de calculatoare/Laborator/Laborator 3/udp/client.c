#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

int main() {
  int c;
  struct sockaddr_in server;
  uint16_t a, b, suma, i, k;
  
  
  c = socket(AF_INET, SOCK_DGRAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
 
  char s[101];
  scanf("%s", s);
  int n = strlen(s);
  char character;
  scanf(" %c", &character);

  sendto(c, &n, sizeof(n), 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, s, sizeof(s[0]) * n, 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, &character, sizeof(character), 0, (struct sockaddr *) &server, sizeof(server));

  int l = sizeof(server);
  int nRez;
  int rez[100];

  recvfrom(c, &nRez, sizeof(nRez), MSG_WAITALL, (struct sockaddr *) &server, &l);
  recvfrom(c, rez, sizeof(rez[0]) * nRez, MSG_WAITALL, (struct sockaddr *) &server, &l);

  for(int i=0;i<nRez;i++)
     printf("%d ", rez[i]);
  printf("\n");

  close(c);
}
