from sorting_algorithm.bubble_sort import Sort
from Domain.book_class import Book
from Errors.my_errors import *

class BookRepo:

    def __init__(self, filename: str):
        self.__book_list: list[Book] = []
        self.filename = filename

    def add_book(self, other: Book):
        """
        add a book to the book list
        :param other: book to add
        :return: None
        """
        self.__book_list.append(other)

    def remove_book(self, other: Book):
        """
        remove a book from the book list
        :param other: book to remove
        :return: None
        """
        self.__book_list.remove(other)

    def get_book(self, other) -> Book:
        """
        get a book from the book list
        :param other: id of the book we want to get
        :return: Book
        :raise BookFoundError: if the book was not found
        """
        for book in self.__book_list:
            if book.eq_with_id(other):
                return book
        raise BookFoundError

    def read_from_file(self) -> None:
        """
        read from file all the books in the database
        :return: None
        """
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[5] = line[5].strip()
                if line[4] == "False":
                    line[4] = False
                else:
                    line[4] = True
                book = Book(line[0], line[1], line[2], line[3], bool(line[4]), int(line[5]))
                self.__book_list.append(book)

    def wright_to_file(self):
        """
        load all the books in the database
        :return: None
        """
        sort = Sort()
        with open(self.filename, "w") as file:
            for book in sort.bubble_sort(
                self.__book_list,
                key=lambda book_: book_.author
            ):
                file.write(
                    f"{book.title},{book.author},{book.description},{book.get_id()},{book.rented},{book.get_how_many_time_rented()}\n")

    def get_list(self) -> list[Book]:
        """
        get all the books in the book list.
        :return: list of books
        """
        return self.__book_list
