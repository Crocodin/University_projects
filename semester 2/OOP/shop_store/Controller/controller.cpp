#include "controller.h"
#include <iostream>

void Controller::addProduct() {
	string name, type, producer, _price;

	std::cout<<"Product name: ";
	std::getline(std::cin, name);

	std::cout<<"Product type: ";
	std::getline(std::cin, type);

	std::cout<<"Product price: ";
	std::getline(std::cin, _price);

	int price;
	try {
		price = stoi(_price);
	}
	catch (const std::exception& e) {
		(void) e;
		std::cout<< "Needs to be a number\n";
		return;
	}

	std::cout<<"Product producer: ";
	std::getline(std::cin, producer);

	try {
		this->service.addProduct(name, type, price, producer);
		std::cout << "Added successfully!\n";
	}
	catch (const std::invalid_argument& e) {
		std::cout << e.what() << '\n';
	}
}

void Controller::print_all(const vector& products) {
	std::cout << "------------// product list \\\\------------\n";
	for (const auto& product: products) {
		std::cout<<product.getName() << ' ' << product.getType() << " - " << product.getPrice() << "$ - by: " <<
			product.getProducer() << '\n';
	}
}


void Controller::remove() {
	string name, producer;
	std::cout << "Product name: ";
	std::getline(std::cin, name);
	std::cout << "Product producer: ";
	std::getline(std::cin, producer);

	try {
		this->service.removeProduct(name, producer);
		std::cout << "Removed successfully!\n";
	}
	catch (const std::exception& e) {
		std::cout << e.what() << '\n';
	}
}

void Controller::changeProduct() {
	string name, producer;
	std::cout << "Product name: ";
	std::getline(std::cin, name);
	std::cout << "Product producer: ";
	std::getline(std::cin, producer);

	Product* p;
	try { p = &this->service.repo.find(name, producer); }
	catch (const std::invalid_argument& e) { std::cout << e.what() << '\n'; return; }

	string type, _price;
	std::cout << "Product name: ";
	std::getline(std::cin, name);

	std::cout << "Product type: ";
	std::getline(std::cin, type);

	std::cout<<"Product price: ";
	std::getline(std::cin, _price);

	int price;
	try {
		price = stoi(_price);
		if (price < 0) throw std::invalid_argument("Invalid price, needs to be a positive number");
	}
	catch (const std::exception& e) {
		(void) e;
		std::cout<< "Needs to be a positive number\n";
		return;
	}

	std::cout<<"Product producer: ";
	std::getline(std::cin, producer);

	Service::changeProduct(*(p), name, type, price, producer);

}

void Controller::printProduct(const Product& p) noexcept {
	std::cout<< "---------------// " << p.getName() << " \\\\---------------\n" <<
		"Product type: " << p.getType() << "\nProduct price: " << p.getPrice() << "\nProduct producer: " << p.getProducer() << '\n';
}

void Controller::findProduct() {
	string name, producer;
	std::cout << "Product name: ";
	std::getline(std::cin, name);
	std::cout << "Product producer: ";
	std::getline(std::cin, producer);

	Product p;
	try { p = this->service.repo.find(name, producer); }
	catch (const std::invalid_argument& e) { std::cout << e.what() << '\n'; return; }

	printProduct(p);
}

void Controller::sortProducts() const {
	std::cout << "--------------// sorted products \\\\--------------\n"
		"1. Price\n" << "2. Name\n" << "3. Type + name\n";
	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}
	vector productsAux = this->service.getAllProducts();
	switch (choice) {
		case '1':
			Service::filterProductsFunction(Product::priceComparison, productsAux);
			break;
		case '2':
			Service::filterProductsFunction(Product::nameComparison, productsAux);
			break;
		case '3':
			Service::filterProductsFunction(Product::typeComparison, productsAux);
			break;
		default:
			std::cout << "Invalid choice!\n";
			return;
	}
	print_all(productsAux);
}

