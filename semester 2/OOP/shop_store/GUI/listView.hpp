#ifndef LISTVIEW_H
#define LISTVIEW_H

#include <QAbstractListModel>
#include "../Service/service.h"

class ProductListModel final : public QAbstractListModel {
	Q_OBJECT

private:
	Service &service;

public:
	explicit ProductListModel(Service &srv, QObject *parent = nullptr)
		: QAbstractListModel(parent), service(srv) {}

	int rowCount(const QModelIndex &parent = QModelIndex()) const override {
		Q_UNUSED(parent);
		return service.getAllProducts().size();
	}

	QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override {
		if (!index.isValid() || index.row() >= (int) service.getAllProducts().size())
			return {};

		const auto &product = service.getAllProducts()[index.row()];

		if (role == Qt::DisplayRole) {
			return QString::fromStdString(
				product.getName() + " | " +
				product.getType() + " | " +
				std::to_string(product.getPrice()) + " | " +
				product.getProducer()
			);
		}

		return {};
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const override {
		if (role == Qt::DisplayRole && orientation == Qt::Vertical)
			return QString::number(section + 1);
		return {};
	}

	void refresh() {
		beginResetModel();
		endResetModel();
	}
};

#endif // LISTVIEW_H
