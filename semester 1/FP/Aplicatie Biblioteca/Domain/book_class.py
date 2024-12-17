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
        """
        returns a title of the book concatenated to the author
        :return: string
        """
        return f'"{self.title}" - {self.author}'

    def __eq__(self, other) -> bool:
        """
        checks if two books have the same title, or if the title is eq to a string
        :param other: book object or a string
        :return: True or False
        """
        if not isinstance(other, Book):
            if isinstance(other, str):
                return str(self) == other
            else: return False
        return str(self) == str(other)

    def eq_with_id(self, other) -> bool:
        """
        checks if two books have the same id, or if the id is eq to a string
        :param other: book object or a string
        :return: True or False
        """
        if not isinstance(other, Book):
            if isinstance(other, str):
                return self.__id == other
            return False
        return self.__id == other.__id

    def change_title(self, title: str) -> None:
        """
        changes the title of the book
        :param title: string
        :return: None
        """
        self.title = title.capitalize()

    def change_author(self, author: str) -> None:
        """
        changes the author of the book
        :param author: string
        :return: None
        """
        self.author = self.format_name(author)

    def change_description(self, description: str) -> None:
        """
        changes the description of the book
        :param description: string
        :return: None
        """
        self.description = description

    def get_id(self) -> str:
        """
        returns the id of the book
        :return: string
        """
        return self.__id

    def set_rented(self) -> None:
        """
        sets the rented status of the book (if it's one it becomes 0, and if it 0 it becomes 1) and inc the times it was rented
        :return: None
        """
        self.rented = (self.rented + 1) % 2
        if self.rented == 1: self.__how_many_time_rented += 1

    def get_how_many_time_rented(self) -> int:
        """
        returns how many times it was rented
        :return: int
        """
        return self.__how_many_time_rented





