from ui.options import Options
from Repository.biblioteca_class import Biblioteca
from Domain.book_class import Book
from Domain.client_class import Client
from errors.my_errors import *
from test import get_bib_with_elements


class Run:

    def __init__(self):
        self.ui = Options()
        #self.biblioteca = get_bib_with_elements()
        self.biblioteca = Biblioteca()

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
                print(f"This client already exists. id: {self.biblioteca.get_client_with_cnp(client.get_cnp()).get_id()}")

    def find_book(self) -> None:
        title = input("    Title: ")
        if self.biblioteca.find_book(title):
            self.biblioteca.print_all_books_with_name(title)
        else: print("The book was not found")

    def find_client(self) -> None:
        cnp = input("    CNP: ")
        try:
            client = self.biblioteca.get_client_with_cnp(cnp)
            print(f"    Client name: {client}, {client.get_id()}\n    {client} has rented:")
            if len(client.get_book_rented()) > 0:
                for id_ in client.get_book_rented():
                    print(self.biblioteca.get_book_with_id(id_))
            else: print("    NO BOOKS")
        except ClientFoundError as e:
            print(f"{e}")

    def statistics(self):
        while True:
            self.ui.statistics()
            match self.ui.match_input():
                case '1': self.ui.print_top_5(self.biblioteca.most_rented_book(), self.biblioteca)
                case '2': self.ui.print_clients(self.biblioteca.get_clients_with_rented_book(), self.biblioteca)
                case '3':
                    most_activ = self.ui.biggest_in_list(self.biblioteca.get_clients(), lambda client: client.rented_books)
                    print(f"The most activ client is: {most_activ}, with a record number of {len(most_activ.rented_books)} rented books!")
                case 'B' | 'BACK': break

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
                case '5':
                    self.ui.remove_client(self.biblioteca)
                case '6':
                    self.find_client()
                case '7':
                    self.ui.return_book(self.biblioteca)
                case '8':
                    self.ui.rent_book(self.biblioteca)
                case '9':
                    self.statistics()
                case 'P' | 'PRT' | 'PRINT':
                    self.biblioteca.print_all_books()
                case 'E': break


if __name__ == '__main__':
    run = Run()
    run.run()