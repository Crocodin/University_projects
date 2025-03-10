#ifndef SERVICE_H
#define SERVICE_H

#include "../repository/repo.h"
#include "../domain/validator.h"

typedef struct service {
	repo* Repo;
	validator* Validator;
	_Bool (*add_meds)(const struct service* Service, const char* name,
					  const char* concentration, const char* quantity);
	void (*Clear)(struct service* Service);
	_Bool (*modify_med)(const struct service* Service, const char* current_name,
				const char* new_name, const char* new_concentration);
	_Bool (*remove_stock)(const struct service* Service, const char* name);
}service;

/// !MOST IMPORTANT FUNCTION!
/// sets the function pointers in the service
/// :param service: service* to witch we set the pointers
/// :return: NULL
void initialization_service(service* Service);

/// creates a med_t in memory and then call add med form the Repo
/// :param name: const char*
/// :param concentration: const char*
/// :param quantity: const char*
/// :return: _Bool tha value of success
_Bool add_meds(const service* Service, const char* name, const char* concentration, const char* quantity);

/// frees all the allocated memory
/// :return: NULL
void Clear(service* Service);

/// changes the name and concentration of a given med
/// :param current_name: const char*
/// :param new_name: const char*
/// :param new_concentration: const char*
/// :return: _Bool tha value of success
_Bool modify_med(const service* Service, const char* current_name,
	            const char* new_name, const char* new_concentration);

/// sets the stock of a med to 0
/// :param name: const char*
/// :return: _Bool tha value of success
_Bool remove_stock(const service* Service, const char* name);

#endif //SERVICE_H
