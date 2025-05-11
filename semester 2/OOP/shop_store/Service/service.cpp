#include "service.h"
#include "../Domain/validator.h"
#include "../Errors/errors.hpp"

void Service::addProduct(const string& name , const string& type, const int& price, const string& producer) {
	if (const Validator validator; !validator.validate(name, type, price, producer))
		throw err::InvalidArgument("Invalid product declaration");
	Product p(name, type, price, producer);
	this->repo->add(p);
	this->undoActions.push_back(new UndoAdd(repo, p));
}

void Service::removeProduct(const string& name, const string& producer) {
	const Product& p = this->repo->find(name, producer);
	this->undoActions.push_back(new UndoRemove(repo, p));
	this->repo->remove(p);
}

vector& Service::getAllProducts() noexcept {
	return this->repo->getAllProducts();
}

void Service::changeProduct(Product& p, const string& name, const string& type, const int& price, const string& producer) noexcept {
	this->undoActions.push_back(new UndoChange(this->repo, p));
	if (!name.empty()) p.setName(name);
	if (!type.empty()) p.setType(type);
	if (price >= 0) p.setPrice(price);
	if (!producer.empty()) p.setProducer(producer);
}

void quickSort(vector& products, int low, int high, const auto compareFunction) {
	if (low < high) {
		auto& pivot = products[high];
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (compareFunction(products[j], pivot)) {
				std::swap(products[++i], products[j]);
			}
		}
		std::swap(products[i + 1], products[high]);

		const int partitionIndex = i + 1;
		quickSort(products, low, partitionIndex - 1, compareFunction);
		quickSort(products, partitionIndex + 1, high, compareFunction);
	}
}

void Service::filterProductsFunction(const cmpFunct compareFunction, vector& products) {
	quickSort(products, 0, static_cast<int>(products.size()) - 1, compareFunction);
}

void Service::removeProductsFunction(const rmFunct removeFunction, void* param_2) {
	const vector& aux_vector = this->repo->getAllProducts();

	for (const auto* undo: this->undoActions)
		delete undo;
	this->undoActions.erase(this->undoActions.begin(), this->undoActions.end());

	for (const Product *it = aux_vector.begin(); it != aux_vector.end(); )
		if (removeFunction(*it, param_2)) this->repo->remove(*it);
		else ++it;
}

void Service::undo() {
	UndoAction* undoItem = this->undoActions.pop();
	undoItem->doUndo();
	delete undoItem;
}
