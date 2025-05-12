#pragma once

#define NULL_TELEM -111111
typedef int TElem;

class IteratorMultime;

class Multime {
	friend class IteratorMultime;

private:

	template <typename T>
	struct DynamicVector {
		T* data;
		int capacity = 65536;
		/// -------------
		DynamicVector() { data = new T[capacity]; }
		void resize() {
			this->capacity = this->capacity << 2;
			T* temp = new T[this->capacity];
			for (int i = 0; i < this->capacity / 2; i++)
				temp[i] = this->data[i];
			delete[] this->data;
			this->data = temp;
		}
		T& operator[](int index) const { return this->data[index]; }
		~DynamicVector() { delete[] this->data; }
	};

	DynamicVector<TElem> elements;
	DynamicVector<int> nextElement;
	int numberOfElements;
	int freePosition;

	int hashFunc(const TElem& elem) const;
	void nextFreePosition();

public:
 	//constructorul implicit
	Multime();

	//adauga un element in multime
	//returneaza adevarat daca elementul a fost adaugat (nu exista deja in multime)
	bool adauga(TElem e);

	//sterge un element din multime
	//returneaza adevarat daca elementul a existat si a fost sters
	bool sterge(TElem e);

	//verifica daca un element se afla in multime
	bool cauta(TElem elem) const;


	//intoarce numarul de elemente din multime;
	int dim() const;

	//verifica daca multimea e vida;
	bool vida() const;

	//returneaza un iterator pe multime
	IteratorMultime iterator() const;

	// destructorul multimii
	~Multime();
};




