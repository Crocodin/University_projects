/*
 Se dau un sir care contine n reprezentari binare pe 8 biti date ca sir de caractere.
 Sa se obtina un sir care contine numerele corespunzatoare reprezentarilor binare.
 Exemplu:
 Se da : '10100111b', '01100011b', '110b', '101011b'
 Se stocheaza : 10100111b, 01100011b, 110b, 101011b
*/


#include <stdio.h>

int asmTrans(char sir[]);

int main() 
{
	char sir[100][16];
	int numere;
	int rezultat[100];

	scanf("%d", &numere); // cate numere "binare" vor fi in sir

	for (int i = 0; i < numere; ++i) // citeste tot sirul
		scanf("%s", &sir[i]); // citeste un cuvant


	for (int i = 0; i < numere; ++i)
		rezultat[i] = asmTrans(sir[i]); // adauga in sir numarul

	for (int i = 0; i < numere; ++i)
		printf("%d ", rezultat[i]); // afiseaza numerele, ele in memorie sunt puse conform cerintei

	scanf("%d", &numere); // nu lasa programul sa se inchida


	return 0;
}