from Domain.book_class import Book
from Repository.book_repo import BookRepo

class BookService:

    def __init__(self, repo: BookRepo):
        self.__repo: BookRepo = repo
        self.__repo.read_from_file()

    def add_book(self, title: str, author: str, description: str):
        book = Book(title, author, description)
        self.__repo.add_book(book)

    def remove(self, id_book: str):
        book = self.__repo.get_book(id_book)
        self.__repo.remove_book(book)

    def get_book(self, other) -> Book:
        return self.__repo.get_book(other)

    def get_sorted_rented(self):
        return sorted(self.__repo.get_list(), key=lambda book: book.get_how_many_time_rented(), reverse=True)

    def load(self):
        self.__repo.wright_to_file()

    def get_all(self) -> list[Book]:
        return self.__repo.get_list()
