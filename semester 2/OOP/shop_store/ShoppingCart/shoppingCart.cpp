#include "shoppingCart.h"

void ShoppingCart::addToShoppingCart(const Product& p) noexcept {
	balance_ += p.getPrice();
	this->forceAdd(p);
}