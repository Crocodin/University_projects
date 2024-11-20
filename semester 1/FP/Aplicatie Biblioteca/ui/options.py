from errors.my_errors import *
from validators.valid_type import *
from Repository.biblioteca_class import Biblioteca

class Options:

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
        print("---------------// main menu //---------------\n\n    1. Add a book             4. Add a client\n    2. Remove a book          5. Remove a client\n    3. Find a book            6. Find a client\n\n    7. Return book            8. Rent book\n\n                 9. Statistics\n\n     E. exit                   P. print")

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
            print("---------------// remove book //---------------\n\n    1. Remove with id\n    2. Remove with list index\n    3. Remove with name (this will remove all the book with this name that aren't rented)\n    B. back\n\n")

            option = self.match_input()
            match option:
                case "1":
                    id_ = self.get_input_with_title("   Book id: ", is_string)
                    try:
                        bib.remove_book(bib.get_book_with_id_string(id_))
                        print("     Removed book successfully!")
                    except BookFoundError as e:
                        print(f"{e}")
                case "2":
                    try:
                        bib.pop_at_index(get_number())
                        print("     Removed book successfully!")
                    except BookExistError as e:
                        print(f"{e}")
                case '3':
                    title = self.get_input_with_title("   Title: ", is_string)
                    removed: bool = False
                    while bib.find_book(title):
                        bib.remove_book(bib.get_book_with_id_string(title))
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

    def remove_client(self, bib: Biblioteca) -> None:
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
                        bib.remove_client(bib.get_client_with_cnp(cnp_))
                        print("     Removed client successfully!")
                    except ClientFoundError as e:
                        print(f"{e}")
                case '2':
                    id_ = self.get_input_with_title("   Client id: ", is_string)
                    try:
                        bib -= bib.get_client(id_)
                        print("     Removed client successfully!")
                    except ClientFoundError as e:
                        print(f"{e}")
                case 'B' | 'BACK': break
                case _:
                    self.invalid_input(option)

    def return_book(self, bib: Biblioteca) -> None:
        id_ = self.get_input_with_title("   Client id: ", is_string)
        try:
            client = bib.get_client(id_)
        except ClientFoundError as e:
            print(f"{e}")
            return
        id_ = self.get_input_with_title("   Book id: ", is_string)
        try:
            bib.get_book(id_).set_rented()
            client.get_book_rented().remove(id_)
            print("     Returned book successfully!")
        except BookFoundError as e:
            print(f"{e}")
        except IndexError as e:
            print(f"{e}")

    def rent_book(self, bib: Biblioteca) -> None:
        client = self.get_input_with_title("    Client id: ", is_string)
        try:
            client = bib.get_client(client)
        except ClientFoundError as e:
            print(f"{e}")
            return
        title = input("    Title: ")
        try:
            book = bib.get_book_reserved(title, False)
            print(f"{str(book)} is available! Want to rent? (y/n)")
            while True:
                option = self.match_input()
                match option:
                    case 'Y' | 'YES':
                        book.set_rented()
                        client.add_rented_book(book.get_id())
                        print("     Rented book successfully!")
                        break
                    case 'N' | 'NO':
                        print("     Book not rented!")
                        break
                    case _: self.invalid_input(option)
        except BookFoundError as e:
            print(f"{e}")

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
    def print_top_5(lista, bib: Biblioteca) -> None:
        lista = lista[:5]
        for element in lista:
            print(str(bib.get_book_with_id(element[1])))

    @staticmethod
    def print_clients(lista, bib: Biblioteca) -> None:
        for element in lista:
            print(str(bib.get_client(element)))

    @staticmethod
    def biggest_in_list(lista, function):
        lista.sort(key=function)
        return lista[0] # this returns the first element
