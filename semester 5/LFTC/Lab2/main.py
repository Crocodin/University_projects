import sys
from fa import FA, EPS, FAFormatError, parse_fa, read_from_file


# ------------------------------------------------------------------ input
def read_from_keyboard():
  print("Enter the State Machine in the same format as the file.")
  print("  states:q0,q1 / alphabet:0-9,a / initial:q0 / final:q1 / transitions:")
  print("  then one 'from label to' per line; finish with an empty line.")
  lines = []
  while True:
    try:
      line = input()
    except EOFError:
      break
    if not line.strip() and lines and lines[-1].strip().startswith("transitions"):
      continue              # allow a blank line right after 'transitions:'
    if not line.strip():
      break
    lines.append(line)
  return parse_fa("\n".join(lines))


# ---------------------------------------------------------------- display
def compress(symbols):
  """['0','1','2','a'] -> '0-2,a'"""
  syms = sorted(symbols, key=ord)
  out, i = [], 0
  while i < len(syms):
    j = i
    while j + 1 < len(syms) and ord(syms[j + 1]) == ord(syms[j]) + 1:
      j += 1
    out.append(syms[i] if i == j else f"{syms[i]}-{syms[j]}" if j > i + 1 else f"{syms[i]},{syms[j]}")
    i = j + 1
  return ",".join(out)


def need_fa(context):
  if context.get("fa") is None:
    print("No State Machine loaded yet (use option 1 or 2).")
    return None
  return context["fa"]


# --------------------------------------------------------------- commands
def cmd_load_file(context):
  path = input("File path: ").strip()
  try:
    context["fa"] = read_from_file(path)
    print("State Machine loaded.", "(deterministic)" if context["fa"].is_deterministic() else "(nondeterministic)")
  except (OSError, FAFormatError) as e:
    print("Error:", e)


def cmd_load_keyboard(context):
  try:
    context["fa"] = read_from_keyboard()
    print("State Machine loaded.", "(deterministic)" if context["fa"].is_deterministic() else "(nondeterministic)")
  except FAFormatError as e:
    print("Error:", e)


def cmd_states(context):
  if fa := need_fa(context):
    print("States:", "{" + ", ".join(fa.states) + "}")


def cmd_alphabet(context):
  if fa := need_fa(context):
    print("Alphabet:", "{" + ", ".join(fa.alphabet) + "}")


def cmd_transitions(context):
  if not (fa := need_fa(context)):
    return
  print("Transitions:")
  grouped = {} # (src, dst) -> symbols
  for (src, sym), targets in fa.transitions.items():
    for dst in targets:
      grouped.setdefault((src, dst), []).append(sym)
  order = {q: i for i, q in enumerate(fa.states)}
  for (src, dst) in sorted(grouped, key=lambda p: (order[p[0]], order[p[1]])):
    syms = grouped[(src, dst)]
    label = "eps" if EPS in syms and len(syms) == 1 else compress(
        [s for s in syms if s != EPS])
    print(f"  {src} --[{label}]--> {dst}")


def cmd_finals(context):
  if fa := need_fa(context):
    print("Final states:", "{" + ", ".join(
        q for q in fa.states if q in fa.finals) + "}")


def cmd_accepts(context):
  if not (fa := need_fa(context)):
    return
  seq = input("Sequence: ").strip()
  print(f"'{seq}' is", "ACCEPTED" if fa.accepts(seq) else "NOT accepted")


def cmd_longest_prefix(context):
  if not (fa := need_fa(context)):
    return
  seq = input("Sequence: ").strip()
  prefix = fa.longest_accepted_prefix(seq)
  if prefix is None:
    print("No prefix of the sequence is accepted.")
  else:
    print(f"Longest accepted prefix: '{prefix}'")


# ------------------------------------------------------------------- menu
MENU = {
  "1": ("Read State Machine from file", cmd_load_file),
  "2": ("Read State Machine from keyboard", cmd_load_keyboard),
  "3": ("Show the set of states", cmd_states),
  "4": ("Show the alphabet", cmd_alphabet),
  "5": ("Show the transitions", cmd_transitions),
  "6": ("Show the set of final states", cmd_finals),
  "7": ("Check if a sequence is accepted", cmd_accepts),
  "8": ("Longest accepted prefix of a sequence", cmd_longest_prefix),
}


def main():
  context = {"fa": None}
  if len(sys.argv) > 1:
    try:
      context["fa"] = read_from_file(sys.argv[1])
    except (OSError, FAFormatError) as e:
      print("Error:", e)
  while True:
    print("\n=== Finite State Machine ===")
    for key, (label, _) in MENU.items():
      print(f" {key}. {label}")
    print(" 0. Exit")
    try:
      choice = input("> ").strip()
    except EOFError:
      break
    if choice == "0":
      break
    if choice in MENU:
      MENU[choice][1](context)
    else:
      print("Invalid option.")


if __name__ == "__main__":
  main()