#ifndef GUI_CONTROLLER_H
#define GUI_CONTROLLER_H

#define MIN_WIDTH 1000  ///< Minimum width for the window
#define MIN_HEIGHT 500 ///< Minimum height for the window

#include <QWidget>
#include <QTableWidget>
#include "../Service/service.h"
#include "../ShoppingCart/shoppingCart.h"
#include <QStackedWidget>
#include <QMainWindow>
#include <QListWidget>

/// Dialog for filtering products based on various criteria
/// This dialog allows the user to filter products by price, name, or producer.
class FilterProductDialog final : public QWidget {
	Q_OBJECT
public:
	/// Constructor for FilterProductDialog
	/// :param parent: optional parent widget, default is nullptr
	explicit FilterProductDialog(QWidget *parent = nullptr);

signals:
	/// Signal emitted when the price filter action is triggered
	/// :param filter: the filter criteria for price (e.g., a price range)
	void priceFilterAction(QString filter);

	/// Signal emitted when the name filter action is triggered
	/// :param filter: the filter criteria for product name
	void nameFilterAction(QString filter);

	/// Signal emitted when the producer filter action is triggered
	/// :param filter: the filter criteria for the producer name
	void producerFilterAction(QString filter);
};

/// Dialog for changing product information
/// This dialog allows the user to change the details of a product.
class ChangeProductDialog final : public QWidget {
	Q_OBJECT
public:
	/// Constructor for ChangeProductDialog
	/// :param parent: optional parent widget, default is nullptr
	explicit ChangeProductDialog(QWidget *parent = nullptr);

signals:
	/// Signal emitted when the product data is entered
	/// :param name: the name of the product
	/// :param producer: the producer of the product
	/// :param price: the price of the product
	/// :param type: the type/category of the product
	void changeProductDataEntered(QString name, QString producer, QString price, QString type);
};

/// Dialog for adding a new product
/// This dialog allows the user to add a new product to the system.
class AddProductDialog final : public QWidget {
	Q_OBJECT
public:
	/// Constructor for AddProductDialog
	/// :param parent: optional parent widget, default is nullptr
	explicit AddProductDialog(QWidget *parent = nullptr);

signals:
	/// Signal emitted when the product data is entered
	/// :param name: the name of the product
	/// :param producer: the producer of the product
	/// :param price: the price of the product
	/// :param type: the type/category of the product
	void productDataEntered(QString name, QString producer, QString price, QString type);
};

/// Admin screen for managing products
/// This screen is used by the admin to view, add, change, or remove products from the list.
class adminScreen final : public QWidget {
	Q_OBJECT
private:
	Service* service;					 ///< Pointer to the Service object for managing the product data
	QListWidget* list;				 ///< Pointer to the table widget displaying the products
	Product* selected_product = nullptr; ///< Pointer to the selected product for editing

private slots:
	/// Slot for handling cell click events in the table
	/// :param row: the row of the clicked cell
	/// :param column: the column of the clicked cell
	void onListCellClicked(QListWidgetItem* item);

	/// Slot for handling Add button click event
	void onAddButtonClicked();

	/// Slot for handling Remove button click event
	void onRemoveButtonClicked();

	/// Slot for handling Undo button click event
	void onUndoButtonClicked();

	/// Slot for adding a product from the dialog
	/// :param name: name of the product to be added
	/// :param producer: producer of the product
	/// :param price: price of the product
	/// :param type: type/category of the product
	void addProductFromDialog(const QString& name, const QString& producer, const QString& price, const QString& type);

	/// Slot for handling Change button click event
	void onChangeButtonClicked();

	/// Slot for changing the product information from the dialog
	/// :param name: new name of the product
	/// :param producer: new producer of the product
	/// :param price: new price of the product
	/// :param type: new type/category of the product
	void changeProductFromDialog(const QString& name, const QString& producer, const QString& price, const QString& type);

	/// Slot triggered when the Filter button is clicked
	/// Opens the filter dialog or initiates a filter operation
	void onFilterButtonClicked();

	/// Slot triggered when a price filter is applied
	/// :param name: the price filter criteria (e.g., max price, range)
	void onPriceFilterAction(const QString& filter);

	/// Slot triggered when a name filter is applied
	/// :param name: the name filter criteria (e.g., partial or full product name)
	void onNameFilterAction(const QString& filter);

