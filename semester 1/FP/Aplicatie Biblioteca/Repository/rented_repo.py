from Domain.rented_class import RentedClass
from Domain.book_class import Book
from Domain.client_class import Client
from Errors.my_errors import *



class RentedClassRepo:

    def __init__(self, filename):
        self.__rented_list: list[RentedClass] = []
        self.filename = filename

    def add_rented(self, other):
        """
        adds a rented class to the rented list
        :param other: rented class
        :return: None
        """
        self.__rented_list.append(other)

    def remove_rented(self, id_client, id_book) -> None:
        """
        removes a rented book from the rented list
        :param id_client: id of the client that rented the book
        :param id_book: id of book
        :return: None
        :raise: BookFoundError, if no book has this id
        :raise: ClientFoundError, if no client has this id
        """
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
        """
        read from file all the rented class objects
        :return: None
        """
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[len(line) - 1] = line[len(line) - 1].strip()
                rented = RentedClass(line[0], line[1:])
                self.__rented_list.append(rented)

    def wright_to_file(self) -> None:
        """
        write to file all the rented class objects
        :return: None
        """
        with open(self.filename, "w") as file:
            for rented in self.__rented_list:
                file.write(rented.get_id_client())
                for books in rented.get_id_books():
                    file.write(f",{books}")
                file.write("\n")

    def get_list(self) -> list[RentedClass]:
        """
        returns all the rented class objects
        :return: list of rented class
        """
        return self.__rented_list