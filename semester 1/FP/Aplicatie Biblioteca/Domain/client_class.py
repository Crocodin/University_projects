from .utility import Utility

class Client(Utility):

    def __init__(self, name: str, cnp: str) -> None:
        self.name: str = self.format_name(name)
        self.__CNP: str = self.valid_cnp(cnp)
        self.__id: str = self.generate_id()
        self.__book_rented = []
        self.rented_books = 0

    def change_name(self, name: str) -> None:
        self.name = self.format_name(name)

    @staticmethod
    def valid_cnp(cnp: str) -> str:
        if len(cnp) > 13: raise ValueError("CNP is too long")
        number:int = int(cnp[3]) * 10 + int(cnp[4])
        if number > 12 or number < 1: raise ValueError("CNP is wrong")
        number:int = int(cnp[5]) * 10 + int(cnp[6])
        if number > 31 or number < 1: raise ValueError("CNP is wrong")
        return cnp

    def get_cnp(self) -> str:
        return self.__CNP

    def __str__(self) -> str:
        return self.name

    def __eq__(self, other: object) -> bool:
        if isinstance(other, Client):
            return self.__id == other.__id
        return False

    def is_this_the_id(self, string: str) -> bool:
        return self.__id == string


    def get_id(self) -> str:
        return self.__id

    def add_rented_book(self, id_) -> None:
        self.__book_rented.append(id_)
        self.rented_books += 1

    def get_book_rented(self) -> list:
        return self.__book_rented
