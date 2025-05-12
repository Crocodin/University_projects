#include "IteratorMultime.h"

#include <stdexcept>

#include "Multime.h"


IteratorMultime::IteratorMultime(const Multime& m) : multime(m){
	this->currentElementIndex = NULL_TELEM;
	this->prim();
}


void IteratorMultime::prim() {
	for (int i = 0; i < this->multime.elements.capacity; ++ i)
		if (this->multime.elements[i] != NULL_TELEM) {
			this->currentElementIndex = i;
			return;
		}
	this->currentElementIndex = NULL_TELEM;
}


void IteratorMultime::urmator() {
	if (this->currentElementIndex == NULL_TELEM) throw std::out_of_range("IteratorMultime is empty");
	for (int i = this->currentElementIndex + 1; i < this->multime.elements.capacity; ++ i)
		if (this->multime.elements[i] != NULL_TELEM) {
			this->currentElementIndex = i;
			return;
		}
	this->currentElementIndex = NULL_TELEM;
}


TElem IteratorMultime::element() const {
	if (this->currentElementIndex == NULL_TELEM) throw std::out_of_range("IteratorMultime is empty");
	return this->multime.elements[this->currentElementIndex];
}

bool IteratorMultime::valid() const {
	return this->currentElementIndex != NULL_TELEM;
}
