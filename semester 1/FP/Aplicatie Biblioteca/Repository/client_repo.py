from sorting_algorithm.bubble_sort import Sort
from Domain.client_class import Client
from Errors.my_errors import *

class ClientRepo:

    def __init__(self, filename):
        self.__client_list: list[Client] = []
        self.filename = filename

    def add_client(self, client: Client):
        """
        add a client to the list of clients
        :param client: client to add
        :return: None
        """
        self.__client_list.append(client)

    def remove_client(self, client: Client):
        """
        remove a client from the list of clients
        :param client: client to remove
        :return: None
        """
        self.__client_list.remove(client)

    def get_client(self, other):
        """
        get a client from the list of clients
        :param other: id of the client to get
        :return: Client
        :raise ClientExistError: if no client exists with that id
        """
        for client in self.__client_list:
            if client.id__eq__(other):
                return client
        raise ClientExistError

    def read_from_file(self) -> None:
        """
        read from file all the clients and adds them to the list of clients
        :return: None
        """
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[1] = line[1].strip()
                client = Client(line[0], line[1], line[2], int(line[3]))
                self.__client_list.append(client)

    def wright_to_file(self) -> None:
        """
        wright to file all the clients
        :return: None
        """
        sort = Sort()
        with open(self.filename, "w") as file:
            for client in sort.bubble_sort(
                self.__client_list,
                key=lambda client_: client_.name,
            ):
                file.write(f"{client.name},{client.get_cnp()},{client.get_id()},{client.rented_books}\n")

    def get_list(self) -> list[Client]:
        """
        returns all the clients
        :return: list of Client
        """
        return self.__client_list