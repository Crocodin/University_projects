from Domain.book_class import Book
from errors.my_errors import *

class BookRepo:

    def __init__(self, filename: str):
        self.__book_list: list[Book] = []
        self.filename = filename

    def add_book(self, other: Book):
        self.__book_list.append(other)

    def remove_book(self, other: Book):
        self.__book_list.remove(other)

    def get_book(self, other) -> Book:
        for book in self.__book_list:
            if book.eq_with_id(other):
                return book
        raise BookFoundError

    def read_from_file(self) -> None:
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
        self.__book_list.sort(key=lambda book_: book_.author)
        with open(self.filename, "w") as file:
            for book in self.__book_list:
                file.write(
                    f"{book.title},{book.author},{book.description},{book.get_id()},{book.rented},{book.get_how_many_time_rented()}\n")

    def get_list(self) -> list[Book]:
        return self.__book_list
