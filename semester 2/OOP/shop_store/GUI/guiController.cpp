#include "guiController.h"
#include <QHBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QMessageBox>
#include <QFormLayout>
#include <QLineEdit>
#include <fstream>
#include <random>
#include <QFile>
#include <QFontDatabase>
#include <QHeaderView>
#include <QListWidget>
#include "Cos/cos_curd_gui.hpp"
#include "Cos/cos_read_only_gui.hpp"

FilterProductDialog::FilterProductDialog(QWidget *parent) : QWidget(parent) {
	this->setWindowTitle("Filter Product");

	auto label = new QLabel("Filter via", this);
	auto lineEdit = new QLineEdit(this);
	label->setAlignment(Qt::AlignCenter);

	auto priceButton = new QPushButton("Price", this);
	connect(priceButton, &QPushButton::clicked, this, [lineEdit, this]() {
		emit priceFilterAction(lineEdit->text());
		this->close();  // close after emitting
		this->deleteLater();
	});

	auto nameButton = new QPushButton("Name", this);
	connect(nameButton, &QPushButton::clicked, this, [lineEdit, this]() {
		emit nameFilterAction(lineEdit->text());
		this->close();  // close after emitting
		this->deleteLater();
	});

	auto producerButton = new QPushButton("Producer", this);
	connect(producerButton, &QPushButton::clicked, this, [lineEdit, this]() {
		emit producerFilterAction(lineEdit->text());
		this->close();  // close after emitting
		this->deleteLater();
	});

	auto layout = new QFormLayout(this);
	layout->addWidget(lineEdit);
	layout->addWidget(label);
	layout->addWidget(priceButton);
	layout->addWidget(nameButton);
	layout->addWidget(producerButton);

}

ChangeProductDialog::ChangeProductDialog(QWidget *parent) : QWidget(parent) {
	this->setWindowTitle("Change Product");

	auto nameEdit = new QLineEdit();
	auto producerEdit = new QLineEdit();
	auto priceEdit = new QLineEdit();
	auto typeEdit = new QLineEdit();

	auto layout = new QFormLayout(this);
	layout->addRow("Name:", nameEdit);
	layout->addRow("Producer:", producerEdit);
	layout->addRow("Price:", priceEdit);
	layout->addRow("Type:", typeEdit);

	auto submitButton = new QPushButton("Change");
	layout->addWidget(submitButton);

	connect(submitButton, &QPushButton::clicked, this, [this, nameEdit, producerEdit, priceEdit, typeEdit]() {
		emit changeProductDataEntered(
			nameEdit->text(),
			producerEdit->text(),
			priceEdit->text(),
			typeEdit->text()
		);
		this->close();  // close after emitting
		this->deleteLater();
	});

}

AddProductDialog::AddProductDialog(QWidget *parent) : QWidget(parent) {
	this->setWindowTitle("Add Product");

	auto nameEdit = new QLineEdit();
	auto producerEdit = new QLineEdit();
	auto priceEdit = new QLineEdit();
	auto typeEdit = new QLineEdit();

	auto layout = new QFormLayout(this);
	layout->addRow("Name:", nameEdit);
	layout->addRow("Producer:", producerEdit);
	layout->addRow("Price:", priceEdit);
	layout->addRow("Type:", typeEdit);

	auto submitButton = new QPushButton("Add");
	layout->addWidget(submitButton);

	connect(submitButton, &QPushButton::clicked, this, [this, nameEdit, producerEdit, priceEdit, typeEdit]() {
		emit productDataEntered(
			nameEdit->text(),
			producerEdit->text(),
			priceEdit->text(),
			typeEdit->text()
		);
		this->close();  // close after emitting
		this->deleteLater();
	});
}

void welcomeScreen::loadLayout() {
	auto mainLayout = new QVBoxLayout();

	auto topLayout = new QHBoxLayout();
	auto welcomeTitle = new QLabel("Welcome to ShopStore");
	welcomeTitle->setObjectName("welcomeTitle");
	welcomeTitle->setAlignment(Qt::AlignCenter);
	topLayout->addWidget(welcomeTitle);

	auto bottomLayout = new QHBoxLayout();
	auto userButton = new QPushButton("USER");
	connect(userButton, &QPushButton::clicked, this, &welcomeScreen::userClicked);
	bottomLayout->addWidget(userButton);
	auto adminButton = new QPushButton("ADMIN");
	bottomLayout->addWidget(adminButton);
	connect(adminButton, &QPushButton::clicked, this, &welcomeScreen::adminClicked);

	mainLayout->addLayout(topLayout);
	mainLayout->addLayout(bottomLayout);
	this->setLayout(mainLayout);
}

