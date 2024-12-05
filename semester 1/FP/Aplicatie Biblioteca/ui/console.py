from ui.options import Options
from Repository.biblioteca_class import Biblioteca
from Repository.book_repo import BookRepo
from Repository.client_repo import ClientRepo
from Repository.rented_repo import RentedClassRepo
from Domain.book_class import Book
from Domain.client_class import Client
from Domain.rented_class import RentedClass
from errors.my_errors import *


class Console:

    def __init__(self, book_repo, client_repo, rented_repo):
        self.ui = Options()
        self.__book_repo: BookRepo = book_repo
        self.__client_repo: ClientRepo = client_repo
        self.biblioteca: Biblioteca = self.init_biblioteca()
        self.__rented_repo: RentedClassRepo = rented_repo
        self.rented_books: list[RentedClass] = self.init_rented_class()

    def init_rented_class(self) -> list[RentedClass]:
        self.__rented_repo.read_from_file()
        return self.__rented_repo.get_list()

    def init_biblioteca(self) -> Biblioteca:
        self.__book_repo.read_from_file()
        self.__client_repo.read_from_file()
        return Biblioteca(self.__book_repo.get_list(), self.__client_repo.get_list())

    def add_book(self) -> None:
        book_info = self.ui.add_book()
        book = Book(book_info[0], book_info[1], book_info[2])
        try:
            self.biblioteca += book
        except BookExistError:
            print("This book already exists. Are you sure you want to add it? (y/n)")
            while True:
                x = self.ui.match_input()
                match x:
                    case 'Y' | 'YES':
                        self.biblioteca._Biblioteca__add_book(book)
                        break
                    case 'N' | 'NO': break
                    case _: pass

    def add_client(self) -> None:
            client_info = self.ui.add_client()
            client = Client(client_info[0], client_info[1])
            try:
                self.biblioteca += client
            except ClientExistError:
                print(f"This client already exists. INVALID ID")

    def find_book(self) -> None:
        title = input("    Title & author: ")
        if self.biblioteca.find_book(title):
            self.biblioteca.print_all_books_with_name(title)
        else: print("The book was not found")

    def find_client(self) -> None:
        cnp = input("    CNP: ")
        try:
            client = self.biblioteca.get_client_with_cnp(cnp)
            print(f"    Client name: {client}, {client.get_id()}\n")
            found: bool = False
            for rented in self.rented_books:
                if client.id__eq__(rented.get_id_client()):
                    if not found: print(f"=================// {str(client)} //=================")
                    for book_id in rented.get_id_books():
                        print(str(self.biblioteca.get_book_with_id(book_id)))
        except ClientFoundError as e:
            print(f"{e}")

    def statistics(self):
        while True:
            self.ui.statistics()
            match self.ui.match_input():
                case '1': self.ui.print_top_5(self.biblioteca.most_rented_book())
                case '2':
                    for rented in self.rented_books:
                        print(f"{str(self.biblioteca.get_client(rented.get_id_client()))}, with {rented.number_of_books()} rented books")
                case '3':
                    most_activ = self.ui.biggest_in_list(self.biblioteca.get_clients(), lambda client: client.rented_books)
                    print(f"The most activ client is: {most_activ}, with a record number of {most_activ.rented_books} rented books!")
                case 'B' | 'BACK': break

    def wright_to_file(self):
        self.__rented_repo.wright_to_file()
        self.__book_repo.wright_to_file()
        self.__client_repo.wright_to_file()

    def run(self):
        while True:
            self.ui.main_menu()
            match self.ui.match_input():
                case '1':
                    self.add_book()
                case '2':
                    self.ui.remove_book(self.biblioteca)
                case '3':
                    self.find_book()
                case '4':
                    self.add_client()
                case 'EDIT' | 'EDIT BOOK':
                    self.ui.edit_book(self.biblioteca)
                case '5':
                    self.ui.remove_client(self.biblioteca, self.rented_books)
                case '6':
                    self.find_client()
                case '7':
                    self.ui.return_book(self.biblioteca, self.rented_books)
                case '8':
                    self.ui.rent_book(self.biblioteca, self.rented_books)
                case '9':
                    self.statistics()
                case 'P' | 'PRT' | 'PRINT':
                    self.biblioteca.print_all_books()
                    self.biblioteca.print_all_clients()
                case 'E' | 'EXIT': break
        self.wright_to_file()