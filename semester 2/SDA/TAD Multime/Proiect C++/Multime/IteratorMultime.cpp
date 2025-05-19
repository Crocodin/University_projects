#include "IteratorMultime.h"

#include <stdexcept>

#include "Multime.h"

/// theta(1)
IteratorMultime::IteratorMultime(const Multime& m) : multime(m){
	this->currentElementIndex = NULL_TELEM;
	this->prim();
}

/// O(m) { m - capacity }
void IteratorMultime::prim() {
	for (int i = 0; i < this->multime.elements.capacity; ++ i)
		if (this->multime.elements[i] != NULL_TELEM) {
			this->currentElementIndex = i;
			return;
		}
	this->currentElementIndex = NULL_TELEM;
}

/// O(m) { m - capacity }
void IteratorMultime::urmator() {
	if (!this->valid()) throw std::out_of_range("IteratorMultime is empty");
	for (int i = this->currentElementIndex + 1; i < this->multime.elements.capacity; ++ i)
		if (this->multime.elements[i] != NULL_TELEM) {
			this->currentElementIndex = i;
			return;
		}
	this->currentElementIndex = NULL_TELEM;
}

/// theta(1)
TElem IteratorMultime::element() const {
	if (!this->valid()) throw std::out_of_range("IteratorMultime is empty");
	return this->multime.elements[this->currentElementIndex];
}

/// theta(1)
bool IteratorMultime::valid() const {
	return this->currentElementIndex != NULL_TELEM;
}

/// O(m) { m - nr de elem }
void IteratorMultime::anterior() {
	if (!this->valid()) throw std::out_of_range("IteratorMultime is invalid");
	for (int i = this->currentElementIndex - 1; i >= 0; -- i)
		if (this->multime.elements[i] != NULL_TELEM) {
			this->currentElementIndex = i;
			return;
		}
	this->currentElementIndex = NULL_TELEM;
}
