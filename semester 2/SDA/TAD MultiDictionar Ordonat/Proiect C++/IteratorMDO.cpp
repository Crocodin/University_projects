#include "IteratorMDO.h"

#include <stdexcept>

#include "MDO.h"

IteratorMDO::IteratorMDO(const MDO& d) : dict(d){
	this->currentNode = this->dict.root;
}

void IteratorMDO::prim(){
	if (dict.root == nullptr) return;
	currentNode = dict.root;
	nodeVectorIndex = 0;

	while (currentNode != nullptr) {
		nodes.push(currentNode);
		currentNode = currentNode->left;
	}

	if (!nodes.empty()) {
		currentNode = nodes.top();
		nodes.pop();
	}
}

void IteratorMDO::urmator(){
	if (!this->valid()) throw std::invalid_argument("IteratorMDO is invalid");
	/// first case, we didnt finish the values in the nodeVector
	if (this->nodeVectorIndex < this->currentNode->valori.size() - 1) {
		this->nodeVectorIndex++;
		return;
	}
	if (nodes.empty()) {
		this->currentNode = nullptr;		///< is invalid
		return;
	}
	currentNode = nodes.top(); nodes.pop();
	nodeVectorIndex = 0;

	MDO::Node* tmp = currentNode->right;
	while (tmp != nullptr) {
		nodes.push(tmp);
		tmp = tmp->left;
	}
}

bool IteratorMDO::valid() const{
	return currentNode != nullptr;
}

TElem IteratorMDO::element() const{
	if (!this->valid()) throw std::out_of_range("IteratorMDO::element");
	TValoare v = this->currentNode->valori[this->nodeVectorIndex];
	TCheie c = this->currentNode->cheie;
	return {c, v};
}


