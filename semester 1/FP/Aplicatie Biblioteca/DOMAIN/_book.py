from . import utility

class Book(utility.Utility):

    def __init__(self, title: str = '', author: str = '', description: str = '') -> None:
        self.title = title.capitalize()
        self.author = self.format_name(author)
        self.description = description
        self.__id = self.generate_id()
        self.rented: bool = False

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
            return False
        return self.__id == other.__id

    def change_title(self, title: str) -> None:
        self.title = title.capitalize()

    def change_author(self, author: str) -> None:
        self.author = self.format_name(author)

    def change_description(self, description: str) -> None:
        self.description = description

    def get_description(self) -> str:
        return self.description

    def get_id(self) -> str:
        return self.__id

