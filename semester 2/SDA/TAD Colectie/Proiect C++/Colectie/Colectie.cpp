#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;

// BC = WC = theta(1)
Colectie::Colectie() {
	this->distinct = new TElem[this->distinct_max_size];
	this->position = new int[this->position_max_size];
}

// BC = theta(1), WC = n*2 + m*2 + n = theta (m), total = O(m)
void Colectie::adauga(TElem elem) {
	if (this->distinct_current_size == this->distinct_max_size) {
		// reallocation for double size
		this->distinct_max_size = this->distinct_max_size << 1;
		TElem* temp = new TElem[this->distinct_max_size];

		for (int i = 0; i < this->distinct_current_size; i++)
			temp[i] = this->distinct[i];

		delete [] this->distinct;
		this->distinct = temp;
	}

	if (this->position_current_size == this->position_max_size) {
		// reallocation for double size
		this->position_max_size = this->position_max_size << 1;
		int* temp = new int[this->position_max_size];

		for (int i = 0; i < this->position_current_size; i++)
			temp[i] = this->position[i];

		delete [] this->position;
		this->position = temp;
	}

	int index_add = -1;
	for (int i = 0; i < this->distinct_current_size; ++ i)
		if (this->distinct[i] == elem)
			index_add = i; // the value to witch the position corresponds
	if (index_add == -1) {
		this->position[this->position_current_size++] = this->distinct_current_size;
		this->distinct[this->distinct_current_size++] = elem;
	}
	else this->position[this->position_current_size++] = index_add;
}


// BC = theta(1) WC = n + max{m, n} + m = theta (m), total = O(m)
bool Colectie::sterge(TElem elem) {
	int index_TElem = -1;
	for (int i = 0; i < this->distinct_current_size and index_TElem == -1; i++)
		if (this->distinct[i] == elem)
			index_TElem = i;
	if (index_TElem == -1) return false;

	bool eliminated = false, there_are_more = false;
	for (int i = this->position_current_size - 1; i > -1 and !there_are_more; -- i)
		if (this->position[i] == index_TElem) {
			// now we shift all the elements down
			if (!eliminated) {
				for (int j = i; j < this->position_current_size - 1; j++)
					this->position[j] = this->position[j + 1];

				this->position_current_size --;
				eliminated = true;
			}
			else there_are_more = true;
		}

	if (!eliminated) return false;
	if (there_are_more) return true;
	// deleting the TElem from distinct
	int aux = index_TElem;
	while (index_TElem < this->distinct_current_size - 1) {
		this->distinct[index_TElem] = this->distinct[index_TElem + 1];
		index_TElem++;
	}
	this->distinct_current_size--;

	for (int i = 0; i < this->position_current_size; i++)
		if (this->position[i] > aux)
			this->position[i]--;
	return true;
}

// BC = theta(1), WC = theta(n), total = O(n)
bool Colectie::cauta(TElem elem) const {

	for (int i = 0; i < this->distinct_current_size; i++)
		if (this->distinct[i] == elem)
			return true;

	return false;
}

// BC = theta(m) WC = n + m = theta (m), total = O(m)
int Colectie::nrAparitii(TElem elem) const {

	int index_TElem = -1;
	for (int i = 0; i < this->distinct_current_size and index_TElem == -1; i++)
		if (this->distinct[i] == elem)
			index_TElem = i; // this is hes position
	if (index_TElem == -1) return 0;

	int cnt = 0;
	for (int i = 0; i < this->position_current_size; i++)
		if (this->position[i] == index_TElem)
			cnt++;

	return cnt;
}


// WC = BC = theta(1)
int Colectie::dim() const {
	return this->position_current_size;
}

// WC = BC = theta(1)
bool Colectie::vida() const {
	return this->distinct_current_size == 0;
}

// WC = BC = theta(1)
IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}

/*
 * adaugaAparitiiMultiple(c, nr, elem)
 *  { pre: c este o colectie }
 *	daca nr < 0 atunci
 *		@ exceptie aruncata
 *	sf.daca
 *	pentru i = 0, nr, 1 executa
 *		adauga(c, elem)
 *	sf.pentru
*/
// BC = theta(1) WC = theta (max{m}) * nr = theta (m * nr)
void Colectie::adaugaAparitiiMultiple(int nr, TElem elem) {
	if (nr < 0) throw std::out_of_range("Negative number of arguments need ,to be added");
	for (int i = 0; i < nr; i++) {
		this->adauga(elem);
	}
}

// BC = WC = theta(1)
Colectie::~Colectie() {
	delete[] this->distinct;
	delete[] this->position;
}


