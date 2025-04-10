#include "service.h"

#include "../Domain/validator.h"

void Service::addProduct(const string& name , const string& type, const int& price, const string& producer) {
	if (const Validator validator; !validator.validate(name, type, price, producer))
		throw std::invalid_argument("Invalid product declaration");
	this->repo.add(Product(name, type, price, producer));
}

void Service::removeProduct(const string& name, const string& producer) {
	const Product& p = this->repo.find(name, producer);
	this->repo.remove(p);
}

vector& Service::getAllProducts() noexcept {
	return this->repo.getAllProducts();
}

void Service::changeProduct(Product& p, const string& name, const string& type, const int& price, const string& producer) noexcept {
	if (!name.empty()) p.setName(name);
	if (!type.empty()) p.setType(type);
	if (price >= 0) p.setPrice(price);
	if (!producer.empty()) p.setProducer(producer);
}


void quickSort(vector& products, int low, int high, const auto compareFunction) {
	if (low < high) {
		auto& pivot = products[high];
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (compareFunction(products[j], pivot)) {
				std::swap(products[++i], products[j]);
			}
		}
		std::swap(products[i + 1], products[high]);

		const int partitionIndex = i + 1;
		quickSort(products, low, partitionIndex - 1, compareFunction);
		quickSort(products, partitionIndex + 1, high, compareFunction);
	}
}

void Service::filterProductsFunction(const cmpFunct compareFunction, vector& products) {
	quickSort(products, 0, static_cast<int>(products.size()) - 1, compareFunction);
}

void Service::removeProductsFunction(const rmFunct removeFunction, void* param_2) {
	const vector& aux_vector = this->repo.getAllProducts();
	for (const Product *it = aux_vector.begin(); it < aux_vector.end() + 1; )
		if (removeFunction(*it, param_2)) this->repo.remove(*it);
		else ++it;
}