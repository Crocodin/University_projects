
def copy_list(lista:list) -> list:
  '''
    face o copie unei liste si returneaza aceasta copie
  '''
  rez = []
  for e in lista:
    rez.append(e)
  return rez

def valid_int(string:str = '') -> bool:
  '''
    primeste un string ca parametru; returneaza 1 daca si numai daca stringul pote fi convertit intr-un int, in caz contrar daca acesta este totus un nunmar se va afisa "Acest numar nu este intreg", iar daca nu "Acesta nu este un numar"
  '''
  try:
    i =  int(string) # tries to convert to a int
    return True
  except:
    try:
      i = float(string) #tries to convert to a float 
      print("Acest numar nu este intreg")
      return False
    except:
      # numarul nu este float si nici int
      print("Acesta nu este un numar")
      return False
    
def modul_complex(number:list = [0, 0]) ->float:
  return ((number[0] ** 2 + number[1] ** 2) ** 0.5)

def modul_real(number:int = 0) -> int:
  '''
    functia primeste un numar intreg si returneaza modulul acestuia
  '''
  if(number < 0): return -number
  return number

def prim(number:int = 0) -> bool:
  '''
    functia primeste un numar intreg; returneaza 1 daca acesta este prim si 0 in caz contrar numerele negative sunt tratate ca numere pozitive, 0 nu este prim
  '''
  number = modul_real(number)
  if(number < 2 or number % 2 == 0): return number == 2
  div = 3
  while div * div <= number:
    if(number % div == 0): return False
    div += 2
  return True

def CreateManager() -> dict:
  '''
    returneaza un dictionarin care cheia 'current' se sefera la fista de valori curente, iar 'past' se refera la o lista care contine toate listele trecute; aceasta functionalitate este utilizata in functia de undo
  '''
  return {'current': [], 'past': []}

def numar_complex(number:str, ok:list) -> list:
  '''
    primeste un string de forma a + bi, transforma acest string intrun vector de forma [a, b]
  '''
  number = number.split()
  if len(number) == 1: # avem ori doar a, ori doar bi
    if number[0][len(number[0]) - 1] == 'I':
      number[0] = number[0].replace('I', '') # get read of the i from the end
      ok[0] = valid_int(number[0])
      if ok[0]:
        return [0, int(number[0])]
    else:
      ok[0] = valid_int(number[0])
      if ok[0]: return [int(number[0]), 0]
  if len(number) != 3: 
    ok[0] = False
    return [0, 0]
  ok[0] = valid_int(number[0])
  if ok[0]:
    if number[2][len(number[2]) - 1] != 'I':
      ok[0] = False
      return [0, 0]
    number[2] = number[2].replace('I', '') # get read of the i from the end
    ok[0] = valid_int(number[2])
    if ok[0]:
      if number[1] == '-': return [int(number[0]), -int(number[2])] 
      return [int(number[0]), int(number[2])]
  return [0, 0]

def add_end(manager:dict = {}, number:str = '', ok:list = [True]) -> None:
  '''
    adauga un numar la final listei
    manager: este un dictionar care contine lista curenta si lista listelor precedente; pentru undo
    number: reprezinta numarul complex de forma (a + bi)
    ok: este un boolean transmis printrun list, cuz' pyton suck, si am nevoie de el pentru a sti daca este un numar valid; asta pentru callul functie task1 cu optiunea 1
  '''
  number = numar_complex(number, ok)
  if ok[0]:
    manager['past'].append(copy_list(manager['current']))
    manager['current'].append(number)

def add_index(manager:dict = {}, number:str = '', index:int = 0, ok:list = [True]):
  '''
    adauga un numar pe o pozitie data
    manager: este un dictionar care contine lista curenta si lista listelor precedente; pentru undo
    number: reprezinta numarul complex de forma (a + bi)
    index: reprezina pozitia pe care inseram numarul complex
    ok: este un boolean transmis printrun list, cuz' pyton suck, si am nevoie de el pentru a sti daca este un numar valid; asta pentru callul functie task1 cu optiunea 2
  '''
  number = numar_complex(number, ok)
  if ok[0]:
    manager['past'].append(copy_list(manager['current']))
    manager['current'].insert(index, number)

def undo(manager:dict) -> None:
  '''
    undos the last operation
  '''
  if len(manager['past']):
    manager['current'] = copy_list(manager['past'][len(manager['past']) - 1])
    manager['past'].pop()
  else: print("No more undos left")

