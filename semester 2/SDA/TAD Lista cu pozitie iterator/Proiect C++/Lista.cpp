
#include <exception>

#include "IteratorLP.h"
#include "Lista.h"

#include <iostream>

/// theta (2n + 2n + n) = theta(n) { n - capacity, m - size }
void Lista::resize() {
	this->position.resize();
	this->elements.resize();

	/// set the free positions
	for (int i = this->position.capacity / 2; i < this->position.capacity; i++)
		this->position[i] = i + 1;
	this->position[this->position.capacity - 1] = -1;
	this->position[this->firstFreePosition] = this->position.capacity / 2;
}

/// theta (n) { n - capacity, m - size }
Lista::Lista() {
	for (int i = 0; i < this->position.capacity; i++)
		this->position[i] = i + 1; /// set the free spaces
	this->position[this->position.capacity - 1] = -1;
	this->firstFreePosition = 0;
	this->firstElement = -1;
}

/// theta (m) { n - capacity, m - size }
int Lista::dim() const {
	int count = 0;
	int temp = this->firstElement;
	while (temp != -1) {
		count++;
		temp = this->position[temp];
	}
	return count;
}

/// theta (1)
bool Lista::vida() const {
	return this->firstElement == -1;
}

/// theta (1)
IteratorLP Lista::prim() const {
	IteratorLP it(*this);
	it.position = this-> firstElement;
    return it;
}

/// theta (1)
TElem Lista::element(IteratorLP poz) const {
	if (!poz.valid()) throw std::invalid_argument("poz is invalid");
	return this->elements[poz.position];
}

/// O(n) { n - capacity, m - size }
TElem Lista::sterge(IteratorLP& poz) {
	if (!poz.valid()) throw std::invalid_argument("Lista::sterge, invalid iterator");

	/// deliting the first element
	if (poz.position == this->firstElement) {
		int temp = this->firstFreePosition;
		while (this->position[temp] != -1)
			temp = this->position[temp];
		/// now we are one the last free space
		this->position[temp] = poz.position;
		this->firstElement = this->position[poz.position];
		temp = poz.position;
		return this->elements[temp];
	}

	int temp_free = this->firstFreePosition;
	int temp_elem = this->firstElement;
	/// here we need the last free space
	while (this->position[temp_free] != -1)
		temp_free = this->position[temp_free];
	/// here the element before the one we are removing
	while (this->position[temp_elem] != poz.position)
		temp_elem = this->position[temp_elem];

	this->position[temp_free] = poz.position;
	this->position[temp_elem] = this->position[poz.position];
	this->position[poz.position] = -1;

	return this->elements[poz.position];
}

/// O(m) { n - capacity, m - size }
IteratorLP Lista::cauta(TElem e) const{
	int temp = this->firstElement;

	while (this->elements[temp] != e and temp != -1) {
		temp = this->position[temp];
	}

	IteratorLP it(*this);
	it.position = temp;
	return it;
}

/// theta (1)
TElem Lista::modifica(IteratorLP poz, TElem e) {
	if (!poz.valid()) throw std::invalid_argument("Lista::modifica, invalid iterator");
	TElem aux = this->elements[poz.position];
	this->elements[poz.position] = e;
	return aux;
}

/// theta (1)
void Lista::adauga(IteratorLP& poz, TElem e) {
	if (!poz.valid()) throw std::invalid_argument("invalid iterator");

	/// if it's not the first
	this->elements[this->firstFreePosition] = e;
	const int temp = this->position[poz.position], aux = this->position[this->firstFreePosition];
	this->position[poz.position] = this->firstFreePosition;
	this->position[this->firstFreePosition] = temp;
	poz.position = this->firstFreePosition;
	this->firstFreePosition = aux;
}

/// theta (1)
void Lista::adaugaInceput(TElem e) {
	if (this->position[this->firstFreePosition] == -1) this->resize();

	this->elements[this->firstFreePosition] = e;

	/// sets the first element
	if (this->firstElement == -1) {
		const int aux = this->position[this->firstFreePosition];
		this->position[this->firstFreePosition] = -1;
		this->firstElement = this->firstFreePosition;
		this->firstFreePosition = aux;
		return;
	}

	const int aux = this->position[this->firstFreePosition];
	this->position[this->firstFreePosition] = this->firstElement;
	this->firstElement = this->firstFreePosition;
	this->firstFreePosition = aux;
}

/// O(m) { n - capacity, m - size }
void Lista::adaugaSfarsit(TElem e) {
	if (this->position[this->firstFreePosition] == -1) this->resize();

	this->elements[this->firstFreePosition] = e;

	/// sets the first element
	if (this->firstElement == -1) {
		const int aux = this->position[this->firstFreePosition];
		this->position[this->firstFreePosition] = -1;
		this->firstElement = this->firstFreePosition;
		this->firstFreePosition = aux;
		return;
	}

	/// going to the last element
	int temp = this->firstElement;
	while (this->position[temp] != -1)
		temp = this->position[temp];

	this->position[temp] = this->firstFreePosition;
	this->firstFreePosition = this->position[this->firstFreePosition];
	this->position[this->position[temp]] = -1;
}

Lista::~Lista() = default;

/// theta (m)
IteratorLP Lista::ultimulIndex(TElem elem) const {
	int temp = this->firstElement, aux = -1;
	while (temp != -1) {
		if (this->elements[temp] == elem) aux = temp;
		temp = this->position[temp];
	}
	IteratorLP it(*this);
	it.position = aux;
	return it;
}

