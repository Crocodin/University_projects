
def is_name(string: str) -> bool:
    """
        Checks if the string is a valid name
        :param string: the string to be checked
        :return: true if the string is a valid name
    """
    ok: bool = False
    for char in string:
        if char == ' ':
            ok = True
    return ok

def is_string(string) -> str:
    """
        returns the string if it is a str
        :param string: string
        :return: the string
        :raises TypeError: if the string is not a string
    """
    if isinstance(string, str) and string != '':
        return string
    else:
        raise TypeError

def get_number() -> int:
    while True:
        try:
            x = int(input("     >>> "))
            return x
        except ValueError:
            print("Not a number, try again")

def is_cnp(string: str) -> bool:
    # 0 00 00 00 00 0000
    if len(string) != 13: return False
    match string[0]:
        case '1' |'2' | '5' | '6': pass
        case _: return False
    if not 12 >= int(string[1] + string[2]) >= 1 : return False
    if not 1 <= int(string[3] + string[4]) <= 31: return False
    return True