#include "console.h"
#include "unistd.h"

#include <iomanip>
#include <iostream>
#include <sstream>
#include <vector>


void Console::drawLine(const char* c) const noexcept {
	for (uint i = 0; i < this->width; ++i)
		std::cout << c;
	std::cout << '\n';
}

void Console::clearScreen() noexcept {
	std::cout << "\033[2J\033[H\n" << std::flush;
}

void Console::centerText(const string& text, const string& color = {"none"}, const char endLine = '\n') const noexcept {
	const int pad = (this->width - text.size()) / 2;
	if (color == "none")
		std::cout << std::string(pad, ' ') << text << endLine;
	else std::cout << std::string(pad, ' ') << color << text << RESET << endLine;
}

void Console::paddedText(const std::string& text, const std::string& color, const char endLine) const noexcept {
	std::cout << this->padding;
	if (color == "none") std::cout << text << endLine;
	else std::cout << color << text << RESET << endLine;
}

void Console::clearLine() const noexcept {
	std::cout << "\r" << std::string(this->width, ' ') << "\r" << std::flush;
}

void Console::welcomeScreen() const noexcept {
	this->drawLine("\033[33m=\033[m"); /// the color here is set manually
	std::cout << '\n';
	this->centerText("🛒 Welcome to ShopStore! 🛒", YELLOW);
	std::cout << '\n';
	this->drawLine("\033[33m=\033[m"); /// the color here is set manually
	std::cout << '\n';
	this->centerText("Initializing", "none", ' ');
	std::cout << '\b' << std::flush;
	for (uint i = 0; i < 3; ++i) {
		sleep(1);
		std::cout << '.' << std::flush;
	}
	for (int i = 0; i < 10; ++i) {
		const auto spin = "|/-\\";
		std::cout << spin[i % 4] << std::flush;
		usleep(100000); /// 0.1 sec
		std::cout << '\b' << std::flush;
	}

	sleep(1);
	this->clearLine();

	/// now, the veiwing mode
	this->paddedText("Enter as:");
	this->paddedText("1. User");
	this->paddedText("2. Admin", BOLD_WHITE);
	std::cout << '\n';
}

void Console::invalid_input() noexcept {
	std::cout << BOLD_RED << "Invalid choice!" << RESET << '\n';
}

void Console::adminMainMenu() const noexcept {
	this->drawLine("\033[36m=\033[m");
	std::cout << '\n';
	this->centerText("🔧 Admin Mode 🔧", CYAN);
	std::cout << '\n';


	const std::vector<std::pair<std::string, std::string>> options = {
		{"📦 1. Add Product", GREEN},
		{"🗑️2. Remove Product", RED},
		{"✏️ 3. Change Product", YELLOW},
		{"🔍 4. Filter", BLUE},
		{"📃 P. Print", WHITE},
		{"❌  E. Exit          🔁 C. Change view", GRAY}
	};

	for (const auto& [text, color] : options) {
		this->paddedText(text, color);
		usleep(200000); // 0.2 seconds per line
	}
	std::cout << '\n';
}

void Console::filterMenu() const noexcept {
	this->drawLine("\033[35m=\033[m"); // Purple/magenta line
	std::cout << '\n';
	this->centerText("🔎 Filter Products 🔎", MAGENTA);
	std::cout << '\n';

	const std::vector<std::pair<std::string, std::string>> filters = {
		{"💰 1. By Price", GREEN},
		{"🔤 2. By Name", BLUE},
		{"🏭 3. By Producer", CYAN}
	};

	for (const auto& [text, color] : filters) {
		this->paddedText(text, color);
		usleep(200000); // 0.2s pause
	}
	std::cout << '\n';
}

void Console::errorText(const string& text) noexcept {
	std::cout << BOLD_RED << text << RESET << '\n';
}

void Console::successfullyText(const string& text) noexcept {
	std::cout << GREEN << text << RESET << '\n';
}

