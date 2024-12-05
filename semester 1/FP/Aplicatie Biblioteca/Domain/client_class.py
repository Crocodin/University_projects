from .utility import Utility

class Client(Utility):

    def __init__(self, name: str, cnp: str, id_: str ='', rented_books: int = 0) -> None:
        self.name: str = self.format_name(name)
        self.__CNP: str = self.valid_cnp(cnp)
        if id_ == '': self.__id: str = self.generate_id()
        else: self.__id = id_
        self.rented_books = rented_books

    def change_name(self, name: str) -> None:
        self.name = self.format_name(name)

    def get_cnp(self) -> str:
        return self.__CNP

    def get_id(self) -> str:
        return self.__id

    def __str__(self) -> str:
        return self.name

    def __eq__(self, other: object) -> bool:
        if isinstance(other, Client):
            return self.__id == other.__id
        return False

    def id__eq__(self, string: str) -> bool:
        return self.__id == string

    @staticmethod
    def valid_cnp(cnp: str) -> str:
        if len(cnp) > 13: raise ValueError("CNP is too long")
        number:int = int(cnp[3]) * 10 + int(cnp[4])
        if number > 12 or number < 1: raise ValueError("CNP is wrong")
        number:int = int(cnp[5]) * 10 + int(cnp[6])
        if number > 31 or number < 1: raise ValueError("CNP is wrong")
        return cnp
