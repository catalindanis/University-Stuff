#include <stdio.h>

/*
13. Descompune in factori primi un numar natural nenul dat.
*/

int citireNumarNaturalNenul(int* n) {
	/*
	Functia citeste un string de la tastatura si il stocheaza
	in adresa variabilei transmise ca parametru daca este numar natural nenul
	:param n: adresa variabilei in care se va stoca valoarea citita
	:param type n: pointer
	:return : 1 (citirea s-a efectuat cu succes) / 0 (in caz contrar iar n-ul ramane nemodificat)
	:return type: int
	*/
	char input[100] = "";
	printf("Introduceti un numar natural nenul: ");
	scanf_s("%s", input, sizeof(input));
	for (int i = 0; i < strlen(input); i++)
		if (!isdigit(input[i]))
			return 0;
	*n = 0;
	for (int i = 0; i < strlen(input); i++)
		*n = (*n) * 10 + input[i] - '0';
	if (*n == 0)
		return 0;
	return 1;
}

void descompunereInFactoriPrimi(int n) {
	/*
	Functia afiseaza descompunerea in factori primi
	a unui numar natural nenul transmis ca parametru
	:param n: numarul natural nenul pentru care se calculeaza
	:param type: int
	*/
	if (n == 1) {
		printf("Numarul nu poate fi descompus in factori primi!");
		return;
	}
	printf("Descompunerea in factori primi pentru n=%d\n", n);
	int d = 2;
	while (n > 1) {
		int c = 0;
		while (n % d == 0)
			n /= d, c++;
		if (c != 0)
			printf("%d apare la puterea: %d\n", d, c);
		d++;
	}
}

int main() {
	/*
	Functia principala a programului care dirijeaza
	executia acestuia
	:return: 0 daca programul s-a efectuat cu succes /
			 alta valoare in caz contrar
	:return type: int
	*/
	int n;
	while (1) {
		if (citireNumarNaturalNenul(&n) == 0)
			printf("Introduceti un numar natural nenul!\n");
		else
			break;
	}
	descompunereInFactoriPrimi(n);
	return 0;
}