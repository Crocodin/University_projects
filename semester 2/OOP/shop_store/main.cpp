#include "Tests/tests.h"
#include "Ui/Controller/controller.h"
#include <iostream>

int main() {
	/// run all test
	run_all_tests();
	std::cout << "Passed all tests!\n";

	Controller controller(50, "   ");
	controller.run();


	return 0;
}
