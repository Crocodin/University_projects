#ifndef VALIDATOR_H
#define VALIDATOR_H
#include <string>

typedef unsigned int uint;
typedef std::string string;

class Validator {
public:
	/// creates the validator
	/// :return: NULL
	Validator() = default;

	/// Validates the name of the product
	/// Ensures the product name adheres to any required format or constraints
	/// :return bool: true if the name is valid, false otherwise
	[[nodiscard]] bool valid_name(const string&) const;

	/// Validates the price of the product
	/// Ensures the price is within a valid range or meets any other specific requirements
	/// :return bool: true if the price is valid, false otherwise
	[[nodiscard]] bool valid_price(const int&) const;

	/// Validates the type of the product
	/// Ensures the price is within a valid range or meets any other specific requirements
	/// :return bool: true if the price is valid, false otherwise
	[[nodiscard]] bool valid_type(const string&) const;

	/// Validates the producer of the product
	/// Ensures the price is within a valid range or meets any other specific requirements
	/// :return bool: true if the price is valid, false otherwise
	[[nodiscard]] bool valid_producer(const string&) const;

	/// Validates the entire producer
	/// Ensures that all attributes of the product (name, price, etc.) are valid
	/// :return bool: true if the entire product is valid, false otherwise
	[[nodiscard]] bool validate(const string& , const string& , const uint&, const string&) const;

	~Validator() = default;
};

#endif //VALIDATOR_H
