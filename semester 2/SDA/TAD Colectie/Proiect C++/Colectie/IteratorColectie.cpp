#include "IteratorColectie.h"

#include <stdexcept>

#include "Colectie.h"

// BC = WC = theta(1)
IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	this->position = 0;
}

// BC = WC = theta(1)
void IteratorColectie::prim() {
	this->position = 0;
}

// BC = WC = theta(1)
void IteratorColectie::urmator() {
	if (!this->valid()) throw std::runtime_error("Invalid call of IteratorColectie::urmator()");
	this->position++;
}

// BC = WC = theta(1)
bool IteratorColectie::valid() const {
	if (this->position < this->col.position_current_size or this->position < 0)
		return true;
	return false;
}


// BC = WC = theta(1)
TElem IteratorColectie::element() const {
	if (!this->valid()) throw std::out_of_range("Invalid iterator, out of range");
	return this->col.distinct[this->col.position[this->position]];
}
