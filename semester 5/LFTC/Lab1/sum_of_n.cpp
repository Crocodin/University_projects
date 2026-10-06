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
 * <code> ::= #include <iostream> <program> | #include <iostream> <defined_type_list> <program>
 * <defined_type_list> ::= <defined_type> | <defined_type> <defined_type_list>
 * <defined_type> ::= struct ID { <field_list> };
 * <field_list> ::= <decl>; | <decl>; <field_list>
 *
 * <program> ::= int main() { <list_instr> return 0; }
 * <list_instr> ::= <instr> | <instr> <list_instr>
 * <instr> ::= <simple_instr>; | <condition_operation> | <loop_operation>
 * <simple_instr> ::= <decl> | <read> | <operation> | <print>
 *
 * <decl> ::= <val_type> <list_ID> | <val_type> ID = <expr>
 * <list_ID> ::= ID | ID, <list_ID>
 * <val_type> ::= int | float
 * <var> ::= ID | ID.ID
 * <operation> ::= <var> = <expr>
 * <expr> ::= <operand> | <operand> <operation_type> <expr>
 * <operand> ::= CONST | <var>
 * <operation_type> ::= + | - | * | / | %
 *
 * <read> ::= std::cin >> ID
 * <print> ::= std::cout << <print_item>
 * <print_item> ::= <expr> | CHAR
 *
 * <condition_operation> ::= if ( <conditions> ) { <list_instr> } else { <list_instr> }
 * <loop_operation> ::= while ( <conditions> ) { <list_instr> }
 * <conditions> ::= <condition> | <condition> <and_or> <conditions>
 * <condition> ::= <expr> <compare> <expr> | <expr>
 * <and_or> ::= && | ||
 * <compare> ::= == | != | <= | < | > | >=
 *
 * ID ::= ^[a-zA-Z][a-zA-Z0-9]*$      (must not be a keyword: int, float, struct, if, else, while, return, main)
 * CONST ::= ^-?[0-9]+(\.[0-9]+)?$
 * CHAR ::= '\n'
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