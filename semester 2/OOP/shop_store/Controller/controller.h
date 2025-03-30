#ifndef CONTROLLER_H
#define CONTROLLER_H
#include "../Service/service.h"

class Controller {
private:
	Service service;
public:

	/// starts & runs the application
	/// :return: NULL
	void run();

	/// ui for adding product
	/// :return: NULL
	void addProduct();
};

#endif //CONTROLLER_H
