
def modul_real(n:int) -> int:
    """
        this function returns the modul of n
        :param n: a number that is hole (int)
    """
    if n < 0: return -n
    return n

def prime(n:int) -> bool:
    """
        this function returns True if n is prime and False otherwise; 0 is not prime
        :param n: a number that is hole (int)
    """
    n = modul_real(n)
    if n < 2 or n % 2 == 0: return n == 2
    div:int = 3
    while div * div <= n:
        if n % div == 0: return False
        div += 2
    return True

def copy_list(l:list) -> list:
    """
        returns a copy of the list
        :param l: a list of numbers
    """
    aux:list = []
    for i in l:
        aux.append(i)
    return aux

def valid_int(string:str) -> str:
    """
        return True if the string is a hole number (int) and False otherwise
        :param string: a string
    """
    try:
        string = int(string)
        return 'int'
    except ValueError:
        try:
            string = float(string)
            return 'float'
        except ValueError:
            return 'string'

def valid_complex(number:list[int]) -> bool:
    """
        return True if the number is a complex number
        :param number: a list of two hole numbers [int, int] that represent the complex number
    """
    if len(number) != 2: return False
    if isinstance(number[0], int) and isinstance(number[1], int): return True
    return False

def modul_complex(number:list[int]) -> int:
    """
        returns the modul of a complex number (a + bi)
        :param number: a list of two hole numbers [int, int] that represent the complex number
    """
    if valid_complex(number):
        return (number[0] ** 2 + number[1] ** 2) ** 0.5
    return -1 # -1 because the modul of a complex number will never be a negative number

def append_to_list(l:list[list[int]], l_undo:list[list[list[int]]], number:list[int]) -> None:
    """
        this function appends a complex numbers to the end of the list
        :param l: a list of complex numbers
        :param l_undo: a list of all the past list of complex numbers
        :param number: the complex number
    """
    if valid_complex(number):
        l_undo.append(copy_list(l))
        l.append(number)

def valid_index(l:list, index:int) -> bool:
    if index >= len(l) or index <= 0 and not isinstance(index, int): return False
    return True

def index_to_list(l:list[list[int]], l_undo:list[list[list[int]]], number:list[int], index:int) -> None:
    """
        this function adds a number at the specified index
        :param l: a list of complex numbers
        :param l_undo: a list of all the past list of complex numbers
        :param number: a complex number
        :param index: the index where we add the number
    """
    if valid_complex(number) and valid_index(l, index):
        l_undo.append(copy_list(l))
        l.insert(index, number)

def valid_undo(l_undo:list[list[list[int]]]) -> bool:
    return len(l_undo) > 0

def undo(l:list[list[int]], l_undo:list[list[list[int]]]) -> None:
    """
        this function removes the last element of the list and changes l to that removed element
        :param l: a list of complex numbers
        :param l_undo: a list of all the past list of complex numbers
    """
    if valid_undo(l_undo):
        l.clear()
        l.extend(l_undo[len(l_undo)-1])
        l_undo.pop()

def pop_at_index(l:list[list[int]], l_undo:list[list[list[int]]], index:int) -> None:
    """
        this function eliminates the element at the specified index
        :param l: a list of complex numbers
        :param l_undo: a list of all the past list of complex numbers
        :param index: the index of the element to be removed
    """
    if valid_index(l, index):
        l_undo.append(copy_list(l))
        l.pop(index)

def valid_index_interval(l, start, end) -> bool:
    """
        checks to se if the numbers are included in a valid interval and that they are valid
        :param l: a list of complex numbers
        :param start: the start of the interval
        :param end: the end of the interval
    """
    if not isinstance(start, int) or not isinstance(end, int): return False
    return start <= end and valid_index(l, start) and valid_index(l, end)

def pop_at_interval(l:list[list[int]], l_undo:list[list[list[int]]], start:int, end:int) -> None:
    """
       this function eliminates the element between the start and end index
       :param l: a list of complex numbers
       :param l_undo: a list of all the past list of complex numbers
       :param start: the index from where we start removing element
       :param end: the index from where we end removing element
    """
    if valid_index_interval(l, start, end):
        l_undo.append(copy_list(l))
        while start <= end:
            l.pop(end)
            end -= 1

def swap_value(l:list[list[int]], l_undo:list[list[list[int]]], value:list[int], number:list[int]) -> None:
    """
        this function swaps all appearances of value with number
        :param l: a list of complex numbers
        :param l_undo: a list of all the past list of complex numbers
        :param value: a complex number
        :param number: a complex number
    """
    if valid_complex(number) and valid_complex(value):
        index:int = 0
        modified:bool = False
        for element in l:
            if element == value:
                if not modified:
                    l_undo.append(copy_list(l))
                    modified = True
                l[index] = number
            index += 1

def sum_complex(l:list[list[int]], start, end) -> list[int]:
    """
        return the sum of the complex numbers
        :param l: a list of complex numbers
    """
    sum_of_numbers = [0, 0]
    while start <= end:
        sum_of_numbers[0] += l[end][0]
        sum_of_numbers[1] += l[end][1]
        end -= 1
    return sum_of_numbers

def prod_of_two_complex(a:list[int], b:list[int]) -> list[int]:
    """
        return the product of two complex numbers
        (a + bi)(c + di) = ac - bd + (ad + cb)i
    """
    return [(a[0] * b[0] - a[1] * b[1]), (a[0] * b[1] + b[0] * a[1])]

def product_complex(l:list[list[int]], start, end) -> list[int]:
    """
       return the product of the complex numbers
       :param l: a list of complex numbers
    """
    product = [1, 0] # this is e; e * a = a and a * e = a
    while start <= end:
        product = prod_of_two_complex(product, l[end])
        end -= 1
    return product

def sort_list_des(l:list[list[int]]) -> list[list[int]]:
    """
        return the list sorted decreasing
        :param l: a list of complex numbers
    """
    #why are we using this dog shit sorting algorithm? cuz' this is python and I can't be bothered to translate a c++ algorithm in this language
    array = copy_list(l)
    n:int = len(array)
    for i in range(n - 1):
        swapped = False
        for j in range(0, n - i - 1):
            if array[j][1] < array[j + 1][1] or array[j][1] == array[j + 1][1] and array[j][0] > array[j + 1][0]:
                array[j], array[j + 1] = array[j + 1], array[j]
                swapped = True
        if not swapped: break
    return array

def complex_number_prime(number:list[int]) -> bool:
    """
        this function returns true if and only if the real part of a complex number is prime, otherwise false
    """
    return prime(number[0])

def filter_with_function(l:list[list[int]], undo:list[list[list[int]]], function) -> bool:
    """
       this function filters the list with function, this function can be anything as long as it return a boolean
       :param l: a list of complex numbers
       :param undo: a list of all the past list of complex numbers
       :param function: a function that takes one complex number [int, int] and returns a boolean
    """
    modified:bool = False
    index:int = len(l) - 1
    while index >= 0:
        if function(l[index]):
            if not modified:
                modified = True
                undo.append(copy_list(l))
            l.pop(index)
        index -= 1
    return modified


