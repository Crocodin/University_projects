#include "product.h"

#include <utility>

Product::Product(string name, string type, const uint& price, string producer)
: name(std::move(name)), type(std::move(type)), price(price), producer(std::move(producer)) {
}

void Product::setName(const string& newName) {
	this->name = newName;
}

void Product::setPrice(const uint& newPrice) {
	this->price = newPrice;
}

void Product::setType(const string& newType) {
	this->type = newType;
}

void Product::setProducer(const string& newProducer) {
	this->producer = newProducer;
}

string Product::getName() const {
	return this->name;
}

uint Product::getPrice() const {
	return this->price;
}

string Product::getType() const {
	return this->type;
}

string Product::getProducer() const {
	return this->producer;
}

bool Product::operator==(const Product& other) const {
	return this->name == other.name &&
			this->type == other.type &&
			this->price == other.price &&
			this->producer == other.producer;
}

bool Product::priceComparison(const Product& p1, const Product& p2) noexcept {
	return p1.getPrice() < p2.getPrice();
}

bool Product::nameComparison(const Product& p1, const Product& p2) noexcept {
	return p1.getName() < p2.getName();
}

bool Product::typeComparison(const Product& p1, const Product& p2) noexcept {
	if ( p1.getType() == p2.getType() ) return p1.getName() < p2.getName();
	else return p1.getType() < p2.getType();
}

bool Product::priceComparison(const Product &p, void* n) noexcept {
	return p.getPrice() == *(static_cast<uint*>(n));
}

bool Product::nameComparison(const Product& p, void* name) noexcept {
	return p.getName() == *(static_cast<string*>(name));
}

bool Product::producerComparison(const Product& p, void* prod) noexcept {
	return p.getProducer() == *(static_cast<string*>(prod));
}

Product::Product(const Product& other) {
	this->name = other.name;
	this->price = other.price;
	this->producer = other.producer;
	this->type = other.type;
}
