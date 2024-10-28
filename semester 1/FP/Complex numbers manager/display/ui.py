
def invalid_float() -> None:
    """
        --------------------// this is not a hole number //--------------------
    """
    print("--------------------// this is not a hole number //--------------------", end='\n')

def invalid_string() -> None:
    """
        --------------------// this is a string, need a hole number //--------------------
    """
    print("--------------------// this is a string, need a hole number //--------------------", end='\n')

def invalid_index() -> None:
    """
        --------------------// invalid number(s) //--------------------
    """
    print("--------------------// invalid number(s) //--------------------", end='\n')

def success() -> None:
    """
        ----------------------// success //----------------------
    """
    print("----------------------// success //----------------------")

def main_menu() -> None:
    """
       ----------------------------// meniu //----------------------------
                            complex numbers meneger

            1. Adauga un numar
            2. Stergere/Modificare
            3. Cautare de numere
            4. Operatii cu lista
            5. Filtrare
            6. Afiseaza lista

                    U. Undo                         E. Exit
    """
    print("----------------------------// meniu //----------------------------\n                     complex numbers meneger\n     1. Adauga un numar\n     2. Stergere/Modificare\n     3. Cautare de numere\n     4. Operatii cu lista\n     5. Filtrare\n     6. Afiseaza lista\n\n                U. Undo                         E. Exit")

def add_numbers_menu() -> None:
    """
        ----------------------// meniu de adaugare //----------------------

            1. Adauga un numar complex la finalul listei
            2. Adauga un numar pe ce pozitie vrei

                    U. Undo                         B. Back
    """
    print("----------------------// meniu de adaugare //----------------------\n\n        1. Adauga un numar complex la finalul listei\n        2. Adauga un numar pe ce pozitie vrei\n\n                U. Undo                         B. Back")

def modificare_lista_menu() -> None:
    """
        ----------------------// meniu de modificare //----------------------

            1. Sterge un element
            2. Sterge elementele de pe un interval
            3. Inlocuieste aparitiile unui numar

                    U. Undo                         B. Back
    """
    print("----------------------// meniu de modificare //----------------------\n\n        1. Sterge un element\n        2. Sterge elementele de pe un interval\n        3. Inlocuieste aparitiile unui numar\n\n                U. Undo                         B. Back")

def print_functions_menu() -> None:
    """
        ----------------------// cautare de numere //----------------------

            1. Tipareste parte imaginara a numerelor
            2. Tipareste nr. cu modulul <, =, > ca 10

                    U. Undo                         B. Back
    """
    print("----------------------// cautare de numere //----------------------\n\n        1. Tipareste parte imaginara a numerelor\n        2. Tipareste nr. cu modulul <, =, > ca 10\n\n                U. Undo                         B. Back")

def operatii_cu_lista() -> None:
    """
        ----------------------// operatii cu lista //----------------------

            1. Suma numerelor dintr-o subsecvență dată
            2. Produsul numerelor dintr-o subsecvență dată
            3. Tipărește lista sortată descrescător după partea imaginară

                    U. Undo                         B. Back
    """
    print("\n----------------------// operatii cu lista //----------------------\n\n        1. Suma numerelor dintr-o subsecvență dată\n        2. Produsul numerelor dintr-o subsecvență dată\n        3. Tipărește lista sortată descrescător după partea imaginară\n\n                U. Undo                         B. Back")

def filtrarea_listei() -> None:
    """
        ----------------------// filtrare lista //----------------------

            1. Filtre parte reală prin
            2. Filtrare modul - elimină din listă nr la care modulul este <, = sau > decât un nr dat de tine

                    U. Undo                         B. Back
    """
    print("----------------------// filtrare lista //----------------------\n\n        1. Filtre parte reală prin\n        2. Filtrare modul - elimină din listă nr la care modulul este <, = sau > decât un nr dat de tine\n\n                U. Undo                         B. Back")

def print_modules_menu() -> None:
    """
            Cum vrei sa fie modulu? (<, =, >)
    """
    print("    Cum vrei sa fie modulu? (<, =, >)")

def print_number_comperation() -> None:
    """
            Comparativ cu ce numar?
    """
    print("     Comparativ cu ce numar?")

def mai_mare_mic_egal() -> None:
    """
            Sa fie <, = sau >?
    """
    print("     Sa fie <, = sau >?")

def input_complex_number() -> None:
    """
            Ce numar complex?
    """
    print("    Ce numar complex?")

def swap_value_message() -> None:
    """
            Ce numar complex vrei sa inlocuiesti?
    """
    print("    Ce numar complex vrei sa inlocuiest?")

def add_input_index() -> None:
    """
            Pe ce pozitie?
    """
    print("     Pe ce pozitie?")

def pop_input_index_start() -> None:
    """
            De pe ce pozitie?
    """
    print("     De pe ce pozitie?")

def pop_input_index_stop() -> None:
    """
            Pana pe ce pozitie?
    """
    print("     Pana pe ce pozitie?")

def input_index() -> None:
    """
            Pe ce pozitie?
    """
    print("     Pe ce pozitie?")

def get_choice() -> str:
    return input("        >>> ").upper().strip()

