#include "repo.h"
#include "../Errors/errors.hpp"

#include <algorithm>
#include <fstream>

void Repository::add(const Product& p) {
	if (this->isIn(p)) throw err::InvalidArgument("Product already exists");
	this->products.push_back(p);
}

uint Repository::getIndex(const Product& p) const {
	int index = 0;
	for (const auto& it : this->products) {
		if (it == p) return index;
		index++;
	}
	throw err::LogicError("No such product :: can't find index");
}

void Repository::remove(const Product & p) {
	const uint index = getIndex(p);
	this->products.erase(this->products.begin() + index);
}

bool Repository::isIn(const Product& p) const noexcept {
	return std::ranges::find(this->products, p) != this->products.end();
}
Product& Repository::find(const std::string& name, const std::string& producer) const {
	auto it = std::ranges::find_if(products,
	[&](const Product& product) {
		return product.getName() == name && product.getProducer() == producer;
	});
	if (it != products.end()) {
		return *it;
	}
	throw err::LogicError("No such product :: can't find product");
}

uint Repository::size() const noexcept {
	return this->products.size();
}

vector& Repository::getAllProducts() noexcept {
	return this->products;
}

void Repository::forceAdd(const Product& p) noexcept {
	this->products.push_back(p);
}

void RepositoryFile::lodeFromFile() {
	std::ifstream file(this->fileName);
	while (!file.eof()) {
		string name, type, price, producer;
		std::getline(file, name, ',');
		std::getline(file, type, ',');
		std::getline(file, price, ',');
		std::getline(file, producer, '\n');
		if (!producer.empty() && producer.back() == '\r') {
			producer.pop_back();
		}
		if (name.empty() or type.empty() or price.empty() or producer.empty())
			continue;
		this->add(Product(name, type, std::stoi(price), producer));
	}
	file.close();
}

void RepositoryFile::lodeToFile() {
	std::ofstream file(this->fileName);
	for (const auto& product : this->getAllProducts())
		file << product.getName() << ',' << product.getType() << ',' << product.getPrice() << ',' << product.getProducer() << '\n';
	file.close();
}