welcomeScreen::welcomeScreen(QWidget *parent_widget) : QWidget(parent_widget) {
	this->setGeometry(0, 0, parent_widget->width(), parent_widget->height());
	this->loadLayout();
}

adminScreen::adminScreen(QWidget *parent_widget, Service* service)
: QWidget(parent_widget), service(service){
	this->setGeometry(0, 0, parent_widget->width(), parent_widget->height());
	QFile adminScreenFile("../GUI/adminScreen.qss");
	if (adminScreenFile.open(QFile::ReadOnly)) {
		QString style = QLatin1String(adminScreenFile.readAll());
		this->setStyleSheet(style);
	} else { qDebug() << "Failed to load stylesheet:" << adminScreenFile.errorString(); }
	this->loadLayout();
}

void adminScreen::onListCellClicked(const QModelIndex &index) {
	int row = index.row();
	this->selected_product = &service->getAllProducts()[row];
	qDebug() << "Row" << row;
}

void adminScreen::onUndoButtonClicked() {
	qDebug() << "Undo";
	try {
		this->service->undo();
		this->populateTable(this->service->getAllProducts());
	}
	catch (const err::OutOfRange& e) {
		qDebug() << e.what();
		this->showError("Can't go back...");
	}
}

void adminScreen::onAddButtonClicked() {
	auto dialog = new AddProductDialog();
	connect(dialog, &AddProductDialog::productDataEntered, this, &adminScreen::addProductFromDialog);
	dialog->setAttribute(Qt::WA_DeleteOnClose);
	dialog->show();
}

void adminScreen::onChangeButtonClicked() {
	if (selected_product == nullptr) {
		qDebug() << "No product selected";
		this->showError("No product selected");
		return;
	}

	auto dialog = new ChangeProductDialog();
	connect(dialog, &ChangeProductDialog::changeProductDataEntered, this, &adminScreen::changeProductFromDialog);
	dialog->setAttribute(Qt::WA_DeleteOnClose);
	dialog->show();
}

void adminScreen::onFilterButtonClicked() {
	auto dialog = new FilterProductDialog();
	connect(dialog, &FilterProductDialog::producerFilterAction, this, &adminScreen::onPriceFilterAction);
	connect(dialog, &FilterProductDialog::nameFilterAction, this, &adminScreen::onNameFilterAction);
	connect(dialog, &FilterProductDialog::producerFilterAction, this, &adminScreen::onProducerFilterAction);
	dialog->setAttribute(Qt::WA_DeleteOnClose);
	dialog->show();
}

void adminScreen::onPriceFilterAction(const QString& filter) {
	int price = filter.toInt();
	this->service->removeProductsFunction(Product::priceComparison, &price);
	this->populateTable(this->service->getAllProducts());
}

void adminScreen::onNameFilterAction(const QString& filter) {
	string name = filter.toStdString();
	this->service->removeProductsFunction(Product::nameComparison, &name);
	this->populateTable(this->service->getAllProducts());
}

void adminScreen::onProducerFilterAction(const QString& filter) {
	string producer = filter.toStdString();
	this->service->removeProductsFunction(Product::producerComparison, &producer);
	this->populateTable(this->service->getAllProducts());
}

void adminScreen::changeProductFromDialog(const QString &name, const QString &producer, const QString &price, const QString &type) {
	string name_, producer_, type_; int price_;
	if (name == "") name_ = this->selected_product->getName();
	else name_ = name.toStdString();

	if (producer == "") producer_ = this->selected_product->getProducer();
	else producer_ = producer.toStdString();

	if (type == "") type_ = this->selected_product->getType();
	else type_ = type.toStdString();

	if (price == "") price_ = this->selected_product->getPrice();
	else price_ = price.toInt();

	this->service->changeProduct(*this->selected_product, name_, type_, price_, producer_);
	this->populateTable(this->service->getAllProducts());
}

