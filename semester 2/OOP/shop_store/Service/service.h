#ifndef SERVICE_H
#define SERVICE_H
#include "../Repository/repo.h"

class Controller;

class Service {
private:
	Repository repo;
	friend class Controller;
public:

	/// adds a product to the repo
	/// :param product: const Product&
	/// :return: NULL
	/// @:exception: invalid_argument if it cant pass the validator or if the product already exists
	void addProduct(const string& , const string& , const int&, const string&);

	/// removes a product to the repo
	/// :param product: const Product&
	/// :return: NULL
	/// @:exception: logic_error if there is no such product in the list
	void removeProduct(const string& , const string&);

	/// get all the product from the repo
	/// :return: vector of elems
	[[nodiscard]] vector getAllProducts() const noexcept;

	/// changes the attributes of product p
	/// :param p: Product to be changed
	/// :param name: potential new name
	/// :param type: potential new type
	/// :param price: potential new price
	/// :param producer: potential new producer
	/// :return: NULL
	static void changeProduct(Product& p, const string& name = "", const string& type = "", const int& price = -1, const string& producer = "") noexcept;

	/// sort the product using quicksort
	/// :param compareFunction: a function that takes two products and checks them
	/// :param products: the now sorted vector
	/// :return: NULL
	typedef bool (*cmpFunct)(const Product&, const Product&) noexcept;
	static void filterProductsFunction(cmpFunct, vector&);

	/// removes the product based on a criteria
	/// :param compareFunction: a function that takes a product and checks if it meets the remove criteria
	/// :return: NULL
	typedef bool (*rmFunct)(const Product&, void*) noexcept;
	void removeProductsFunction(rmFunct, void*);
};

#endif //SERVICE_H
