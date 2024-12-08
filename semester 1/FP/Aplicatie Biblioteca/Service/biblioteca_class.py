from Domain.book_class import Book
from Domain.client_class import Client
from errors.my_errors import *

class Biblioteca:

    def __init__(self, books: list[Book], clients: list[Client]):
        self.__book_list: list[Book] = books
        self.__client_list: list[Client] = clients

    def __add_book(self, book: Book) -> None:
        self.__book_list.append(book)

    def __add_client(self, client: Client) -> None:
        self.__client_list.append(client)

    def get_book(self, book_name: str) -> Book:
        for book in self.__book_list:
            if str(book) == book_name:
                return book
        raise BookFoundError("Book not found")

    def find_book(self, book_name: str) -> bool:
        for book in self.__book_list:
            if str(book) == book_name:
                return True
        return False

    def find_client(self, id_: str) -> bool:
        for client in self.__client_list:
            if client.id__eq__(id_):
                return True
        return False

    def get_client(self, id_: str) -> Client:
        for client in self.__client_list:
            if client.id__eq__(id_):
                return client
        raise ClientFoundError("Client not found")

    def __iadd__(self, other: Client | Book):
        if isinstance(other, Book):
            if not self.find_book(str(other)):
                self.__add_book(other)
            else: raise BookExistError("Book already exist")
        if isinstance(other, Client):
            if not self.find_client(other.get_id):
                self.__add_client(other)
            else: raise ClientExistError("Client already exist")
        return self

    def remove_book(self, other: Book) -> None:
        for index, book in enumerate(self.__book_list):
            if book.eq_with_id(other):
                self.__book_list.pop(index)
                return
        raise BookFoundError("Can't remove Book; not found")

    def pop_at_index(self, index: int) -> None:
        try:
            self.__book_list.pop(index)
        except IndexError:
            raise BookExistError("Books with this index not found")

    def get_book_with_id(self, other: Book | str) -> Book:
        for book in self.__book_list:
            if book.eq_with_id(other):
                return book
        raise BookFoundError("Book id not found")

    def remove_client(self, other: Client) -> None:
        for index, client in enumerate(self.__client_list):
            if client == other:
                self.__client_list.pop(index)
                return
        raise ClientFoundError("Can't remove client; not found")

    def __isub__(self, other: Client | Book):
        if isinstance(other, Book):
            self.remove_book(other)
        if isinstance(other, Client):
            self.remove_client(other)
        return self

    def get_client_with_cnp(self, cnp: str) -> Client:
        for client in self.__client_list:
            if client.get_cnp() == cnp:
                return client
        raise ClientFoundError(f"Can't find client with this cnp, {cnp}")

    def get_book_reserved(self, book_name: str, rented: bool) -> Book:
        for book in self.__book_list:
            if book.rented != rented: continue
            if book_name == str(book):
                return book
        raise BookFoundError(f"No books with this name were found that are {rented} reserved")

    def most_rented_book(self) -> list[tuple[int, str]]:
        rented = [] # first element is the number of time it was rented, the second the id
        for book in self.__book_list:
            rented.append((book.get_how_many_time_rented(), str(book)))
        rented.sort(reverse=True, key = lambda rented_object: rented_object[0])
        return rented