void adminScreen::addProductFromDialog(const QString& name, const QString& producer, const QString& price, const QString& type) {
	try {
		this->service->addProduct(
			name.toStdString(),
			type.toStdString(),
			price.toInt(),
			producer.toStdString()
		);
		this->showSuccess("Product added");
		this->populateTable(this->service->getAllProducts());
	}
	catch(const err::InvalidArgument& e) {
		this->showError(e.what());
	}
}

void adminScreen::onRemoveButtonClicked() {
	if (selected_product == nullptr) {
		qDebug() << "No product selected";
		this->showError("No product selected");
		return;
	}
	this->service->removeProduct(this->selected_product->getName(), this->selected_product->getProducer());
	this->populateTable(this->service->getAllProducts());
	this->selected_product = nullptr;
}

void adminScreen::populateTable(const List<Product>& products) {
	(void) products;
	auto* model = dynamic_cast<ProductListModel*>(list->model());
	if (model) {
		model->refresh();
	}
}

void adminScreen::showError(const QString &message) {
	QMessageBox::critical(this, "Error", message);
}

void adminScreen::showSuccess(const QString &message) {
	QMessageBox::information(this, "Success", message);
}

QListView *adminScreen::loadTabel() {
	auto *list = new ProductListModel(*this->service, this);

	auto* view = new QListView(this);
	view->setModel(list);
	this->list = view;

	this->populateTable(this->service->getAllProducts());

	connect(view, &QListView::clicked, this, &adminScreen::onListCellClicked);
	return view;
}

void adminScreen::loadLayout() {
	auto mainLayout = new QHBoxLayout();

	auto list = this->loadTabel();
	mainLayout->addWidget(list);

	auto secondLayout = new QVBoxLayout();

	auto addItemButton = new QPushButton("Add");
	connect(addItemButton, &QPushButton::clicked, this, &adminScreen::onAddButtonClicked);
	secondLayout->addWidget(addItemButton);
	auto removeItemButton = new QPushButton("Remove");
	connect(removeItemButton, &QPushButton::clicked, this, &adminScreen::onRemoveButtonClicked);
	secondLayout->addWidget(removeItemButton);
	auto filterItemButton = new QPushButton("Filter");
	connect(filterItemButton, &QPushButton::clicked, this, &adminScreen::onFilterButtonClicked);
	secondLayout->addWidget(filterItemButton);
	auto changeItem = new QPushButton("Change Product");
	connect(changeItem, &QPushButton::clicked, this, &adminScreen::onChangeButtonClicked);
	secondLayout->addWidget(changeItem);

	auto userButtonLayout = new QHBoxLayout();
	auto undoButton = new QPushButton("Undo");
	connect(undoButton, &QPushButton::clicked, this, &adminScreen::onUndoButtonClicked);
	auto changeModeButton = new QPushButton("Change View");
	auto controller = qobject_cast<guiController*>(this->parent());
	connect(changeModeButton, &QPushButton::clicked, controller, &guiController::userButtonPressed);
	userButtonLayout->addWidget(undoButton);
	userButtonLayout->addWidget(changeModeButton);
	secondLayout->addLayout(userButtonLayout);

	mainLayout->addLayout(secondLayout);
	this->setLayout(mainLayout);
}

void guiController::adminButtonPressed() {
	this->stackedWidget->setCurrentIndex(1);
}

void guiController::userButtonPressed() {
	this->myUser->populateTable(this->myUser->service->getAllProducts(), this->myUser->displayTable);
	this->stackedWidget->setCurrentIndex(2);
}

