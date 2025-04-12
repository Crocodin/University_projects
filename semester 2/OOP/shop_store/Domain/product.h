#ifndef PRODUCT_H
#define PRODUCT_H

#include <string>

using uint = unsigned int;
using string = std::string;


class Product {
private:
	string name;
	string type;
	uint price {};
	string producer;
public:

	/// creating the object
	/// :param string: name of the product
	/// :param string: type of the product
	/// :param uint: price of the product
	/// :param string: producer of the product
	Product(string, string,const uint&, string);
	Product() = default;

	/// Sets the name of the product
	/// :param string: new name of the product
	void setName(const string&) noexcept;

	/// Sets the price of the product
	/// :param uint: new price of the product
	void setPrice(const uint&) noexcept;

	/// Sets the type of the product
	/// :param string: new type of the product
	void setType(const string&) noexcept;

	/// Sets the producer of the product
	/// :param string: new producer of the product
	void setProducer(const string&) noexcept;

	/// Retrieves the name of the product
	/// :return string: the type of the product
	[[nodiscard]] string getName() const noexcept;

	/// Retrieves the price of the product
	/// :return uint: the current price of the product
	[[nodiscard]] uint getPrice() const noexcept;

	/// Retrieves the type of the product
	/// :return string: the type of the product
	[[nodiscard]] string getType() const noexcept;

	/// Retrieves the producer of the product
	/// :return string: the producer of the product
	[[nodiscard]] string getProducer() const noexcept;

	/// compares if two products are equal
	/// :param Product: another product to compare against
	/// :return: true if both products are the same, false otherwise
	bool operator==(const Product&) const noexcept;

	/// makes a copy, you idiot!
	Product(const Product&);
	Product& operator=(const Product&) = default;

	/// compares the price of two products
	/// :param Product: another product to compare against
	/// :return: true if the first product's price is less than or equal to the second product's price
	static bool priceComparison(const Product&, const Product&) noexcept;

	/// compares the price of a product with a pointer to a price value
	/// :param Product: product to compare
	/// :param void*: a pointer to the price value to compare against
	/// :return: true if the product's price matches the value pointed to by the price pointer
	static bool priceComparison(const Product&, void*) noexcept;

	/// compares the names of two products
	/// :param Product: another product to compare against
	/// :return: true if the products have the same name
	static bool nameComparison(const Product&, const Product&) noexcept;

	/// compares the name of a product with a pointer to a name string
	/// :param Product: product to compare
	/// :param void*: a pointer to the name string to compare against
	/// :return: true if the product's name matches the string pointed to by the pointer
	static bool nameComparison(const Product&, void*) noexcept;

	/// compares the type of two products
	/// :param Product: another product to compare against
	/// :return: true if both products have the same type
	static bool typeComparison(const Product&, const Product&) noexcept;

	/// compares the producer of a product with a pointer to a producer name
	/// :param Product: product to compare
	/// :param void*: a pointer to the producer name string to compare against
	/// :return: true if the product's producer matches the string pointed to by the pointer
	static bool producerComparison(const Product&, void*) noexcept;
};

#endif //PRODUCT_H
