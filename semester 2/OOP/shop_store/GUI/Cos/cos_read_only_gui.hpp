#ifndef COS_READ_ONLY_GUI_HPP
#define COS_READ_ONLY_GUI_HPP

#include <QWidget>
#include <QPainter>
#include "observer.hpp"
#include "../../ShoppingCart/shoppingCart.h"
#include <random>

class CosReadOnly_GUI final : public QWidget, private Observer {
	Q_OBJECT
private:
	ShoppingCart& shoppingCart;

public:
	explicit CosReadOnly_GUI(ShoppingCart& cart, QWidget* parent = nullptr)
		: QWidget(parent), shoppingCart(cart) {
		shoppingCart.addObserver(this);
		setWindowTitle("Cos Read Only");
		setMinimumSize(400, 300);
		this->setAttribute(Qt::WA_DeleteOnClose);
	}

	void update() override {
		repaint();
	}

	~CosReadOnly_GUI() override {
		shoppingCart.removeObserver(this);
	}

protected:
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
