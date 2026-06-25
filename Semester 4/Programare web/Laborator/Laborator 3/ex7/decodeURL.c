#include <stdio.h>
#include <string.h>
 
int hexatoint(char c) {
  if ((c>='a') && (c<='f'))
    return c - 'a' + 10;
  if ((c>='A') && (c<='F'))
    return c - 'A' + 10;
  return c - '0';
}
 
void decode(char *s) {
  int i = 0, j;
  while (s[i] != 0) {
    if (s[i] == '+')
      s[i] = ' ';
    if (s[i] == '%') {
      char c = 16 * hexatoint(s[i+1]) + hexatoint(s[i+2]);
      s[i] = c;
      j = i + 1;
      do {
        s[j] = s[j + 2];
        j++;
      } while (s[j] != 0);
    }
    i++;
  }  
}
 
int main() {
  char line[1000];
  printf("Content-type: text/html\n\n");

  fgets(line, 1000, stdin);

  line[strcspn(line, "\n")] = 0;

  char *value = strstr(line, "url=");

  if (value != NULL) {
    value += 4;
    
    decode(value);

    printf("Original value: %s<br>\n", value);
    decode(value);
    printf("Decoded value: %s", value);
  } else {
    printf("No 'url' parameter found");
  }

  return 0;
}