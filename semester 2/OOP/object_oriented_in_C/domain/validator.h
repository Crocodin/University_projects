#ifndef VALIDATOR_H
#define VALIDATOR_H

typedef struct validator {
	_Bool (*valid_name)(const char* name);
	_Bool (*valid_number)(const char* number);
	int (*convert_char_to_int)(const char* string);
}validator;

/// !MOST IMPORTANT FUNCTION!
/// sets the function pointers in the validator
/// :param Med: validator* to witch we set the pointers
/// :return: NULL
void initialization_validator(validator* Valid);

/// any name is valid
/// :return: true always
_Bool valid_name(const char* name);

/// checks to se if it's a number
/// :param number: char* a potential number
/// :return: _Bool
_Bool valid_number(const char* number);

/// converts char to int
/// :param string: char*
/// :return: int
int convert_char_to_int(const char* string);

#endif //VALIDATOR_H
