from Repository.client_repo import ClientRepo
from Domain.client_class import Client

class ClientService:

    def __init__(self, repo: ClientRepo):
        self.__repo: ClientRepo = repo
        self.__repo.read_from_file()

    def add_client(self, name: str, cnp: str):
        """
        add a client to the repository
        :param name: name of the client
        :param cnp: cnp of the client
        :return: None
        """
        client = Client(name, cnp)
        self.__repo.add_client(client)

    def remove_client(self, id_client: str):
        """
        remove a client from the repository
        :param id_client: id of the client with we remove
        :return: None
        """
        client = self.__repo.get_client(id_client)
        self.__repo.remove_client(client)

    def find_client(self, id_client: str) -> Client:
        """
        search for a client in the repository
        :param id_client: the id of
        :return: the client with the given id
        :raise: ClientExistError, if no client was found
        """
        return self.__repo.get_client(id_client)

    def get_sorted_rented(self) -> list[Client]:
        """
        returns a sorted list of the client based on the amount of books rented
        :return: list of clients
        """
        return sorted(self.__repo.get_list(), key=lambda client: client.rented_books, reverse=True)

    def load(self):
        """
        loads the client from the repository to the file
        :return: None
        """
        self.__repo.wright_to_file()

    def get_all(self) -> list[Client]:
        """
        returns a list of all clients
        :return: list of clients
        """
        return self.__repo.get_list()