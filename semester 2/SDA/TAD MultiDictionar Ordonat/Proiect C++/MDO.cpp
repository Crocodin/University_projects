#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>
#include <algorithm>

#include <exception>
using namespace std;

MDO::MDO(Relatie r) : cmp(r) {
	this->root = nullptr;
}


void MDO::adauga(TCheie c, TValoare v) {
	numberOfElements++;

	if (this->root == nullptr) {			/// we add the first element
		this->root = new Node(c, v);
		return;
	}

	Node *current = this->root, *parent = nullptr;
	do {
		if (current->cheie == c) {			/// we found the key in the tree
			current->valori.push_back(v);
			return;
		}
		parent = current;
		if (cmp(c, current->cheie))			/// true if c <= current.c
			current = current->left;
		else current = current->right;
	}
	while (current != nullptr);
	/// if we left the loop it means that the key is not in the tree
	current = new Node(c, v);
	if (cmp(current->cheie, parent->cheie))	/// true if current.c < parent.c
		parent->left = current;
	else parent->right = current;
}

vector<TValoare> MDO::cauta(TCheie c) const {
	if (this->root == nullptr) return {};
	Node *current = this->root;
	do {
		if (current->cheie == c)
			return current->valori;
		if (cmp(c, current->cheie))
			current = current->left;
		else current = current->right;
	}
	while (current != nullptr);
	return {};
}

bool MDO::sterge(TCheie c, TValoare v) {
	if (root == nullptr) return false;
	Node *current = this->root, *parent = nullptr;
	do {
		if (current->cheie == c) {
			auto it = std::find(current->valori.begin(), current->valori.end(), v);
			if (it != current->valori.end()) {
				current->valori.erase(it);
				numberOfElements--;
			}
			else return false;
			if (current->valori.empty()) {
				if (current->right == nullptr) {
					/// first case, we dont have a right tree so we know all the values are smaller then
					/// the parent.left and they are in order
					if (parent == nullptr) {		/// we are deleting the root
						this->root = current->left; /// current.left = Node* | nullprt
					}
					else parent->left = current->left;
					delete current;
					return true;
				}
				if (current->left == nullptr) {
					/// the second case, we dont have a left three so we know all the values are smaller
					/// then the paren.left, and thay are in order
					if (parent == nullptr) {		/// we are deleting the root
						this->root = current->right;/// current.right = Node* | nullprt
					}
					else parent->left = current->left;
					delete current;
					return true;
				}
				/// now, the last case, we need to delete a sub-tree, for this i will be taking the right side
				/// smallest key and replacing the head with it

				Node *minimum = current->right, *previous_min = nullptr;
				while (minimum->left != nullptr) {
					previous_min = minimum;
					minimum = minimum->left;
				}

				/// now we make the new conections
				minimum->left = current->left;		/// we know that min.left is nil and cur.left is smaller
				if (previous_min != nullptr) {		/// this means that min is NOT the head of the sub-tree
					previous_min->left = minimum->right;
				}
				minimum->right = current->right;

				/// first, are we deleting the root?
				if (parent == nullptr) {
					this->root = minimum;
				}
				else {
					if (cmp(c, parent->cheie))			/// true if c < parent.c
						parent->left = minimum;			/// because we are in the left sub-tree
					else parent->right = minimum;		/// because we are in the right sub-tree
				}
				delete current;
				return true;
			}
			return true;
		}
		parent = current;
		if (cmp(c, current->cheie))
			current = current->left;
		else current = current->right;
	}
	while (current != nullptr);
	return false;
}

int MDO::dim() const {
	return numberOfElements;
}

bool MDO::vid() const {
	return this->root == nullptr;
}

IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

MDO::~MDO() {
	/// todo recursvide delete
}
