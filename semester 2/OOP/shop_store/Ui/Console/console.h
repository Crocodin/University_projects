#ifndef CONSOLE_H
#define CONSOLE_H
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

		/// clears the console screen
	static void clearScreen() noexcept;

	/// clears the current line in the console
	void clearLine() const noexcept;

	/// prints centered text with color and optional line ending
	/// :param text: the text to center
	/// :param color: the text color
	/// :param endLine: the end line character
	void centerText(const string& text, const string& color, char endLine) const noexcept;

	/// prints padded text with optional color and line ending
	/// :param text: the text to display
	/// :param color: the text color (default is "none")
	/// :param endLine: the end line character
	void paddedText(const std::string& text, const std::string& color = "none", char endLine = '\n') const noexcept;

	/// the welcome screen, the first introduction to the user of this app
	/// :param NULL:
	/// :return: NULL
	/// @:exception: noexcept
	void welcomeScreen() const noexcept;

	/// prints an "Invalid input" message to the user
	static void invalid_input() noexcept;

	/// displays the main menu for the admin
	void adminMainMenu() const noexcept;

	/// displays the filter options menu
	void filterMenu() const noexcept;

	/// prints an error message in red or appropriate color
	/// :param unnamed: the error message
	static void errorText(const string&) noexcept;

	/// prints a success message in green or appropriate color
	/// :param unnamed: the success message
	static void successfullyText(const string&) noexcept;

	/// prints a list of products
	/// :param unnamed: the list of products to print
	void printProducts(const list&) const;

	/// prompts user to press a key to continue
	/// :param message: custom message shown to the user
	void waitForKey(const string& message = "Press ENTER to continue...") const noexcept;

	/// displays the menu for sorting products
	void sortedProductsMenu() const noexcept;

	/// prints detailed information about a single product
	/// :param p: the product to display
	void printDetailedProduct(const Product& p) const noexcept;

	/// displays the main menu for a user
	/// :param balance: the user's current balance
	void userMainMenu(const uint& balance) const noexcept;

	/// displays menu to add items to the shopping cart
	void addToShoppingCartMenu() const noexcept;

	/// displays the shopping cart with product list and balance
	/// :param unnamed: list of products
	/// :param unnamed: current user balance
	void shoppingCart(const list&, const uint&) const noexcept;

	/// displays options for the shopping cart
	/// :param unnamed: current user balance
	void shoppingCartOptions(const uint&) const noexcept;

	/// displays export options (e.g., CSV, HTML)
	void exportMenu() const noexcept;

};

#endif //CONSOLE_H
