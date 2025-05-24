#ifndef COS_READ_ONLY_GUI_HPP
#define COS_READ_ONLY_GUI_HPP

#include <QWidget>
#include <QPainter>
#include "observer.hpp"
#include "../../ShoppingCart/shoppingCart.h"
#include <random>

/// Read-only GUI view for the shopping cart
/// This widget observes the ShoppingCart and graphically displays random shapes
/// representing each product, updating the view on changes. It does not allow user interaction.
class CosReadOnly_GUI final : public QWidget, private Observer {
	Q_OBJECT
private:
	ShoppingCart& shoppingCart;			 ///< Reference to the shopping cart being observed

public:
	/// Constructor for CosReadOnly_GUI
	/// Initializes the widget, registers as observer, and configures basic settings
	/// :param cart: reference to the shopping cart to observe
	/// :param parent: optional parent widget, default is nullptr
	explicit CosReadOnly_GUI(ShoppingCart& cart, QWidget* parent = nullptr)
		: QWidget(parent), shoppingCart(cart) {
		shoppingCart.addObserver(this);
		setWindowTitle("Cos Read Only");
		setMinimumSize(400, 300);
		this->setAttribute(Qt::WA_DeleteOnClose);
	}

	/// Called when the shopping cart is updated (Observer pattern)
	/// This triggers a repaint of the window to reflect the new cart state
	void update() override {
		repaint();
	}

	/// Destructor for CosReadOnly_GUI
	/// Automatically removes this widget from the shopping cart's observer list
	~CosReadOnly_GUI() override {
		shoppingCart.removeObserver(this);
	}

protected:
	/// Custom paint event handler that draws random shapes for each product in the cart
	/// Each shape is rendered in a random position with a random color and form
	/// :param ev: paint event (unused)
	void paintEvent(QPaintEvent* ev) override {
		(void) ev;
		QPainter painter(this);
		const uint itemCount = shoppingCart.getAllProducts().size();

		for (uint i = 0; i < itemCount; ++i) {
			constexpr int shapeSize = 30;
			int x = rand() % (width() - shapeSize);
			int y = rand() % (height() - shapeSize);

			// Pick a random shape
			int shapeType = rand() % 3; // 0: ellipse, 1: rect, 2: triangle

			QColor color(rand() % 256, rand() % 256, rand() % 256); // random color
			painter.setBrush(QBrush(color));

			switch (shapeType) {
				case 0:
					painter.drawEllipse(x, y, shapeSize, shapeSize);
					break;
				case 1:
					painter.drawRect(x, y, shapeSize, shapeSize);
					break;
				case 2: {
					QPolygon triangle;
					triangle << QPoint(x + shapeSize / 2, y)
							 << QPoint(x, y + shapeSize)
							 << QPoint(x + shapeSize, y + shapeSize);
					painter.drawPolygon(triangle);
					break;
				}
				default: ;
			}
		}
	}
};

#endif //COS_READ_ONLY_GUI_HPP
