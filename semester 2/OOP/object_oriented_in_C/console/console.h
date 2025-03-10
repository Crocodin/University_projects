#ifndef CONSOLE_H
#define CONSOLE_H

#include "../service/service.h"

typedef struct console {
	service* Service;
	int stock_filter;
	char letter_filter;

	void (*filter)(struct console* restrict Console);
}console;

void run(console* restrict Console);

void filter(console* restrict Console);

#endif //CONSOLE_H
