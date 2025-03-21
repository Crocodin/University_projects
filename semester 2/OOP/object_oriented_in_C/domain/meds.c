#include <string.h>
#include "meds.h"

#include <stdlib.h>

void initialization(meds_t* Med) {
	Med->change_quantity = change_quantity;
	Med->equal = equal;
	Med->set_name = set_name;
	Med->name = NULL;
}

void change_quantity(meds_t* Med, const int number) {
	Med->quantity = number;
}

_Bool equal(const meds_t* Med1, const meds_t* Med2) {
	return strcmp(Med1->name, Med2->name) == 0;
}

void set_name(meds_t* Med, const char *name) {
	Med->name = (char*)malloc(strlen(name) + 1);
	strcpy(Med->name, name);
}
