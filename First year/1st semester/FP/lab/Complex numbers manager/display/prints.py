
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
    print(end='( ')
    for element in l[:-1]:
        print_complex_number(element)
        print(', ', end='')
    print_complex_number(l[-1])
    print(end=' )')

def print_imaginary_part(l:list[list[int]]) -> None:
    """
        for a list of complex numbers [int, int] print only the imaginary part in a more human-readable way
        :param l: a list that represents a complex number of the a + bi tipe print
    """
    print(end='( ')
    for element in l[:-1]:
        if element[1] != 0:
            print(f"{element[1]}i", end=', ')
    print(f"{l[-1][1]}i", end=' )')

print(print_imaginary_part([[0, -32], [21, -9], [12, -4], [-9, -4], [12, -4], [32, -8], [12, -4]]))