#include "validator.h"

bool Validator::valid_name(const string& name) const {
	if (!name.empty()) return true;
	return false;
}

bool Validator::valid_price(const int& price) const {
	if (price < 0) return false;
	return true;
}

bool Validator::valid_type(const string& type) const {
	if (!type.empty()) return true;
	return false;
}

bool Validator::valid_producer(const string& producer) const {
	if (!producer.empty()) return true;
	return false;
}

bool Validator::validate(const string& name, const string& type, const uint& price, const string& producer) const {
	return this->valid_name(name) && this->valid_price(price) && this->valid_type(type) && this->valid_producer(producer);

}
