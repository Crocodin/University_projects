import os
import sys
from fa import FA, read_from_file

KEYWORDS = ["int", "float", "struct", "if", "else", "while", "return", "main", "include", "std", "cin", "cout"]
OPERATORS = ["==", "!=", "<=", ">=", "&&", "||", "<<", ">>", "+", "-", "*", "/", "%", "=", "<", ">", "."]
SEPARATORS = ["::", "(", ")", "{", "}", ";", ",", "#"]

CODES = {"ID": 0, "CONST": 1}
CODES.update({a: k for k, a in enumerate(KEYWORDS + OPERATORS + SEPARATORS, start=2)})

SYMBOLS_BY_LENGTH = sorted(OPERATORS + SEPARATORS, key=len, reverse=True)

WHITESPACE = " \t\r\n"

HERE = os.path.dirname(os.path.abspath(__file__))


class BinarySearchTree:
  class Node:
    def __init__(self, key, kind, index):
      self.left = None
      self.right = None
      self.key = key
      self.kind = kind
      self.index = index

  def __init__(self):
    self.root = None
    self.size = 0
    self.nodes = []

  def _new_node(self, key, kind):
    self.size += 1
    node = self.Node(key, kind, self.size)
    self.nodes.append(node)
    return node

  def insert(self, key, kind):
    if self.root is None:
      self.root = self._new_node(key, kind)
      return self.root
    cur = self.root
    while True:
      if key == cur.key:
        return cur
      if cur.key < key:
        if cur.right is None:
          cur.right = self._new_node(key, kind)
          return cur.right
        cur = cur.right
      else:
        if cur.left is None:
          cur.left = self._new_node(key, kind)
          return cur.left
        cur = cur.left

  def inorder(self):
    stack, cur = [], self.root
    while stack or cur:
      while cur:
        stack.append(cur)
        cur = cur.left
      cur = stack.pop()
      yield cur
      cur = cur.right

  def to_vectors(self):
    n = self.size
    atoms = [None] * (n + 1)
    left = [0] * (n + 1)
    right = [0] * (n + 1)
    for node in self.nodes:
      i = node.index
      atoms[i] = node.key
      left[i] = node.left.index if node.left else -1
      right[i] = node.right.index if node.right else -1
    return atoms, left, right

  def write(self, out):
    atoms, left, right = self.to_vectors()
    for i in range(1, self.size + 1):
      out.write(f"{atoms[i]:<8} {left[i]:<3} {right[i]:<3}\n")


def load_automata() -> list[tuple[str, "FA"]]:
  """Order = priority when two automata accept the same length."""
  return [
    ("CHAR", read_from_file(os.path.join(HERE, "char.in"))),
    ("REAL", read_from_file(os.path.join(HERE, "real.in"))),
    ("INT",  read_from_file(os.path.join(HERE, "integer_literals.in"))),
    ("ID",   read_from_file(os.path.join(HERE, "ID.in"))),
  ]


def longest_token(automata: list[tuple[str, "FA"]], line: str, i: int):
  best_kind, best_text = None, ""
  rest = line[i:]
  for kind, fa in automata:
    prefix = fa.longest_accepted_prefix(rest)
    if prefix and len(prefix) > len(best_text):
      best_kind, best_text = kind, prefix
  return best_kind, best_text


def match_symbol(line: str, i: int):
  for s in SYMBOLS_BY_LENGTH:
    if line.startswith(s, i):
      return s
  return None


def scan(src: str, automata: list[tuple[str, "FA"]] | None = None):
  """Returns (ts, fip, errors).
  fip    : list of (atom_code, ts_node or None)
  errors : list of (line, column, message)
  """
  automata = automata or load_automata()
  ts = BinarySearchTree()
  fip, errors = [], []

  for line_no, line in enumerate(src.splitlines(), start=1):
    i = 0
    while i < len(line):
      if line[i] in WHITESPACE:
        i += 1
        continue

      kind, text = longest_token(automata, line, i)

      if kind is None:
        sym = match_symbol(line, i)
        if sym is not None:
          fip.append((CODES[sym], None))
          i += len(sym)
        else:
          errors.append((line_no, i + 1, f"unknown character '{line[i]}'"))
          i += 1
        continue

      end = i + len(text)

      if kind == "ID":
        if text in KEYWORDS:
          fip.append((CODES[text], None))
        else:
          fip.append((CODES["ID"], ts.insert(text, "ID")))
      else:
        fip.append((CODES["CONST"], ts.insert(text, "CONST")))
      i = end

  return ts, fip, errors


def main():
  if len(sys.argv) < 2:
    sys.exit("Usage: python lexer.py program.cpp [output_prefix]")

  with open(sys.argv[1], encoding="utf-8") as f:
    ts, fip, errors = scan(f.read())

  prefix = sys.argv[2] if len(sys.argv) > 2 else "out"

  with open(prefix + "_TS.txt", "w", encoding="utf-8") as f:
    ts.write(f)

  with open(prefix + "_FIP.txt", "w", encoding="utf-8") as f:
    for code, node in fip:               # Theta(1): node.index is already known
      f.write(f"{code} {node.index if node is not None else '-'}\n")

  with open(prefix + "_ERRORS.txt", "w", encoding="utf-8") as f:
    if errors:
      for line, col, msg in errors:
        f.write(f"Line {line}, col {col}: {msg}\n")
    else:
      f.write("none\n")


if __name__ == "__main__":
  main()