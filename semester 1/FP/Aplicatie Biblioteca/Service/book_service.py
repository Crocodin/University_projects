from Domain.book_class import Book
from Repository.book_repo import BookRepo
from sorting_algorithm.bubble_sort import Sort


class BookService:

    def  __init__(self, repo: BookRepo):
        self.__repo: BookRepo = repo
        self.__repo.read_from_file()

    def add_book(self, title: str, author: str, description: str):
        """
        add a book to the book repository
        :param title: title of the book
        :param author: author of the book
        :param description: description of the book
        :return: None
        """
        book = Book(title, author, description)
        self.__repo.add_book(book)

    def remove(self, id_book: str):
        """
        remove a book from the book repository
        :param id_book: the id of the book to be removed
        :return: None
        """
        book = self.__repo.get_book(id_book)
        self.__repo.remove_book(book)

    def get_book(self, other) -> Book:
        """
        find a book from the book repository
        :param other: id of the book to be found
        :return: a book from the book repository
        :raise: BookFoundError, if no book is found with the given id
        """
        return self.__repo.get_book(other)

    def get_sorted_rented(self):
        """
        returns a sorted list of books sorted by the amount of times they were rented
        :return: list of books
        """
        sort = Sort()
        return sort.bubble_sort(
            self.__repo.get_list(),
            key=lambda book: book.get_how_many_time_rented(),
            reverse=True
        )

    def load(self):
        """
        loads the books from the book repository to the file
        :return: None
        """
        self.__repo.wright_to_file()

    def get_all(self) -> list[Book]:
        return self.__repo.get_list()
