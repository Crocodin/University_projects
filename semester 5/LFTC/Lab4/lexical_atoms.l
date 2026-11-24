%{
#include <stdio.h>
#include "grammer.tab.h"

int lex_errors = 0;
static void error(const char *msg, const char *text);
%}

%option noyywrap yylineno

ID         [a-zA-Z_][a-zA-Z0-9_]*
SUFFIX     ([uU]?([lL]{1,2}|[zZ]))|(([lL]{1,2}|[zZ])?[uU])
HEXA       0[xX][0-9a-fA-F]+{SUFFIX}?
BIN        0[bB][01]+{SUFFIX}?
OCT        0[0-7]*{SUFFIX}?
DEC        [1-9][0-9]*{SUFFIX}?
INT        {HEXA}|{BIN}|{OCT}|{DEC}
REAL       [0-9]+\.[0-9]+
CHAR       '\\n'
CONST      {INT}|{REAL}
BAD_CONST  ({INT}|{REAL}){ID}
BAD_OCT    0[0-9]*[89][0-9]*

%%
[ \t\r\n]+      { /* skip */ }

"int"           { return INT; }
"float"         { return FLOAT; }
"struct"        { return STRUCT; }
"if"            { return IF; }
"else"          { return ELSE; }
"while"         { return WHILE; }
"return"        { return RETURN; }
"main"          { return MAIN; }
"include"       { return INCLUDE; }
"std"           { return STD; }
"cin"           { return CIN; }
"cout"          { return COUT; }

"=="            { return EQ; }
"!="            { return NE; }
"<="            { return LE; }
">="            { return GE; }
"&&"            { return AND; }
"||"            { return OR; }
"<<"            { return SHL; }
">>"            { return SHR; }
"::"            { return SCOPE; }

[-+*/%=<>.(){};,#]   { return yytext[0]; }

{CHAR}          { return CHAR; }
{CONST}         { return CONST; }
{BAD_CONST}     { error("invalid token", yytext); }
{BAD_OCT}       { error("invalid octal constant", yytext); }
{ID}            { return ID; }
.               { error("unknown character", yytext); }
%%

static void error(const char *msg, const char *text) {
  printf("Line %d: lexical error: %s '%s'\n", yylineno, msg, text);
  lex_errors++;
}
