#include "repo.h"

#include <stdlib.h>
#include <string.h>

void initialization_repo(repo* Repo) {
	Repo->add_medicament = add_medicament;
	Repo->resize = resize;
	Repo->find = find;
	Repo->find_with_name = find_with_name;
	Repo->generate_id = generate_id;
	Repo->clear = clear;
	Repo->get_index_with_name = get_index_with_name;
	// Repo->remove_med_index = remove_med_index;
	Repo->sort = sort;
	declare_repo(Repo);
}

void declare_repo(repo* Repo) {
	Repo->number_of_meds = 0;
	Repo->size = 16;
	Repo->list = (meds_t**)malloc(Repo->size * sizeof(meds_t*));
}

void resize(repo* Repo) {
	Repo->size = Repo->size << 1;
	meds_t** new_list = (meds_t**)realloc(Repo->list, Repo->size * sizeof(meds_t*));
	if (new_list != NULL)
		Repo->list = new_list;
}

void add_medicament(repo* Repo, meds_t* Med) {
	meds_t* NewMed = Repo->find(Repo, Med);
	if (NewMed == NULL) {
		if (Repo->number_of_meds == Repo->size - 1)
			Repo->resize(Repo);
		// insert at the right position
		int position = 0;
		while (position < (int) Repo->number_of_meds)
			if (strcmp(Repo->list[position]->name, Med->name) < 0)
				position++;
			else break;
		for (unsigned int k = Repo->number_of_meds; (int) k > position; --k)
			Repo->list[k] = Repo->list[k - 1];
		Repo->number_of_meds++;
		Repo->list[position] = Med;
	}
	else {
		NewMed->change_quantity(NewMed, NewMed->quantity + Med->quantity);
		free(Med->name);
		free(Med);
	}
}

meds_t* find(const repo* Repo, const meds_t* Med) {
	for (int i = 0; i < (int) Repo->number_of_meds; i++)
		if (Repo->list[i]->equal(Repo->list[i], Med))
			return Repo->list[i];
	return NULL;
}

int generate_id(const repo* Repo) {
	return (int)Repo->number_of_meds;
}

void clear(repo* Repo) {
	for (int i = 0; i < (int) Repo->number_of_meds; i++)
		free(Repo->list[i]->name), free(Repo->list[i]);
	free(Repo->list);
	free(Repo);
}

meds_t* find_with_name(const repo* Repo, const char* name) {
	for (int i = 0; i < (int) Repo->number_of_meds; i++)
		if (strcmp(Repo->list[i]->name, name) == 0)
			return Repo->list[i];
	return NULL;
}

int get_index_with_name(const repo* Repo, const char* name) {
	for (int i = 0; i < (int) Repo->number_of_meds; i++)
		if (strcmp(Repo->list[i]->name, name) == 0)
			return i;
	return -1;
}

void sort(repo* Repo, comp_function function) {
	for (int i = 0; i < (int) Repo->number_of_meds - 1; i++) {
		for (int j = i + 1; j < (int) Repo->number_of_meds; j++) {
			if (function(Repo->list[j], Repo->list[j])) {
				meds_t* Temp = Repo->list[i];
				Repo->list[i] = Repo->list[j];
				Repo->list[j] = Temp;
			}
		}
	}
}

_Bool ascending_repo(const meds_t* Med1, const meds_t* Med2) {
	return strcmp(Med1->name, Med2->name) <= 0;
}

_Bool descending_repo(const meds_t* Med1, const meds_t* Med2) {
	return strcmp(Med1->name, Med2->name) >= 0;
}

// void remove_med_index(repo* Repo, const int index) {
// 	free(Repo->list[index]->name);
// 	free(Repo->list[index]);
// 	for (int i = index; i < Repo->number_of_meds - 1; i++) {
// 		Repo->list[i] = Repo->list[i + 1];
// 	}
// 	Repo->number_of_meds--;
// }