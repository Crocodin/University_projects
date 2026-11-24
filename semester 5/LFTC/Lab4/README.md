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

<read> ::= std::cin >> <var>
<print> ::= std::cout << <print_item>
<print_item> ::= <expr> | CHAR

<condition_operation> ::= if ( <conditions> ) { <list_instr> } else { <list_instr> }
<loop_operation> ::= while ( <conditions> ) { <list_instr> }
<conditions> ::= <condition> | <condition> <and_or> <conditions>
<condition> ::= <expr> <compare> <expr> | <expr>
<and_or> ::= && | ||
<compare> ::= == | != | <= | < | > | >=

ID         [a-zA-Z_][a-zA-Z0-9_]*
SUFFIX     (?:[uU]?(?:[lL]{1,2}|[zZ]))|(?:(?:[lL]{1,2}|[zZ])?[uU])
HEXA       0[xX][0-9a-fA-F]+{SUFFIX}?
BIN        0[bB][01]+{SUFFIX}?
OCT        0[0-7]*{SUFFIX}?
DEC        [1-9][0-9]*{SUFFIX}?
INT        {HEXA}|{BIN}|{OCT}|{DEC}
REAL       [0-9]+\.[0-9]+
CHAR       '\\n'
CONST      {INT}|{REAL}|{CHAR}
BAD_CONST  ({INT}|{REAL}){ID}
BAD_OCT    0[0-9]*[89][0-9]*
```

>[!NOTE]
>According to this [website](https://en.cppreference.com/cpp/language/integer_literal) there are 4 types of such literals having sufixes.

>[!IMPORTANT]
>Run with bison -v to not have shift/reduce or reduce/reduce