void Controller::filterProducts() {
	std::cout << "--------------// filter products \\\\--------------\n"
		"1. Price\n" << "2. Name\n" << "3. Producer\n";
	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}
	switch (choice) {
		case '1' : {
			string _price;
			std::cout<<"Product price: ";
			std::getline(std::cin, _price);
			int price;
			try {
				price = stoi(_price);
				if (price < 0) throw std::invalid_argument("Invalid price, needs to be a positive number");
			}
			catch (const std::exception& e) {
				(void) e;
				std::cout<< "Needs to be a positive number\n";
				return;
			}
			this->service.removeProductsFunction(Product::priceComparison, &price);
			break;
		}
		case '2': {
			string name;
			std::cout << "Product name: ";
			std::getline(std::cin, name);
			this->service.removeProductsFunction(Product::nameComparison, &name);
			break;
		}
		case '3': {
			string producer;
			std::cout << "Product producer: ";
			std::getline(std::cin, producer);
			this->service.removeProductsFunction(Product::producerComparison, &producer);
			break;
		}
		default:
			std::cout << "Invalid choice!\n";
			return;
	}
	print_all(this->service.getAllProducts());
}

void Controller::add_deafult() {
	this->service.repo.add(Product("Laptop", "Electronics", 1200, "TechCorp"));
	this->service.repo.add(Product("Laptop", "Electronics", 1100, "ByteTech"));
	this->service.repo.add(Product("Phone", "Electronics", 800, "TechCorp"));
	this->service.repo.add(Product("Phone", "Electronics", 750, "GigaComm"));
	this->service.repo.add(Product("Tablet", "Electronics", 600, "TechCorp"));
	this->service.repo.add(Product("Tablet", "Electronics", 620, "ByteTech"));
	this->service.repo.add(Product("Monitor", "Electronics", 300, "DisplayMax"));
	this->service.repo.add(Product("Monitor", "Electronics", 280, "ScreenPro"));
	this->service.repo.add(Product("Keyboard", "Accessories", 100, "KeyMasters"));
	this->service.repo.add(Product("Keyboard", "Accessories", 90, "TypeFast"));
	this->service.repo.add(Product("Mouse", "Accessories", 50, "ClickyTech"));
	this->service.repo.add(Product("Mouse", "Accessories", 55, "FastClick"));
	this->service.repo.add(Product("Headphones", "Audio", 150, "SoundWave"));
	this->service.repo.add(Product("Headphones", "Audio", 140, "BeatTech"));
	this->service.repo.add(Product("Speaker", "Audio", 200, "SoundWave"));
	this->service.repo.add(Product("Speaker", "Audio", 210, "BoomBox"));
	this->service.repo.add(Product("Smartwatch", "Wearables", 300, "WristTech"));
	this->service.repo.add(Product("Smartwatch", "Wearables", 290, "TimeGears"));
	this->service.repo.add(Product("VR Headset", "Gaming", 500, "ImmersiView"));
	this->service.repo.add(Product("VR Headset", "Gaming", 480, "HyperLens"));
}

void Controller::run() {
	bool run = true;
	while (run) {
		std::cout << "---------------// nemu \\\\---------------\n"
			"1. Add product\n2. Remove product\n3. Change product\n4. Search for product\n"
			"5. Filter\n6. Sort\nE. EXIT                    P. Print\n\n";
		char choice;
		std::cin >> choice;
		while (std::cin.get() != '\n') {}
		switch (choice) {
			case '1':
				this->addProduct();
				break;
			case '2':
				this->remove();
				break;
			case '3':
				this->changeProduct();
				break;
			case '4':
				this->findProduct();
				break;
			case '5':
				this->filterProducts();
				break;
			case '6':
				this->sortProducts();
				break;
			case 'P': case 'p':
				Controller::print_all(this->service.getAllProducts());
				break;
			case 'E': case 'e':
				run = false;
				break;
			case '!':
				Controller::add_deafult();
				break;
			default:
				std::cout << "Invalid choice\n";
		}
	}
}
