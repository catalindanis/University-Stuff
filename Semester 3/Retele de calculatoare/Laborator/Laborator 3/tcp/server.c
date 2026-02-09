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
    int n;
    char sir[100], caracter;
    c = accept(s, (struct sockaddr *) &client, &l);
    printf("S-a conectat un client.\n");
    
    if(fork() == 0) {
    	// deservirea clientului
    	recv(c, &n, sizeof(n), MSG_WAITALL);
	n = ntohl(n);
    	recv(c, sir, sizeof(sir[0]) * n, MSG_WAITALL);
    	recv(c, &caracter, sizeof(caracter), MSG_WAITALL);
    
	// printf("%d %s %c\n", n, sir, caracter);

    	int rez[100], nRez = 0;
    	for(int i=0;i<n;i++) {
	    if(sir[i] == caracter) {
		rez[nRez++] = i;
	    	//printf("Am gasit %c pe pozitia %d\n", caracter, i);
	    }
    		
    	}

	nRez = ntohl(nRez);
    	send(c, &nRez, sizeof(nRez), 0);
	nRez = htonl(nRez);
	for(int i=0;i<nRez;i++) {
		rez[i] = ntohl(rez[i]);
    		send(c, &rez[i], sizeof(rez[i]), 0);
	}
	close(c);
    	// sfarsitul deservirii clientului;
    	exit(0);
    }
  }
}
