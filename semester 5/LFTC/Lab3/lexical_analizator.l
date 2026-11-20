%{
#include <stdio.h>
#include <stdlib.h>
void emit(int code, const char *kind, const char *text);
void error(const char *msg, const char *text);
%}

%option noyywrap yylineno

ID         [a-zA-Z_][a-zA-Z0-9_]*
SUFFIX     [uUlL]{2}
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
"daca"          { emit(39, "KEYWORD",   yytext); }
"atunci"        { emit(40, "KEYWORD",   yytext); }
"sfdaca"        { emit(41, "KEYWORD",   yytext); }

{CONST}         { emit(1, "CONST", yytext); }
{BAD_CONST}     { error("invalid token", yytext); }
{BAD_OCT}       { error("invalid octal constant", yytext); }
{ID}            { emit(0, "ID",    yytext); }
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
		printf("Invalid usage!\nusage: lexical_analizator input output\n");
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
