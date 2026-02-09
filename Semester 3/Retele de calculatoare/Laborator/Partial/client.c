#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/select.h>

#define BUF_SIZE 1024

int main() {
    int sock;
    struct sockaddr_in server_addr;
    char buffer[BUF_SIZE];
    fd_set readfds;

    sock = socket(AF_INET, SOCK_STREAM, 0);
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(5555);
    inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr);

    connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr));
    // printf("Conectat la server. Comenzi disponibile: /nick, /msg, /join, /say.\n");

    while (1) {
        FD_ZERO(&readfds);
        FD_SET(sock, &readfds);
        FD_SET(STDIN_FILENO, &readfds);

        select(sock + 1, &readfds, NULL, NULL, NULL);

        if (FD_ISSET(STDIN_FILENO, &readfds)) {
            fgets(buffer, BUF_SIZE, stdin);
            send(sock, buffer, strlen(buffer), 0);
        }

        if (FD_ISSET(sock, &readfds)) {
            int bytes = recv(sock, buffer, BUF_SIZE - 1, 0);
            if (bytes <= 0) {
                printf("Deconectat de la server.\n");
                break;
            }
            buffer[bytes] = '\0';
            printf("%s", buffer);
        }
    }

    close(sock);
    return 0;
}