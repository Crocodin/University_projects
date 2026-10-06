### To compile the code

```yaml
g++ -std=c++20 file.cpp -o file.run
```

### MLP
This MLP is one for all the problems. In each file there is an MLP that can be applied only for that one problem and not for the others too.

```html
    <program> ::= int main() { <list_instr> return 0; }
    <list_instr> ::= <instr>; | <instr>; <list_instr>
    <instr> ::= <decl> | <read> | <operation> | <loop_operation> | <condition_operation> | <print>
    <decl> ::= <val_type> <list_ID> | <val_type> <operation>
    <list_ID> ::= ID | ID, <list_ID>
    <val_type> ::= int | float
    <read> ::= std::cin >> ID
    <operation> ::= ID = <operation_start> <operation_ID>
    <operation_strat> ::= <operation_ID> <operation_type> | <operation_ID> <operation_type> <operation_start>
    <operation_ID> ::= CONST | ID
    <operation_type> ::= + | *
    <condition_operation> ::=  if ( <conditions> ) { <list_instr> } else { <list_instr> }
    <loop_operation> ::= while ( <conditions> ) { <list_instr> }
    <conditions> ::= <conditions> | <conditions> <and_or> <conditions>
    <and_or> ::= && | ||
    <conditions> ::= <operation_start> <operation_ID> <compare> <operation_start> <operation_ID> | <operation_ID> <compare> <operation_ID>
    <compare> ::= == | != | <= | < | > | >=
    <print> ::= std::cout << <list_print>
    <print_list> ::= ID | ID << <print_list>
    ID ::= ^[a-zA-Z][a-zA-Z0-9]*$
    CONST ::= ^[0-9]+$
```

1. Circle Math

```c++
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
```
This code contains error that are error in the c++ language and for whom we specified a subset in the MLP
```c++
#include <iostream>

int main() {
	int r, p, a, pi;
	3.14 = pi;
	std::cin << r;
	p = 2 * pi * r;
	a = pi * r * r;
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