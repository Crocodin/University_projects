
class RentedClass:

    def __init__(self, id_client, id_books = None):
        if id_books is None:
            id_books = []
        self.__id_books = id_books
        self.__id_client = id_client

    def get_id_client(self) -> str:
        """
        returns the id of the client
        :return: string
        """
        return self.__id_client

    def get_id_books(self) -> list[str]:
        """
        id of the books
        :return: list of book ids [strings]
        """
        return self.__id_books

    def is_book_in(self, id_book) -> bool:
        """
        checks if a book is in the book list
        :param id_book: id of the book
        :return: False if the book is not in, True otherwise
        """
        return id_book in self.__id_books

    def number_of_books(self):
        """
        returns the number of books
        :return: int
        """
        return len(self.__id_books)

    def rent_book(self, id_book):
        """
        add a book id to the book list
        :param id_book: id of the book
        :return: None
        """
        self.__id_books.append(id_book)