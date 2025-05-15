#include "tests.h"
#include "../ShoppingCart/shoppingCart.h"
#include <vector>
#include <cassert>

#include "../Errors/errors.hpp"


void test_shopping_cart() {
	ShoppingCart cart;

	const std::vector<string> names = {"p1", "p2", "p3", "p4", "p5"};
	const std::vector<string> producer = {"prod1", "prod2", "prod3", "prod4", "prod5"};

	for (int i = 0; i < static_cast<int>(names.size()); i++) {
		for (const auto & j : producer) {
			Product p(names[i], "default", 1, j);
			cart.addToShoppingCart(p);
			assert(cart.isIn(p));
		}
		assert(static_cast<int>(cart.size()) == 5 * (i + 1));
	}
	assert(static_cast<int>(cart.size()) == 25);
	assert(balance_ == 25);

	const Product p = cart.find("p1", "prod1");
	cart.removeFromShoppingCart(p);

	assert(!cart.isIn(p));
	assert(balance_ == 24);
	assert(cart.size() == 24);

	try {
		Product& aux = cart.find("p1", "prod1");
		(void) aux;
		assert(false);
	} catch (const err::LogicError& e) {
		(void) e; assert(true);
	}

	try {
		cart.removeFromShoppingCart(p);
		assert(false);
	} catch (const err::LogicError& e) {
		(void) e; assert(true);
	}
}
