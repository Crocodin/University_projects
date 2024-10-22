from domain import utility, complex_number

def modul_real() -> None:
    assert utility.modul_real(0) == 0
    assert utility.modul_real(1) == 1
    assert utility.modul_real(-1) == 1

def prime() ->None:
    assert utility.prime(9) == False
    assert utility.prime(-7) == True
    assert utility.prime(0) == False
    assert utility.prime(25) == False

def copy_list() -> None:
    assert utility.copy_list([]) == []
    assert utility.copy_list([1, 2, -3, 4]) == [1, 2, -3, 4]

def valid_int() -> None:
    assert utility.valid_int('123') == 'int'
    assert utility.valid_int('123.45') == 'float'
    assert utility.valid_int('adc.45') == 'string'

def valid_complex() -> None:
    assert utility.valid_complex([1, -2]) == True
    assert utility.valid_complex(['1', 2]) == False
    assert utility.valid_complex([1, 2, 3, 4]) == False

def modul_complex() -> None:
    assert utility.modul_complex([1, 1]) == 2 ** 0.5
    assert utility.modul_complex([-4, 3]) == 5
    assert utility.modul_complex([6, 8]) == 10
    assert utility.modul_complex([-6, -4]) < 8
    assert utility.modul_complex(['1', -3]) == -1
    assert utility.modul_complex(['8', 6]) != 10

def CreateManager() -> None:
    assert complex_number.CreateManager() == {'current': [], 'past': []}

def number_complex() -> None:
    assert complex_number.number_complex('1 + 2I') == [1, 2]
    assert complex_number.number_complex('14 - 98I') == [14, -98]
    assert complex_number.number_complex('4I') == [0, 4]
    assert complex_number.number_complex('55') == [55, 0]
    assert complex_number.number_complex('4I + 76') == []
    assert complex_number.number_complex('wordI') == []
    assert complex_number.number_complex('12 + 4rI') == []
    assert complex_number.number_complex('98 76I') == []
    assert complex_number.number_complex('21 + 23') == []

def append_to_list() -> None:
    l:list = [[1, 2], [1, 3]]
    undo:list = []
    utility.append_to_list(l, undo,[1, 4])
    assert l == [[1, 2], [1, 3], [1, 4]]
    assert undo == [[[1, 2], [1, 3]]]
    utility.append_to_list(l, undo, [1, '4'])
    assert l == [[1, 2], [1, 3], [1, 4]]
    assert undo == [[[1, 2], [1, 3]]]

def pop_at_index() -> None:
    l:list = [[1, 0], [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]]
    undo:list[list] = []
    utility.pop_at_index(l, undo, 4)
    assert l == [[1, 0], [1, 1], [1, 2], [1, 3], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]]
    assert undo == [[[1, 0], [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]]]
    utility.pop_at_index(l, undo, 6)
    assert l == [[1, 0], [1, 1], [1, 2], [1, 3], [1, 5], [1, 6], [1, 8], [1, 9], [1, 10]]
    assert undo == [[[1, 0], [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]], [[1, 0], [1, 1], [1, 2], [1, 3], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]]]

def pop_at_interval() -> None:
    l:list = [[1, 0], [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [1, 6], [1, 7], [1, 8], [1, 9], [1, 10]]
    utility.pop_at_interval(l, [], 2, 7)
    assert l == [[1, 0], [1, 1], [1, 8], [1, 9], [1, 10]]

def swap_value() -> None:
    l:list = [[1, 0], [1, 1], [1, 2], [1, 1], [1, 4], [1, 5], [1, 6], [1, 1], [1, 8], [1, 9], [1, 1]]
    undo:list[list] = []
    utility.swap_value(l, undo, [1, 11], [0, 0])
    assert l == [[1, 0], [1, 1], [1, 2], [1, 1], [1, 4], [1, 5], [1, 6], [1, 1], [1, 8], [1, 9], [1, 1]]
    assert undo == []
    utility.swap_value(l, undo, [1, 1], [0, 0])
    assert l == [[1, 0], [0, 0], [1, 2], [0, 0], [1, 4], [1, 5], [1, 6], [0, 0], [1, 8], [1, 9], [0, 0]]
    assert undo == [[[1, 0], [1, 1], [1, 2], [1, 1], [1, 4], [1, 5], [1, 6], [1, 1], [1, 8], [1, 9], [1, 1]]]

def sum_complex() -> None:
    l:list = [[0, 0]]
    assert utility.sum_complex(l) == [0, 0]
    utility.append_to_list(l, [], [12, 3])
    assert utility.sum_complex(l) == [12, 3]
    utility.append_to_list(l, [], [21, -6])
    assert utility.sum_complex(l) == [33, -3]

def prod_of_two_complex() -> None:
    assert utility.prod_of_two_complex([1, 1], [1, 1]) == [0, 2]
    assert utility.prod_of_two_complex([0, 0], [8, 6]) == [0, 0]
    assert utility.prod_of_two_complex([1, 1], [1, -1]) == [2, 0]

def product_complex() -> None:
    l:list = [[1, 1], [1, -1]]
    assert utility.product_complex(l) == [2, 0]
    utility.append_to_list(l, [], [12, -6])
    assert utility.product_complex(l) == [24, -12]

def sort_list_des() -> None:
    l:list = [[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8], [12, -4]]
    assert utility.sort_list_des(l) == [[-9, -4], [12, -4], [12, -4], [12, -4], [32, -8], [21, -9], [0, -32]]

if __name__ == 'test':
    modul_real()
    prime()
    copy_list()
    valid_int()
    valid_complex()
    modul_complex()
    CreateManager()
    number_complex()
    append_to_list()
    pop_at_index()
    pop_at_interval()
    swap_value()
    sum_complex()
    prod_of_two_complex()
    product_complex()
    print("test worked")
