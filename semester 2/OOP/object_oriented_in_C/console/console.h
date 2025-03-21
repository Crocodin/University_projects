#ifndef CONSOLE_H
#define CONSOLE_H

#include "../service/service.h"

typedef struct console {
	service* Service;
	int stock_filter;
	char letter_filter;

	void (*filter)(struct console* restrict Console);
}console;

/// runs the app
/// :param Console: console* restrict
/// :return: NULL
void run(console* restrict Console);

/// sets the default filter
/// :param Console: console* restrict
/// :return: NULL
void filter(console* restrict Console);

#endif //CONSOLE_H
