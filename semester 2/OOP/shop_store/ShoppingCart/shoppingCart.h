#ifndef SHOPPINGCART_H
#define SHOPPINGCART_H
#include "../Repository/repo.h"

inline uint balance_ = 0;

class ShoppingCart : public Repository {
public:
	void addToShoppingCart(const Product&) noexcept;
};

#endif //SHOPPINGCART_H
