import re
import sys

KEYWORDS = ["int", "float", "struct", "if", "else", "while", "return", "main", "include", "std", "cin", "cout"]
OPERATORS = ["==", "!=", "<=", ">=", "&&", "||", "<<", ">>", "+", "-", "*", "/", "%", "=", "<", ">", "."]
SEPARATORS = ["::", "(", ")", "{", "}", ";", ",", "#"]

CODES = {"ID": 0, "CONST": 1}
CODES.update({atom: k for k, atom in enumerate(KEYWORDS + OPERATORS + SEPARATORS, start=2)})

# longest symbols first -> longest match
SYMBOLS = "|".join(re.escape(s) for s in sorted(OPERATORS + SEPARATORS, key=len, reverse=True))

REGEX = re.compile(rf"""
    (?P<SKIP>\s+)
  | (?P<CHAR>'\\n')
  | (?P<BAD_NUM>\d+(?:\.\d+)?[a-zA-Z][a-zA-Z0-9]*)
  | (?P<CONST>\d+(?:\.\d+)?)
  | (?P<WORD>[a-zA-Z][a-zA-Z0-9]*(?:\.[a-zA-Z][a-zA-Z0-9]*)?)
  | (?P<SYMBOL>{SYMBOLS})
  | (?P<UNKNOWN>.)
""", re.VERBOSE)


def category(atom):
    if atom in KEYWORDS:
        return "KEYWORD"
    return "OPERATOR" if atom in OPERATORS else "SEPARATOR"


def scan(src: str):
    atoms, errors = [], []

    for line_index, line in enumerate(src.splitlines(), start=1):
        for m in REGEX.finditer(line):
            kind, text = m.lastgroup, m.group()

            if kind == "SKIP":
                continue

            elif kind in ("CONST", "CHAR"):
                atoms.append((CODES["CONST"], "CONST", text, line_index))
            
            elif kind == "WORD":
                if text in KEYWORDS:
                    atoms.append((CODES[text], "KEYWORD", text, line_index))
                else:
                    atoms.append((CODES["ID"], "ID", text, line_index))
                
            elif kind == "SYMBOL":
                atoms.append((CODES[text], category(text), text, line_index))
                
            elif kind == "BAD_NUM":
                errors.append((line_index, f"invalid token '{text}'"))
            else:
                errors.append((line_index, f"unknown character '{text}'"))

    return atoms, errors


def main():
    if len(sys.argv) < 2:
        sys.exit("Usage: python lexer.py program.cpp [output.txt]")

    with open(sys.argv[1], encoding="utf-8") as f:
        atoms, errors = scan(f.read())

    out = open(sys.argv[2], "w", encoding="utf-8") if len(sys.argv) > 2 else sys.stdout

    print("=== ATOM CODES ===", file=out)
    for atom, code in CODES.items():
        print(f"{code:>3}  {atom}", file=out)
    print()

    print("=== ATOMS ===", file=out)
    print(f"{'LINE':<6}{'CODE':<6}{'KIND':<11}TEXT", file=out)
    for code, kind, text, line in atoms:
        print(f"{line:<6}{code:<6}{kind:<11}{text}", file=out)
    print()

    print("=== LEXICAL ERRORS ===", file=out)
    if errors:
        for line, msg in errors:
            print(f"Line {line}: {msg}", file=out)
    else:
        print("none", file=out)

    if out is not sys.stdout:
        out.close()


if __name__ == "__main__":
    main()