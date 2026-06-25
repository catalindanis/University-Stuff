#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <io.h>
#include <unistd.h>
#include <direct.h> // for _getcwd

struct data {
    int nr;
    int tries;
};

void getTmpFilePath(int id, char *filename, size_t size) {
    char cwd[1024];
    _getcwd(cwd, sizeof(cwd));

    // Ensure tmp folder exists
    char tmpDir[1024];
    sprintf(tmpDir, "%s/tmp", cwd);
    struct stat st = {0};
    if (stat(tmpDir, &st) == -1) {
        mkdir(tmpDir);
    }

    snprintf(filename, size, "%s/tmp/%d.txt", cwd, id);
}

int getIdFromCookie() {
    char *cookies = getenv("HTTP_COOKIE");
    if (!cookies) return -1;

    char *pos = strstr(cookies, "session_id=");
    if (!pos) return -1;

    int id;
    if (sscanf(pos, "session_id=%d", &id) != 1) return -1;
    return id;
}

int getNumberFromQueryString() {
    char *qs = getenv("QUERY_STRING");
    if (!qs) return -1;

    int nr;
    if (sscanf(qs, "nr=%d", &nr) != 1) return -1;
    return nr;
}

int init() {
    int r, id;
    int code;
    char filename[1024];
    struct data d;

    srand((unsigned int)getpid());
    r = rand() % 100;

    int attempts = 0;
    do {
        id = rand();
        getTmpFilePath(id, filename, sizeof(filename));
        code = _open(filename, _O_CREAT | _O_EXCL | _O_WRONLY | _O_BINARY, _S_IREAD | _S_IWRITE);
        attempts++;
        if (attempts > 20) {
            fprintf(stderr, "Failed to create tmp file after 20 attempts\n");
            exit(1);
        }
    } while (code < 0);

    d.nr = r;
    d.tries = 0;
    _write(code, &d, sizeof(d));
    _close(code);

    return id;
}

void destroy(int id) {
    char filename[1024];
    getTmpFilePath(id, filename, sizeof(filename));
    remove(filename);
}

int getNumberFromFile(int id) {
    char filename[1024];
    getTmpFilePath(id, filename, sizeof(filename));

    int fd = _open(filename, _O_RDWR | _O_BINARY);
    if (fd < 0) return -1;

    struct data d;
    _read(fd, &d, sizeof(d));
    d.tries++;
    _lseek(fd, 0, SEEK_SET);
    _write(fd, &d, sizeof(d));
    _close(fd);
    return d.nr;
}

int getNoOfTries(int id) {
    char filename[1024];
    getTmpFilePath(id, filename, sizeof(filename));

    int fd = _open(filename, _O_RDONLY | _O_BINARY);
    if (fd < 0) return -1;

    struct data d;
    _read(fd, &d, sizeof(d));
    _close(fd);
    return d.tries;
}

int isNewUser() {
    int id = getIdFromCookie();
    if (id < 0) return 1;

    char filename[1024];
    getTmpFilePath(id, filename, sizeof(filename));

    return access(filename, F_OK) != 0;
}

void printForm() {
    printf("<form action='play.cgi' method='get'>\n");
    printf("Nr: <input type='text' name='nr'><br>\n");
    printf("<input type='submit' value='Trimite'>\n");
    printf("</form>");
}

int main() {
    setbuf(stdout, NULL); 
    int id, status;

    printf("Content-type: text/html\r\n");

    if (isNewUser()) {
        id = init();
        printf("Set-Cookie: session_id=%d; Path=/\r\n\r\n", id);
        status = 0;
    } else {
        printf("\r\n");
        id = getIdFromCookie();
        int nr = getNumberFromQueryString();
        int nr2 = getNumberFromFile(id);

        if (nr2 == -1) status = 1;
        else if (nr == nr2) status = 2;
        else if (nr < nr2) status = 3;
        else status = 4;
    }

    printf("<html>\n<body>\n");
    switch (status) {
        case 0: printf("Ati inceput un joc nou.<br>\n"); printForm(); break;
        case 1: printf("Eroare. Click <a href='play.cgi'>here</a> for a new game!"); break;
        case 2:
            printf("Ati ghicit din %d incercari. Click <a href='play.cgi'>here</a> for a new game!", getNoOfTries(id));
            destroy(id);
            break;
        case 3: printf("Prea mic!<br>\n"); printForm(); break;
        case 4: printf("Prea mare!<br>\n"); printForm(); break;
    }
    printf("</body>\n</html>");
    return 0;
}