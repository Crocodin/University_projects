#include "console.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define this const console* restrict Console

void filter(console* restrict Console) {
	char size[10];
	printf("Filtering service...\n");
	printf("Stock filter:");
	scanf("%10s", size); getchar();
	if (!Console->Service->Validator->valid_number(size)) {
		printf("Invalid input.\n");
		return;
	}
	Console->stock_filter = Console->Service->Validator->convert_char_to_int(size);
	printf("Starting character: ");
	scanf("%4s", size); getchar();
	if (strcmp(size, "none") == 0 || strcmp(size, "NONE") == 0)
		Console->letter_filter = '\0';
	else Console->letter_filter = size[0];
}

static void delete_stock(this) {
	char name[20];
	printf("Enter medication name: ");
	scanf("%20s", name); getchar();
	if (!Console->Service->remove_stock(Console->Service, name))
		printf("Invalid name\n");
}

static void display(this) {
	char input[2];
	printf("Ascending (0) or descending (1)?\n");
	scanf("%1s", input); getchar();
	if (input[0] == '1') {
		Console->Service->Repo->sort(Console->Service->Repo, descending_repo);
		printf("\n------------------// meds \\\\------------------\n");
		for (unsigned int i = 0; i < Console->Service->Repo->number_of_meds; -- i) {
			const meds_t* Med = Console->Service->Repo->list[i - 1];
			// here we test for the filter
			if (Console->stock_filter <= Med->quantity &&
				(Console->letter_filter == Med->name[0] || Console->letter_filter == '\0'))
				printf("ID %d: %s - %d%%, stock %d\n", Med->id, Med->name, Med->concentration, Med->quantity);
		}
		// the repo needs to be sorted back because when adding an elem it's already sorted using insertion sort
		Console->Service->Repo->sort(Console->Service->Repo, ascending_repo);
	}
	else { // the repo is already sorted
		Console->Service->Repo->sort(Console->Service->Repo, ascending_repo);
		for (unsigned int i = 0; i < Console->Service->Repo->number_of_meds; i++) {
			const meds_t* Med = Console->Service->Repo->list[i];
			if (Console->stock_filter <= Med->quantity &&
				(Console->letter_filter == Med->name[0] || Console->letter_filter == '\0'))
				printf("ID %d: %s - %d%%, stock %d\n", Med->id, Med->name, Med->concentration, Med->quantity);
		}
	}
	printf("\n");
}

// static void display(this) {
// 	char input[2];
// 	printf("Ascending (0) or descending (1)?\n");
// 	scanf("%1s", input); getchar();
// 	if (input[0] == '1') {
// 		printf("\n------------------// meds \\\\------------------\n");
// 		for (unsigned int i = Console->Service->Repo->number_of_meds; i > 0; -- i) {
// 			const meds_t* Med = Console->Service->Repo->list[i - 1];
// 			if (Console->stock_filter <= Med->quantity &&
// 				(Console->letter_filter == Med->name[0] || Console->letter_filter == '\0'))
// 				printf("ID %d: %s - %d%%, stock %d\n", Med->id, Med->name, Med->concentration, Med->quantity);
// 		}
// 		return;
// 	}
// 	printf("\n------------------// meds \\\\------------------\n");
// 	for (unsigned int i = 0; i < Console->Service->Repo->number_of_meds; i++) {
// 		const meds_t* Med = Console->Service->Repo->list[i];
// 		if (Console->stock_filter <= Med->quantity &&
// 			(Console->letter_filter == Med->name[0] || Console->letter_filter == '\0'))
// 			printf("ID %d: %s - %d%%, stock %d\n", Med->id, Med->name, Med->concentration, Med->quantity);
// 	}
// 	printf("\n");
// }

static void modify(this) {
	char current_name[20], new_name[20], new_concentration[10];
	printf("Enter the current name: ");
	scanf("%20s", current_name); getchar();
	printf("Enter the new name: ");
	scanf("%20s", new_name); getchar();
	printf("Enter the new concentration: ");
	scanf("%10s", new_concentration); getchar();

	if (!Console->Service->modify_med(Console->Service, current_name, new_name, new_concentration))
		printf("Something went wrong when modifying the medication\n");
}

static void add_element(this) {
	char name[20], concentration[10], quantity[10];
	printf("Enter the name of the medicament: ");
	scanf("%20s", name); getchar();
	printf("Enter the concentration: ");
	scanf("%10s", concentration); getchar();
	printf("Enter the quantity: ");
	scanf("%10s", quantity); getchar();

	if (!Console->Service->add_meds(Console->Service, name, concentration, quantity))
		printf("Something went wrong when adding the medication\n");
}

void run(console* restrict Console) {
	// initialization
	Console->filter = filter;
	Console->stock_filter = 0;
	Console->letter_filter = '\0';

	// run app
	_Bool IsRunning = 1;
	while (IsRunning) {
		char input[2];
		printf("------------------// menu \\\\------------------\n");
		printf("1. Add medication\n");
		printf("2. Modify concentration and name\n");
		printf("3. Delete stock from one med\n");
		printf("4. Display all meds\n");
		printf("5. Filter\n");
		printf("E. Exit\n");
		scanf("%1s", input); getchar();
		switch (input[0]) {
			case '1':
				add_element(Console);
				break;
			case '2':
				modify(Console);
				break;
			case '3':
				delete_stock(Console);
				break;
			case '4':
				display(Console);
				break;
			case '5':
				Console->filter(Console);
				break;
			case 'E':
			case 'e': // exit the program case
				IsRunning = 0;
				break;
			default:
				printf("Invalid input\n");
				break;
		}
	}
	Console->Service->Clear(Console->Service);
}
