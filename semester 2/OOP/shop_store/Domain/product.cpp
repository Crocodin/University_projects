#include "product.h"

Product::Product(const string&  name, const string&  type, const uint& price, const string&  producer)
: name(name), type(type), price(price), producer(producer) {
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

uint Product::getPrice() const {
	return this->price;
}

string Product::getType() const {
	return this->type;
}

string Product::getProducer() const {
	return this->producer;
}
