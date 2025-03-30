#ifndef REPO_H
#define REPO_H

#include <vector>
#include <exception>
#include <stdexcept>
#include "../Domain/product.h"

typedef std::vector<Product> vector;

class Repository {
private:
	vector products;
public:
	Repository() = default;

	/// adds a product to the vector
	/// :param Product: a product
	/// :return: NULL
	/// @:exceptions: invalid_argument if the product is already added
	void add(const Product&);

	/// removes a product
	/// :param Product: a product
	/// :return: NULL
	/// @:exception: might throw the exception from getIndex
	void remove(const Product&);

	/// test to see if a product is in the vector
	/// :param Product: a product
	/// :return bool: true if the product is in, false otherwise
	[[nodiscard]] bool isIn(const Product &p) const noexcept;

	/// find the index in the vector of a product
	/// :param Product: a product
	/// :return uint: the position
	/// @:exception: logic_error if there is no product in the list
	[[nodiscard]] uint getIndex(const Product&) const;

	/// returns the actual product in the vector
	/// :param string: the name of the product
	/// :param string: the producer of the product
	/// :return Product: the product
	/// @:exception: logic_error if there is no such product in the list
	[[nodiscard]] Product& find(const string&, const string&);

	/// gets the size of the repo
	/// :return uint:
	[[nodiscard]] uint size() const noexcept;

	/// get all the elements from the vector
	/// :return: vector of elems
	[[nodiscard]] vector getAllProducts() const noexcept;
};

#endif //REPO_H
