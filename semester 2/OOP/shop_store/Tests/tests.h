#ifndef TESTS_H
#define TESTS_H

void test_domain();

void test_repo();

inline void run_all_tests() {
	test_domain();
	test_repo();
}

#endif //TESTS_H
