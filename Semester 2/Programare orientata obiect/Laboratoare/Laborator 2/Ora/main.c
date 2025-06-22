#include <stdio.h>

int main() {
	double sum = 0, val;
	int n;
	printf("Introduceti nr de elemente: ");
	scanf_s("%d", &n);
	if (n < 0) {
		printf("Numarul de elemente trebuie sa fie >= 0");
		return 1;
	}
	for (int i = 1; i <= n; i++) {
		printf("Introduceti numarul #%d: ", i);
		scanf_s("%lf", &val);
		sum += val;
	}
	printf("Suma este: %.2lf", sum);
	return 0;
}