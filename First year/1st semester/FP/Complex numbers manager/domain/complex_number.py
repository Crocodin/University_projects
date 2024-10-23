# this is the domain
from domain.utility import valid_int


def CreateManager() -> dict:
    """
        this function creates the manager; a manager, in this case, is a pear of two lists: the first one is a lista of complex numbers [int, int] and the second one is a list of multiple lists (this will be used for the undo function)
    """
    return {'current': [], 'past': []}

def m_get_complex_list(manager:dict) -> list[list[int]]:
    return manager['current']

def m_get_undo_list(manager:dict) -> list[list[list[int]]]:
    return manager['past']

def number_complex(number:str) -> list[int]:
    """
        this function returns a complex number; a list of two number if they are valid aka. if the user input was correct, otherwise an empty list.
        :param number: the input number
    """
    number = number.split()
    if len(number) == 1: # we have: ori doar a, ori doar bi
        if number[0][len(number[0]) - 1] == 'I':
            number[0] = number[0].replace('I', '') # get read of the i from the end
            aux = valid_int(number[0])
            if aux == 'int': return [0, int(number[0])]
        else:
            aux = valid_int(number[0])
            if aux == 'int': return [int(number[0]), 0]
    if len(number) == 3:
        aux = valid_int(number[0])
        if aux == 'int':
            if number[2][len(number[2]) - 1] == 'I':
                number[2] = number[2].replace('I', '') # get read of the i from the end
                aux = valid_int(number[2])
                if aux == 'int':
                    if number[1] == '-': return [int(number[0]), -int(number[2])]
                    else : return [int(number[0]), int(number[2])]
    return []