guiController::guiController(const char* fileName) : service(fileName) {
	this->setObjectName("mainWindow");
	int fontId = QFontDatabase::addApplicationFont(":/Utilities/Agbalumo/Agbalumo-Regular.ttf");
	if (fontId == -1) { qDebug() << "Failed to load font"; } else {
		QStringList families = QFontDatabase::applicationFontFamilies(fontId);
		qDebug() << "Loaded font family:" << families;
	}

	fontId = QFontDatabase::addApplicationFont(":/Utilities/BebasNeue/BebasNeue-Regular.ttf");
	if (fontId == -1) { qDebug() << "Failed to load font"; } else {
		QStringList families = QFontDatabase::applicationFontFamilies(fontId);
		qDebug() << "Loaded font family:" << families;
	}

	fontId = QFontDatabase::addApplicationFont(":/Utilities/JetBrains_Mono/JetBrainsMono-VariableFont_wght.ttf");
	if (fontId == -1) { qDebug() << "Failed to load font"; } else {
		QStringList families = QFontDatabase::applicationFontFamilies(fontId);
		qDebug() << "Loaded font family:" << families;
	}

	QFile controllerFile("../GUI/controller.qss");
	if (controllerFile.open(QFile::ReadOnly)) {
		QString style = QLatin1String(controllerFile.readAll());
		this->setStyleSheet(style);
	} else { qDebug() << "Failed to load stylesheet:" << controllerFile.errorString(); }

	this->setMinimumSize(QSize(MIN_WIDTH, MIN_HEIGHT));
	this->setWindowTitle("Shop Store");
	this->stackedWidget =  new QStackedWidget(this);
	auto admin = new adminScreen(this, &this->service);
	this->myUser = new userScreen(this, &this->service);

	auto welcome = new welcomeScreen(this);
	QFile welcomeScreenFile("../GUI/welcomeScreen.qss");
	if (welcomeScreenFile.open(QFile::ReadOnly)) {
		QString style = QLatin1String(welcomeScreenFile.readAll());
		welcome->setStyleSheet(style);
	} else { qDebug() << "Failed to load stylesheet:" << welcomeScreenFile.errorString(); }

	connect(welcome, &welcomeScreen::adminClicked, this, &guiController::adminButtonPressed);
	connect(welcome, &welcomeScreen::userClicked, this, &guiController::userButtonPressed);

	this->stackedWidget->addWidget(welcome);		/// index 0
	this->stackedWidget->addWidget(admin);			/// index 1
	this->stackedWidget->addWidget(this->myUser);	/// index 2
	this->setCentralWidget(this->stackedWidget);
}

void guiController::startApplication() {
	this->stackedWidget->setCurrentIndex(0); /// welcomeScreen
}

void userScreen::populateTable(const List<Product>& products, QTableWidget* table) {
	// Clear all rows and content
	table->clearContents();     // Clears the cell items but keeps headers
	table->setRowCount(0);      // Resets the row count

	int actualRow = 0;

	for (int i = 0; i < (int) products.size(); ++i) {
		table->insertRow(actualRow); // Insert a new row

		auto nameItem = new QTableWidgetItem(QString::fromStdString(products[i].getName()));
		auto producerItem = new QTableWidgetItem(QString::fromStdString(products[i].getProducer()));
		auto priceItem = new QTableWidgetItem(QString::number(products[i].getPrice()));
		auto typeItem = new QTableWidgetItem(QString::fromStdString(products[i].getType()));

		nameItem->setFlags(nameItem->flags() & ~Qt::ItemIsEditable);
		producerItem->setFlags(producerItem->flags() & ~Qt::ItemIsEditable);
		priceItem->setFlags(priceItem->flags() & ~Qt::ItemIsEditable);
		typeItem->setFlags(typeItem->flags() & ~Qt::ItemIsEditable);

		table->setItem(i, 0, nameItem);
		table->setItem(i, 1, producerItem);
		table->setItem(i, 2, priceItem);
		table->setItem(i, 3, typeItem);

		++actualRow;
	}
}

void userScreen::showError(const QString &message) {
	QMessageBox::critical(this, "Error", message);
}

void userScreen::showSuccess(const QString &message) {
	QMessageBox::information(this, "Success", message);
}

void userScreen::addToShoppingCartTable(int row) {
	this->shoppingCart.addToShoppingCart(this->displayItems[row]);
	userScreen::populateTable(this->shoppingCart.getAllProducts(), this->shoppingCartTable);
	this->shoppingCart.notifyObservers();
}

void userScreen::removeFromShoppingCartTable(int row) {
	this->shoppingCart.removeFromShoppingCart(this->shoppingCart.getAllProducts()[row]);
	userScreen::populateTable(this->shoppingCart.getAllProducts(), this->shoppingCartTable);
	this->shoppingCart.notifyObservers();
}

