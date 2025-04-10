#include "repo.h"

void Repository::add(const Product& p) {
	if (this->isIn(p)) throw std::invalid_argument("Product already exists");
	this->products.push_back(p);
}

uint Repository::getIndex(const Product& p) const {
	int index = 0;
	for (const Product *it = this->products.begin(); it < this->products.end() + 1; ++it) {
		if (*it == p) return index;
		index++;
	}
	throw std::logic_error("No such product");
}

void Repository::remove(const Product & p) {
	this->products.erase(this->products.begin() + this->getIndex(p));
}

bool Repository::isIn(const Product& p) const noexcept {
	for (const Product *it = this->products.begin(); it < this->products.end() + 1; ++it) {
		if (*it == p) return true;
	}
	return false;
}
Product& Repository::find(const string& name, const string& producer) const {
	for (Product *it = this->products.begin(); it < this->products.end() + 1; ++it) {
		if (it->getName() == name && it->getProducer() == producer) return *it;
	}
	throw std::logic_error("No such product");
}

uint Repository::size() const noexcept {
	return this->products.size();
}

vector& Repository::getAllProducts() noexcept {
	return this->products;
}