void Console::printProducts(const list& products) const {
	this->drawLine("\033[36m=\033[0m"); // Line above the title
	this->centerText("🛍️ Product List", CYAN);
	this->drawLine("\033[36m=\033[0m");
	std::cout << '\n';

	// Print header with aligned columns
	this->paddedText("📦 Name           🏷️Type          💰Price      🏭 Producer", LIGHT_BLUE);

	// Iterate through products and print them with proper alignment
	for (const auto& product : products) {
		std::stringstream line;
		line << "• "
			 << std::setw(17) << std::left << product.getName() << '|'
			 << std::setw(17) << std::left << product.getType() << '|'
			 << std::setw(10) << std::left << product.getPrice() << '|'
			 << std::setw(20) << std::left << product.getProducer();  // Producer aligned to left
		this->paddedText(line.str(), LIGHT_BLUE);
	}
}

void Console::waitForKey(const string& message) const noexcept {
	constexpr int numeric_limit = 100000;
	this->paddedText("🔁 " + message, GRAY, ' ');
	std::cin.ignore(numeric_limit, '\n');
}

void Console::sortedProductsMenu() const noexcept {
	this->centerText("============ Sorted Products ============", LIGHT_BLUE);
	this->paddedText("1. 📊 Sort by Price", LIGHT_BLUE);
	this->paddedText("2. 🏷️ Sort by Name", LIGHT_BLUE);
	this->paddedText("3. 🔠 Sort by Type + Name", LIGHT_BLUE);
}

void Console::printDetailedProduct(const Product& p) const noexcept {
	this->paddedText("============// " + p.getName() + " \\\\============", LIGHT_BLUE);
	this->paddedText("📦 Product Type:  " + p.getType(), LIGHT_BLUE);
	this->paddedText("💰 Product Price: " + std::to_string(p.getPrice()) + "$", LIGHT_BLUE);
	this->paddedText("🏭 Product Producer: " + p.getProducer(), LIGHT_BLUE);
}

void Console::userMainMenu(const uint& balance) const noexcept {
	std::cout << BLUE << "=================================== " << balance << "$ ====\n" << RESET; ;
	std::cout << '\n';
	this->centerText("✨ User Mode ✨", BLUE);
	std::cout << '\n';


	const std::vector<std::pair<std::string, std::string>> options = {
		{"📦 1. Find product", GREEN},
		{"🔍 2. Sort products", YELLOW},
		{"🛒 3. Shopping cart", BLUE},
		{"📃 P. Print", WHITE},
		{"❌  E. Exit          🔁 C. Change view", GRAY}
	};

	for (const auto& [text, color] : options) {
		this->paddedText(text, color);
		usleep(200000); // 0.2 seconds per line
	}
	std::cout << '\n';
}

void Console::addToShoppingCartMenu() const noexcept {
	std::cout << '\n';
	this->paddedText("Do you want to add this to the shopping car? (y/n)", GREEN);
}

void Console::shoppingCart(const list& products, const uint& full_price) const noexcept {
	this->drawLine("\033[36m=\033[0m"); // Line above the title
	this->centerText("🛍️ Shopping cart", CYAN);
	this->drawLine("\033[36m=\033[0m");
	std::cout << '\n';

	this->paddedText("📦 Name           🏷️Type          💰Price      🏭 Producer", LIGHT_BLUE);

	for (const auto& product : products) {
		std::stringstream line;
		line << "• "
			 << std::setw(17) << std::left << product.getName() << '|'
			 << std::setw(17) << std::left << product.getType() << '|'
			 << std::setw(10) << std::left << product.getPrice() << '|'
			 << std::setw(20) << std::left << product.getProducer();  // Producer aligned to left
		this->paddedText(line.str(), LIGHT_BLUE);
	}

	std::stringstream line;
	line << "Total sum is: " << full_price << "$";
	this->paddedText(line.str(), LIGHT_BLUE);
	std::cout << '\n';
}

void Console::shoppingCartOptions() const noexcept {
	this->drawLine("\033[36m=\033[0m");
	std::cout << '\n';
	this->paddedText("🗑️1. Empty shopping cart", GREEN);
	this->paddedText("💾 2. Export shopping cart", YELLOW);
	this->paddedText("👀 3. View shopping cart", BLUE);
	this->paddedText("↩️ B. GO BACK", GRAY);
	std::cout << '\n';
}

void Console::exportMenu() const noexcept {
	this->drawLine("\033[33m=\033[0m");
	std::cout << '\n';
	this->centerText("Export", YELLOW);
	std::cout << '\n';
	this->drawLine("\033[33m=\033[0m");
	std::cout << '\n';
	this->paddedText("File name:", GREEN, ' ');
}
