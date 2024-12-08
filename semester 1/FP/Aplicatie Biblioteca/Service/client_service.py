from Repository.client_repo import ClientRepo
from Domain.client_class import Client

class ClientService:

    def __init__(self, repo: ClientRepo):
        self.__repo: ClientRepo = repo
        self.__repo.read_from_file()

    def add_client(self, name: str, cnp: str):
        client = Client(name, cnp)
        self.__repo.add_client(client)

    def remove_client(self, id_client: str):
        client = self.__repo.get_client(id_client)
        self.__repo.remove_client(client)

    def find_client(self, id_client: str):
        return self.__repo.get_client(id_client)

    def get_sorted_rented(self):
        return sorted(self.__repo.get_list(), key=lambda client: client.rented_books, reverse=True)

    def load(self):
        self.__repo.wright_to_file()

    def get_all(self) -> list[Client]:
        return self.__repo.get_list()