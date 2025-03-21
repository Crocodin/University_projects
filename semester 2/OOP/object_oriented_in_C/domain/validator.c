#include "validator.h"

void initialization_validator(validator* Valid) {
	Valid->convert_char_to_int = convert_char_to_int;
	Valid->valid_name = valid_name;
	Valid->valid_number = valid_number;
}

_Bool valid_name(const char* name) {
	(void) name;
	return 1;
}

_Bool valid_number(const char* number) {
	for (unsigned int i = 0; number[i] != '\0'; ++i)
		if (!('0' <= number[i] && number[i] <= '9'))
			return 0;
	return 1;
}

int convert_char_to_int(const char* string) {
	int result = 0;
	for (unsigned int i = 0; string[i] != '\0'; ++i)
		result = result * 10 + (string[i] - '0');
	return result;
}