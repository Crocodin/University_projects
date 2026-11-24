%{
#include <stdio.h>

int yylex(void);
void yyerror(const char *msg);

extern int yylineno;
extern int lex_errors;
extern FILE *yyin;
%}

%define parse.error verbose

%token INCLUDE STRUCT ID CONST CHAR INT FLOAT MAIN RETURN STD CIN COUT IF ELSE WHILE
%token SCOPE "::" SHL "<<" SHR ">>" EQ "==" NE "!=" LE "<=" GE ">=" AND "&&" OR "||"

%%
prog: header opt_types program ;
header: '#' INCLUDE '<' ID '>' ;
opt_types: 
    | defined_type_list
    ;
defined_type_list: defined_type
    | defined_type_list defined_type
    ;
defined_type: STRUCT ID '{' field_list '}' ';' ;
field_list: field
    | field_list field
    ;
field: decl ';' ;

program: INT MAIN '(' ')' '{' list_instr RETURN CONST ';' '}' ;
list_instr: instr
    | list_instr instr
    ;
instr: simple_instr ';'
    | condition_operation
    | loop_operation
    ;
simple_instr: decl | read | operation | print ;

decl: type list_id ;
type: INT | FLOAT | ID ;
list_id: ID
    | list_id ',' ID
    ;
var: ID
    | ID '.' ID
    ;
operation: var '=' expr ;
expr: operand
    | expr operation_type operand
    ;
operand: CONST
    | var
    | '(' expr ')'
    ;
operation_type: '+' | '-' | '*' | '/' | '%' ;

read: STD SCOPE CIN SHR var ;
print: STD SCOPE COUT SHL print_item ;
print_item: expr | CHAR ;

condition_operation: IF '(' conditions ')' '{' list_instr '}' ELSE '{' list_instr '}' ;
loop_operation: WHILE '(' conditions ')' '{' list_instr '}' ;
conditions: condition
    | conditions and_or condition
    ;
condition: expr compare expr
    | expr
    ;
and_or: AND | OR ;
compare: EQ | NE | LE | GE | '<' | '>' ;
%%

void yyerror(const char *msg) {
  printf("Line %d: %s\n", yylineno, msg);
}

int main(int argc, char *argv[]) {
  if (argc != 2) {
    fprintf(stderr, "usage: %s program.cpp\n", argv[0]);
    return 1;
  }
  FILE *f = fopen(argv[1], "r");
  if (f == NULL) {
    fprintf(stderr, "The input file '%s' is not opened.\n", argv[1]);
    return 1;
  }
  yyin = f;

  int rc = yyparse();
  fclose(f);

  if (rc == 0 && lex_errors == 0)
    printf("The program is syntactically correct.\n");
  else
    printf("The program has errors.\n");
  return rc != 0 || lex_errors != 0;
}
