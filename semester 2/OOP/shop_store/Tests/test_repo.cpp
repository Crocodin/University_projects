#include "tests.h"
#include <cassert>
#include "../Repository/repo.h"

void test_repo() {
	Repository repo;

	const std::vector<string> names = {"p1", "p2", "p3", "p4", "p5"};
	const std::vector<string> producer = {"prod1", "prod2", "prod3", "prod4", "prod5"};

	for (int i = 0; i < static_cast<int>(names.size()); i++) {
		for (int j = 0; j < static_cast<int>(producer.size()); j++) {
			Product p(names[i], "default", 0, producer[j]);
			repo.add(p);
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
		Product aux = repo.find(p.getName(), p.getProducer());
		assert(false);
	}
	catch (const std::logic_error& e) { (void)e; assert(true); }

	try {
		Product aux = repo.find(p.getName(), p.getProducer());
		assert(false);
	}
	catch (const std::logic_error& e) { (void)e; assert(true); }

	try {
		uint aux = repo.getIndex(p);
		(void)aux;
		assert(false);
	}
	catch (const std::logic_error& e) { (void)e; assert(true); }


}
