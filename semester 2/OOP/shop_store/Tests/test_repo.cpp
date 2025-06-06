#include "tests.h"
#include <cassert>
#include "../Repository/repo.h"
#include <vector>

#include "../Errors/errors.hpp"

void test_repo() {
	Repository repo;

	const std::vector<string> names = {"p1", "p2", "p3", "p4", "p5"};
	const std::vector<string> producer = {"prod1", "prod2", "prod3", "prod4", "prod5"};

	for (int i = 0; i < static_cast<int>(names.size()); i++) {
		for (const auto & j : producer) {
			Product p(names[i], "default", 0, j);
			repo.add(p);
			assert(repo.isIn(p));
		}
		assert(static_cast<int>(repo.size()) == 5 * (i + 1));
	}

	assert(repo.isIn(Product("p1", "default", 0, "prod1")));
	assert(!repo.isIn(Product("p6", "default", 0, "prod1")));

	Product p = repo.find("p1", "prod1");
	assert(p.getProducer() == "prod1");
	assert(p.getName() == "p1");

	repo.remove(p);
	try {
		const Product& aux = repo.find(p.getName(), p.getProducer());
		(void) aux;
		assert(false);
	}
	catch (const err::LogicError& e) { (void)e; assert(true); }

	try {
		const Product& aux = repo.find(p.getName(), p.getProducer());
		(void) aux;
		assert(false);
	}
	catch (const err::LogicError& e) { (void)e; assert(true); }

	try {
		const uint aux = repo.getIndex(p);
		(void)aux;
		assert(false);
	}
	catch (const err::LogicError& e) { (void)e; assert(true); }
}
