
class RentedClass:

    def __init__(self, id_client, id_books = None):
        if id_books is None:
            id_books = []
        self.__id_books = id_books
        self.__id_client = id_client

    def get_id_client(self):
        return self.__id_client

    def get_id_books(self):
        return self.__id_books

    def is_book_in(self, id_book) -> bool:
        return id_book in self.__id_books

    def number_of_books(self):
        return len(self.__id_books)

    def rent_book(self, id_book):
        self.__id_books.append(id_book)