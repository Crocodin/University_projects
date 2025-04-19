#include "tests.h"
#include "../Repository/repo.h"
#include <cassert>

void test_repoFile() {
	RepositoryFile repoFile("../Tests/test.txt");
	assert(repoFile.size() == 4);

	Product p;
	try { p = repoFile.find("Tablet", "TechCorp"); }
	catch (...) { assert(false); }
	assert(p.getName() == "Tablet");
	assert(p.getProducer() == "TechCorp");

	repoFile.remove(p);
	assert(repoFile.size() == 3);

	repoFile.lodeToFile();
	/// deleting all the elements
	auto& list = repoFile.getAllProducts();
	assert(list.size() == 3);
	list.erase(list.begin(), list.end());
	assert(list.empty());
	assert(repoFile.size() == 0);

	repoFile.lodeFromFile();
	assert(repoFile.size() == 3);
	repoFile.add(p);
	/// when the distructor is called the repo should be saved
}
