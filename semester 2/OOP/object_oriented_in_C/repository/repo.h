#ifndef REPO_H
#define REPO_H

#include "../domain/meds.h"

typedef _Bool (*comp_function)(const meds_t*, const meds_t*);

typedef struct repo {
	meds_t** list;
	unsigned int number_of_meds;
	unsigned int size;

	/// :param Repo: repo* the caller is "this"
	void (*resize)(struct repo* Repo);
	void (*add_medicament)(struct repo* Repo, meds_t* Med);
	meds_t* (*find)(const struct repo* Repo, const meds_t* Med);
	int (*generate_id)(const struct repo* Repo);
	void (*clear)(struct repo* Repo);
	meds_t* (*find_with_name)(const struct repo* Repo, const char* name);
	int (*get_index_with_name)(const struct repo* Repo, const char* name);
	void (*remove_med_index)(struct repo* Repo, const int index);
	void (*sort)(struct repo* Repo, comp_function function);
} repo;

/// !MOST IMPORTANT FUNCTION!
/// sets the function pointers in the repo
/// :param Repo: repo* to witch we set the pointers
/// :return: NULL
void initialization_repo(repo* Repo);

/// declares the vector with an initial size of 16
/// and the size of 0
/// :return: NULL
void declare_repo(repo* Repo);

/// doubles the size of the list
/// :return: new repo
void resize(repo* Repo);

/// add to the repo medicine
/// :param Med: meds_t*
/// :return: NULL
void add_medicament(repo* Repo, meds_t* Med);

/// checks to see if a meds_t is already in the repo
/// :param Med: meds_t*
/// :return: meds_t* that is in the repo
meds_t* find(const repo* Repo, const meds_t* Med);

/// generate an id
/// :return: int
int generate_id(const repo* Repo);

/// frees all the allocated memory
/// :return: NULL
void clear(repo* Repo);

/// checks to see if a meds_t is already in the repo with the given name
/// :param Med: meds_t*
/// :return: meds_t* that is in the repo
meds_t* find_with_name(const repo* Repo, const char* name);

/// checks to see if a meds_t is already in the repo with the given name
/// :param Med: meds_t*
/// :return: unsigned int that corresponds to the index of med_t
int get_index_with_name(const repo* Repo, const char* name);

/// removes the med at the given index
/// :param index: int
/// :return: NULL
//void remove_med_index(repo* Repo, const int index);

/// sorts the list
/// :param Repo: const repo*
/// :param function: const _Bool *
/// :return: NULL
void sort(repo* Repo, comp_function function);

/// compares two med_t elements, returns TRUE if the first one is smaller
/// :param Med1: const meds_t*
/// :param Med1: const meds_t*
/// :return: _Bool
_Bool ascending_repo(const meds_t* Med1, const meds_t* Med2);

/// compares two med_t elements, returns TRUE if the first one is bigger
/// :param Med1: const meds_t*
/// :param Med1: const meds_t*
/// :return: _Bool
_Bool descending_repo(const meds_t* Med1, const meds_t* Med2);

#endif //REPO_H
