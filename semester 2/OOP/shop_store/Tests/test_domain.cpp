#include <cassert>
#include "tests.h"
#include "../Domain/product.h"
#include "../Domain/validator.h"

void test_product() {
	Product p { "name", "type", 12, "producer" };
	assert(p.getName() == "name");
	assert(p.getPrice() == 12);
	assert(p.getType() == "type");
	assert(p.getProducer() == "producer");

	p.setName("different_name");
	assert(p.getName() == "different_name");
	assert(p.getPrice() == 12);
	assert(p.getType() == "type");
	assert(p.getProducer() == "producer");

	p.setPrice(0);
	assert(p.getPrice() == 0);

	p.setType("different_type");
	assert(p.getType() == "different_type");

	p.setProducer("different_producer");
	assert(p.getProducer() == "different_producer");

	Product p2 { "name", "type", 13, "producer" };
	string something = "name";
	uint number = 13;
	assert(Product::nameComparison(p2, p) != true);
	assert(Product::priceComparison(p2, p) != true);
	assert(Product::typeComparison(p2, p) != true);
	assert(Product::nameComparison(p2, &something) == true);
	assert(Product::priceComparison(p2, &number) == true);
	something = "producer";
	assert(Product::producerComparison(p2, &something) == true);
}

void test_validator() {
	Validator v;

	assert(v.valid_name("name"));
	assert(v.valid_price(12));
	assert(v.valid_price(12));
	assert(v.valid_type("type"));
	assert(v.valid_producer("producer"));
	assert(!v.valid_producer(""));
	assert(!v.valid_name(""));
	assert(!v.valid_type(""));

	assert(v.validate("name", "type", 12, "producer"));
}

void test_domain() {
	/// test for product.h
	test_product();
	test_validator();
}