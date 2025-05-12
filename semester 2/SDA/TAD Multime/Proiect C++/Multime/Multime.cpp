#include "Multime.h"

#include <iostream>

#include "IteratorMultime.h"

int Multime::hashFunc(const TElem& elem) const {
	return abs(elem) % this->elements.capacity;
}

void Multime::nextFreePosition() {
	this->freePosition++;
	while (this->elements[this->freePosition] != NULL_TELEM)
		this->freePosition++;
}


Multime::Multime() {
	/// in the beginning they are equal
	for (int i = 0; i < this->elements.capacity; i++)
		this->elements[i] = this->nextElement[i] = NULL_TELEM;
	this->freePosition = 0;
	this->numberOfElements = 0;
}


bool Multime::adauga(TElem elem) {
	if (this->cauta(elem)) return false;
	int hash = hashFunc(elem);
	if (this->elements[hash] == NULL_TELEM) {
		this->elements[hash] = elem;
		if (this->freePosition == hash) this->nextFreePosition();
	}
	else {
		/// we place the element in the free space
		this->elements[this->freePosition] = elem;

		/// now we travel to get to the last element in the "linked list"
		while (this->nextElement[hash] != NULL_TELEM)
			hash = this->nextElement[hash];

		/// now we set the next element in the "linked list" to be the current free position
		/// where the elem we just added is
		this->nextElement[hash] = this->freePosition;

		this->nextFreePosition();
	}
	this->numberOfElements++;
	return true;
}


bool Multime::sterge(TElem elem) {
	int hash = hashFunc(elem), previousTElem = NULL_TELEM;
	while (hash != NULL_TELEM && this->elements[hash] != elem) {
		previousTElem = hash;
		hash = this->nextElement[hash];
	}
	if (hash == NULL_TELEM) return false;

	bool done = false;
	while (!done) {
		int p = this->nextElement[hash], pp = hash;
		while (p != NULL_TELEM && this->hashFunc(this->elements[p]) != hash) {
			pp = p;
			p = this->nextElement[p];
		}
		if (p == NULL_TELEM) done = true;
		else {
			this->elements[hash] = this->elements[p];
			hash = p;
			previousTElem = pp;
		}
	}
	if (previousTElem == NULL_TELEM) {
		int k = 0;
		while (k < this->elements.capacity && this->nextElement[k] != hash)
			++k;
		if (k < this->elements.capacity) previousTElem = k;
	}
	if (previousTElem != NULL_TELEM) this->nextElement[previousTElem] = this->nextElement[hash];
	this->elements[hash] = NULL_TELEM;
	this->nextElement[hash] = NULL_TELEM;
	if (hash < this->freePosition) this->freePosition = hash;
	this->numberOfElements--;
	return true;
}

bool Multime::cauta(TElem elem) const {
	int hash = hashFunc(elem);
	do {
		if (this->elements[hash] == elem) return true;
		hash = this->nextElement[hash];
	}
	while (hash != NULL_TELEM);
	return false;
}


int Multime::dim() const {
	return this->numberOfElements;
}

bool Multime::vida() const {
	return this->numberOfElements == 0;
}


Multime::~Multime() = default;



IteratorMultime Multime::iterator() const {
	return IteratorMultime(*this);
}

