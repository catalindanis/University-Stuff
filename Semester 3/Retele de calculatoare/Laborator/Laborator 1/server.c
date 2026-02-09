#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  int c, l;
  
  s = socket(AF_INET, SOCK_STREAM, 0);
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
 
  listen(s, 5);
  
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  
  while (1) {
    uint16_t n;
    char sir[100], caracter;
    c = accept(s, (struct sockaddr *) &client, &l);
    printf("S-a conectat un client.\n");
    
    // deservirea clientului
    recv(c, &n, sizeof(n), MSG_WAITALL);
    recv(c, sir, sizeof(sir[0]) * n, MSG_WAITALL);
    recv(c, &caracter, sizeof(caracter), MSG_WAITALL);
    
    int rez[100], nRez = 0;
    for(int i=0;i<n;i++) {
	    if(sir[i] == caracter) {
		rez[nRez++] = i;
	    	//printf("Am gasit %c pe pozitia %d\n", caracter, i);
	    }
    		
    }
    send(c, &nRez, sizeof(nRez), 0);
    send(c, rez, sizeof(rez[0]) * nRez, 0);
    close(c);
    // sfarsitul deservirii clientului;
  }
}
