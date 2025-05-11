#include <iostream>
#include <fstream>
#include <QApplication>
#include "Tests/tests.h"
//#include "Ui/Controller/controller.h"
#include "GUI/guiController.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	/// run all test
	run_all_tests();
	std::cout << "Passed all tests!\n";

	// Controller controller(50, "   ", "../Utilities/productData.txt");
	// controller.run();

	 guiController controller("../Utilities/productData.txt");
	 controller.startApplication();
	 controller.show();

	return QApplication::exec();
}
