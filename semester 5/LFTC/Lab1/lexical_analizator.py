
import sys

class BinarySearchTree:
  class Node:
    def __init__(self, atom, type, index):
      self.left = None
      self.right = None
      self.key = atom
      self.index: int = index
      self.type = type

  def __init__(self):
    self.root = None
    self.size = 0 # this is also the index of the node

  def insert(self, key, type):
    if self.root is None:
      self.root = self.Node(key, type, self.size)
      self.size += 1
      return self.root
    
    cur = self.root
    while True:
      if key == cur.key:
        return cur
      
      if cur.key < key:
        if cur.right is None:
          cur.right = self.Node(key, type, self.size)
          self.size += 1
          return cur.right
        cur = cur.right

      else:
        if cur.left is None:
          cur.left = self.Node(key, type, self.size)
          self.size += 1
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

  def write(self, out):
    out.write(f"{"CODE":<10} {"ATOM":<5} {"INDEX":<5}\n")
    for node in self.inorder():
      out.write(f"{node.key:<10} {node.type:<5} {node.index:<5}\n")

def get_atom_codes(file: str) -> dict[str, int]:
  atom_codes = dict()

  for line in file.splitlines():
    if line in "=== ATOM CODES ===":
      continue
    elif line in "=== ATOMS ===":
      return atom_codes
    else:
      code, atom = line.strip().split(maxsplit=1)
      atom_codes[atom] = int(code)

def make_tables(text: str):
  tree = BinarySearchTree()
  fip: list[tuple[int, int]] = []

  reading = False
  skip_header = True

  for line in text.splitlines():
    if not reading:
      if "=== ATOMS ===" in line:
        reading = True
      continue
    if skip_header:
      skip_header = False
      continue
    if "=== LEXICAL ERRORS ===" in line:
      break
    if not line.strip():
      continue
    _, code, type, atom = line.split(None, 3)
    index_in_tf = tree.insert(atom, type).index if type in ("ID", "CONST") else None
    fip.append((int(code), index_in_tf))

  return tree, fip
      

if __name__ == '__main__':
  if len(sys.argv) < 2:
    sys.exit("Usage: python lexer.py atoms.txt [output_prefix]")
  with open(sys.argv[1], encoding="utf-8") as f:
    text = f.read()

  atom_codes = get_atom_codes(text)
  ts, fip = make_tables(text)

  if len(sys.argv) > 2:
    with open(sys.argv[2] + "_TS.txt", "w", encoding="utf-8") as f:
      ts.write(f)
    with open(sys.argv[2] + "_FIP.txt", "w", encoding="utf-8") as f:
      f.write(f"CODE INDEX IN TF\n")
      for code, index in fip:
        f.write(f"{code:<4} {index if index is not None else '-':<4}\n")
  else:
    ts.write(sys.stdout)
    for code, index in fip:
      print("CODE INDEX IN TF")
      print(f"{code:<4}, {index:<4}")