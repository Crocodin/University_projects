import backend

def modul_real() -> None:
  assert backend.modul_real(-12) == 12
  assert backend.modul_real(12) == 12
  assert backend.modul_real() == 0

def prim() -> None:
  assert backend.prim(25) == False
  assert backend.prim(12) == False
  assert backend.prim(13) == True
  assert backend.prim(-7) == True
  assert backend.prim(-80) == False
  assert backend.prim() == False

def numar_complex() -> None:
  assert backend.numar_complex('1 + 2I', [True]) == [1, 2]
  assert backend.numar_complex('14 - 98I', [True]) == [14, -98]
  assert backend.numar_complex('4I', [True]) == [0, 4]
  assert backend.numar_complex('55', [True]) == [55, 0]
  assert backend.numar_complex('4I + 76', [True]) == [0, 0]
  assert backend.numar_complex('erfsfgI', [True]) == [0, 0]
  assert backend.numar_complex('12 + 4rI', [True]) == [0, 0]
  assert backend.numar_complex('98 76I', [True]) == [0, 0] 
  assert backend.numar_complex('21 + 23', [True]) == [0, 0]

def add_end() -> None:
  manager = backend.CreateManager()
  assert manager == {'current': [], 'past': []}
  backend.add_end(manager, '12 - 4I', [False]) 
  assert manager == {'current': [[12, -4]], 'past': [[]]}
  backend.add_end(manager, '-9 - 4I', [False]) 
  assert manager == {'current': [[12, -4], [-9, -4]], 'past': [[], [[12, -4]]]}
  del manager

def add_index() -> None:
  manager = {'current': [[12, -4], [-9, -4]], 'past': [[], [[12, -4]]]}
  backend.add_index(manager, '-32I', 0, [False])
  assert manager == {'current': [[0, -32], [12, -4], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]]]}
  backend.add_index(manager, '21 - 9I', 1, [False])
  assert manager == {'current': [[0, -32], [21, -9], [12, -4], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]]]}
  del manager

def undo() -> None:
  manager = {'current': [[0, -32], [21, -9], [12, -4], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]]]}
  backend.undo(manager)
  print(manager)
  assert manager == {'current': [[0, -32], [12, -4], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]]]}
  backend.undo(manager)
  assert manager == {'current': [[12, -4], [-9, -4]], 'past': [[], [[12, -4]]]}
  backend.undo(manager)
  backend.undo(manager)
  assert manager == {'current': [], 'past': []}
  backend.undo(manager)
  assert manager == {'current': [], 'past': []}
  del manager

def task2_pop() -> None:
  manager = {'current': [[12, -4], [-9, -4]], 'past': [[], [[12, -4]]]}
  backend.task2_pop(manager, 1)
  assert manager == {'current': [[12, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]]]}
  del manager

def task2_pop_interval() -> None:
  manager = {'current': [[0, -32], [21, -9], [12, -4], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]]]}
  backend.task2_pop_interval(manager, 1, 3)
  assert manager == {'current': [[0, -32]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4]]]}
  backend.undo(manager)
  backend.task2_pop_interval(manager, 1, 2)
  assert manager == {'current': [[0, -32], [-9, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4]]]}
  del manager

def inlocuire_aparitii() -> None:
  manager = {'current': [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8], [12, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8]]]}
  backend.inlocuire_aparitii(manager, [12, -4], [101, 101])
  assert manager == {'current': [[0, -32], [21, -9], [101, 101], [-9, -4], [101, 101], [32, -8], [101, 101]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8], [12, -4]]]}
  del manager

def sum() -> None:
  manager = backend.CreateManager()
  assert backend.sum(manager) == [0, 0]
  backend.add_end(manager, '12 + 3I', [False])
  assert backend.sum(manager) == [12, 3]
  backend.add_end(manager, '21 - 4I', [False])
  assert backend.sum(manager) == [33, -1]
  del manager

def prdous() -> None:
  assert backend.prdous() == [1, 1]
  assert backend.prdous([1, 1], [1, 1]) == [0, 2]
  assert backend.prdous([0, 0], [8, 6]) == [0, 0]
  assert backend.prdous([1, 1], [1, -1]) == [2, 0]

def prdous_loop() -> None:
  manager = backend.CreateManager()
  backend.add_end(manager, '1 + 1I', [False])
  backend.add_end(manager, '1 - 1I', [False])
  assert backend.prdous_loop(manager) == [2, 0]
  backend.add_end(manager, '12 - 6I', [False])
  assert backend.prdous_loop(manager) == [24, -12]
  del manager

def modul_complex() -> None:
  assert backend.modul_complex([1, 1]) == 2 ** 0.5
  assert backend.modul_complex([-4, 3]) == 5
  assert backend.modul_complex([6, 8]) == 10
  assert backend.modul_complex([-6, -4]) < 8

def sort_list() -> None:
  manager = {'current': [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8], [12, -4]], 'past': [[], [[12, -4]], [[12, -4], [-9, -4]], [[0, -32], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4]], [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8]]]}
  assert backend.sort_list(manager) == [[-9, -4], [12, -4], [12, -4], [12, -4], [32, -8], [21, -9], [0, -32]]

def elim_prim() -> None:
  manager = {'current': [[7, -32], [21, -7], [3, -4], [-9, -4]], 'past': [[]]}
  backend.elim_(manager, lambda a: backend.prim(a[0]))
  assert manager == {'current': [[21, -7], [-9, -4]], 'past': [[], [[7, -32], [21, -7], [3, -4], [-9, -4]]]}

def elim_mod_mic() -> None:
  manager = {'current': [[7, -32], [21, -7], [3, -4], [-6, -4]], 'past': [[]]}
  backend.elim_(manager, lambda a: backend.modul_complex(a) < 10)
  assert manager == {'current': [[7, -32], [21, -7]], 'past': [[], [[7, -32], [21, -7], [3, -4], [-6, -4]]]}

def elim_mod_mare() -> None:
  manager = {'current': [[7, -32], [21, -7], [3, -4], [-6, -4]], 'past': [[]]}
  backend.elim_(manager, lambda a: backend.modul_complex(a) > 10)
  assert manager == {'current': [[3, -4], [-6, -4]], 'past': [[], [[7, -32], [21, -7], [3, -4], [-6, -4]]]}

def elim_mod_egal() -> None:
  manager = {'current': [[3, -9], [21, -7], [3, -4], [-6, -8]], 'past': [[]]}
  backend.elim_(manager, lambda a: backend.modul_complex(a) == 10)
  print(manager)
  assert manager == {'current': [[3, -9], [21, -7], [3, -4]], 'past': [[], [[3, -9], [21, -7], [3, -4], [-6, -8]]]}

if __name__ == "testing":
  modul_real()
  prim()
  numar_complex()
  add_end()
  add_index()
  undo()
  task2_pop()
  task2_pop_interval()
  inlocuire_aparitii()
  sum()
  prdous()
  prdous_loop()
  sort_list()
  modul_complex()
  elim_prim()
  elim_mod_mic()
  elim_mod_egal()
  elim_mod_mare()
  print('\n' * 20)