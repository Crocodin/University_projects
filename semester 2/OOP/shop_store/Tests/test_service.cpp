#include <cassert>
#include "tests.h"
#include "../Domain/product.h"
#include "../Service/service.h"

void test_service() {
	/// change atributes
	Service service;
	Product product {"Ibuprofen", "for seek people", 12, "ibuprofen.org"};
	service.changeProduct(product);
	assert(product.getName() == "Ibuprofen");
	assert(product.getType() == "for seek people");
	assert(product.getPrice() == 12);
	assert(product.getProducer() == "ibuprofen.org");

	service.changeProduct(product, "NewName", "NewType");
	assert(product.getName() == "NewName");
	assert(product.getType() == "NewType");
	assert(product.getPrice() == 12);
	assert(product.getProducer() == "ibuprofen.org");

	service.changeProduct(product, "", "", 21, "NewProducer");
	assert(product.getName() == "NewName");
	assert(product.getType() == "NewType");
	assert(product.getPrice() == 21);
	assert(product.getProducer() == "NewProducer");

	/// add product
	service.addProduct("Laptop", "Electronics", 1200, "TechCorp");
	service.addProduct("Laptop", "Electronics", 1100, "ByteTech");
	try { service.addProduct("", "", -1100, ""); assert(false); }
	catch (...) { assert(true); }
	assert(service.getAllProducts().size() == 2);
	try {
		service.addProduct("Laptop", "Electronics", 1200, "TechCorp");
		assert(false);
	}
	catch(...) {assert(true);}

	/// remove product
	service.removeProduct("Laptop", "TechCorp");
	assert(service.getAllProducts().size() == 1);

	/// filter products
	service.addProduct("Laptop", "Alibaba", 1200, "TechCorp");
	vector& v = service.getAllProducts();
	Service::filterProductsFunction(Product::typeComparison, v);
	assert(v[0].getPrice() == 1200);
	assert(v[1].getPrice() == 1100);

	string name = "Laptop";
	service.removeProductsFunction(Product::nameComparison, &name);
	assert(service.getAllProducts().empty());
}
