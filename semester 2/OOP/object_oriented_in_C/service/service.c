#include "service.h"
#include <stdlib.h>
#include <string.h>

void initialization_service(service* Service) {
	Service->add_meds = add_meds;
	Service->Clear = Clear;
	Service->modify_med = modify_med;
	Service->remove_stock = remove_stock;
}

_Bool add_meds(const service* Service, const char* name, const char* concentration, const char* quantity) {
	if (!Service->Validator->valid_name(name)) return 0;
	if (!Service->Validator->valid_number(concentration)) return 0;
	if (!Service->Validator->valid_number(quantity)) return 0;

	meds_t* Med = (meds_t*)malloc(sizeof(meds_t));
	initialization(Med);
	Med->id = Service->Repo->generate_id(Service->Repo);
	Med->set_name(Med, name);
	Med->concentration = Service->Validator->convert_char_to_int(concentration);
	Med->quantity = Service->Validator->convert_char_to_int(quantity);
	Service->Repo->add_medicament(Service->Repo, Med);
	return 1;
}

void Clear(service* Service) {
	Service->Repo->clear(Service->Repo);
	free(Service->Validator);
	free(Service);
}

_Bool modify_med(const service* Service, const char* current_name,
				const char* new_name, const char* new_concentration) {
	if (!Service->Validator->valid_name(current_name)) return 0;
	if (!Service->Validator->valid_name(new_name)) return 0;
	if (!Service->Validator->valid_number(new_concentration)) return 0;

	int index = Service->Repo->get_index_with_name(Service->Repo, new_name);
	if (index != -1) return 0;
		// meds_t* Med = Service->Repo->list[index];
		// Med->change_quantity(Med,Med->quantity + Service->Validator->convert_char_to_int(new_concentration));
		// Service->Repo->remove_med_index(Service->Repo, index);
		// return 1;
	index = Service->Repo->get_index_with_name(Service->Repo, current_name);
	if (index == -1) return 0;
	meds_t* Med = Service->Repo->list[index];

	Med->concentration = Service->Validator->convert_char_to_int(new_concentration);
	free(Med->name);
	Med->set_name(Med, new_name);

	int i = index; // now a quick sort so that the new element will be at the right position
	// Move element forward if it's too small
	while (i > 0 && strcmp(Service->Repo->list[i - 1]->name, Med->name) > 0) {
		Service->Repo->list[i] = Service->Repo->list[i - 1];  // Shift left
		i--;
	} // Move element backward if it's too large
	while (i < (int) Service->Repo->number_of_meds - 1 && strcmp(Service->Repo->list[i + 1]->name, Med->name) < 0) {
		Service->Repo->list[i] = Service->Repo->list[i + 1];  // Shift right
		i++;
	} // Insert the misplaced element at the correct position
	Service->Repo->list[i] = Med;
	return 1;
}

_Bool remove_stock(const service* Service, const char* name) {
	if (!Service->Validator->valid_name(name)) return 0;
	meds_t* Med = Service->Repo->find_with_name(Service->Repo, name);
	if (Med == NULL) return 0;
	Med->quantity = 0;
	return 1;
}