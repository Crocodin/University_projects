from Service.book_service import BookService
from Service.client_service import ClientService
from Service.rented_service import RentedService
from ui.display import Display
from Errors.my_errors import *


class Console:

    def __init__(self, book_service, client_service, rented_service):
        self.__book_service: BookService = book_service
        self.__client_service: ClientService = client_service
        self.__rented_service: RentedService = rented_service
        self.__display = Display() # all the menus

    def run(self) -> None:
        while True:
            self.__display.main_menu()
            option = input(self.__display.input_text_choice).strip().upper()
            match option:
                case "1":
                    title = input(self.__display.input_text_title)
                    author = input(self.__display.input_text_author)
                    description = input(self.__display.input_text_description)
                    self.__book_service.add_book(title, author, description)
                case "2":
                    id_book = input(self.__display.input_text_id_book)
                    try:
                        self.__book_service.remove(id_book)
                        self.__rented_service.remove_book(id_book)
                    except ValueError:
                        print("Book not found with given id")
                case "3":
                    id_book = input(self.__display.input_text_id_book)
                    try:
                        book = self.__book_service.get_book(id_book)
                        self.__display.book_info(book)
                    except BookFoundError:
                        print("Book not found with given id")
                case "4":
                    name = input(self.__display.input_text_name)
                    cnp = input(self.__display.input_text_cnp)
                    try:
                        self.__client_service.add_client(name, cnp)
                    except Exception:
                        print("Invalid CNP")
                case "5":
                    id_client = input(self.__display.input_text_id_client)
                    try:
                        self.__client_service.remove_client(id_client)
                        self.__rented_service.remove_client(id_client)
                    except ClientExistError:
                        print("Client not found with given id")
                case "6":
                    id_client = input(self.__display.input_text_id_client)
                    client = self.__client_service.find_client(id_client)
                    self.__display.client_info(client, self.__rented_service.get_client(id_client), self.__book_service)
                case "7":
                    id_book = input(self.__display.input_text_id_book)
                    id_client = input(self.__display.input_text_id_client)
                    try:
                        self.__rented_service.remove_rented(id_client, id_book)
                    except RentedExistError:
                        print("No client with given id rented book with given id")
                case "8":
                    id_book = input(self.__display.input_text_id_book)
                    id_client = input(self.__display.input_text_id_client)
                    try:
                        book = self.__book_service.get_book(id_book)
                        client = self.__client_service.find_client(id_client)
                        self.__rented_service.add_rented(client, book)
                    except Exception:
                        print("    Invalid input, try again.")
                case "9":
                    while True:
                        self.__display.statistics()
                        option = input(self.__display.input_text_choice).strip().upper()
                        match option:
                            case "1":
                                lista = self.__book_service.get_sorted_rented()
                                for book in lista[:5]:
                                    print(f"    {str(book)}")
                            case "2":
                                for rented in self.__rented_service.get_all():
                                    print(f"    {str(self.__client_service.find_client(rented.get_id_client()))}")
                            case "3":
                                client = self.__client_service.get_sorted_rented()[0]
                                self.__display.client_info(client, self.__rented_service.get_client(client.get_id()), self.__book_service)
                            case "B" | "BACK":
                                break
                case "E" | "EXIT":
                    break
                case "P" | "PRINT":
                    print("---------------// books //---------------")
                    for book in self.__book_service.get_all():
                        print(f"    {str(book)}, rented status: {book.rented}")
                    print("---------------// clients //---------------")
                    for client in self.__client_service.get_all():
                        print(f"    {str(client)}")
        self.__book_service.load()
        self.__client_service.load()
        self.__rented_service.wright_to_file()
