from Errors.my_errors import *
from validators.valid_type import *
from Service.biblioteca_class import Biblioteca
from Service.rented_service import RentedService
from Domain.client_class import Client
from Domain.book_class import Book
from Domain.rented_class import RentedClass

class Options:


    @staticmethod
    def match_input() -> str:
        return input("     >>>    ").strip().upper()

    @staticmethod
    def get_input_with_title(title: str, valid) -> str:
        while True:
            x = input(title).strip()
            if x == 'BACK': break
            if valid(x): return x
            else: print(f"{title} Invalid input. Please try again.")

    @staticmethod
    def invalid_input(my_input) -> None:
        print(f"{my_input} is input. Please try again.")

    def add_book(self) -> tuple[str, str, str]:
        """
            ---------------// add book //---------------

                Title:
                Author:

                Do you want to add a description? (y/n)
        """
        print("---------------// add book //---------------\n")

        title = self.get_input_with_title("   Title: ", is_string)
        author = self.get_input_with_title("   Author: ", is_name)
        description = ''
        print("   Do you want to add a description? (y/n)")
        while True:
            x = input("       >>>    ").strip().upper()
            if x == "Y":
                description = self.get_input_with_title("   Description:   ", is_string)
                break
            if x == "N": break
            else: self.invalid_input(x)
        return title, author, description

    def remove_book(self, bib: Biblioteca) -> None:
        """
            ---------------// remove book //---------------

                1. Remove with id
                2. Remove with list index
                3. Remove with name (this will remove all the book with this name that aren't rented)

                B. back

        """

        while True:
            print("---------------// remove book //---------------\n\n    1. Remove with id\n    2. Remove with list index\n    3. Remove with name (this will remove all the book with this name that aren't rented)\n\n    B. back\n")

            option = self.match_input()
            match option:
                case "1":
                    id_ = self.get_input_with_title("   Book id: ", is_string)
                    try:
                        bib.remove_book(bib.get_book_with_id(id_))
                        print("     Removed book successfully!")
                    except BookFoundError as e:
                        print(f"{e}")
                case "2":
                    try:
                        bib.pop_at_index(get_number() - 1)
                        print("     Removed book successfully!")
                    except BookExistError as e:
                        print(f"{e}")
                case '3':
                    title = self.get_input_with_title("   Title: ", is_string)
                    removed: bool = False
                    while bib.find_book(title):
                        bib.remove_book(bib.get_book_with_id(title))
                        removed = True
                    if removed: print("     Removed book successfully!")
                    else: print("     No book found!")
                case 'B' | 'BACK': break
                case _:
                    self.invalid_input(option)

    def add_client(self) -> tuple[str, str]:
        """
            ---------------// add client //---------------

                Name:
                CNP:
        """
        print("---------------// add book //---------------\n")

        name = self.get_input_with_title("   Name: ", is_string)
        cnp = self.get_input_with_title("   CNP: ", is_cnp)
        return name, cnp

    def remove_client(self, bib: Biblioteca, lista: list[RentedClass]) -> None:
        """
        ---------------// remove client //---------------

            1. Remove with cnp
            2. Remove with id

            B. back
        """
        while True:
            print("---------------// remove client //---------------\n    1. Remove with cnp\n    2. Remove with id\n\n    B. back\n")

            option = self.match_input()
            match option:
                case "1":
                    cnp_ = self.get_input_with_title("   Client cnp: ", is_cnp)
                    try:
                        client = bib.get_client_with_cnp(cnp_)
                        for rented in lista:
                            if rented.get_id_client() == client.get_id():
                                for item in rented.get_id_books():
                                    bib.get_book_with_id(item).set_rented()
                        bib.remove_client(client)
                        print("     Removed client successfully!")
                    except ClientFoundError as e:
                        print(f"{e}")
                case '2':
                    id_ = self.get_input_with_title("   Client id: ", is_string)
                    try:
                        client = bib.get_client(id_)
                        for rented in lista:
                            if rented.get_id_client() == client.get_id():
                                for item in rented.get_id_books():
                                    bib.get_book_with_id(item).set_rented()
                        bib.remove_client(client)
                        print("     Removed client successfully!")
                    except ClientFoundError as e:
                        print(f"{e}")
                case 'B' | 'BACK': break
                case _:
                    self.invalid_input(option)

    def return_book(self, bib: Biblioteca, lista: list[RentedClass]) -> None:
        id_ = self.get_input_with_title("   Client id: ", is_string)
        try:
            client = bib.get_client(id_)
            id_ = self.get_input_with_title("   Book id: ", is_string)
            try:
                bib.get_book_with_id(id_).set_rented()
                for rented in lista:
                    if rented.get_id_client() == client.get_id():
                        try:
                            rented.get_id_books().remove(id_)
                            if rented.number_of_books() == 0:
                                del rented
                        except IndexError:
                            print("Book not found! Unsuccessfully returned!")
                        break
                print("     Returned book successfully!")
            except BookFoundError as e:
                print(f"{e}")
            except IndexError as e:
                print(f"{e}")
        except ClientFoundError as e:
            print(f"{e}")
            return

    @staticmethod
    def append_rent_book(rented_books: list, id_client: str, id_book: str) -> None:
        for rented in rented_books:
            if rented.get_id_client() == id_client:
                rented.rent_book(id_book)
                return
        rented = RentedClass(id_client, [id_book])
        rented_books.append(rented)

    def rent_book(self, bib: Biblioteca, rented_service: RentedService) -> None:
        client = self.get_input_with_title("    Client id: ", is_string)
        try:
            client = bib.get_client(client)
        except ClientFoundError as e:
            print(f"{e}")
            return
        title = input("    Title & author: ")
        try:
            book = bib.get_book_reserved(title, False)
            print(f"{str(book)} is available! Want to rent? (y/n)")
            while True:
                option = self.match_input()
                match option:
                    case 'Y' | 'YES':
                        rented_service.add_rented(client, book)
                        print("     Rented book successfully!")
                        break
                    case 'N' | 'NO':
                        print("     Book not rented!")
                        break
                    case _: self.invalid_input(option)
        except BookFoundError as e:
            print(f"{e}")

    @staticmethod
    def print_top_5(lista: list[tuple[int, str]]) -> None:
        lista = lista[:5]
        for element in lista:
            print(element[1])

    @staticmethod
    def biggest_in_list(lista: list, function):
        lista.sort(key=function, reverse=True)
        return lista[0] # this returns the first element

    @staticmethod
    def edit_book_options():
        """
        ---------------// EDIT BOOK //---------------

            1. Change title
            2. Change author
            3. Change description

            B. back
        """

    def edit_book(self, bib: Biblioteca) -> None:
        id_ = self.get_input_with_title("   Book id: ", is_string)
        try:
            book = bib.get_book_with_id(id_)
            self.edit_book_options()
            while True:
                 match self.match_input():
                     case '1':
                         title = self.get_input_with_title("    Title: ", is_string)
                         book.change_title(title)
                         break
                     case '2':
                         author = self.get_input_with_title("    Author: ", is_string)
                         book.change_author(author)
                         break
                     case '3':
                         description = self.get_input_with_title("    Description: ", is_string)
                         book.change_description(description)
                         break
                     case _:
                         self.invalid_input("edit option")
        except BookFoundError as e:
            print(f"{e}")

    @staticmethod
    def print_activ_client(client: Client) -> None:
        print(
            f"The most activ client is: {client}, with a record number of {client.rented_books} rented books!")

    @staticmethod
    def print_all_clients(client_list: list[Client]) -> None:
        print("---------------// all the clients //---------------\n")
        index: int = 1
        for client in client_list:
            print(f"{index}. {str(client)}, {client.get_id()}", end='\n')
            index += 1
        print(end='\n')

    @staticmethod
    def print_all_books_with_name(book_list: list[Book], title: str) -> None:
        index: int = 1
        for book in book_list:
            if str(book) == title:
                print(f"{index}. {str(book)}; {book.get_id()}", end='')
                if book.rented: print(" rented")
                else: print(" not rented")
            index += 1
        print('\n')