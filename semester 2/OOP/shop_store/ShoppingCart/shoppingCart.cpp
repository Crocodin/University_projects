#include "shoppingCart.h"

void ShoppingCart::addToShoppingCart(const Product& p) noexcept {
	balance_ += p.getPrice();
	this->forceAdd(p);
}

void ShoppingCart::removeFromShoppingCart(const Product & p) {
	balance_ -= p.getPrice();
	this->remove(p);
}
