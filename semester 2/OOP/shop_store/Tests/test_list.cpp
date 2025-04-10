#include <cassert>
#include <iostream>
#include "tests.h"
#include "../DinamicList/list.hpp"

void test_int_list() {
	List<int> list;
	assert(list.empty());

	for (int i = 0; i < 20; i++)
		list.push_back(i);

	assert(list.size() == 20);
	assert(*list.begin() == 0);
	assert(*list.end() == 19);

	for (int i = 0; i < 20; i++)
		assert(list[i] == i);

	list.erase(list.begin() + 5);
	assert(list.size() == 19);

	for (int i = 0; i < 19; i++)
		if (i < 5) assert(list[i] == i);
		else if (i == 5) assert(list[i] != i);
		else assert(list[i] == i + 1);

	list.erase(list.begin() + 5, list.end() - 5);
	assert(list.size() ==11);
	for (int i = 0; i < 5; i++)
		assert(list[i] == i);
	for (int i = 5; i < 10; i++)
		assert(list[i] == i + 14 - 5);
}

void test_list() {
	test_int_list();
	// test_product_list();
}