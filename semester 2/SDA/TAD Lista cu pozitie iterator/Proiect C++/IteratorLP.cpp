#include "IteratorLP.h"
#include "Lista.h"
#include <exception>
#include <stdexcept>

IteratorLP::IteratorLP(const Lista& lista) : lista(lista) {

}

/// theta (1)
void IteratorLP::prim() {
	this->position = this->lista.firstElement;
}

/// theta (1)
void IteratorLP::urmator(){
	if (!this->valid()) throw std::invalid_argument("IteratorLP::valid");
	this->position = this->lista.position[this->position];
}

/// theta (1)
bool IteratorLP::valid() const {
	return this->position != -1 and this->lista.firstElement != -1;
}

/// theta (1)
TElem IteratorLP::element() const {
	if (!this->valid()) throw std::invalid_argument("IteratorLP::element");
	return this->lista.elements[this->position];
}


