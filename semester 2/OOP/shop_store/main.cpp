#include "Tests/tests.h"
#include "Controller/controller.h"
#include <iostream>


int main() {
	/// run all test
	run_all_tests();
	std::cout << "Passed all tests!\n";

	Controller controller;
	controller.run();

	return 0;
}
