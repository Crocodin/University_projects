#pragma once

#include <vector>

typedef int TCheie;
typedef int TValoare;

#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

using namespace std;

class IteratorMDO;

typedef bool(*Relatie)(TCheie, TCheie);

class MDO {
	friend class IteratorMDO;
    private:

	const Relatie cmp;				///< relatia de ordine

	struct Node {
		TCheie cheie;
		vector<TValoare> valori;
		Node *left, *right;

		/// "class" functions
		explicit Node(TCheie cheie, TValoare valoare) : cheie(cheie) {
			valori.push_back(valoare);
			left = right = nullptr;
		}
	};

	void deleteRec(Node* node);
	Node *root;
	int numberOfElements = 0;

public:

	// constructorul implicit al MultiDictionarului Ordonat
	MDO(Relatie r);

	// adauga o pereche (cheie, valoare) in MDO
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MDO 
	int dim() const;

	//verifica daca MultiDictionarul Ordonat e vid 
	bool vid() const;

	// se returneaza iterator pe MDO
	// iteratorul va returna perechile in ordine in raport cu relatia de ordine
	IteratorMDO iterator() const;

	// destructorul 	
	~MDO();

	typedef TValoare(*searchF)(TValoare a, TValoare b);
	TValoare searchCMP(Node* current, searchF cmp) const;
	int maxMinDiff() const;
};
