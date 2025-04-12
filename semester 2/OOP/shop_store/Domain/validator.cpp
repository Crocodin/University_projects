#include "validator.h"

bool Validator::valid_name(const string& name) noexcept {
	return !name.empty();
}

bool Validator::valid_price(const int& price) noexcept {
	return price > 0;
}

bool Validator::valid_type(const string& type) noexcept {
	return !type.empty();
}

bool Validator::valid_producer(const string& producer) noexcept {
	return !producer.empty();
}

bool Validator::validate(const string& name, const string& type, const int& price, const string& producer) const noexcept {
	return valid_name(name) && valid_price(price) && valid_type(type) && valid_producer(producer);
}
