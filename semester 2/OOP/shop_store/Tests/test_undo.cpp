#include "tests.h"
#include "../Service/service.h"
#include <cassert>

void test_undo() {
	Service service;
	service.addProduct("Laptop", "Electronics", 1200, "TechCorp");
	assert(service.getAllProducts().size() == 1);
	service.undo();
	assert(service.getAllProducts().empty());
	service.addProduct("1", "Electronics", 1200, "_");
	service.addProduct("2", "Electronics", 1200, "_");
	service.addProduct("3", "Electronics", 1200, "_");
	assert(service.getAllProducts().size() == 3);
	service.removeProduct("3", "_");
	assert(service.getAllProducts().size() == 2);
	service.addProduct("4", "Electronics", 1200, "_");
	assert(service.getAllProducts()[2].getName() == "4");
	service.undo();
	assert(service.getAllProducts().size() == 2);
	service.undo();
	assert(service.getAllProducts().size() == 3);
	assert(service.getAllProducts()[2].getName() == "3");
}
