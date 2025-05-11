#pragma once

typedef int TElem;
typedef unsigned int uint;

class IteratorLP;

class Lista {
private:
	friend class IteratorLP;

	template <typename T>
	struct DynamicVector {
		T* data;
		int capacity = 16;
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

	void resize();

	DynamicVector<TElem> elements;
	DynamicVector<int> position;
	int firstElement;
	int firstFreePosition;

public:
		// constructor
		Lista ();

		// returnare dimensiune
		int dim() const;

		// verifica daca lista e vida
		bool vida() const;

		// prima pozitie din lista
		IteratorLP prim() const;

		// returnare element de pe pozitia curenta
		//arunca exceptie daca poz nu e valid
		TElem element(IteratorLP poz) const;

		// modifica element de pe pozitia poz si returneaza vechea valoare
		//arunca exceptie daca poz nu e valid
		TElem modifica(IteratorLP poz, TElem e);

		// adaugare element la inceput
		void adaugaInceput(TElem e);		

		// adaugare element la sfarsit
		void adaugaSfarsit(TElem e);

		// adaugare element dupa o pozitie poz
		//dupa adaugare poz este pozitionat pe elementul adaugat
		//arunca exceptie daca poz nu e valid
		void adauga(IteratorLP& poz, TElem e);

		// sterge element de pe o pozitie poz si returneaza elementul sters
		//dupa stergere poz este pozitionat pe elementul de dupa cel sters
		//arunca exceptia daca poz nu e valid
		TElem sterge(IteratorLP& poz);

		// cauta element si returneaza prima pozitie pe care apare (sau iterator invalid)
		IteratorLP cauta(TElem e) const;

		//destructor
		~Lista();

		IteratorLP ultimulIndex(TElem elem) const;
};
