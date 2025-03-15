#include <stdlib.h>

#include "console/console.h"
#include "service/service.h"
#include "repository/repo.h"
#include "domain/validator.h"

#include "test/test.h"

int main(void) {
	/// testing
	test* Test = (test*)malloc(sizeof(test));
	initialization_test(Test);
	Test->run_test(Test);
	free(Test);
	/// end fo tests

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
	free(Console);
	return 0;
}