#ifndef SHOPPINGCART_H
#define SHOPPINGCART_H
#include "../Repository/repo.h"
#include "../GUI/Cos/observer.hpp"
inline uint balance_ = 0;

class ShoppingCart final : public Repository, public Subject {
public:
	/// adds a product to the shopping cart
	/// :param Product: the product to add
	/// :return: NULL
	void addToShoppingCart(const Product&) noexcept;

	/// removes a product from the shopping cart
	/// :param Product: the product to remove
	/// :return: NULL
	/// @:exception: might throw LogicError
	void removeFromShoppingCart(const Product&);


};

#endif //SHOPPINGCART_H
