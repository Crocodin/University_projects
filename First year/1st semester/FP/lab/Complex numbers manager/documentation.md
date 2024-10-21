# Complex numbers manager

**TASK** Creați un program care lucrează cu numere complexe (a + bi). Programul gestionează o listă de numere complexe și permite efectuarea repetată a urmatorelor acțiuni:
- T1: adaugă numere în listă
  - adaugă număr complex la sfârșitul listei
  - inserare număr complex pe o poziție dată
- T2: modificare elemente din listă
  - șterge element de pe o poziție dată
  - șterge element de pe un interval de poziții 
  - înlocuiește toate aparițiile unui număr complex cu un alt număr complex
- T3: cautare numere
  - tipărește partea imaginară pentru numerele din listă. Se dă intervalul de pozitii (sub secvențe)
  - tipărește toate numerele complexe care au modului mai mic decât 10
  - tipărește toate numerele complexe care au modului egal 10
- T4: operații cu numerele din listă
  - suma numerelor dintr-o subsecvență dată
  - produsul numerelor dintr-o subsecvență dată
  - tipărește lista sortată descrescător după partea imaginară
- T5: filtrare
  - filtre parte reală prin - elimină din listă numerele complexe la -> care partea reală este primă
  - filtrare modul - elimină din listă numerele complexe la care modulul este <, = sau > decât un număr dat
- T6: undo
  - reface ultima operație (lista de numere revine la numerele ce existau înainte de ultima operație care a modificat lista) - **NU folosesc funcția deepCopy**

## Cerinţe generale:
  - Folosiți procesul de dezvoltare: Incrementală bazată pe funcționalități şi Dezvoltare dirijată de teste 
  - Planificați iterații pentru 3 laboratoare succesive. În fiecare săptămână primiţi o notă pentru ce s-a realizat pentru iterația din săptămâna curentă.
  - Prima iterație trebuie sa conţină cel puțin 3 cerințe (din funcționalităţile 3-5)
  - Documentația trebuie să conțină: enunțul, lista de funcționalități, planul de iterații, scenarii de rulare, lista de taskuri (activităţi)
  - Toate funcțiile trebuie să includă specificații, toate funcțiile trebuie sa fie testate (funcții de test cu assert) în afară de partea cu interacţiunea utilizator.
  - Separați partea de interfață utilizator de restul aplicației (sa nu aveți funcții care fac 2 lucruri: un calcul + tipărire/citire)
  - Datele de intrare trebuie validate, programul semnalează erorile către utilizator.

> [!NOTE] 
> **Urmatoarele functii vor putea fi chemate peste tot prin folosirea unui short cut**
> - **print** | **p**: afisează lista
> - **undo** | **u**: functia de undo, intoarce lista la ultima iteratie <br>

> [!NOTE] 
> **Functionalitatea de verificare se afla la fiecare etapa (aceasta nu va fi scrisa pentru a evita repetitia)**[^1]

###  *T0:*
 - Crearea unui meniu principal cu mai multe optiune
 - Optinuea de exit: *'e' sau "exit" pentru a iesi din program*
 - Optiunea de und0: *'u' sau "undo" pentru a executa functia de undo*

  | Ecran         | Utilizator | Backend                        |
  |---------------|------------|--------------------------------|
  | T1            |            |                                |
  | T2            |            |                                |
  | T3            |            |                                |
  | T4            |            |                                |
  | T5            |            |                                |
  | T6            |            |                                |
  | *exit / undo* |            |                                |
  |               | input      | validare input:                |
  |               |            | -> corect: go in the function  |
  |               |            | -> greșit: error dose T1 again |

###  *T1:* adaugă numere în listă 
- **T1.1**: daugă un număr complex la sfârșitul listei
  - T1.1.1 adaugare

  | Ecran                   | Utilizator | Backend                         |
  |-------------------------|------------|---------------------------------|
  | T1.1                    |            |                                 |
  | ce numar vrei să adaugi | n          | este numarul valid?             |
  |                         |            | -> da: adauga n                 |
  |                         |            | -> nu: eroare face T1.1 din nou |

- **T1.2**: inserare număr complex pe o poziție dată
  - T1.2.1 adaugare pe pozitia data
  - T1.3: crearea unui meniu secundar care contine cele doua moduri de adaugare 
  - T1.4: optiunea de "go back" care ne intoarce la T0

>[!IMPORTANT] 
> This ,as well, is going to be a while and when the input is 'b' or "back" it will break, automatically going back to the main program (or the program that last coled this action)
 
>[!NOTE]
> The "go back" function is going to appear in every function/task, this will not be mentioned again

### *T2:* modificare elemente din listă
- T2.1: șterge element de pe o poziție dată

  | Ecran                             | Utilizator        | Backend                         |
  |-----------------------------------|-------------------|---------------------------------|
  | de pe ce pozitie stergem valoarea | p                 | este pozitia valid?             |
  |                                   |                   | -> da: stergem                  |
  |                                   |                   | -> nu: eroare face T2.1 din nou |
