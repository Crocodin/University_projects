
#include "CP.h"
#include <exception>
#include <iostream>
#include <ostream>
#include <stdio.h>

using namespace std;


CP::CP(const Relatie r) : relatie(r) { }


void CP::adauga(TElem e, TPrioritate p) {
	Node* newNode = new Node();
	newNode->info = { e, p };
	newNode->next = nullptr;
	newNode->prev = this->tail;

	if (this->head == nullptr) {
		this->head = this->tail = newNode;
	}
	else {
		this->tail->next = newNode;
		this->tail = newNode;
	}
}

//arunca exceptie daca coada e vida
Element CP::element() const {
	if (this->head == nullptr) throw exception();
	Node* temp = this->head,* current = temp;
	while (current != nullptr) {
		if (this->relatie(current->info.second, temp->info.second))
			temp = current;
		current = current->next;
	}
	return pair <TElem, TPrioritate>  (temp->info);       // copy constructor
}

Element CP::sterge() {
	if (this->head == nullptr) throw exception();
	Node* temp = this->head,* current = temp;
	while (current != nullptr) {
		if (this->relatie(current->info.second, temp->info.second))
			temp = current;
		current = current->next;
	}
	/// now we connect the otthers
	Element aux = temp->info;
	if (temp == this->head) {
		if (this->tail == this->head)
			this->head = this->tail = nullptr;
		else {
			this->head = this->head->next;
			this->head->prev = nullptr;
		}
	} else {
		if (temp == this->tail) {
			this->tail = this->tail->prev;
			this->tail->next = nullptr;
		} else {
			temp->prev->next = temp->next;
			temp->next->prev = temp->prev;
		}
	}
	delete temp;
	return pair <TElem, TPrioritate>  (aux);
}

bool CP::vida() const {
	return this->head == nullptr;
}


CP::~CP() {
	while (this->head != nullptr) {
		const Node* temp = this->head;
		this->head = this->head->next;
		delete temp;
	}
};

