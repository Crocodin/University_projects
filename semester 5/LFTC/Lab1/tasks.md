### To compile the code

```yaml
g++ -std=c++20 file.cpp -o file.run
```

### MLP
This MLP is one for all the problems. In each file there is an MLP that can be applied only for that one problem and not for the others too.

```html
<code> ::= #include <iostream> <program> | #include <iostream> <defined_type_list> <program>
<defined_type_list> ::= <defined_type> | <defined_type> <defined_type_list>
<defined_type> ::= struct ID { <field_list> };
<field_list> ::= <decl>; | <decl>; <field_list>

<program> ::= int main() { <list_instr> return 0; }
<list_instr> ::= <instr> | <instr> <list_instr>
<instr> ::= <simple_instr>; | <condition_operation> | <loop_operation>
<simple_instr> ::= <decl> | <read> | <operation> | <print>

<decl> ::= <val_type> <list_ID> | <val_type> ID = <expr>
<list_ID> ::= ID | ID, <list_ID>
<val_type> ::= int | float
<var> ::= ID | ID.ID
<operation> ::= <var> = <expr>
<expr> ::= <operand> | <operand> <operation_type> <expr>
<operand> ::= CONST | <var>
<operation_type> ::= + | - | * | / | %

<read> ::= std::cin >> ID
<print> ::= std::cout << <print_item>
<print_item> ::= <expr> | CHAR

<condition_operation> ::= if ( <conditions> ) { <list_instr> } else { <list_instr> }
<loop_operation> ::= while ( <conditions> ) { <list_instr> }
<conditions> ::= <condition> | <condition> <and_or> <conditions>
<condition> ::= <expr> <compare> <expr> | <expr>
<and_or> ::= && | ||
<compare> ::= == | != | <= | < | > | >=

ID ::= ^[a-zA-Z][a-zA-Z0-9]*$      (must not be a keyword: int, float, struct, if, else, while, return, main)
CONST ::= ^[0-9]+(\.[0-9]+)?$
CHAR ::= '\n'
```

1. Circle Math

```c++
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
```
This code contains error that are error in the c++ language and for whom we specified a subset in the MLP
```c++
#include <iostream>

struct Circle {
	float radius;
};

int main() {
	float p, a, pi;
	Circle c;
	3.14 = pi;
	std::cin << c.radius;
	p = 2 * pi * c.radius;
	a = pi * c.radius * c.radius;
	std::cout << p << '\n';
	std::cout << a << '\n';
	return 0;
}
```

2. CMMDC

```c++
#include <iostream>

int main() {
	int a, b, cmmdc;
	std::cin >> a >> b;
	if (a == 0 && b == 0) { cmmdc = -1; }
	else {
		while (b) {
			int r = a % b;
			a = b;
			b = r;
		}
		cmmdc = a;
	}
	std::cout << cmmdc;
	return 0;
}
```

3. Sum of N numbers

```c++
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
```
This code contains errors that are error in the MLP language, but not in the c++ language
```c++
#include <iostream>

int main() {
	int n, sum = 0, i = 0;
	std::cin >> n;
	while (i < n) {
		int x;
		std::cin >> x;
		sum += x;
		i = i + 1;
	}
	std::cout << sum;
	return 0;
}
```