void userScreen::onSearchTextChanged(const QString& text) {
	const List<Product>& allProducts = service->getAllProducts();
	try { this->displayItems.erase(this->displayItems.begin(), this->displayItems.end()); }
	catch (const err::OutOfRange& e) { qDebug() << e.what(); } /// this only happends if it empty, so it's ok to go on

	std::copy_if(allProducts.begin(), allProducts.end(), std::back_inserter(displayItems),
				 [&](const Product& p) {
					 return QString::fromStdString(p.getName()).contains(text, Qt::CaseInsensitive) or
				 	QString::fromStdString(p.getProducer()).contains(text, Qt::CaseInsensitive);
				 });

	populateTable(this->displayItems, this->displayTable);
}

void userScreen::generateShoppingCart() {
	auto generateSC = new QWidget();

	generateSC->setWindowTitle("Generate Shopping Cart");
	auto layout = new QFormLayout(generateSC);
	auto lineEdit = new QLineEdit();
	layout->addRow("Number of products", lineEdit);

	auto generateButton = new QPushButton("Generate");
	connect(generateButton, &QPushButton::clicked, generateSC, [lineEdit, generateSC, this]() {
		if (lineEdit->text().isEmpty()) {
			this->showError("No input!");
			generateSC->close();
			generateSC->deleteLater();
		}
		std::mt19937 mt{ std::random_device{}() };
		std::uniform_int_distribution<int> distrib{ 0, static_cast<int>(this->service->getAllProducts().size() - 1) };

		const vector& products = this->service->getAllProducts();
		for (int i = 0; i < lineEdit->text().toInt(); i++) {
			const int rndNr = distrib(mt);
			this->shoppingCart.addToShoppingCart(products[rndNr]);
		}
		userScreen::populateTable(this->shoppingCart.getAllProducts(), this->shoppingCartTable);
		this->shoppingCart.notifyObservers();
		generateSC->close();
		generateSC->deleteLater();
	});

	layout->addWidget(generateButton);
	generateSC->setAttribute(Qt::WA_DeleteOnClose);
	generateSC->show();
}

