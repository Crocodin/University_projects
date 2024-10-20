
def meniu() -> None:
  '''
  --------------------------// meniu //--------------------------
                       complex numbers meneger
    
      1. Adauga un numar
      2. Stergere/Modificare
      3. Cautare de numere
      4. Filtrare
      5. Afiseaza lista

               U. Undo                         E. Exit
  '''
  print("--------------------------// meniu //--------------------------\n                     complex numbers meneger\n\n    1. Adauga un numar\n    2. Stergere/Modificare\n    3. Cautare de numere\n    4. Filtrare\n    5. Afiseaza lista\n\n            U. Undo                         E. Exit\n\n")

def task1() -> None:
  '''
    -------------------// meniu de adaugare //-------------------

      1. Adauga un numar complex la finalul listei
      2. Adauga pe ce pozitie vrei

               U. Undo                         E. Exit
  '''
  print("-------------------// meniu de adaugare //-------------------\n\n  1. Adauga un numar complex la finalul listei\n  2. Adauga pe ce pozitie vrei\n\n           U. Undo                         E. Exit\n\n")

def task1_add_end() -> None:
  '''
    -------------------// meniu de adaugare //-------------------

        Ce numar vrei sa adaugi de forma (a + bi)? 
  '''
  print("-------------------// meniu de adaugare //-------------------\n\n    Ce numar vrei sa adaugi de forma (a + bi)?\n")

def added_succesfuly() -> None:
  '''
    -------------------// ADDED SUCCESFULY //-------------------
  '''
  print("-------------------// ADDED SUCCESFULY //-------------------")

def task1_add_index_measge1() -> None:
  '''
    -------------------// meniu de adaugare //-------------------

        Pe ce pozitie vrei sa pui acest numar, incepand de la 0?

        >>> optiune
        Ce numar vrei sa adaugi de forma (a + bi)? 
  '''
  print("-------------------// meniu de adaugare //-------------------\n\n        Pe ce pozitie vrei sa pui acest numar, incepand de la 0?\n")

def task1_add_index_measge2() -> None:
  '''
    -------------------// meniu de adaugare //-------------------

        Pe ce pozitie vrei sa pui acest numar, incepand de la 0?

        >>> optiune
        Ce numar vrei sa adaugi de forma (a + bi)? 
  '''
  print("    Ce numar vrei sa adaugi de forma (a + bi)?")

def vlaue_error() -> None:
  '''
    -------------------// INVALID NUMBER //-------------------
  '''
  print("-------------------// INVALID NUMBER //-------------------")

def task2() -> None:
  '''
    -------------------// stergere/modificare //-------------------

      1. Sterge un numar de pe ce pozitie vrei
      2. Sterge numerele de pe ce interval vrei tu; interval deschis
      3. Inlocuieste toate aparitiile unui numar complex cu un alt numar complex

               U. Undo                         E. Exit
  '''
  print("-------------------// stergere/modificare //-------------------\n\n  1. Sterge un numar de pe ce pozitie vrei\n  2. Sterge numerele de pe ce interval vrei tu; interval deschis\n  3. Inlocuieste toate aparitiile unui numar complex cu un alt numar complex\n\n           U. Undo                         E. Exit\n")

def task2_pop() -> None:
  '''
    -------------------// stergere/modificare //-------------------

        Pe ce pozitie se afla numarul pe care vrei sa-l stergi?
      
  '''
  print("-------------------// stergere/modificare //-------------------\n\n    Pe ce pozitie se afla numarul pe care vrei sa-l stergi?\n\n")

def task2_pop_interval() -> None:
  '''
    -------------------// stergere/modificare //-------------------

        Pe ce interval se afla numarul pe care vrei sa-l stergi?
      
  '''
  print("-------------------// stergere/modificare //-------------------\n\n    Pe ce pozitie se afla numarul pe care vrei sa-l stergi?\n\n")

def inlocuire_aparitii() -> None:
  '''
    -------------------// stergere/modificare //-------------------
  '''
  print("-------------------// stergere/modificare //-------------------")

def task3() -> None:
  '''
    --------------------// cautare de numere/secvente //---------------------

        1. Tipareste parte intrega pentru numerle din intervalul tau
        2. Tiparește toate numerele complexe care au modului mai mic decat 10
        3. Tiparește toate numerele complexe care au modului egal cu 10
        4. Suma numerelor dintr-o subsecventa data
        5. Produsul numerelor dintr-o subsecventa data
        6. Tipareste lista sortata descrescator dupa partea imaginara

                 U. Undo                         E. Exit
  '''
  print("--------------------// cautare de numere/secvente //---------------------\n\n    1. Tipareste parte intrega pentru numerle din intervalul tau\n    2. Tiparește toate numerele complexe care au modului mai mic decat 10\n    3. Tiparește toate numerele complexe care au modului egal cu 10\n    4. Suma numerelor dintr-o subsecventa data\n    5. Produsul numerelor dintr-o subsecventa data\n    6. Tipareste lista sortata descrescator dupa partea imaginara\n\n             U. Undo                         E. Exit\n")

def task3_1() -> None:
  '''
    --------------------// cautare de numere/secvente //---------------------
        Tipareste parte intrega pentru numerle din intervalul tau
    
  '''
  print("--------------------// cautare de numere/secvente //---------------------\n    Tipareste parte intrega pentru numerle din intervalul tau\n\n")

def task4() -> None:
  '''
    ------------------------// filtrare //-------------------------

        1. Elimina numerele complexe cu parte intreaga prima
        2. Elimina numerele cu modulul <, = sau > decat un numar dat de tine

                 U. Undo                         E. Exit
  '''
  print("------------------------// filtrare //-------------------------\n\n    1. Elimina numerele complexe cu parte intreaga prima\n    2. Elimina numerele cu modulul <, = sau > decat un numar dat de tine\n\n             U. Undo                         E. Exit\n")

def task4_2_1() -> None:
  '''
    ------------------------// filtrare //-------------------------

        Cu ce valoare vrei sa compari modulul?

        >>> optiune
        Cum vrei sa fie acesta fata de modul? (<, = sau >)
  '''
  print("------------------------// filtrare //-------------------------\n\n    Cu ce valoare vrei sa compari modulul?\n")

def task4_2_2() -> None:
  print("    Cum vrei sa fie acesta fata de modul? (<, = sau >)\n")
