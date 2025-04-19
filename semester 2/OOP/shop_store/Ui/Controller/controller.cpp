#include "controller.h"
#include "../../Errors/errors.hpp"

#include <fstream>
#include <iostream>
#include <random>


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
		this->console.waitForKey();
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
	catch (const err::LogicError& e) {
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
	try { p = &this->service.repo->find(name, producer); }
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

void Controller::addToShoppingCart(const Product& p) noexcept {
	this->console.addToShoppingCartMenu();
	char choice;
	std::cin >> choice;
	while (std::cin.get() != '\n') {}
	if (choice == 'n') return;
	this->shoppingCart.addToShoppingCart(p);
}

void Controller::findProduct() {
	std::cout << '\n'; /// i know it shouldn't be here... but...
	string name, producer;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	Product p;
	try { p = this->service.repo->find(name, producer); }
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
	this->service.repo->add(Product("Laptop", "Electronics", 1200, "TechCorp"));
	this->service.repo->add(Product("Laptop", "Electronics", 1100, "ByteTech"));
	this->service.repo->add(Product("Phone", "Electronics", 800, "TechCorp"));
	this->service.repo->add(Product("Phone", "Electronics", 750, "GigaComm"));
	this->service.repo->add(Product("Tablet", "Electronics", 600, "TechCorp"));
	this->service.repo->add(Product("Tablet", "Electronics", 620, "ByteTech"));
	this->service.repo->add(Product("Monitor", "Electronics", 300, "DisplayMax"));
	this->service.repo->add(Product("Monitor", "Electronics", 280, "ScreenPro"));
	this->service.repo->add(Product("Keyboard", "Accessories", 100, "KeyMasters"));
	this->service.repo->add(Product("Keyboard", "Accessories", 90, "TypeFast"));
	this->service.repo->add(Product("Mouse", "Accessories", 50, "ClickyTech"));
	this->service.repo->add(Product("Mouse", "Accessories", 55, "FastClick"));
	this->service.repo->add(Product("Headphones", "Audio", 150, "SoundWave"));
	this->service.repo->add(Product("Headphones", "Audio", 140, "BeatTech"));
	this->service.repo->add(Product("Speaker", "Audio", 200, "SoundWave"));
	this->service.repo->add(Product("Speaker", "Audio", 210, "BoomBox"));
	this->service.repo->add(Product("Smartwatch", "Wearables", 300, "WristTech"));
	this->service.repo->add(Product("Smartwatch", "Wearables", 290, "TimeGears"));
	this->service.repo->add(Product("VR Headset", "Gaming", 500, "ImmersiView"));
	this->service.repo->add(Product("VR Headset", "Gaming", 480, "HyperLens"));
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

void Controller::exportToHtml(const vector& products) const noexcept {
	Console::clearScreen();
	this->console.exportMenu();
	string outputFile {"../Exports/"}, fileName;
	std::getline(std::cin, fileName);
	outputFile += fileName + ".html";

	std::ifstream templateIn("template.html"); /// GO TO THE CMAKELIST IF IT DOSEN'T WORK
	if (!templateIn.is_open()) {
		Console::errorText("❌ Failed to open template file.");
		this->console.waitForKey(); return;
	}

	std::ofstream out(outputFile);
	if (!out.is_open()) {
		/// CHECK THE outputFile PATH, IT IS RELATIV TO THE CMAKE-BUILD-DEBUG
		Console::errorText("❌ Failed to open output file.");
		this->console.waitForKey(); return;
	}

	std::string line;
	while (std::getline(templateIn, line)) {
		// Inject product rows where marker is
		if (line.find("<!-- PRODUCTS GO HERE -->") != std::string::npos) {
			for (const auto& product : products) {
				out << "<tr><td>" << product.getName() << "</td>"
					<< "<td>" << product.getType() << "</td>"
					<< "<td>" << product.getPrice() << "</td>"
					<< "<td>" << product.getProducer() << "</td></tr>\n";
			}
		} else {
			if (line.find("<!-- PRICE GO HERE-->") != std::string::npos) {
				out << balance_;
			}
			else out << line << "\n";  // copy the line as is
		}
	}
	templateIn.close();
	out.close();
	string successExport = {"✅ HTML exported to " + outputFile};
	Console::successfullyText(successExport);
	this->console.waitForKey();
}

void Controller::generateCart() noexcept {
	if (static_cast<int>(this->service.getAllProducts().size() - 1) == 0) {
		Console::errorText("No products to be added!");
		this->console.waitForKey(); return;
	}

	std::mt19937 mt{ std::random_device{}() };
	std::uniform_int_distribution<int> distrib{ 0, static_cast<int>(this->service.getAllProducts().size() - 1) };

	string number_; int number;
	this->console.paddedText("♾️ Number of elements: ", YELLOW, ' ');
	std::getline(std::cin, number_);

	try {
		number = stoi(number_);
		if (number < 0) throw err::InvalidArgument("Invalid price, needs to be a positive number");
	}
	catch (...) {
		Console::errorText("Needs to be a positive number");
		this->console.waitForKey(); return;
	}

	const vector& products = this->service.getAllProducts();
	for (int i = 0; i < number; i++) {
		const int rndNr = distrib(mt);
		this->shoppingCart.addToShoppingCart(products[rndNr]);
	}
}

void Controller::removeCartProduct() noexcept {
	string name, producer;

	this->console.paddedText("📝 Product name: ", LIGHT_BLUE, ' ');
	std::getline(std::cin, name);

	this->console.paddedText("🏷️Product producer: ", GREEN, ' ');
	std::getline(std::cin, producer);

	Product p;
	try { p = this->service.repo->find(name, producer); }
	catch (const err::LogicError& e) {
		Console::errorText(e.what());
		this->console.waitForKey(); return;
	}

	this->shoppingCart.removeFromShoppingCart(p);
}

void Controller::exportToCSV(const vector& products) const noexcept {
	this->console.exportMenu();
	string outputFile {"../Exports/"}, fileName;
	std::getline(std::cin, fileName);
	outputFile += fileName + ".scv";

	std::ofstream out(outputFile);
	if (!out.is_open()) {
		Console::errorText("Failed to open output file.");
		this->console.waitForKey(); return;
	}

	for (const auto& product : products)
		out << product.getName() << ',' << product.getType() << ',' << product.getPrice() << ',' << product.getProducer() << '\n';
	out.close();
	string successExport = {"✅ CSV exported to " + outputFile};
	Console::successfullyText(successExport);
	this->console.waitForKey();
}

void Controller::shoppingCartOptions() noexcept {
	bool stop = false;
	while (!stop) {
		Console::clearScreen();
		this->console.shoppingCartOptions(balance_);
		char choice;
		std::cin >> choice;
		while (std::cin.get() != '\n') {}
		switch (choice) {
			case 'B': case 'b': case 'E': case 'e':
				stop = true;
			break;
			case '1': {
				vector& auxList = this->shoppingCart.getAllProducts();
				/// delets all form the shopping cart
				try {
					auxList.erase(auxList.begin(), auxList.end());
				} catch (err::OutOfRange& e) {
					Console::errorText("Can't delete empty cart");
					console.waitForKey(); (void) e;
				}
				balance_ = 0;
			}
			break;
			case '2': {
				this->console.paddedText("Export to HTML? (y/n)", YELLOW);
				char _choice;
				std::cin >> _choice;
				while (std::cin.get() != '\n') {}
				if (_choice == 'y' or _choice == 'Y')
					this->exportToHtml(this->shoppingCart.getAllProducts());
				else this->exportToCSV(this->shoppingCart.getAllProducts());
			}
			break;
			case '3': {
				this->console.shoppingCart(this->shoppingCart.getAllProducts(), balance_);
				this->console.waitForKey();
			}
			break;
			case '4':
				this->removeCartProduct();
			break;
			case '5':
				this->generateCart();
			break;
			default:
				Console::invalid_input();
		}
	}
}

void Controller::userController() noexcept {
	Console::clearScreen();
	this->console.userMainMenu(balance_);
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
			this->shoppingCartOptions();
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
	balance_ = 0;
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
