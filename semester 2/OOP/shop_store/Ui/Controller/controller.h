#ifndef CONTROLLER_H
#define CONTROLLER_H
#include "../../Service/service.h"
#include "../../ShoppingCart/shoppingCart.h"
#include "../Console/console.h"

class Service;

class Controller {
private:
	/// in the service we have all the object, so in this case, all the products
	Service service;

	/// this is the shopping cart, it has products in it
	ShoppingCart shoppingCart;

	/// this is the console, here we do all the printing!
	/// don't ask me why I did this, it wasn't needed, it just thought it would be cooler
	Console console;
protected:
	/// why this is a friend class?
	/// to reduce the amount we needed getAlls and that tipe of stuff
	friend class Service;

	/// ui for adding product
	/// :return: NULL
	void addProduct();

	/// ui for removing product
	/// :return: NULL
	void remove();

	/// ui for printing elems
	/// :return: NULL
	void print_all(const vector&) const;

	/// ui for changing items
	/// :return: NULL
	void changeProduct() const;

	/// prints the product
	/// :return: NULL
	void printProduct(const Product &p) const noexcept;

	/// ui for find product, add ask to add the product to the shopping cart
	/// :return: NULL
	void findProduct();

	/// ui for sorting
	/// :return: NULL
	void sortProducts();

	/// ui for filtering (removing based on a condition)
	/// :return: NULL
	void filterProducts();

	/// adding default products
	/// :return: NULL
	void add_default();

	void addToShoppingCart(const Product& p) noexcept;
public:

	/// this class represents how much the user can see, this being a shopping app you can
	/// have two modes of viewing: the guest (so the shopper) & the admin
	class ViewLevel {
	private:
		unsigned short level = 0;
	public:
		void low() { this->level = 0; }; /// guest mode
		void high() { this->level = 1; }; /// admin mode
		explicit operator bool() const { return this->level != 0; };
		void change() { this->level = (this->level + 1) % 2; };
	};

	explicit Controller(const uint width, const char* padding)
	: console(width, padding) {};

	/// starts & runs the application
	void run();

	void adminController() noexcept;

	void exportToHtml(const vector &products) const noexcept;

	void generateCart() noexcept;

	void removeCartProduct() noexcept;

	void shoppingCartOptions() noexcept;

	void userController() noexcept;
};

inline Controller::ViewLevel viewLevel;

inline bool appRunning;

#endif //CONTROLLER_H
