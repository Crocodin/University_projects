from Domain.book_class import Book
from Domain.client_class import Client
from Domain.rented_class import RentedClass
from Service.book_service import BookService


class Display:

    def __init__(self):
        self.input_text_choice = '    >>> '
        self.input_text_title = '    Enter title: '
        self.input_text_author = '    Enter author: '
        self.input_text_title_author = '    Enter title & author: '
        self.input_text_description = '    Enter description: '
        self.input_text_id_book = '    Enter ID book: '
        self.input_text_id_client = '    Enter ID client: '
        self.input_text_name = '    Enter name: '
        self.input_text_cnp = '    Enter CNP: '

    @staticmethod
    def main_menu() -> None:
        """
            ---------------// main menu //---------------

                1. Add a book             4. Add a client
                2. Remove a book          5. Remove a client
                3. Find a book            6. Find a client

                7. Return book            8. Rent book

                             9. Statistics

                E. exit                   P. print
        """
        print(
            "---------------// main menu //---------------\n\n    1. Add a book             4. Add a client\n    2. Remove a book          5. Remove a client\n    3. Find a book            6. Find a client\n    7. Return book            8. Rent book\n\n                 9. Statistics\n\n     E. exit                   P. print")

    @staticmethod
    def statistics() -> None:
        """
        ---------------// statistics //---------------

            1. The most rented books (top 5)
            2. Clients with rented books
            3. The most active clients

            B. back
        """
        print("---------------// statistics //---------------\n\n    1. The most rented books (top 5)\n    2. Clients with rented books\n    3. The most active clients\n\n    B. back")

    @staticmethod
    def book_info(book: Book) -> None:
        """
        =====// book_name //=====

        description

        if it;s rented           how meny time it was rented
        """
        print(f"\n=====// {str(book)}//=====\n\n    {book.description}\n")
        if book.rented:
            print(f"The book is rented               rented {book.get_how_many_time_rented()} times")
        else: print(f"The book is rented               rented {book.get_how_many_time_rented()} times")

    @staticmethod
    def client_info(client: Client, rented: RentedClass, service: BookService) -> None:
        """
        =====// client_name //=====

        what book he has rented
        """
        print(f"\n=====// {str(client)} //=====\n")
        for book_id in rented.get_id_books():
            print(f"    {str(service.get_book(book_id))}")
        print('\n')

