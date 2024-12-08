from Domain.rented_class import RentedClass
from Domain.book_class import Book
from Domain.client_class import Client
from Errors.my_errors import *



class RentedClassRepo:

    def __init__(self, filename):
        self.__rented_list: list[RentedClass] = []
        self.filename = filename

    def add_rented(self, other):
        self.__rented_list.append(other)

    def remove_rented(self, id_client, id_book) -> None:
        for rented_class in self.__rented_list:
            if rented_class.get_id_client() == id_client:
                for book in rented_class.get_id_books():
                    if book == id_book:
                        rented_class.get_id_books().remove(book) # removes the book
                        if rented_class.number_of_books() == 0:
                            self.__rented_list.remove(rented_class) # removes the now empty reserved object
                        return
                raise BookFoundError(f'no book with {id_book} found!')
        raise ClientFoundError(f'no client with {id_client} found!')


    def read_from_file(self) -> None:
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[len(line) - 1] = line[len(line) - 1].strip()
                rented = RentedClass(line[0], line[1:])
                self.__rented_list.append(rented)

    def wright_to_file(self) -> None:
        with open(self.filename, "w") as file:
            for rented in self.__rented_list:
                file.write(rented.get_id_client())
                for books in rented.get_id_books():
                    file.write(f",{books}")
                file.write("\n")

    def get_list(self) -> list[RentedClass]:
        return self.__rented_list