- T2.2: șterge element de pe un interval de poziții

  | Ecran                                        | Utilizator        | Backend                                |
  |----------------------------------------------|-------------------|----------------------------------------|
  | care este pozitia de inceput si cea de final | p1, p2            | -> da: stergem de la p1 la p2 inclusiv |
  |                                              |                   | -> nu: eroare face T2.1 din nou        |

- T2.3: înlocuiește toate aparițiile unui număr complex cu un alt număr complex

  | Ecran                                    | Utilizator     | Backend                                |
  |------------------------------------------|----------------|----------------------------------------|
  | ce numar vrei sa inlocuiesti             | n              | exista numarul?                        |
  |                                          |                | -> da: continua                        |
  |                                          |                | -> nu: mesaj eroare face T2.1 din nou  |
  | cu ce numar vrei sa inlocuiesti valoarea | x              | este numarul valid?                    |
  |                                          |                | -> da: inlocuieste                     |
  |                                          |                | -> nu: mesaj eroare  face T1.2 din nou |

- T2.4 si T2.5 la fel ca **T1.3, respectiv T1.4**
- T2.6 sterge intreaga lista

### *T3:* cautare numere
- T3.1: tipărește partea imaginară pentru numerele din listă. Se dă intervalul de pozitii (sub secvențe)
   T3.1.1 afiseaza partea imaginara a numerelor din subsecventa

  | Ecren                                        | Utilizator    | Backend                                  |
  |----------------------------------------------|---------------|------------------------------------------|
  | care este pozitia de inceput si cea de final |               |                                          |
  |                                              | p1, p2        | este pozitia valid?                      |
  |                                              |               | -> da: tipareste de la p1 la p2 inclusiv |
  |                                              |               | -> nu: mesaj eroare face T3.1 din nou    |

>[!NOTE] 
> Aceste functii vor fi o singura functie cu un parametru lambda
- T3.2: calcularea modulului
- T3.3: tipărește toate numerele complexe care au modului mai mic decât 10
- T3.4: tipărește toate numerele complexe care au modului egal 10

  | Ecren                                        | Utilizator    | Backend                                  |
  |----------------------------------------------|---------------|------------------------------------------|
  | care este pozitia de inceput si cea de final |               |                                          |
  |                                              | p1, p2        | este pozitia valid?                      |
  |                                              |               | -> da: tipareste de la p1 la p2 inclusiv |
  |                                              |               | -> nu: mesaj eroare face T3.1 din nou    |

### *T4:* operații cu numerele din listă
- T4.1: suma numerelor dintr-o subsecvență dată
- T4.2: produsul numerelor dintr-o subsecvență dată

  | Ecran                                        | Utilizator         | Backend                                                                             |
  |----------------------------------------------|--------------------|-------------------------------------------------------------------------------------|
  | care este pozitia de inceput si cea de final |                    |                                                                                     | 
  |                                              | p1, p2             | este pozitia valid?                                                                 |
  |                                              |                    | -> da: it call a function with one of the paramiters a lambda for ider T4.1 or T4.2 |
  |                                              |                    | -> nu: mesaj eroare face T3 din nou                                                 |

- T4.3: tipărește lista sortată descrescător după partea imaginară
>[!IMPORTANT] 
> T4.3.1: copiaza lista in alt vector
- T4.3.2: sorteza lista
- T4.3.3: afiseaza lista

  | Ecran                                        | Utilizator          | Backend                                 |
  |----------------------------------------------|---------------------|-----------------------------------------|
  | care este pozitia de inceput si cea de final |                     |                                         |
  |                                              | p1, p2              | este pozitia valid?                     |
  |                                              |                     | -> da: afiseaza de la p1 la p2 inclusiv |
  |                                              |                     | -> nu: mesaj eroare face T4.3 din nou   |

### *T5:* filtrare
- T5.1: o functie cu un paramteru lambda dupa care face filtrarea
- T5.2: functie de prim modulul este <, = sau > decât un număr dat // avem deva functe pt modul T3.2
      <, =, > is also a paramtier (in the function is a operator match)

  | Ecran                                         | Utilizator       | Backend                                                              |
  |-----------------------------------------------|------------------|----------------------------------------------------------------------|
  | care este functia pe care vreiso indeplinesti |                  |                                                                      | 
  |                                               | p1, p2           | este pozitia valid?                                                  |
  |                                               |                  | -> da: it call a function with one of the paramiters a lambda (T5.1) |
  |                                               |                  | -> nu: mesaj eroare face T3 din nou                                  |

### *T6:* undo
- T4.1.1: functie de copiere
- T6.1: muntarea acestui vector copiat intr-o lista de vectori in care gasit toate iteratiile prin pare am trecut

[^1]: the function is going be different for every tipe of variable that needs verified
