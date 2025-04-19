#ifndef REPO_H
#define REPO_H

#include <iostream>
#include <utility>

#include "../DynamicList/list.hpp"
#include "../Domain/product.h"

using vector = List<Product>;

class Repository {
private:

	vector products;
protected:

	/// add to the repo even if it's not valid
	/// :return: NULL
	void forceAdd(const Product& p) noexcept;
public:
	Repository() = default;

	virtual ~Repository() = default;

	/// adds a product to the vector
	/// :param Product: a product
	/// :return: NULL
	/// @:exceptions: InvalidArgument if the product is already added
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
	/// @:exception: LogicError if there is no product in the list
	[[nodiscard]] uint getIndex(const Product&) const;

	/// returns the actual product in the vector
	/// :param string: the name of the product
	/// :param string: the producer of the product
	/// :return Product: the product
	/// @:exception: LogicError if there is no such product in the list
	[[nodiscard]] Product& find(const string&, const string&) const;

	/// gets the size of the repo
	/// :return uint:
	[[nodiscard]] uint size() const noexcept;

	/// get all the elements from the vector
	/// :return: vector of elems
	[[nodiscard]] vector& getAllProducts() noexcept;
};

class RepositoryFile final : public Repository {
private:
	const string fileName;

public:
	explicit RepositoryFile(string  file) : fileName(std::move(file)) {
		this->lodeFromFile();
	}

	void lodeFromFile();

	void lodeToFile();

	~RepositoryFile() override { this->lodeToFile(); };
};

#endif //REPO_H
