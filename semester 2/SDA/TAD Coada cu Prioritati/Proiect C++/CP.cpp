#include "CP.h"
#include <exception>
#include <iostream>

using namespace std;

/// theta(1)
CP::CP(const Relatie r) : relatie(r) { }

/// theta(1)
CP::Node* CP::XOR(const Node* a, const Node* b) {
	return (Node*)((uintptr_t)a xor (uintptr_t)b);
	/// 'C-style cast is used', just... let me be... it looks better
}

/// theta(1)
void CP::adauga(TElem e, TPrioritate p) {
	Node* newNode = new Node();
	newNode->info = { e, p };
	newNode->xor_head = nullptr;

	/// the linked list is empty
	if (this->head == nullptr) {
		this->head = this->tail = newNode;
	}
	else {
		/// "last" xor ( "last" xor "next") = "next"
		newNode->xor_head = XOR(this->tail, nullptr);
		this->tail->xor_head = XOR(this->tail->xor_head, newNode);
		/// what heppends here? as i am The 'one and only' Codirn (i ate dirt when i was younger, i'm not joking...
		/// i have a picture), the best (and easiest) way to explain this code and this add concept is:
		/// tail is not null. so the xor_head is (last, next) pair. but as we work with the tail
		/// we dont have a next. so the pair is actualy (last, NULL). now we add the "next" (i.e. newNode)
		/// so now the pair is (last, newNode). the porblem that i'm realizeing as i'm writhing this is
		/// that the newNode dosent have a last... so i'll add it now
		this->tail = newNode;
	}
}

/// theta(n)
Element CP::element() const {
	if (this->head == nullptr) throw exception();
	Node* temp = this->head,* current = temp,* last = nullptr;
	while (current != nullptr) {
		if (this->relatie(current->info.second, temp->info.second))
			temp = current;
		Node* aux = current;
		current = XOR(last, current->xor_head);
		last = aux;
	}
	return pair <TElem, TPrioritate>  (temp->info);       // copy constructor
}

/// theta(n)
Element CP::sterge() {
	if (this->head == nullptr) throw exception();
	Node* temp = this->head,* tempLast = nullptr,* current = temp,* last = nullptr;
	while (current != nullptr) {
		if (this->relatie(current->info.second, temp->info.second)) {
			tempLast = last;
			temp = current;
		}
		Node* aux = current;
		current = XOR(last, current->xor_head);
		last = aux;
	}
	/// now we connect the otthers
	Element aux = temp->info; /// to be returned
	if (temp == this->head) {
		/// we eliminate the first node
		if (this->tail == this->head)
			/// we onli have one node in the list
			this->head = this->tail = nullptr;
		else {
			/// removing head from the list
			Node* next = XOR(nullptr, this->head->xor_head); /// the next element from the head
			next->xor_head = XOR(temp, next->xor_head); /// removes the "last" from the xor comdo, now it will be (null ^ "next")
			this->head = next; /// we set the head next to be the head
		}
	} else {
		if (temp == this->tail) { /// we need to delete the last element
			Node* prev = XOR(nullptr, this->tail->xor_head); /// this will be the new tail, !!! i know last already exist, STFU !!!
			prev->xor_head = XOR(temp, prev->xor_head); /// remove the tail form the combo, new combo (last, NULL)
			this->tail = prev;
		} else { /// implemeting this will be pain...
			/// this is the time line:    superLast -> last -> temp -> next -> superNext
			Node* prev = tempLast; /// the last from the loop
			Node* next = XOR(tempLast, temp->xor_head);

			/// now... prev->xor_head is (superLast, temp), we need to eliminate temp and add next.
			prev->xor_head = XOR(prev->xor_head, temp); /// removing temp
			prev->xor_head = XOR(prev->xor_head, next); /// adding next

			/// next->xor_head is (temp, superNext), need to be (last, superNext)
			next->xor_head = XOR(next->xor_head, temp); /// removing temp
			next->xor_head = XOR(next->xor_head, prev); /// adding last
		}
	}
	delete temp;
	return pair <TElem, TPrioritate>  (aux);
}

/// theta(1)
bool CP::vida() const {
	return this->head == nullptr;
}

/// theta(n)
CP::~CP() {
	Node* last = nullptr;
	/// timeline: superLast -> prev -> current -> next
	///              ^          ^         ^
	///             temp       last     head
	while (this->head != nullptr) {
		const Node* temp = last;
		Node* aux = this->head;
		this->head = XOR(this->head->xor_head, last);
		/// we dont need to worry about setting the prev at every operation ass we are deliting everyting
		last = aux; /// see why we needed the aux, you look like a goofball rn :)
		delete temp; /// we delete superLast
	}
	delete last;
};

