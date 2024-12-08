from Domain.client_class import Client
from errors.my_errors import *

class ClientRepo:

    def __init__(self, filename):
        self.__client_list: list[Client] = []
        self.filename = filename

    def add_client(self, client: Client):
        self.__client_list.append(client)

    def remove_client(self, client: Client):
        self.__client_list.remove(client)

    def get_client(self, other):
        for client in self.__client_list:
            if client.id__eq__(other):
                return client
        raise ClientExistError

    def read_from_file(self) -> None:
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[1] = line[1].strip()
                client = Client(line[0], line[1], line[2], int(line[3]))
                self.__client_list.append(client)

    def wright_to_file(self) -> None:
        with open(self.filename, "w") as file:
            for client in self.__client_list:
                file.write(f"{client.name},{client.get_cnp()},{client.get_id()},{client.rented_books}\n")

    def get_list(self) -> list[Client]:
        return self.__client_list