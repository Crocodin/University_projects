
# Bibliotecă

**TASK:** Scrieți o aplicație pentru o bibliotecă.

## Data types
 - cărți: id, titlu, descriere, autor, etc
 - clienți: id, nume, CNP, etc

## Functionalitați
 - **T1:** gestiunea listei de cărți și clienți.
 - **T2:** adaugă, șterge, modifică, lista de cărți, lista de clienți
 - **T3:** căutare carte, căutare clienți.
 - **T4:** Închiriere/returnare carte
 - **Rapoarte:**
   - Cele mai inchiriate cărți. 
   - Clienți cu cărți închiriate ordonat dupa: nume,  după numărul de cărți închiriate
   - Primi 20% dintre cei mai activi clienți (nume client si numărul de cărți închiriate)

## Cerinte generale
 - Folosiți dezvoltarea iterativă bazat pe funcționalități
 - Identificați și planificați funcționalități pe 3 iterații
 - Folosiți dezvoltare dirijate de teste. Toate funcțiile trebuie testate și specificate
 - Folosiți arhitectură stratificată (UI, Controller, Domain, Repository)
 - Validați datele – pentru intrări invalide, aplicația sa tipărescă mesaje de eroare corespunzătoare – folosiți excepții.
 - Documentația conține: Enunț, lista de funcționalități, planul de iterații. Pentru fiecare funcționalitate: scenariu de rulare

> [!NOTE] 
> **Urmatoarele functii vor putea fi folosirea unui short cut**
> - **print** | **prt** | **p**: afisează lista

> [!NOTE] 
> **Functionalitatea de verificare se afla la fiecare etapa (aceasta nu va fi scrisa pentru a evita repetitia)**[^1]

### *D0:*
- crearea clasei Book care conține:
  -  id: un numar generat automat la momentul inregistrarii
     - verificare id -> sa nu mai existe deja
     - in momentul in care vom adauga inca o carte care este deja in data de baza a bibilotecii vom avea un counter care se va mari
     - vom avea o lista cu toate id-urile cartilor identice
  - nume
  - autor
  - descriere
  - data lansarii
  - editura

### *D1:*
- crearea clasei Client care contine:
    - id: un numar generat automat la momentul inregistrarii
      - verificare id -> sa nu mai existe deja
    - nume
    - prenume
    - CMP:
      - verificare corectitudine
    - data nasterii
      - generata automat pe baza CMP-ului
    - telefon
      - validare: incepe cu 07 are 10 numere
    - adresa


###  *T0:*
 - Crearea unui meniu principal cu mai multe optiune
 - Optinuea de exit: *'e' sau "exit" pentru a iesi din program*
 - Optiunea de und0: *'u' sau "undo" pentru a executa functia de undo*

form `ui/options.py`

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

### *T2:*
- **T2.1:** adaugă 
  - `def __add_client(self, client: Client) -> None`
  - `def __add_book(self, book: Book) -> None`
- **T2.2:** șterge
  - for the delete function we have more the one method for both domain:
    - `def remove_book(self, other: Book) -> None`
    - `def pop_at_index(self, index: int) -> None`
      - removing the book that is index at a specific place
    - `def remove_all_books(self, other: Book) -> None`
      - removing all the book that are equal to the name of this book
    - `def remove_client(self, other: Client) -> None`
- **T2.3:** modifică
  - T2.3.1: lista de cărți
  - T2.3.1: lista de clienți

| Ecran          | Utilizator  | Backend                        |
|----------------|-------------|--------------------------------|
| T2.1           |             |                                |
| T2.2           |             |                                |
| T2.3           |             |                                |
| *exit / undo*  |             |                                |
|                | input       | validare input:                |
|                |             | -> corect: go in the function  |
|                |             | -> greșit: error dose T2 again |
|                | 2.3         |                                |
| T2.3.1         |             |                                |
| T2.3.2         |             |                                |
|                | input       | validare input:                |
|                |             | -> corect: go in the function  |
|                |             | -> greșit: error dose T2 again |

>[!IMPORTANT] 
> This ,as well, is going to be a while and when the input is 'b' or "back" it will break, automatically going back to the main program (or the program that last coled this action)
 
>[!NOTE]
> The "go back" function is going to appear in every function/task, this will not be mentioned again

### *T3:* 
- **T3.1:** căutare carte
- **T3.2:** căutare clienți

| Ecran         | Utilizator  | Backend                        |
|---------------|-------------|--------------------------------|
| T3.1          |             |                                |
| T3.2          |             |                                |
| *exit / undo* |             |                                |
|               | input       | validare input:                |
|               |             | -> corect: go in the function  |
|               |             | -> greșit: error dose T3 again |
|               | 2.3         |                                |
| T2.3.1        |             |                                |
| T2.3.2        |             |                                |
|               | input       | validare input:                |
|               |             | -> corect: go in the function  |
|               |             | -> greșit: error dose T3 again |

All of these functions are in the `bibloteca` domain, this domain is the "repository" for both the `client` and `book` domains. Containing:
`list[Book]` & `list[Client]`
- `def get_book(self, book_name: str) -> Book`
  - gets the book that has this name (the first on found)
- `def get_book_with_id_string(self, id_: str) -> Book`
  - gets the book with a id, we give the id
- `def get_book_with_id(self, other: Book) -> Book`
  - gets the book that has the same id with a different book, we give the book
- `def get_client_with_cnp(self, cnp: str) -> Client`
- `def get_client(self, id_: str) -> Client`

### *T4:* 
- **T4.1:** Închiriere carte
- **T4.2:** Returnare carte

| Ecran         | Utilizator  | Backend                        |
|---------------|-------------|--------------------------------|
| T4.1          |             |                                |
| T4.2          |             |                                |
|               | input       | validare input:                |
|               |             | -> corect: go in the function  |
|               |             | -> greșit: error dose T4 again |





[^1]: the function is going be different for every tipe of variable that needs verified