void userScreen::exportShoppingCart() {
	auto exportHTML = new QWidget();

	exportHTML->setWindowTitle("Export to HTML");
	auto layout = new QFormLayout(exportHTML);
	auto lineEdit = new QLineEdit();
	layout->addRow("File name", lineEdit);

	auto exportButton = new QPushButton("Export");
	connect(exportButton, &QPushButton::clicked, exportHTML, [exportHTML, lineEdit, this]() {
		std::ifstream templateIn("template.html"); /// GO TO THE CMAKELIST IF IT DOSEN'T WORK
		if (!templateIn.is_open()) {
			this->showError("Failed to open template file.");
			exportHTML->close();
			exportHTML->deleteLater();
			return;
		}

		string outputFile = "../Exports/" + lineEdit->text().toStdString() + ".html";

		std::ofstream out(outputFile);
		if (!out.is_open()) {
			/// CHECK THE outputFile PATH, IT IS RELATIV TO THE CMAKE-BUILD-DEBUG
			this->showError("Failed to open output file.");
			exportHTML->close();
			exportHTML->deleteLater();
			return;
		}

		std::string line;
		while (std::getline(templateIn, line)) {
			// Inject product rows where marker is
			if (line.find("<!-- PRODUCTS GO HERE -->") != std::string::npos) {
				for (const auto& product : this->shoppingCart.getAllProducts()) {
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
		string successExport = {"HTML exported to " + outputFile};
		this->showSuccess(QString::fromStdString(successExport));
		exportHTML->close();
		exportHTML->deleteLater();
	});
	layout->addWidget(exportButton);
	exportHTML->setLayout(layout);
	exportHTML->setAttribute(Qt::WA_DeleteOnClose);
	exportHTML->show();
}

void userScreen::loadLayout() {
	auto mainLayout = new QHBoxLayout();

	auto leftLayout = new QVBoxLayout();
	auto lineEdit = new QLineEdit(this);
	lineEdit->setEchoMode(QLineEdit::Normal);
	connect(lineEdit, &QLineEdit::textChanged, this, &userScreen::onSearchTextChanged);

	this->displayTable = new QTableWidget(this);
	this->displayTable->setColumnCount(4);
	this->displayTable->setHorizontalHeaderLabels({"Product", "Producer", "Price", "Tipe"});
	this->displayTable->setMaximumWidth(450);
	this->displayTable->setSelectionBehavior(QAbstractItemView::SelectRows);
	this->displayTable->setSelectionMode(QAbstractItemView::SingleSelection);
	connect(this->displayTable, &QTableWidget::cellDoubleClicked, this, &userScreen::addToShoppingCartTable);
	userScreen::populateTable(this->displayItems, this->displayTable);

	leftLayout->addWidget(lineEdit);
	leftLayout->addWidget(this->displayTable);

	this->shoppingCartTable = new QTableWidget(this);
	this->shoppingCartTable->setColumnCount(4);
	this->shoppingCartTable->setHorizontalHeaderLabels({"Product", "Producer", "Price", "Tipe"});
	this->shoppingCartTable->setMaximumWidth(450);
	this->shoppingCartTable->setSelectionBehavior(QAbstractItemView::SelectRows);
	this->shoppingCartTable->setSelectionMode(QAbstractItemView::SingleSelection);
	connect(this->shoppingCartTable, &QTableWidget::cellDoubleClicked, this, &userScreen::removeFromShoppingCartTable);
	userScreen::populateTable(this->shoppingCart.getAllProducts(), this->shoppingCartTable);

	auto clearButton = new QPushButton("Clear");
	connect(clearButton, &QPushButton::clicked, this, [this]() {
		vector& auxList = this->shoppingCart.getAllProducts();
		/// delets all form the shopping cart
		try { auxList.erase(auxList.begin(), auxList.end()); }
		catch (err::OutOfRange& e) { this->showError("Can't delete empty cart"); }
		this->populateTable(auxList, this->shoppingCartTable);
		balance_ = 0;
		this->shoppingCart.notifyObservers();
	});

	auto exportButton = new QPushButton("Export");
	connect(exportButton, &QPushButton::clicked, this, &userScreen::exportShoppingCart);
	auto changeModeButton = new QPushButton("Change View");
	auto controller = qobject_cast<guiController*>(this->parent());
	connect(changeModeButton, &QPushButton::clicked, controller, &guiController::adminButtonPressed);
	auto generateButton = new QPushButton("Generate");
	connect(generateButton, &QPushButton::clicked, this, &userScreen::generateShoppingCart);

	auto shoppWindowButton = new QPushButton("Shopping Window");
	auto shopShapesButton = new QPushButton("Shopping Shapes");

	connect(shoppWindowButton, &QPushButton::clicked, this, [this]() {
		auto window = new CosCurd_GUI(this->shoppingCart, this);
		window->show();
	});

	connect(shopShapesButton, &QPushButton::clicked, this, [this]() {
		auto window = new CosReadOnly_GUI(this->shoppingCart);
		window->show();
	});

	auto rightFLayout = new QHBoxLayout();
	rightFLayout->addWidget(clearButton);
	rightFLayout->addWidget(exportButton);

	auto rightSLayout = new QHBoxLayout();
	rightSLayout->addWidget(generateButton);
	rightSLayout->addWidget(changeModeButton);
	rightSLayout->addWidget(shoppWindowButton);
	rightSLayout->addWidget(shopShapesButton);

	auto rightLayout = new QVBoxLayout();
	rightLayout->addWidget(this->shoppingCartTable);
	rightLayout->addLayout(rightFLayout);
	rightLayout->addLayout(rightSLayout);

	mainLayout->addLayout(leftLayout);
	mainLayout->addLayout(rightLayout);
	this->setLayout(mainLayout);
}

userScreen::userScreen(QWidget *parent_widget, Service* service)
: QWidget(parent_widget), service(service) {
	QFile userScreenFile("../GUI/userScreen.qss");
	if (userScreenFile.open(QFile::ReadOnly)) {
		QString style = QLatin1String(userScreenFile.readAll());
		this->setStyleSheet(style);
	} else { qDebug() << "Failed to load stylesheet:" << userScreenFile.errorString(); }

	this->displayItems = this->service->getAllProducts();
	this->setGeometry(0, 0, parent_widget->width(), parent_widget->height());
	this->loadLayout();
}
