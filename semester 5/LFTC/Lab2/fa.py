EPS = "eps"

class FAFormatError(Exception):
  """Raised when the input does not respect the file format."""

class FA:
  def __init__(self, states, alphabet, initial, finals, transitions):
    """transitions: dict {(state, symbol_or_EPS): set(of states)}"""
    self.states: list[str] = list(states)             # keeps the order from the file
    self.alphabet: list[str] = list(alphabet)
    self.initial: str = initial
    self.finals: set = set(finals)
    self.transitions: dict[(str, str), str] = transitions

  def is_deterministic(self):
    return all(sym != EPS and len(tgt) == 1 for (_, sym), tgt in self.transitions.items())

  # ------------------------------------------------------------ simulation
  def _eps_closure(self, current):
    stack, closure = list(current), set(current)
    while stack:
      q = stack.pop()
      for next in self.transitions.get((q, EPS), ()):
        if next not in closure:
          closure.add(next)
          stack.append(next)
    return closure

  def _step(self, current, symbol):
    next = set()
    for q in current:
      next |= self.transitions.get((q, symbol), set())
    return self._eps_closure(next)

  def accepts(self, sequence):
    """True if the whole sequence is accepted"""
    current = self._eps_closure({self.initial})
    for ch in sequence:
      current = self._step(current, ch)
      if not current:
        return False
    return bool(current & self.finals)

  def longest_accepted_prefix(self, sequence):
    current = self._eps_closure({self.initial})
    best = 0 if current & self.finals else None
    for i, ch in enumerate(sequence, start=1):
      current = self._step(current, ch)
      if not current:
        break
      if current & self.finals:
        best = i
    return None if best is None else sequence[:best]


def _expand_item(item: str, line_no):
  """'a' -> ['a'];  '0-9' -> ['0', ..., '9']"""
  item = item.strip()
  if len(item) == 1:
    return [item]
  if len(item) == 3 and item[1] == "-":
    lo, hi = item[0], item[2]
    if ord(lo) > ord(hi):
      raise FAFormatError(f"line {line_no}: invalid range '{item}'")
    return [chr(c) for c in range(ord(lo), ord(hi) + 1)]
  raise FAFormatError(f"line {line_no}: invalid symbol or range '{item}'")


def _split_names(text: str):
  return [x.strip() for x in text.split(",") if x.strip()]


def _read_header(lines: str, pos, keyword):
  if pos >= len(lines):
    raise FAFormatError(f"missing '{keyword}:' section")
  line_no, line = lines[pos]
  head, sep, rest = line.partition(":")
  if head.strip() != keyword or not sep:
    raise FAFormatError(f"line {line_no}: expected '{keyword}:'")
  return rest, line_no


def parse_fa(text: str):
  lines = [(i, l.strip()) for i, l in enumerate(text.splitlines(), start=1) if l.strip()]

  rest, _ = _read_header(lines, 0, "states")
  states = _split_names(rest)

  rest, ln = _read_header(lines, 1, "alphabet")
  alphabet = []
  for item in _split_names(rest):
    for s in _expand_item(item, ln):
      if s not in alphabet:
        alphabet.append(s)

  rest, ln = _read_header(lines, 2, "initial")
  initial = rest.strip()

  rest, ln = _read_header(lines, 3, "final")
  finals = _split_names(rest)

  rest, ln = _read_header(lines, 4, "transitions")

  # --- validation of the sections
  if len(set(states)) != len(states):
    raise FAFormatError("duplicate state names")
  if initial not in states:
    raise FAFormatError(f"initial state '{initial}' is not in the state set")
  for f in finals:
    if f not in states:
      raise FAFormatError(f"final state '{f}' is not in the state set")

  # --- transitions
  transitions = {}
  for line_no, line in lines[5:]:
    parts = line.split()
    if len(parts) != 3:
      raise FAFormatError(f"line {line_no}: expected 'from label to'")
    source, label, destination = parts
    for q in (source, destination):
      if q not in states:
        raise FAFormatError(f"line {line_no}: unknown state '{q}'")
    symbols = [EPS] if label == EPS else _expand_item(label, line_no)
    for s in symbols:
      if s != EPS and s not in alphabet:
        raise FAFormatError(f"line {line_no}: symbol '{s}' not in alphabet")
      transitions.setdefault((source, s), set()).add(destination)

  return FA(states, alphabet, initial, finals, transitions)


def read_from_file(path):
  with open(path, encoding="utf-8") as f:
    return parse_fa(f.read())