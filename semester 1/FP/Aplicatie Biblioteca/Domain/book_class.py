from .utility import Utility

class Book(Utility):

    def __init__(self, title: str = '', author: str = '', description: str = '', id_ = '', rented: bool = False, number: int = 0) -> None:
        self.title = title.capitalize()
        self.author = self.format_name(author)
        self.description = description
        if id_ == '': self.__id = self.generate_id()
        else : self.__id = id_
        self.rented: bool = rented
        self.__how_many_time_rented: int = number

    def __str__(self) -> str:
        return f'"{self.title}" - {self.author}'

    def __eq__(self, other) -> bool:
        if not isinstance(other, Book):
            if isinstance(other, str):
                return str(self) == other
            else: return False
        return str(self) == str(other)

    def eq_with_id(self, other) -> bool:
        if not isinstance(other, Book):
            if isinstance(other, str):
                return self.__id == other
            return False
        return self.__id == other.__id

    def change_title(self, title: str) -> None:
        self.title = title.capitalize()

    def change_author(self, author: str) -> None:
        self.author = self.format_name(author)

    def change_description(self, description: str) -> None:
        self.description = description

    def get_id(self) -> str:
        return self.__id

    def set_rented(self) -> None:
        self.rented = (self.rented + 1) % 2
        if self.rented == 1: self.__how_many_time_rented += 1

    def get_how_many_time_rented(self) -> int:
        return self.__how_many_time_rented





