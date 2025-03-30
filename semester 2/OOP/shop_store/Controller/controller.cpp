#include "controller.h"
#include <iostream>

void Controler::addProduct() {
	uint price;
	string name, type, producer;

	std::cout<<"Product name: ";
	std::cin>>name;

	std::cout<<"Product type: ";
	std::cin>>type;

	std::cout<<"Product price: ";
	std::cin>>price;

	std::cout<<"Product producer: ";
	std::cin>>producer;

	try {
		this->service.addProduct(name, type, price, producer);
		std::cout << "Added successfully";
	}
	catch (const std::invalid_argument& e) {
		std::cout << e.what();
	}
}


void Controler::run() {
	while (true) {
		std::cout << "---------------// nemu \\\\---------------\n"
			"1. Add product\n2. Remove product\n3. Change product\n4. Search for product\n"
			"5. Filter\n6. Sort\nE. EXIT\n\n";
		char choice;
		std::cin >> choice;
		switch (choice) {
			case '1':
				this->addProduct();
		}
	}
}
