#ifndef CONSOLE_H
#define CONSOLE_H
#include <list>
#include <string>

#include "../../DynamicList/list.hpp"
#include "../../Domain/product.h"

#define RESET     "\033[0m"

#define MAGENTA   "\033[35m"
#define RED       "\033[31m"
#define GREEN     "\033[32m"
#define YELLOW    "\033[33m"
#define BLUE      "\033[34m"
#define MAGENTA   "\033[35m"
#define CYAN      "\033[36m"
#define WHITE     "\033[37m"
#define GRAY      "\033[90m"  // Bright black, used for dim text

#define LIGHT_BLUE "\033[94m"

#define BOLD_WHITE "\033[1;97m" // Bold bright white
#define BOLD_RED   "\033[1;31m"

using string = std::string;
using list = List<Product>;

class Console {
private:
	const char* padding;
	const unsigned int width;
public:

	/// setting the base width of the "screen" so the console
	explicit Console(const unsigned int width, const char* padding)
	: padding(padding), width(width) {};

	/// prints the text width times
	void drawLine(const char* c) const noexcept;

	static void clearScreen() noexcept;

	void clearLine() const noexcept;

	void centerText(const string& text, const string& color, char endLine) const noexcept;

	void paddedText(const std::string& text, const std::string& color = "none", char endLine = '\n') const noexcept;

	/// the welcome screen, the first introduction to the user of this app
	void welcomeScreen() const noexcept;

	static void invalid_input() noexcept;

	void adminMainMenu() const noexcept;

	void filterMenu() const noexcept;

	static void errorText(const string&) noexcept;

	static void successfullyText(const string&) noexcept;

	void printProducts(const list&) const;

	void waitForKey(const string& message = "Press ENTER to continue...") const noexcept;

	void sortedProductsMenu() const noexcept;

	void printDetailedProduct(const Product& p) const noexcept;

	void userMainMenu(const uint& balance) const noexcept;

	void addToShoppingCartMenu() const noexcept;

	void shoppingCart(const list&, const uint&) const noexcept;

	void shoppingCartOptions(const uint&) const noexcept;

	void exportMenu() const noexcept;
};

#endif //CONSOLE_H