	/// Slot triggered when a producer filter is applied
	/// :param name: the producer filter criteria (e.g., producer name or keyword)
	void onProducerFilterAction(const QString& filter);

protected:

	/// Populates the table with product data
	/// :param products: list of products to be displayed in the table
	void populateTable(const List<Product>& products);

	/// Loads the layout for the admin screen
	void loadLayout();

	/// Loads and returns a table widget
	/// :return: a pointer to the QTableWidget used for displaying products
	QListWidget* loadTabel();

	/// Displays an error message
	/// :param message: the error message to be displayed
	void showError(const QString& message);

	/// Displays a success message
	/// :param message: the success message to be displayed
	void showSuccess(const QString& message);

public:
	/// Constructor for adminScreen
	/// :param parent_widget: optional parent widget, default is nullptr
	/// :param service: pointer to the Service object for managing products
	explicit adminScreen(QWidget *parent_widget, Service* service);

	/// Destructor for adminScreen
	~adminScreen() override = default;
};

/// Welcome screen that presents the option to go to the admin screen
/// This screen provides the entry point for navigating to the admin screen.
class welcomeScreen final : public QWidget {
	Q_OBJECT
signals:
	/// Signal emitted when the admin button is clicked
	void adminClicked();
	void userClicked();

protected:
	/// Loads the layout for the welcome screen
	void loadLayout();

public:
	/// Constructor for welcomeScreen
	/// :param parent_widget: optional parent widget, default is nullptr
	explicit welcomeScreen(QWidget* parent_widget);

	/// Destructor for welcomeScreen
	~welcomeScreen() override = default;
};

/// Screen for user interactions in the application
/// This screen allows users to browse products, manage a shopping cart, and perform related actions.
class userScreen final : public QWidget {
	Q_OBJECT
private slots:
	/// Adds a product to the shopping cart table
	/// :param row: the row index of the selected product in the display table
	void addToShoppingCartTable(int row);

	/// Removes a product from the shopping cart table
	/// :param row: the row index of the product in the shopping cart table to be removed
	void removeFromShoppingCartTable(int row);

	/// Triggered when the search text is changed by the user
	/// :param text: the current input text used for filtering products
	void onSearchTextChanged(const QString& text);

	/// Exports the contents of the shopping cart
	/// This might save the cart to a file or another format depending on implementation
	void exportShoppingCart();

	/// Generates a shopping cart with random products
	/// Typically used for demonstration or testing purposes
	void generateShoppingCart();

public:
	QTableWidget* displayTable;			///< Table displaying all available products
	List<Product> displayItems;			///< List of currently displayed products (after filtering)
	Service* service;					///< Pointer to the service managing product logic and data
	ShoppingCart shoppingCart;			///< Object representing the user's current shopping cart
	QTableWidget* shoppingCartTable;	///< Table displaying the contents of the shopping cart

	/// Loads the layout of the user screen, including tables and buttons
	void loadLayout();

	/// Populates a given table widget with a list of products
	/// :param products: list of products to be displayed
	/// :param table: pointer to the QTableWidget to populate
	static void populateTable(const List<Product>& products, QTableWidget* table);

	/// Displays an error message to the user
	/// :param message: the error message string to be shown
	void showError(const QString& message);

	/// Displays a success message to the user
	/// :param message: the success message string to be shown
	void showSuccess(const QString& message);

	/// Constructor for userScreen
	/// :param parent_widget: optional parent widget, default is nullptr
	/// :param service: pointer to the service object for product and cart operations
	explicit userScreen(QWidget* parent_widget, Service* service);
};


/// Main controller for the GUI application
/// This is the main widget that initializes the application and controls the navigation.
class guiController final : public QMainWindow {
	Q_OBJECT
private:
	Service service; ///< Service object for managing the product data
	QStackedWidget* stackedWidget;
	userScreen* myUser;

private slots:
	/// Slot for handling the admin & user button press
	void adminButtonPressed();
	void userButtonPressed();

public:
	/// Friend class declaration for welcomeScreen
	friend class adminScreen;
	friend class userScreen;

	/// Constructor for guiController
	/// :param fileName: the file name for loading data
	explicit guiController(const char* fileName);

	/// Starts the application
	void startApplication();
};

#endif //GUI_CONTROLLER_H
