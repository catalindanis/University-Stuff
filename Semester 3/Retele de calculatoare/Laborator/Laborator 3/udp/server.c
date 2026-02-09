#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  int l;
  uint16_t k, old = 0;
  
  s = socket(AF_INET, SOCK_DGRAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;
  
  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
 
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  
  int n;
  char sir[101], c;

  recvfrom(s, &n, sizeof(n), MSG_WAITALL, (struct sockaddr *) &client, &l);
  //printf("Am primit: %d", n);
  recvfrom(s, &sir, sizeof(sir[0]) * n, MSG_WAITALL, (struct sockaddr *) &client, &l);
  sir[n] = '\0';
  //printf("Am primit: %s", sir);
  recvfrom(s, &c, sizeof(c), MSG_WAITALL, (struct sockaddr *) &client, &l);
  //printf("Am primit: %c", c);

  int index = 0;
  int positions[101];

  for(int i=0;i<n;i++) 
     if(sir[i] == c)
        positions[index++] = i;	     

  sendto(s, &index, sizeof(index), 0, (struct sockaddr *) &client, sizeof(client)); 
  sendto(s, positions, sizeof(positions[0]) * index, 0, (struct sockaddr *) &client, sizeof(client));

  close(s);
}
