from Domain.rented_class import RentedClass
from Domain.book_class import Book
from Domain.client_class import Client
from Repository.rented_repo import RentedClassRepo
from Repository.client_repo import ClientRepo
from Repository.book_repo import BookRepo
from Errors.my_errors import *

class RentedService:

    def __init__(self, repo: RentedClassRepo, book_repo: BookRepo, client_repo: ClientRepo):
        self.__repo:RentedClassRepo = repo
        self.__book_repo:BookRepo = book_repo
        self.__client_repo:ClientRepo = client_repo
        self.__repo.read_from_file()

    def add_rented(self, client: Client, book: Book):
        """
        add a book to the rented class if the client already has one associated to him, else it created a new one
        :param client: client object
        :param book: book object with we add
        :return: None n
        """
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
        """
        removes a book from the rented list with the client id == id_client
        :param id_client: id of the client
        :param id_book: id of the book
        :return: None
        """
        self.__repo.remove_rented(id_client, id_book)
        self.__book_repo.get_book(id_book).set_rented()

    def remove_book(self, id_book) -> None:
        """
        removes the rented book in with the book id is found
        :param id_book: book id
        :return: None
        """
        for rented in self.__repo.get_list():
            for book_id in rented.get_id_books():
                if book_id == id_book:
                    rented.get_id_books().remove(book_id)
                    if rented.number_of_books() == 0:
                        self.__repo.get_list().remove(rented)  # removes the now empty reserved object

    def remove_client(self, other) -> None:
        """
        removes the rented class for the given client aka. the rented class in with the client id = other
        :param other: id of a client
        :return: None
        """
        for rented in self.__repo.get_list():
            if rented.get_id_client() == other:
                for book_id in rented.get_id_books():
                    book = self.__book_repo.get_book(book_id)
                    book.set_rented()
                self.__repo.get_list().remove(rented)

    def get_all(self) -> list[RentedClass]:
        """
        returns the full list of rented books
        :return: list of rented books
        """
        return self.__repo.get_list()

    def wright_to_file(self) -> None:
        """
        calls the repo function that wright the data to the file
        :return: None
        """
        self.__repo.wright_to_file()

    def get_client(self, id_) -> RentedClass:
        """
        returns the rented object for client with the given id
        :param id_: the id of the client
        :return: rented object
        :raise: RentedExistError, if there is no client with the given id
        """
        for rented in self.__repo.get_list():
            if rented.get_id_client() == id_:
                return rented
        raise RentedExistError

    def get_client_recursive(self, id_, index) -> RentedClass:
        """
        returns the rented object for client with the given id
        :param id_: the id of the client
        :param index: rented object index
        :return: rented object
        :raise: RentedExistError, if there is no client with the given id


        // ------- COMPLEXITY ------- //
        *Q = theta

        BC = when the first element is the searched element
        T(n) = Q(1)
        WC = when the second element is the last element
        T(n) = Q(n)

               / 1 if elem.id = id
        T(n) = |
               \ T(n - 1) + 1 otherwise

        T(n) = T(n - 1) + 1
        T(n - 1) = T(n - 2) + 1
        ...
        T(1) = 1
        ---------// +
        T(n) = 1 + 1 + ... + 1, n times
        T(n) = Q(n)

        AC: Sum E(i)P(i) = Sum 1/n*i = 1/n Sum i = 1/n * n(n - 1)/2 = (n - 1)/2 is in Q(n)

        => overall time complexity = O(n)
        """
        if index == len(self.__repo.get_list()):
            raise RentedExistError
        if self.__repo.get_list()[index].get_id_client() == id_:
            return self.__repo.get_list()[index]
        return self.get_client_recursive(id_, index + 1)
