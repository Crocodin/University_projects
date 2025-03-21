#include "test.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../repository/repo.h"

void initialization_test(test* Test) {
	Test->run_test = run_test;
}

void run_test(const test* Test) {
	(void) Test;
	test_domain();
	test_validator();
	test_initialization_repo();
	test_add_medicament();
	test_initialization_service();
	test_service();
}

void test_domain() {
	meds_t Med;
	initialization(&Med);
	assert(Med.change_quantity == change_quantity);
	assert(Med.equal == equal);
	assert(Med.set_name == set_name);
	Med.id = 0;
	assert(Med.id == 0);
	Med.set_name(&Med, "test");
	assert(strcmp(Med.name, "test") == 0);
	Med.concentration = 0;
	assert(Med.concentration == 0);
	Med.quantity = 0;
	assert(Med.quantity == 0);
	Med.change_quantity(&Med, 1);
	assert(Med.quantity == 1);
	meds_t Med2;
	initialization(&Med2);
	Med2.set_name(&Med2, "test");
	assert(Med2.equal(&Med2, &Med) == 1);
	free(Med2.name);
	Med2.set_name(&Med2, "_test_");
	assert(Med2.equal(&Med2, &Med) == 0);
	free(Med.name);
	free(Med2.name);
}

void test_validator() {
	validator Valid;
	initialization_validator(&Valid);
	assert(Valid.valid_name == valid_name);
	assert(Valid.valid_number == valid_number);
	assert(Valid.convert_char_to_int == convert_char_to_int);
	assert(Valid.valid_name("test"));
	assert(Valid.valid_number("1234"));
	assert(!Valid.valid_number("12ab"));
	assert(Valid.convert_char_to_int("1234") == 1234);
}

void test_initialization_repo() {
	repo *Repo = (repo*)malloc(sizeof(repo));
	initialization_repo(Repo);

	// Check if function pointers are correctly initialized
	assert(Repo->add_medicament != NULL);
	assert(Repo->resize != NULL);
	assert(Repo->find != NULL);
	assert(Repo->generate_id != NULL);
	assert(Repo->clear != NULL);
	assert(Repo->find_with_name != NULL);
	assert(Repo->get_index_with_name != NULL);
	//assert(Repo->remove_med_index != NULL);

	// Check initial state
	assert(Repo->number_of_meds == 0);
	assert(Repo->size == 16);

	free(Repo->list);
	free(Repo);
}

void test_add_medicament() {
	repo *Repo = (repo*)malloc(sizeof(repo));
	initialization_repo(Repo);
	Repo->size = 1;

	// Create two medications
	meds_t *med1 = (meds_t*)malloc(sizeof(meds_t));
	initialization(med1);
	med1->set_name(med1, "Aspirin");
	med1->id = Repo->generate_id(Repo);
	med1->quantity = 100;
	assert(med1->id == 0);

	meds_t *med2 = (meds_t*)malloc(sizeof(meds_t));
	initialization(med2);
	med2->set_name(med2, "Paracetamol");
	med2->quantity = 50;

	meds_t *med3 = (meds_t*)malloc(sizeof(meds_t));
	initialization(med3);
	med2->set_name(med3, "Codrinol");
	med2->quantity = 90;

	meds_t *med4 = (meds_t*)malloc(sizeof(meds_t));
	initialization(med4);
	med2->set_name(med4, "Codrinol");
	med2->quantity = 90;

	// Add med1 and med2 to the repo
	Repo->add_medicament(Repo, med1);
	Repo->add_medicament(Repo, med2);

	// Check that both meds are added
	assert(Repo->number_of_meds == 2);
	assert(strcmp(Repo->find(Repo, med1)->name, "Aspirin") == 0);
	assert(strcmp(Repo->find(Repo, med2)->name, "Paracetamol") == 0);

	Repo->add_medicament(Repo, med3);
	Repo->add_medicament(Repo, med4);

	assert(Repo->number_of_meds == 3);

	int index = Repo->get_index_with_name(Repo, "Codrinol");
	meds_t* med = Repo->find_with_name(Repo, "Codrinol");
	assert(strcmp(Repo->list[index]->name, med->name) == 0);

	index = Repo->get_index_with_name(Repo, "asdfg");
	med = Repo->find_with_name(Repo, "asdfg");

	assert(index == -1);
	assert(med == NULL);

	Repo->sort(Repo, descending_repo);
	assert(strcmp(Repo->list[0]->name, "Paracetamol") == 0);

	Repo->sort(Repo, ascending_repo);
	assert(strcmp(Repo->list[0]->name, "Aspirin") == 0);

	Repo->clear(Repo);
}

void test_initialization_service() {
	service* Service = (service*)malloc(sizeof(service));
	initialization_service(Service);
	assert(Service->add_meds != NULL);
	assert(Service->modify_med != NULL);
	assert(Service->remove_stock != NULL);
	assert(Service->Clear != NULL);
	free(Service);
}

void test_service() {
	_Bool succes = 0;

	validator* Validator = (validator*)malloc(sizeof(validator));
	initialization_validator(Validator);

	repo* Repo = (repo*)malloc(sizeof(repo));
	initialization_repo(Repo);

	service* Service = (service*)malloc(sizeof(service));
	initialization_service(Service);

	Service->Repo = Repo;
	Service->Validator = Validator;

	succes = Service->add_meds(Service, "Codrinol", "90", "1");
	assert(succes == 1);
	assert(Service->Repo->find_with_name(Service->Repo, "Codrinol") != NULL);

	succes = Service->remove_stock(Service, "Codrinol");
	assert(succes == 1);
	assert(Service->Repo->find_with_name(Service->Repo, "Codrinol")->quantity == 0);

	succes = Service->modify_med(Service, "Codrinol", "Teodorino", "1");
	assert(succes == 1);
	assert(Service->Repo->find_with_name(Repo, "Teodorino") != NULL);
	assert(Service->Repo->find_with_name(Repo, "Teodorino")->concentration == 1);

	Service->add_meds(Service, "Codrinol", "90", "1");
	Service->add_meds(Service, "Mirciulino", "90", "1");

	succes = Service->modify_med(Service, "Mirciulino", "Aaaaa", "1");
	assert(succes == 1);
	succes = Service->modify_med(Service, "Aaaaa", "Xxxxx", "1");
	assert(succes == 1);

	Service->Clear(Service);
}