#include <iostream>

int main() {
	float r, p, a, pi;
	pi = 3.14;
	std::cin >> r;
	p = 2 * pi * r;
	a = pi * r * r;
	std::cout << p << '\n';
	std::cout << a << '\n';
	return 0;
}

/*
 * <program> ::= int main() { <list_instr> return 0; }
 * <list_instr> ::= <instr>; | <instr>; <list_instr>
 * <instr> ::= <decl> | <read> | <operation> | <print>
 * <decl> ::= <val_type> <list_ID>
 * <val_id> ::= int | float
 * <list_ID> ::= ID | ID, <list_ID>
 * <read> ::= std::cin >> ID
 * <operation> ::= ID = <operation_start> <operation_ID>
 * <operation_strat> ::= <operation_ID> <operation_type> | <operation_ID> <operation_type> <operation_start>
 * <operation_ID> ::= CONST | ID
 * <operation_type> ::= + | *
 * <print> ::= std::cout << <list_print>
 * <print_list> ::= ID | ID << <print_list>
 * ID ::= ^[a-zA-Z][a-zA-Z0-9]*$
 * CONST ::= ^[0-9]+$
*/

// int main() {
// 	int r, p, a, pi;
// 	3.14 = pi;
// 	std::cin << r;
// 	p = 2 * pi * r;
// 	a = pi * r * r;
// 	return 0;
// }