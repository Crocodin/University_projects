import backend
import testing
import menus

def task1(manager:dict = {}) -> None:
  while True:
    menus.task1()
    optiune = input("   >>> ").strip().upper()
    match optiune:
      case '1': 
        menus.task1_add_end()
        ok:list = [False]
        while not ok[0]:
          optiune = input("   >>> ").strip().upper()
          backend.add_end(manager, optiune, ok)
        menus.added_succesfuly()
      case '2':
        menus.task1_add_index_measge1()
        ok:list = [False]
        while not ok[0]:
          index = input("   >>> ").strip().upper()
          ok[0] = backend.valid_int(index)
          # some backend to verifi if index is in range
          if ok[0] == True: index = int(index)
          if index < 0 or index > len(manager) - 1: 
            ok[0] = False
            menus.vlaue_error()
        menus.task1_add_index_measge2()
        ok[0] = False
        while not ok[0]:
          optiune = input("   >>> ").strip().upper()
          backend.add_index(manager, optiune, index, ok)
        menus.added_succesfuly()
      case 'U' | 'UNDO': backend.undo(manager)
      case 'E' | 'EXIT': break
      case 'PRINT' | 'P' | 'PRT': backend.print_manager(manager['current'])
      case 'LEN': print(len(manager['current']))
      case _: menus.vlaue_error()

def task2(manager:dict = {}) -> None:
  while True:
    menus.task2()
    optiune = input("   >>> ").strip().upper()
    match optiune:
      case '1':
        menus.task2_pop()
        ok:bool = False
        while not ok:
          index = input("   >>> ").strip().upper()
          #some backend to verifi if index is good
          ok = backend.valid_int(index)
          if ok:
            index = int(index)
            if index < 0 or index > (len(manager['current']) - 1): 
              ok = False
              menus.vlaue_error()
            if ok: backend.task2_pop(manager, int(index))
      case '2': 
        menus.task2_pop_interval()
        ok:list = [False]
        while not ok[0]:
          start = input("De la pozitia?\n   >>> ").strip().upper()
          final = input("Pana la (inclusiv)?\n   >>> ").strip().upper()
          if backend.valid_int(start) and backend.valid_int(final) and int(start) > 0 and int(start) <= int(final) and int(final) < (len(manager['current']) - 1): ok[0] = True
          else: menus.vlaue_error()
          if ok[0]: backend.task2_pop_interval(manager, int(start), int(final))
      case '3': 
        menus.inlocuire_aparitii()
        ok:list = [False]
        while not ok[0]:
          nr1 = input("Ce valoare vrei sa inlocuiesti?\n   >>> ").strip().upper()
          nr1 = backend.numar_complex(nr1, ok)
          if ok[0]:
            nr2 = input("\nCu ce valoare vrei sa inlocuiesti?\n   >>> ").strip().upper()
            nr2 = backend.numar_complex(nr2, ok)
          if ok: backend.inlocuire_aparitii(manager, nr1, nr2)
      case 'U' | 'UNDO': backend.undo(manager)
      case 'PRINT' | 'P' | 'PRT': backend.print_manager(manager['current'])
      case 'LEN': print(len(manager['current']))
      case 'E' | 'EXIT': break
      case _: menus.vlaue_error()

def task_sum(manager:dict) -> None:
  print("       Suma numerelor complexe este:", end=' '), backend.print_complex_number(backend.sum(manager)), print(end='\n')

def task_prod(manager:dict) ->None:
  print("       Produsul numerelor complexe este:", end=' '), backend.print_complex_number(backend.prdous_loop(manager)), print(end='\n')

def task3(manager:dict = {}) -> None:
  while True:
    menus.task3()
    optiune = input("   >>> ").strip().upper()
    match optiune:
      case '1':
        menus.task3_1()
        ok:bool = False
        while not ok:
          start = input("De unde vrei sa afisezi?\n   >>> ").strip().upper()
          final = input("Pana unde vrei sa afisezi?\n   >>> ").strip().upper()
          if backend.valid_int(start) and backend.valid_int(final) and int(start) > 0 and int(start) <= int(final) and int(final) < (len(manager['current']) - 1): ok = True
          else: menus.vlaue_error()
          if ok: backend.task3_1(manager, int(start), int(final))
      case '2': backend.task3_loop(manager, lambda a: backend.modul_complex(a) < 10.0)
      case '3': backend.task3_loop(manager, lambda a: backend.modul_complex(a) == 10.0)
      case '4' | 'SUMA' | 'SUM': task_sum(manager)
      case '5' | 'PROD' | 'PRODUS': task_prod(manager)
      case '6' | 'S' | 'SORT': backend.print_manager(backend.sort_list(manager))
      case 'U' | 'UNDO': backend.undo(manager)
      case 'E' | 'EXIT': break
      case 'LEN': print(len(manager['current']))
      case 'PRINT' | 'P' | 'PRT': backend.print_manager(manager['current'])
      case _: menus.vlaue_error()

def task4(manager:dict = {}) -> None:
  while True:
    menus.task4()
    optiune = input("   >>> ").strip().upper()
    match optiune:
      case '1': backend.elim_(manager, lambda a: backend.prim(a[0]))
      case '2':
        menus.task4_2_1()
        numar:int = 0
        while True:
          numar = input("   >>> ").strip().upper()
          if backend.valid_int(numar): break
          else: menus.vlaue_error()
        ok:bool = False
        numar = int(numar)
        menus.task4_2_2()
        while not ok:
          ok = True
          optiune = input("   >>> ").strip().upper()
          match optiune:
            case '>': backend.elim_(manager, lambda a: backend.modul_complex(a) > numar)
            case '<': backend.elim_(manager, lambda a: backend.modul_complex(a) < numar)
            case '=': backend.elim_(manager, lambda a: backend.modul_complex(a) == numar)
            case 'E' | 'EXIT': break
            case _: 
              ok = False
              menus.vlaue_error()

      case 'U' | 'UNDO': backend.undo(manager)
      case 'E' | 'EXIT': break
      case 'PRINT' | 'P' | 'PRT': backend.print_manager(manager['current'])
      case 'LEN': print(len(manager['current']))
      case _: menus.vlaue_error()

def run():
  manager = backend.CreateManager()
  while True:
    menus.meniu()
    optiune = input("   >>> ").strip().upper()
    match optiune:
      case '1': task1(manager)
      case '2': task2(manager)
      case '3': task3(manager)
      case '4': task4(manager)
      case '5' | 'PRINT' | 'P' | 'PRT': backend.print_manager(manager['current'])
      case 'U' | 'UNDO': backend.undo(manager)
      case 'E' | 'EXIT': break
      case 'S' | 'SUM' | 'SUMA': task_sum(manager)
      case 'PROD' | 'PRODUS': task_prod(manager)
      case 'LEN': print(len(manager['current']))
      case _: menus.vlaue_error()

if __name__ == "__main__":
 run()
