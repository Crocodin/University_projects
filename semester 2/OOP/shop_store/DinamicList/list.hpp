#pragma once
#include <stdexcept>

using uint = unsigned int;
template <typename T> class List {
private:
	T* data = nullptr;
	uint _size = 0;
	uint capacity = 16;

	/// doubles the capacity of the list
	/// :return: NULL
	void redim() {
		this->capacity <<= 1;
		T* newData = new T[this->capacity];
		for (uint i = 0; i < this->_size; i++) {
			newData[i] = this->data[i];
		}
		delete[] this->data;
		this->data = newData;
	}
public:
	List() noexcept { data = new T[capacity]; }

	/// adds at the end of the vector an element
	/// :param elem: const T* - element to be added (makes a copy of it)
	/// :return: NULL
	void push_back(const T& elem) {
		if (this->_size == capacity) {
			this->redim();
		}
		data[_size++] = elem;
	}

	/// returns a pointer to the first element
	/// :return: T*
	T* begin() const noexcept { return data; }

	/// returns a pointer to the last element
	/// :return: T* - pointer to the last element
	T* end() const noexcept { return data + _size; }

	/// removes elements from the list starting from `fromWhere` to `toWhere`
	/// :param fromWhere: pointer to the first element to be removed
	/// :param toWhere: pointer to the last element to be removed (defaults to `fromWhere + 1`)
	/// :return: NULL
	/// @:exception: might throw std::out_of_range if the range is invalid
	void erase(T* fromWhere, const T* toWhere = nullptr) {
		if (toWhere == nullptr) toWhere = fromWhere + 1;  // erase single element

		int amountToErase = toWhere - fromWhere;
		if (amountToErase <= 0 || amountToErase > static_cast<int>(this->_size - (fromWhere - this->data)))
			throw std::out_of_range("FATAL call of erase");

		T* moveFrom = fromWhere + amountToErase;
		T* moveTo = fromWhere;

		while (moveFrom < this->data + this->_size) {
			*moveTo++ = *moveFrom++;
		}

		this->_size -= amountToErase;
	}

	/// returns the number of elements in the list
	/// :return: uint - the size of the list
	[[nodiscard]] uint size() const { return this->_size; }

	/// returns a reference to the element at index `i`
	/// :param i: index of the element to access
	/// :return: T& - reference to the element at index `i`
	/// @:exception: none
	T& operator[](uint i) const { return this->data[i]; }

	/// checks if the list is empty
	/// :return: bool - true if the list is empty, false otherwise
	[[nodiscard]] bool empty() const { return this->_size == 0; }

	/// destructor that frees the allocated memory for the list
	/// :return: NULL
	/// @:exception: none
	~List() noexcept { delete[] data; }
};

template <typename T> class Iterator {
private:
	const List<T>& list;
	uint index = 0;
public:
	/// constructor that initializes an iterator at the beginning of the list
	/// :param list: const reference to the list to iterate over
	/// :return: none
	/// @:exception: none
	explicit Iterator(const List<T>& list) noexcept : list(list) {}

	/// constructor that initializes an iterator at a specific index of the list
	/// :param list: const reference to the list to iterate over
	/// :param index: the starting index for the iterator
	/// :return: none
	/// @:exception: none
	Iterator(const List<T>& list, const uint index) noexcept : list(list), index(index) {}

	/// checks if the iterator points to a valid element
	/// :return: bool - true if index is within bounds, false otherwise
	[[nodiscard]] bool valid() const noexcept { return index < list.size(); }

	/// returns a reference to the element at the current iterator index
	/// :return: T& - reference to the current element
	/// @:exception: none
	T& element() const noexcept { return list[index]; }

	/// advances the iterator to the next element
	/// :return: void
	/// @:exception: none
	void next() noexcept { index++; }

	/// advances the iterator to the next element (prefix increment)
	/// :return: Iterator& - reference to the updated iterator
	/// @:exception: none
	Iterator& operator++() noexcept { next(); return *this; }
};