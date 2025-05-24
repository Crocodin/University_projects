#ifndef COS_CURD_GUI_HPP
#define COS_CURD_GUI_HPP

#include <QWidget>
#include <QListWidget>
#include <QHBoxLayout>
#include <QPushButton>
#include <QVBoxLayout>
#include "../../ShoppingCart/shoppingCart.h"
#include "observer.hpp"
#include "../guiController.h"

class CosCurd_GUI final : public QWidget, private Observer {
	Q_OBJECT
private:
	ShoppingCart& shoppingCart;
	QListWidget* listWidget;
public:
	explicit CosCurd_GUI(ShoppingCart& cart, QWidget* parent)
		: QWidget(nullptr), shoppingCart(cart) {
		auto layout = new QVBoxLayout(this);
		listWidget = new QListWidget();
		this->populateList();
		layout->addWidget(listWidget);
		layout->setAlignment(Qt::AlignCenter);

		auto hbox = new QHBoxLayout();
		auto clearButton = new QPushButton("Clear");

		connect(clearButton, &QPushButton::clicked, [parent]() {
			vector& auxList = ((userScreen*)parent)->shoppingCart.getAllProducts();
			/// delets all form the shopping cart
			try { auxList.erase(auxList.begin(), auxList.end()); }
			catch (err::OutOfRange& e) { ((userScreen*)parent)->showError("Can't delete empty cart"); }
			((userScreen*)parent)->populateTable(auxList, ((userScreen*)parent)->shoppingCartTable);
			balance_ = 0;
			((userScreen*)parent)->shoppingCart.notifyObservers();
		});

		auto generateButton = new QPushButton("Generate");

		connect(generateButton, &QPushButton::clicked, [parent]() {
			((userScreen*)parent)->generateShoppingCart();
		});
		hbox->addWidget(clearButton);
		hbox->addWidget(generateButton);
		layout->addLayout(hbox);

		this->setLayout(layout);
		this->setWindowTitle("Shopping cart");

		shoppingCart.addObserver(this);
		this->setAttribute(Qt::WA_DeleteOnClose);
	}

	void populateList() {
		listWidget->clear();
		for (const auto& prod : shoppingCart.getAllProducts()) {
			QString text = QString::fromStdString(prod.getName()) + " - " + QString::fromStdString(prod.getProducer());
			listWidget->addItem(text);
		}
	}

	void update() override {
		this->populateList();
	}

	~CosCurd_GUI() override {
		shoppingCart.removeObserver(this);
	}
};

#endif //COS_CURD_GUI_HPP
