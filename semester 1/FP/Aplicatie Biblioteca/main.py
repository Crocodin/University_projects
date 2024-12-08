from ui.console import Console
from Repository.book_repo import BookRepo
from Repository.client_repo import ClientRepo
from Repository.rented_repo import RentedClassRepo
# ----------
from Service.book_service import BookService
from Service.client_service import ClientService
from Service.rented_service import RentedService

book_repo = BookRepo('data/books.txt')
client_repo = ClientRepo('data/clients.txt')
rented_repo = RentedClassRepo('data/rented.txt')

book_service = BookService(book_repo)
client_service = ClientService(client_repo)
rented_service = RentedService(rented_repo, book_repo, client_repo)

console = Console(book_service, client_service, rented_service)
console.run()
