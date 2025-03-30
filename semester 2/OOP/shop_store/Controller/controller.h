#ifndef CONTROLLER_H
#define CONTROLLER_H
#include "../Service/service.h"

class Service;

class Controller {
private:
	Service service;
	friend class Service;

	/// ui for adding product
	/// :return: NULL
	void addProduct();

	/// ui for removing product
	/// :return: NULL
	void remove();

	/// ui for printing elems
	/// :return: NULL
	static void print_all(const vector&) ;

	/// ui for changing items
	/// :return: NULL
	void changeProduct();

	/// prints the product
	/// :return: NULL
	static void printProduct(const Product &p) noexcept;

	/// ui for find product
	/// :return: NULL
	void findProduct();

	/// ui for sorting
	/// :return: NULL
	void sortProducts() const;

	/// ui for filtering (removing based on a condition)
	/// :return: NULL
	void filterProducts();

	void add_deafult();

public:

	/// starts & runs the application
	/// :return: NULL
	void run();
};

#endif //CONTROLLER_H
