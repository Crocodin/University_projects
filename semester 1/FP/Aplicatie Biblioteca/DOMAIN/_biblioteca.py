from . import _book
from . import _client

class Biblioteca:

    def __init__(self):
        self.__book_list = {}
        self.__client_list = {}

    def add_book(self, book: _book.Book) -> None:
        if self.book_is_in(str(book)):
            raise IndexError("Book already exists")
        self.__book_list[str(book)] = book

    def add_client(self, client: _client.Client) -> None:
        if self.client_is_in_cnp(client.get_cnp()):
            raise IndexError("Client already exists")
        self.__client_list[client.get_cnp()] = client

    def remove_book(self, book: _book.Book) -> None:
        if self.book_is_in(str(book)):
            self.__book_list.pop(str(book), None)
        else: raise IndexError("Book not found")

    def delete_book(self, book) -> None:
        if self == book:
            del self.__book_list[str(book)]
        else: raise IndexError("Book not found")

    def remove_client(self, client) -> None:
        if self == client:
            self.__client_list.pop(client.get_cnp(), None)
        else: raise IndexError("Client not found")

    def delete_client(self, client) -> None:
        if self == client:
            del self.__client_list[client.get_cnp()]
        else: raise IndexError("Client not found")

    def book_is_in(self, name_and_author:str) -> bool:
        try:
            aux = self.__book_list[name_and_author]
            return True
        except KeyError:
            return False

    def client_is_in_cnp(self, cnp:str) -> bool:
        try:
            aux = self.__client_list[cnp]
            return True
        except KeyError:
            return False

    def __eq__(self, other):
        if isinstance(other, _book.Book):
            if self.book_is_in(str(other)):
                return self.__book_list[str(other)].eq_with_id(other)
            return False
        if isinstance(other, _client.Client):
            if self.client_is_in_cnp(other.get_cnp()):
                return self.__client_list[other.get_cnp()].eq_with_id(other)


    def __isub__(self, other) -> None:
        if isinstance(other, _book.Book):
            self.remove_book(other)
        if isinstance(other, _client.Client):
            self.remove_client(other)
        return self

    def __iadd__(self, other) -> None:
        if isinstance(other, _book.Book):
            self.add_book(other)
        if isinstance(other, _client.Client):
            self.add_client(other)
        return self