def print_complex_number(lista:list) -> None:
  '''
    for a list that reprezent a and b for a complex number of the a + bi tipe print the reprezentation of the complex number
  '''
  if lista[0] == 0: print(f"{lista[1]}i", end=", ")
  elif lista[1] == 0: print(f"{lista[0]}", end=", ")
  elif lista[1] > 0: 
    if lista[0] < 0: print(f"{lista[1]}i - {-lista[0]}", end=', ')
    else: print(f"{lista[0]} + {lista[1]}i", end=', ')
  elif lista[1] < 0: print(f"{lista[0]} - {-lista[1]}i", end=', ')

def print_manager(lista:list) -> None:
  '''
    prints a list of complex numbers
  '''
  for e in lista:
    print_complex_number(e)
  print('\n')

def task2_pop(manager:dict, index:int) -> None:
  '''
    removes the element at the specifded index
  '''
  manager['past'].append(copy_list(manager['current']))
  manager['current'].pop(index)

def task2_pop_interval(manager:dict, start:int, final:int):
  manager['past'].append(copy_list(manager['current']))
  while(final >= start): 
    manager['current'].pop(start)
    final -= 1
  print('\n')

def inlocuire_aparitii(manager:dict, nr1:list, nr2:list) -> None:
  '''
    inlocuieste toate aparitile lui nr1 din dict cu nr2
    manager: este un dictionar care contine lista curenta si lista listelor precedente; pentru undo
    nr1 si nr: valori de tip int
  '''
  ok:bool = False
  index = 0
  for e in manager['current']:
    if e[0] == nr1[0] and e[1] == nr1[1]:
      if not ok:
        manager['past'].append(copy_list(manager['current']))
        ok = True
      manager['current'][index] = nr2
    index += 1

def task3_1(manager:dict, start:int, final:int):
  '''
    afiseaza partea imagina a numerelor de pe un interval dat
  '''
  while(final >= start): 
    print(f"{manager['current'][start][1]}i", end=', ')
    start += 1
  print('\n')

def task3_loop(manager:dict, functio) -> None:
  '''
    face niste magie =))
    afiseaza numerele din dictionar cu modulul = sau > decat 10, in functie de functia lambda trecuta print parametru se gasesc in interface.py la task3 2 si 3
  '''
  for e in manager['current']:
    if functio(e): print_complex_number(e)
  print('\n')

def sum(manager:dict) -> list:
  '''
    returneaza suma numerlor din dictionar, numerele sunt de forma a + bi
  '''
  suma = [0, 0]
  for e in manager['current']:
    suma[0] += e[0]
    suma[1] += e[1]
  return suma

def prdous(a:list = [1, 1], b:list = [1, 0]) -> list:
  '''
    calculeaza produsul a doua numere reale

    (a + bi)(c + di) = ac - bd + (ad + cb)i
  '''
  return [(a[0] * b[0] - a[1] * b[1]), (a[0] * b[1] + b[0] * a[1])]

def prdous_loop(manager:dict) -> list:
  '''
    returneaza produsul numerelor complexe din dictionar
  '''
  prod = [1, 0] # this is e; e * a = a and a * e = a
  for e in manager['current']:
    prod = prdous(prod, e)
  return prod

def sort_list(manager:dict) -> list:
  '''
    returneaza o copie sortata a listei bazat pe valoarea imaginara a numarului cimplex

    why are we using this dog shit sorting algorithm? cuz' this is python and i can't be bothered to translate a c++ algorithm in this language
  '''
  array = copy_list(manager['current'])
  n:int = len(array)
  for i in range(n - 1):
    swapped = False
    for j in range(0, n - i - 1):
      if array[j][1] < array[j + 1][1] or array[j][1] == array[j + 1][1] and array[j][0] > array[j + 1][0]:
        array[j], array[j + 1] = array[j + 1], array[j]
        swapped = True
    if not swapped: break
  return array

def elim_(manager:dict, functie) -> None:
  '''
    elimina toate numerele cu parte reala prima (the a from a + bi) sau complexe cu modulul <, =, > 
    lambda a: backend.modul_complex(a) <, =, > numar
    lambda a: backend.prim(a[0])
    paramentru functe nu corespunde funtiilorde mai sus calcultorul va exploda :j
  '''
  eliminat:bool = False
  index:int = 0
  while index < len(manager['current']):
    if functie(manager['current'][index]):
      if not eliminat:
        manager['past'].append(copy_list(manager['current']))
        eliminat = True
      manager['current'].pop(index)
      index -= 1
    index += 1
  if not eliminat: print("----------------------// nu s-au gasit astfel de valori //----------------------")
