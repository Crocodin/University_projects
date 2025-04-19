#include "tests.h"
#include <cassert>

#include "../Service/service.h"

void test_serviceFile() {
	{
		Service service("../Tests/test.txt");
		/// this test reprezent test for te loding to file and what not
		/// they dont test the "service" functions
		assert(service.getAllProducts().size() == 4);
		service.addProduct("testServiceFile", "_", 1, "_");
		assert(service.getAllProducts().size() == 5);
	}   /// the scope anded so it should be loded in the test file
	{
		Service service("../Tests/test.txt");
		assert(service.getAllProducts().size() == 5);
		service.removeProduct("testServiceFile", "_");
		assert(service.getAllProducts().size() == 4);
	}
	Service service("../Tests/test.txt");
	assert(service.getAllProducts().size() == 4);
}
