#include <stdio.h>

/*
3. Determina toate reprezentarile posibile a unui numar natural ca suma
   de numere naturale consecutive.
*/

int citesteNumarNatural(int* n) {
	/*
	Functia citeste un string de la tastatura si il stocheaza
	in adresa variabilei transmise ca parametru daca este numar natural
	:param n: adresa variabilei in care se va stoca valoarea citita
	:param type n: pointer
	:return : 1 (citirea s-a efectuat cu succes) / 0 (in caz contrar iar n-ul ramane nemodificat)
	:return type: int
	*/
	char input[100] = "";
	printf("Introduceti o valoare: ");
	scanf_s("%s", input, sizeof(input));
	for (int i = 0; i < strlen(input); i++)
		if (!isdigit(input[i]))
			return 0;
	*n = 0;
	for (int i = 0; i < strlen(input); i++)
		*n = (*n) * 10 + input[i] - '0';
	return 1;
}

void afiseazaValoriIntregiIntreIndici(int st, int dr) {
	/*
	Functia afiseaza toate numerele intregi situate in intervalul
	[st, dr] (pre-conditii: st <= dr)
	:param st: capatul stang al intervalului
	:param st type: int
	:param dr: capatul drept al intervalului
	:param dr type: int
	*/
	for (int i = st; i <= dr; i++)
		printf("%d ", i);
}

void determinaReprezentariPosibile(int n) {
	/*
	Functia determina toate reprezentarile posibile de a-l scrie
	pe n ca suma de numere naturale consecutive
	:param n: numarul pentru care se determina reprezentarile
	:param type n: int (>= 0)
	*/
	printf("Reprezentarile numarului n=%d ca suma de numere naturale consecutive\n", n);
	int sumaCurenta = 0;
	for (int i = 0; i <= n / 2; i++) {
		sumaCurenta = i;
		int j = i;
		if (j < n) {
			for (j = i + 1; j <= n; j++) {
				sumaCurenta += j;
				if (sumaCurenta >= n)
					break;
			}
			if (sumaCurenta == n) {
				afiseazaValoriIntregiIntreIndici(i, j);
				printf("\n");
			}
		}
	}
	afiseazaValoriIntregiIntreIndici(n, n);
}

int main() {
	/*
	Functia principala a programului, care dirijeaza executia acestuia
	:return: 0 daca programul a rulat corect / alta valoare in caz contrar
	:return type: int
	*/
	int n;
	while (1) {
		if (citesteNumarNatural(&n))
			break;
		else
			printf("Introduceti un numar natural!\n");
	}
	determinaReprezentariPosibile(n);
	return 0;
}
