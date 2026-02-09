#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main() {
  int c;
  struct sockaddr_in server;
  uint16_t a, b, suma;
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(8889);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("10.51.1.14");
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }
 
  uint16_t id = (uint16_t) 50380;

  id = htons(id);
  send(c, &id, sizeof(id), 0);
  
  uint16_t lungimeEnunt;
  recv(c, &lungimeEnunt, sizeof(lungimeEnunt), MSG_WAITALL);

  lungimeEnunt = ntohs(lungimeEnunt);
  printf("Lungimea enuntului este %hu\n", lungimeEnunt);
 
  char enunt[500];
  recv(c, enunt, sizeof(enunt[0]) * lungimeEnunt, MSG_WAITALL);
  enunt[lungimeEnunt] = '\0';

  lungimeEnunt = htons(lungimeEnunt);
  send(c, &lungimeEnunt, sizeof(lungimeEnunt), 0);

  printf("%s\n", enunt);

  
  char length;
  char input[256];
  while(1) {
    recv(c, &length, sizeof(length), MSG_WAITALL);
    recv(c, input, sizeof(input[0]) * length, MSG_WAITALL);
    input[length] = '\0';
    
    if(length == 0)
	    break;
  }

  close(c);
}
