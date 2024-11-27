from zoneinfo import reset_tzpath

from Domain.book_class import Book
from Domain.client_class import Client
from errors.my_errors import *

class Biblioteca:

    def __init__(self):
        self.__book_list: list[Book] = []
        self.__client_list: list[Client] = []

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
            if client.is_this_the_id(id_):
                return True
        return False

    def get_client(self, id_: str) -> Client:
        for client in self.__client_list:
            if client.is_this_the_id(id_):
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

    def remove_all_books(self, other: Book) -> None:
        for index, book in enumerate(self.__book_list):
            if book.eq_with_id(other):
                self.__book_list.pop(index)
        raise BookFoundError("Can't remove Book; not found")

    def get_book_with_id(self, other: Book) -> Book:
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

    def get_description(self, string: str) -> Book:
        for book in self.__book_list:
            if book.get_description() == string:
                return book
        raise BookFoundError("No books with this description was found")

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

    def print_all_books(self):
        print("---------------// all the books //---------------\n")
        index: int = 1
        for book in self.__book_list:
            print(f"{index}. {str(book)}", end=';')
            if book.rented: print(" rented")
            else: print(" not rented")
            index += 1
        print(end='\n')

    def get_book_with_id_string(self, id_: str) -> Book:
        for book in self.__book_list:
            if book.eq_with_id(id):
                return book
        raise BookFoundError(f"Can't find book with id {id_}")

    def print_all_books_with_name(self, title: str) -> None:
        index: int = 1
        for book in self.__book_list:
            if str(book) == title:
                print(f"{index}. {str(book)};, {book.get_id()}", end='')
                if book.rented: print(" rented")
                else: print(" not rented")
            index += 1

    def most_rented_book(self) -> list[list[int, str]]:
        rented = [] # first element is the number of time it was rented, the second the id
        for book in self.__book_list:
            rented.append([book.get_how_many_time_rented(), book.get_id()])
        rented.sort(key = lambda book_id: book_id[0])
        return rented

    def get_clients_with_rented_book(self) -> list[str]:
        clients_with_rented_book = []
        for client in self.__client_list:
            if len(client.get_book_rented()) > 0:
                clients_with_rented_book.append(client.get_id())
        return clients_with_rented_book

    def get_clients(self):
        return self.__client_list

    def print_all_clients(self):
        print("---------------// all the clients //---------------\n")
        index: int = 1
        for client in self.__client_list:
            print(f"{index}. {str(client)}, {client.get_id()}", end='\n')
            index += 1
        print(end='\n')

