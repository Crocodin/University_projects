from Domain.rented_class import RentedClass
from Domain.book_class import Book
from Domain.client_class import Client
from Repository.rented_repo import RentedClassRepo
from Repository.client_repo import ClientRepo
from Repository.book_repo import BookRepo
from errors.my_errors import *

class RentedService:

    def __init__(self, repo: RentedClassRepo, book_repo: BookRepo, client_repo: ClientRepo):
        self.__repo:RentedClassRepo = repo
        self.__book_repo:BookRepo = book_repo
        self.__client_repo:ClientRepo = client_repo
        self.__repo.read_from_file()

    def add_rented(self, client: Client, book: Book):
        for rented in self.__repo.get_list():
            if rented.get_id_client() == client.get_id():
                rented.rent_book(book.get_id())
                book.set_rented()
                client.rented_books += 1
                return
        rented = RentedClass(client.get_id(), [book.get_id()])
        self.__repo.add_rented(rented)
        # sets the book status to the correct one
        book.set_rented()
        client.rented_books += 1

    def remove_rented(self, id_client, id_book) -> None:
        self.__repo.remove_rented(id_client, id_book)
        self.__book_repo.get_book(id_book).set_rented()

    def remove_book(self, id_book) -> None:
        for rented in self.__repo.get_list():
            for book_id in rented.get_id_books():
                if book_id == id_book:
                    rented.get_id_books().remove(book_id)
                    if rented.number_of_books() == 0:
                        self.__repo.get_list().remove(rented)  # removes the now empty reserved object

    def remove_client(self, other) -> None:
        for rented in self.__repo.get_list():
            if rented.get_id_client() == other:
                for book_id in rented.get_id_books():
                    book = self.__book_repo.get_book(book_id)
                    book.set_rented()
                self.__repo.get_list().remove(rented)

    def get_all(self):
        return self.__repo.get_list()

    def wright_to_file(self):
        self.__repo.wright_to_file()

    def get_client(self, id_):
        for rented in self.__repo.get_list():
            if rented.get_id_client() == id_:
                return rented
        raise RentedExistError
