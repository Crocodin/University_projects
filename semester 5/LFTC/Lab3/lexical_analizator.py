import os
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
LEXER_NAMES = "lexical_analizator"

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

KINDS = ("ID", "CONST", "KEYWORD", "OPERATOR", "SEPARATOR")

def read_atoms(text):
  atoms, errors = [], []
  for raw in text.splitlines():
    line = raw.strip()
    if not line:
      continue
    parts = line.split(None, 3)
    if (len(parts) == 4 and parts[0].isdigit() and parts[1].isdigit()
        and parts[2] in KINDS):
      atoms.append((int(parts[0]), int(parts[1]), parts[2], parts[3]))
    elif line.startswith("Line "):
      errors.append(line)
  if not atoms and not errors:
    print("Warning: no atoms found in the lexer output.", file=sys.stderr)
  return atoms, errors


def make_tables(atoms):
  ts = BinarySearchTree()
  fip = []                            # (code, ts_node or None)
  for _, code, kind, atom in atoms:
    node = ts.insert(atom, kind) if kind in ("ID", "CONST") else None
    fip.append((code, node))
  return ts, fip


def run_lexer(source, atoms_path):
  """Runs the already compiled flex analyzer: source program -> atoms file."""
  exe = os.path.join(HERE, LEXER_NAMES)
  if exe is None:
    sys.exit("The lexer executable was not found next to tables.py "
             f"(looked for: {', '.join(LEXER_NAMES)}). "
             "Compile it first: flex lexer.l && gcc lex.yy.c -o lexical_analizator")
  subprocess.run([exe, source, atoms_path], check=True)


def main():
  if len(sys.argv) < 2:
    sys.exit("Usage: python tables.py program.cpp [output_prefix]")
  source = sys.argv[1]
  prefix = sys.argv[2] if len(sys.argv) > 2 else "out"
  if not os.path.isfile(source):
    sys.exit(f"The input file '{source}' does not exist.")

  atoms_path = prefix + "_ATOMS.txt"
  run_lexer(source, atoms_path)

  with open(atoms_path, encoding="utf-8") as f:
    atoms, errors = read_atoms(f.read())
  ts, fip = make_tables(atoms)

  with open(prefix + "_TS.txt", "w", encoding="utf-8") as f:
    ts.write(f)
  with open(prefix + "_FIP.txt", "w", encoding="utf-8") as f:
    for code, node in fip:
      f.write(f"{code} {node.index if node is not None else '-'}\n")
  with open(prefix + "_ERRORS.txt", "w", encoding="utf-8") as f:
    f.write("\n".join(errors) + "\n" if errors else "none\n")

  print(f"{len(fip)} atoms, {ts.size} TS entries, {len(errors)} lexical errors")


if __name__ == "__main__":
  main()