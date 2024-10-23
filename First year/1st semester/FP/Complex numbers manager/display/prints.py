
def print_complex_number(l:list[int]) -> None:
    """
        for a list that represents a complex number of the a + bi tipe print the representation of it
        :param l: a list that represents a complex number of the a + bi tipe print
   """
    if l[0] == 0: print(f"{l[1]}i", end='')
    elif l[1] == 0: print(f"{l[0]}", end='')
    elif l[1] > 0:
        if l[0] < 0: print(f"{l[1]}i - {-l[0]}", end='')
        else: print(f"{l[0]} + {l[1]}i", end='')
    elif l[1] < 0: print(f"{l[0]} - {-l[1]}i", end='')
    
def print_list(l:list[list[int]]) -> None:
    """
        for a list of complex numbers [int, int] print the list in a more human-readable way
        :param l: a list that represents a complex number of the a + bi tipe print
    """
    if len(l) == 0:
        print("( )", end='\n')
        return
    print(end='( ')
    for element in l[:-1]:
        print_complex_number(element)
        print(', ', end='')
    print_complex_number(l[-1])
    print(end=' )\n')

def print_imaginary_part(l:list[list[int]]) -> None:
    """
        for a list of complex numbers [int, int] print only the imaginary part in a more human-readable way
        :param l: a list that represents a complex number of the a + bi tipe print
    """
    if len(l) == 0:
        print("( )", end='\n')
        return
    print(end='( ')
    for element in l[:-1]:
        if element[1] != 0:
            print(f"{element[1]}i", end=', ')
    print(f"{l[-1][1]}i", end=' )\n')


def print_with_function(l: list[list[int]], function) -> None:
    """
       this function prints the list with function, this function can be anything as long as it return a boolean
       :param l: a list of complex numbers
       :param undo: a list of all the past list of complex numbers
       :param function: a function that takes one complex number [int, int] and returns a boolean
    """
    print('(', end='')
    for element in l:
        if function(element):
            print_complex_number(element)
    print(')', end='\n')
