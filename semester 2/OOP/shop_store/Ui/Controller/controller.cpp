#include "controller.h"
#include <iostream>

#include "../../Errors/errors.hpp"

void Controller::addProduct() {
	string name, type, producer, _price;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product type: ", GREEN, ' ');
	std::getline(std::cin, type);

	this->console.paddedText("💰 Product price: ", YELLOW, ' ');
	std::getline(std::cin, _price);

	int price;
	try {
		price = stoi(_price);
	}
	catch (const std::exception& e) {
		(void) e;
		Console::errorText("Needs to be a number");
		return;
	}

	this->console.paddedText("🏭 Product producer: ", CYAN, ' ');
	std::getline(std::cin, producer);

	try {
		this->service.addProduct(name, type, price, producer);
		Console::successfullyText("✔️  Added successfully!");
	}
	catch (const err::InvalidArgument& e) {
		Console::errorText(e.what());
	}
	this->console.waitForKey();
}

void Controller::print_all(const vector& products) const {
	this->console.printProducts(products);
	this->console.waitForKey();
}

void Controller::remove() {
	string name, producer;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	try {
		this->service.removeProduct(name, producer);
		Console::successfullyText("✔️ Removed successfully!");
	}
	catch (const std::exception& e) {
		Console::errorText(e.what());
	}
	this->console.waitForKey();
}

void Controller::changeProduct() const {
	string name, producer;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	Product* p;
	try { p = &this->service.repo.find(name, producer); }
	catch (const err::LogicError& e) {
		Console::errorText(e.what());
		this->console.waitForKey(); return;
	}

	string type, _price;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product type: ", GREEN, ' ');
	std::getline(std::cin, type);

	this->console.paddedText("💰 Product price: ", YELLOW, ' ');
	std::getline(std::cin, _price);

	int price;
	try {
		price = stoi(_price);
		if (price < 0) throw err::InvalidArgument("Invalid price, needs to be a positive number");
	}
	catch (...) {
		Console::errorText("Needs to be a positive number");
		this->console.waitForKey(); return;
	}

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	Service::changeProduct(*(p), name, type, price, producer);
}

void Controller::printProduct(const Product& p) const noexcept {
	this->console.printDetailedProduct(p);
}

void Controller::addToShoppingCart(const Product& p) const noexcept {

}

void Controller::findProduct() const {
	string name, producer;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	Product p;
	try { p = this->service.repo.find(name, producer); }
	catch (const err::LogicError& e) {
		Console::errorText(e.what());
		this->console.waitForKey(); return;
	}

	printProduct(p);
	this->addToShoppingCart(p);
}

void Controller::sortProducts() {
	this->console.sortedProductsMenu();
	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}
	vector& productsAux = this->service.getAllProducts();
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
			Console::invalid_input();
			return;
	}
	this->print_all(productsAux);
}

void Controller::filterProducts() {
	Console::clearScreen();
	this->console.filterMenu();

	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}

	switch (choice) {
		case '1' : {
			string _price;
			this->console.paddedText("Product price:", GREEN, ' ');
			std::getline(std::cin, _price);
			int price;
			try {
				price = stoi(_price);
				if (price < 0) throw err::InvalidArgument("Invalid price, needs to be a positive number");
			}
			catch (const std::exception& e) {
				(void) e;
				Console::errorText("Needs to be a positive number");
				return;
			}
			this->service.removeProductsFunction(Product::priceComparison, &price);
			break;
		}
		case '2': {
			string name;
			this->console.paddedText("Product name:", BLUE, ' ');
			std::getline(std::cin, name);
			this->service.removeProductsFunction(Product::nameComparison, &name);
			break;
		}
		case '3': {
			string producer;
			this->console.paddedText("Product producer:", CYAN, ' ');
			std::getline(std::cin, producer);
			this->service.removeProductsFunction(Product::producerComparison, &producer);
			break;
		}
		default:
			Console::invalid_input();
			return;
	}
	this->print_all(this->service.getAllProducts());
}

void Controller::add_default() {
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

void Controller::adminController() noexcept {
	Console::clearScreen();
	this->console.adminMainMenu();
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
			this->filterProducts();
		break;
		case 'P': case 'p':
			this->print_all(this->service.getAllProducts());
		break;
		case 'E': case 'e':
			appRunning = false;
		break;
		case '!':
			Controller::add_default();
		break;
		case 'C': case 'c':
			viewLevel.change();
		break;
		default:
			Console::invalid_input();
	}
}

void Controller::userController() noexcept {
	Console::clearScreen();
	this->console.userMainMenu(viewLevel.balance);
	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}
	switch (choice) {
		case '1':
			this->findProduct();
		break;
		case '2':
			this->sortProducts();
		break;
		case '3':
			/// TODO this->shoppingCart();
		break;
		case 'P': case 'p':
			this->print_all(this->service.getAllProducts());
		break;
		case 'E': case 'e':
			appRunning = false;
		break;
		case 'C': case 'c':
			viewLevel.change();
		break;
		default:
			Console::invalid_input();
	}
}

void Controller::run() {
	appRunning = true;

	this->console.welcomeScreen();
	while (appRunning) {
		char choice;
		std::cin >> choice;
		while (std::cin.get() != '\n') {}
		switch (choice) {
			case '1': {
				viewLevel.low();
				appRunning = false;
				break;
			}
			case '2': {
				viewLevel.high();
				appRunning = false;
				break;
			}
			default:
				Console::invalid_input();
		}
	}

	appRunning = true;

	while (appRunning) {
		if (viewLevel)  /// admin mode
			this->adminController();
		else this->userController();
	}
}
