import test
from display import ui
from display import prints
from domain import complex_number
from domain import utility


# i have to check if len of undo is > 0 here; the function is made
# i have to check before adding with index, or not if the number is a complex number and make the transformation

def while_not_number(message) -> list[int]:
    number:list[int] = []
    while not number:
        message()
        number = complex_number.number_complex(ui.get_choice())
        if not number: ui.invalid_index()
    return number

def while_not_index(message) -> int:
    while True:
        message()
        index = ui.get_choice()
        if utility.valid_int(index) == 'int': return int(index)
        else: ui.invalid_index()

def while_not_interval(manager:dict) -> list[int]:
    index:list[int] = [-1, '0']
    while not utility.valid_index_interval(complex_number.m_get_complex_list(manager), index[0], index[1]):
        index[0] = while_not_index(ui.pop_input_index_start)
        index[1] = while_not_index(ui.pop_input_index_stop)
        if utility.valid_index_interval(complex_number.m_get_complex_list(manager), index[0], index[1]):
            return index
        else: ui.invalid_index()


def add_numbers_t(manager:dict) -> None:
    while True:
        ui.add_numbers_menu()
        choice = ui.get_choice()
        match choice:
            case '1':
                number:list[int] = while_not_number(ui.input_complex_number)
                utility.append_to_list(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), number)
            case '2':
                number:list[int] = while_not_number(ui.input_complex_number)
                index:int = while_not_index(ui.add_input_index)
                utility.index_to_list(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), number, index)
            case 'B' | 'BACK': break
            case 'U' | 'UNDO': utility.undo(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager))
            case 'P' | 'PRT' | 'PRINT': prints.print_list(complex_number.m_get_complex_list(manager))
            case _: ui.invalid_index()


def modificare_lista_t(manager:dict) -> None:
    while True:
        ui.modificare_lista_menu()
        choice = ui.get_choice()
        match choice:
            case '1':
                index:int = while_not_index(ui.pop_input_index_start)
                utility.pop_at_index(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), index)
            case '2':
                interval = while_not_interval(manager)
                utility.pop_at_interval(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), interval[0], interval[1])
            case '3':
                value:list[int] = while_not_number(ui.swap_value_message)
                number:list[int] = while_not_number(ui.input_complex_number)
                utility.swap_value(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), value, number)
            case 'B' | 'BACK': break
            case 'U' | 'UNDO': utility.undo(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager))
            case 'P' | 'PRT' | 'PRINT': prints.print_list(complex_number.m_get_complex_list(manager))
            case _: ui.invalid_index()

def print_functions(manager:dict) -> None:
    while True:
        ui.print_functions_menu()
        choice = ui.get_choice()
        match choice:
            case '1':
                interval = while_not_interval(manager)
                prints.print_imaginary_part(complex_number.m_get_complex_list(manager), interval[0], interval[1])
            case '2':
                while True:
                    ui.mai_mare_mic_egal()
                    choice = ui.get_choice()
                    match choice:
                        case '>':
                            prints.print_with_function(complex_number.m_get_complex_list(manager), lambda a: utility.modul_complex(a) > 10)
                            break
                        case '<':
                            prints.print_with_function(complex_number.m_get_complex_list(manager), lambda a: utility.modul_complex(a) < 10)
                            break
                        case '=':
                            prints.print_with_function(complex_number.m_get_complex_list(manager), lambda a: utility.modul_complex(a) == 10)
                            break
                        case _: ui.invalid_index()
            case 'B' | 'BACK': break
            case 'U' | 'UNDO': utility.undo(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager))
            case 'P' | 'PRT' | 'PRINT': prints.print_list(complex_number.m_get_complex_list(manager))
            case _: ui.invalid_index()

def operatii_cu_lista(manager:dict) -> None:
    while True:
        ui.operatii_cu_lista()
        choice = ui.get_choice()
        match choice:
            case '1':
                interval = while_not_interval(manager)
                prints.print_complex_number(utility.sum_complex(complex_number.m_get_complex_list(manager), interval[0], interval[1]))
            case '2':
                interval = while_not_interval(manager)
                prints.print_complex_number(utility.product_complex(complex_number.m_get_complex_list(manager), interval[0], interval[1]))
            case '3':
                prints.print_list(utility.sort_list_des(complex_number.m_get_complex_list(manager)))
            case 'B' | 'BACK': break
            case 'U' | 'UNDO': utility.undo(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager))
            case 'P' | 'PRT' | 'PRINT': prints.print_list(complex_number.m_get_complex_list(manager))
            case _: ui.invalid_index()

def filtrarea_listei(manager:dict) -> None:
    while True:
        ui.filtrarea_listei()
        choice = ui.get_choice()
        match choice:
            case '1':
                utility.filter_with_function(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), utility.complex_number_prime)
            case '2':
                index: int = while_not_index(ui.print_number_comperation)
                while True:
                    ui.mai_mare_mic_egal()
                    choice = ui.get_choice()
                    match choice:
                        case '>':
                            utility.filter_with_function(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), lambda a: utility.modul_complex(a) > index)
                            break
                        case '<':
                            utility.filter_with_function(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), lambda a: utility.modul_complex(a) < index)
                            break
                        case '=':
                            utility.filter_with_function(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager), lambda a: utility.modul_complex(a) == index)
                            break
                        case _:
                            ui.invalid_index()
            case 'B' | 'BACK':
                break
            case 'U' | 'UNDO':
                utility.undo(complex_number.m_get_complex_list(manager), complex_number.m_get_undo_list(manager))
            case 'P' | 'PRT' | 'PRINT':
                prints.print_list(complex_number.m_get_complex_list(manager))
            case _:
                ui.invalid_index()

def run() -> None:
    """
        this is the main function, here will be the main menu
    """
    manager = complex_number.CreateManager()

    while True:
        ui.main_menu()
        choice = ui.get_choice() # a string
        match choice:
            case '1': add_numbers_t(manager)
            case '2': modificare_lista_t(manager)
            case '3': print_functions(manager)
            case '4': operatii_cu_lista(manager)
            case '5': filtrarea_listei(manager)
            case '6': prints.print_list(complex_number.m_get_complex_list(manager))
            case 'E' | 'EXIT': break
            case 'U' | 'UNDO': pass
            case 'P' | 'PRT' | 'PRINT': prints.print_list(complex_number.m_get_complex_list(manager))
            case 'PRINT MANAGER': print(manager)
            case _: ui.invalid_index()

if __name__ == '__main__':
    run()