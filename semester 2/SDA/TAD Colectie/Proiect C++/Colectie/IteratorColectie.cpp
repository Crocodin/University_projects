#include "IteratorColectie.h"

#include <stdexcept>

#include "Colectie.h"


IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	this->position = 0;
}

void IteratorColectie::prim() {
	this->position = 0;
}


void IteratorColectie::urmator() {
	if (!this->valid()) throw std::runtime_error("Invalid call of IteratorColectie::urmator()");
	this->position++;
}


bool IteratorColectie::valid() const {
	if (this->position < this->col.position_current_size or this->position < 0)
		return true;
	return false;
}



TElem IteratorColectie::element() const {
	if (!this->valid()) throw std::out_of_range("Invalid iterator, out of range");
	return this->col.distinct[this->col.position[this->position]];
}
