#include <stdlib.h>

#include "console/console.h"
#include "service/service.h"
#include "repository/repo.h"
#include "domain/validator.h"

int main(void) {
	validator* Validator = (validator*)malloc(sizeof(validator));
	initialization_validator(Validator);

	repo* Repo = (repo*)malloc(sizeof(repo));
	initialization_repo(Repo);

	service* Service = (service*)malloc(sizeof(service));
	initialization_service(Service);
	Service->Repo = Repo;
	Service->Validator = Validator;


	console* restrict Console = (console*)malloc(sizeof(console));
	Console->Service = Service;

	run(Console);
}