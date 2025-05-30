#include "MDO.h"
#include "IteratorMDO.h"
#include <vector>
#include <algorithm>
using namespace std;

/// theta (1)
MDO::MDO(Relatie r) : cmp(r) {
	this->root = nullptr;
}

/// O(h)
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

/// O(h)
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

/// theta(max(h, m) { where m is the number of elements with that key }
bool MDO::sterge(TCheie c, TValoare v) {
	if (root == nullptr) return false;
	Node *current = this->root, *parent = nullptr;
	do {
		if (current->cheie == c) {
			/// this operation, i'll have to gues, it O(m) { where m is the number of elements with that key }
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

/// theta (1)
int MDO::dim() const {
	return numberOfElements;
}

/// theta (1)
bool MDO::vid() const {
	return this->root == nullptr;
}

/// theta (1)
IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

/// theta(h)
void MDO::deleteRec(Node* node) {
	if (node == nullptr)
		return;
	deleteRec(node->left);
	deleteRec(node->right);
	delete node;
}

/// theta(h)
MDO::~MDO() {
	deleteRec(this->root);
}

TValoare maxTValoare(TValoare a, TValoare b) {
	if (a > b) return a;
	return b;
}

TValoare minTValoare(TValoare a, TValoare b) {
	if (a < b) return a;
	return b;
}

/*
 * subprogram difRec(d, curent, cmp) este:
 *		{ d este un MDO }
 *		{ cmp este o functie de comparare }
 *		{ curent ^Nod }
 *		daca curent = NIL:
 *			daca cmp = min atunci:
 *				difRec <- -inf
 *			altfel: difRec <- +inf
 *		sf.daca
 *
 *		loacl <- [curent].valori[0]
 *		pentru nod in valori:
 *			loacl <- cmp(local, nod)
 *
 *		stanga <- difRec([curent].stanga, cmp)
 *		dreapta <- difRec([curent].dreapta, cmp)
 *		difRec <- cmp(local, cmp(stanga, dreapta))
 *	sf.subprogram
 *
 * subprogram diferentaValoareMaxMin(d, cmp) este:
 *		{ d este un MDO }
 *		daca d.vid() atunci:
 *			diferentaValoareMaxMin <- -1;
 *		sf. daca
 *
 *		min <- difRec(d, d.root, min)
 *		max <- difRec(d, d.root, max)
 *		diferentaValoareMaxMin <- (max - min)
 * sf.subprogram
 */

/// theta (n)
TValoare MDO::searchCMP(MDO::Node* current, const searchF cmp) const{
	if (current == nullptr)
		return cmp == minTValoare ? 10000 : -10000;

	TValoare local = current->valori[0];
	for (int i = 1; i < current->valori.size(); ++i) {
		local = cmp(local, current->valori[i]);
	}

	TValoare left = searchCMP(current->left, cmp);
	TValoare right = searchCMP(current->right, cmp);

	return cmp(local, cmp(left, right));
}

int MDO::maxMinDiff() const {
	if (this->root == nullptr) return -1;
	return searchCMP(this->root, maxTValoare) - searchCMP(this->root, minTValoare);
}