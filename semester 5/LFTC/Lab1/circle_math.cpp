#include <iostream>

struct Circle {
	float radius;
};

int main() {
	float p, a, pi;
	Circle c;
	pi = 3.14;
	std::cin >> c.radius;
	p = 2 * pi * c.radius;
	a = pi * c.radius * c.radius;
	std::cout << p << '\n';
	std::cout << a << '\n';
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
 * ID ::= ^[a-zA-Z][a-zA-Z0-9]*$
 * CONST ::= ^[0-9]+(.[0-9]+)?$
 * CHAR ::= '\n'
*/

// int main() {
// 	int r, p, a, pi;
// 	3.14 = pi;
// 	std::cin << r;
// 	p = 2 * pi * r;
// 	a = pi * r * r;
// 	return 0;
// }