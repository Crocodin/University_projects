from ui.console import Console
from Repository.book_repo import BookRepo
from Repository.client_repo import ClientRepo
from Repository.rented_repo import RentedClassRepo

book_repo = BookRepo('data/books.txt')
client_repo = ClientRepo('data/clients.txt')
rented_repo = RentedClassRepo('data/rented.txt')

console = Console(book_repo, client_repo, rented_repo)
console.run()