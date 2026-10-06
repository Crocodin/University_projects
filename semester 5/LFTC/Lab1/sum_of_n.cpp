#include <iostream>

int main() {
	int n, sum, i;
	i = 0;
	sum = 0;
	std::cin >> n;
	while (i < n) {
		int x;
		std::cin >> x;
		sum = sum + x;
		i = i + 1;
	}
	std::cout << sum;
	return 0;
}

/*
 * <program> ::= int main() { <list_instr> return 0; }
 * <list_instr> ::= <instr>; | <instr>; <list_instr>
 * <instr> ::= <decl> | <read> | <operation> | <loop_operation> | <condition_operation> | <print>
 * <decl> ::= <val_type> <list_ID> | <val_type> <operation>
 * <list_ID> ::= ID | ID, <list_ID>
 * <val_type> ::= int | float
 * <read> ::= std::cin >> ID
 * <operation> ::= ID = <operation_start> <operation_ID>
 * <operation_strat> ::= <operation_ID> <operation_type> | <operation_ID> <operation_type> <operation_start>
 * <operation_ID> ::= CONST | ID
 * <operation_type> ::= + | *
 * <condition_operation> ::=  if ( <conditions> ) { <list_instr> } else { <list_instr> }
 * <loop_operation> ::= while ( <conditions> ) { <list_instr> }
 * <conditions> ::= <conditions> | <conditions> <and_or> <conditions>
 * <and_or> ::= && | ||
 * <conditions> ::= <operation_start> <operation_ID> <compare> <operation_start> <operation_ID> | <operation_ID> <compare> <operation_ID>
 * <compare> ::= == | != | <= | < | > | >=
 * <print> ::= std::cout << <list_print>
 * <print_list> ::= ID | ID << <print_list>
 * ID ::= ^[a-zA-Z][a-zA-Z0-9]*$
 * CONST ::= ^[0-9]+$
*/

// int main() {
// 	int n, sum = 0, i = 0;
// 	std::cin >> n;
// 	while (i < n) {
// 		int x;
// 		std::cin >> x;
// 		sum += x;
// 		i = i + 1;
// 	}
// 	std::cout << sum;
// 	return 0;
// }