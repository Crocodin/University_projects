#ifndef PRODUCT_H
#define PRODUCT_H

#include <string>

typedef unsigned int uint;
typedef std::string string;


class Product {
private:
	string name;
	string type;
	uint price;
	string producer;
public:

	/// creating the object
	/// :param string: name of the product
	/// :param string: type of the product
	/// :param uint: price of the product
	/// :param string: producer of the product
	Product(const string& , const string& , const uint&, const string&);

	/// Sets the name of the product
	/// :param string: new name of the product
	void setName(const string&);

	/// Sets the price of the product
	/// :param uint: new price of the product
	void setPrice(const uint&);

	/// Sets the type of the product
	/// :param string: new type of the product
	void setType(const string&);

	/// Sets the producer of the product
	/// :param string: new producer of the product
	void setProducer(const string&);

	/// Retrieves the name of the product
	/// :return string: the type of the product
	[[nodiscard]] string getName() const;

	/// Retrieves the price of the product
	/// :return uint: the current price of the product
	[[nodiscard]] uint getPrice() const;

	/// Retrieves the type of the product
	/// :return string: the type of the product
	[[nodiscard]] string getType() const;

	/// Retrieves the producer of the product
	/// :return string: the producer of the product
	[[nodiscard]] string getProducer() const;

	bool operator==(const Product&) const;
};

#endif //PRODUCT_H
