#ifndef TEST_H
#define TEST_H

#include "../domain/meds.h"
#include "../domain/validator.h"
#include "../service/service.h"
#include "assert.h"

typedef struct test {
	void (*run_test)(const struct test* Test);
}test;

/// !MOST IMPORTANT FUNCTION!
/// sets the function pointers in the test struct
/// :param Test: test* to witch we set the pointers
/// :return: NULL
void initialization_test(test* Test);

/// !SECOND MOST IMPORTANT FUNCTION!
/// runs all the test
/// :param Test: test*
/// :return: NULL
void run_test(const test* Test);

/// test all the functions in domain -> meds
/// :return: NULL
void test_domain();

/// test all the functions in domain -> validator
/// :return: NULL
void test_validator();

/// test all the functions in repo
/// :return: NULL
void test_initialization_repo();
void test_add_medicament();


/// test all the function in service
/// :return: NULL
void test_initialization_service();
void test_service();

#endif //TEST_H
