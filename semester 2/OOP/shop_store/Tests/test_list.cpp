#include <cassert>
#include <iostream>

#include "tests.h"
#include "../DynamicList/list.hpp"

void test_int_list() {
	List<int> list;
	assert(list.empty());

	for (int i = 0; i < 20; i++)
		list.push_back(i);

	assert(list.size() == 20);
	assert(*list.begin() == 0);
	assert(*(list.end() - 1) == 19);

	for (int i = 0; i < 20; i++)
		assert(list[i] == i);

	list.erase(list.begin() + 5);
	assert(list.size() == 19);

	for (int i = 0; i < 19; i++)
		if (i < 5) assert(list[i] == i);
		else if (i == 5) assert(list[i] != i);
		else assert(list[i] == i + 1);

	list.erase(list.begin() + 5, list.end() - 5);
	assert(list.size() == 10);
	for (int i = 0; i < 5; i++)
		assert(list[i] == i);
	for (int i = 5; i < 10; i++)
		assert(list[i] == i + (19 - 4) - 5);

	for (int i = 19; i >= 19 - 4; i--)
		assert(list.pop() == i);
	for (int i = 4; i >= 0; i--)
		assert(list.pop() == i);

	try { list.pop(); assert(false); }
	catch (...) { assert(true); }
}

void test_list() {
	test_int_list();
	// test_product_list();
}