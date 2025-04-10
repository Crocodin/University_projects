#ifndef TESTS_H
#define TESTS_H

/// tests domain logic
/// :return: NULL
void test_domain();


/// tests repository functionality
/// :return: NULL
void test_repo();


/// tests service layer functionality
/// :return: NULL
void test_service();

/// tests list functionality
/// :return: NULL
void test_list();

inline void run_all_tests() {
    test_list();
	test_domain();
	test_repo();
	test_service();
}

#endif //TESTS_H
