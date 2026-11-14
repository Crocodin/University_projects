%{
#include <stdio.h>
#include <stdlib.h>
void emit(int code, const char *kind, const char *text);
void error(const char *msg, const char *text);
%}

%option noyywrap yylineno

ID         [a-zA-Z][a-zA-Z0-9]*
CONST      [0-9]+(\.[0-9]+)?
CHAR       '\\n'
BAD_CONST  {CONST}{ID}

%%
[ \t\r\n]+      { /* skip */ }

"int"           { emit(2, "KEYWORD", yytext); }
"float"         { emit(3, "KEYWORD", yytext); }
"struct"        { emit(4, "KEYWORD", yytext); }
"if"            { emit(5, "KEYWORD", yytext); }
"else"          { emit(6, "KEYWORD", yytext); }
"while"         { emit(7, "KEYWORD", yytext); }
"return"        { emit(8, "KEYWORD", yytext); }
"main"          { emit(9, "KEYWORD", yytext); }
"include"       { emit(10, "KEYWORD", yytext); }
"std"           { emit(11, "KEYWORD", yytext); }
"cin"           { emit(12, "KEYWORD", yytext); }
"cout"          { emit(13, "KEYWORD", yytext); }
"=="            { emit(14, "OPERATOR", yytext); }
"!="            { emit(15, "OPERATOR", yytext); }
"<="            { emit(16, "OPERATOR", yytext); }
">="            { emit(17, "OPERATOR", yytext); }
"&&"            { emit(18, "OPERATOR", yytext); }
"||"            { emit(19, "OPERATOR", yytext); }
"<<"            { emit(20, "OPERATOR", yytext); }
">>"            { emit(21, "OPERATOR", yytext); }
"+"             { emit(22, "OPERATOR", yytext); }
"-"             { emit(23, "OPERATOR", yytext); }
"*"             { emit(24, "OPERATOR", yytext); }
"/"             { emit(25, "OPERATOR", yytext); }
"%"             { emit(26, "OPERATOR", yytext); }
"="             { emit(27, "OPERATOR", yytext); }
"<"             { emit(28, "OPERATOR", yytext); }
">"             { emit(29, "OPERATOR", yytext); }
"."             { emit(30, "OPERATOR", yytext); }
"::"            { emit(31, "SEPARATOR", yytext); }
"("             { emit(32, "SEPARATOR", yytext); }
")"             { emit(33, "SEPARATOR", yytext); }
"{"             { emit(34, "SEPARATOR", yytext); }
"}"             { emit(35, "SEPARATOR", yytext); }
";"             { emit(36, "SEPARATOR", yytext); }
","             { emit(37, "SEPARATOR", yytext); }
"#"             { emit(38, "SEPARATOR", yytext); }

{CHAR}          { emit(1, "CONST", yytext); }
{BAD_CONST}     { error("invalid token", yytext); }
{CONST}         { emit(1, "CONST", yytext); }
{ID}            { emit(0, "ID", yytext); }
.               { error("unknown character", yytext); }
%%

void emit(int code, const char *kind, const char *text) {
  fprintf(yyout, "%-6d%-6d%-11s%s\n", yylineno, code, kind, text);
}

void error(const char *msg, const char *text) {
  fprintf(yyout, "Line %d: %s '%s'\n", yylineno, msg, text);
}

int main(int argc, char *argv[]) {
  if (argc != 3) {
    printf("Invalid usage!\nusage: lexical_analizator input output");
    return 1;
  }
  const char *in_name = argv[1];
  const char *out_name = argv[2];

  FILE *fptr = fopen(in_name, "r");
  if (fptr == NULL) {
    fprintf(stderr, "The input file '%s' is not opened.\n", in_name);
    return 1;
  }

  FILE *optr = fopen(out_name, "w");
  if (optr == NULL) {
    fprintf(stderr, "The output file '%s' is not opened.\n", out_name);
    fclose(fptr);
    return 1;
  }

  yyin = fptr;
  yyout = optr;

  yylex();

  fclose(fptr);
  fclose(optr);
  return 0;
}