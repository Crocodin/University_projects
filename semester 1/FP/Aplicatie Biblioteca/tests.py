import unittest

from Errors.my_errors import *

from Domain.book_class import Book
from Domain.client_class import Client
from Domain.rented_class import RentedClass
# ----------
from Repository.book_repo import BookRepo
from Repository.client_repo import ClientRepo
from Repository.rented_repo import RentedClassRepo
# ----------
from Service.book_service import BookService
from Service.client_service import ClientService
from Service.rented_service import RentedService

from sorting_algorithm.bubble_sort import Sort

class Test(unittest.TestCase):

    def setUp(self):
        # this will run to many times, so it's not that good
        book_repo = BookRepo('data/test_book.txt')
        client_repo = ClientRepo('data/test_client.txt')
        rented_repo = RentedClassRepo('data/test_rented.txt')

        self.book_id_1 = 'e8d5669f-2ae9-4d93-88c4-9451a28b837f'
        self.book_id_2 = '00677f65-e26e-434e-92da-f31d7714d5cc'

        self.client_id_1 = '46857144-d401-4100-8f67-46bc4ac3d751'
        self.client_id_2 = 'f264bf8f-7dc9-470f-856f-e4a04b11414a'

        self.book_service = BookService(book_repo)
        self.client_service = ClientService(client_repo)
        self.rented_service = RentedService(rented_repo, book_repo, client_repo)

    def test_book_create(self):
        book = Book()
        self.assertEqual(book.title, '')
        self.assertEqual(book.author, '')
        self.assertEqual(book.description, '')

        # ----------
        book = Book("title", "author", "description")
        self.assertEqual(str(book), '"Title" - Author')

    def test_book_eq(self):
        book1 = self.book_service.get_book(self.book_id_1)
        book2 = self.book_service.get_book(self.book_id_2)
        self.assertNotEqual(book1, book2)
        self.assertEqual(book1, str(book1))
        self.assertFalse(book1.eq_with_id(book2))
        self.assertNotEqual(book1.get_id(), book2.get_id())
        self.assertRaises(BookFoundError, self.book_service.get_book, 'invalid')

    def test_book_update(self):
        book = self.book_service.get_book(self.book_id_1)
        book.change_title('codrin parmac')
        self.assertEqual(book.title, 'Codrin parmac')
        book.change_author('codrin parmac')
        self.assertEqual(book.author, 'Codrin Parmac')
        book.change_description('codirn parmac')
        self.assertEqual(book.description, 'codirn parmac')

    def test_book_rent(self):
        book = self.book_service.get_book(self.book_id_1)
        self.assertTrue(book.rented)
        book.set_rented()
        self.assertFalse(book.rented)

    def test_add_remove_book(self):
        self.book_service.add_book('', '', '')
        book = self.book_service.get_all()[-1] # the last book
        self.assertIsNotNone(self.book_service.get_book(book.get_id()))

        # ----------
        self.book_service.remove(book.get_id())
        self.assertRaises(BookFoundError, self.book_service.get_book, book.get_id())

    def test_client_create(self):
        self.assertRaises(TypeError, Client)
        client = Client('Codrin parmac', '1010101010011')
        self.assertEqual(client.name, 'Codrin Parmac')
        client.change_name('mircea parmac')
        self.assertEqual(client.name, 'Mircea Parmac')

    def test_client_eq(self):
        client1 = self.client_service.find_client(self.client_id_1)
        client2 = self.client_service.find_client(self.client_id_2)
        self.assertNotEqual(client1, client2)
        self.assertTrue(client1.id__eq__(client1.get_id()))
        self.assertNotEqual(str(client1), str(client2))

    def test_add_remove_client(self):
        self.client_service.add_client('codrin parmac', '1010101010011')
        client = self.client_service.get_all()[-1]
        self.assertIsNotNone(self.client_service.find_client(client.get_id()))

        # ----------
        self.client_service.remove_client(client.get_id())
        self.assertRaises(ClientExistError, self.client_service.find_client, client.get_id())

    def test_rented(self):
        rented = self.rented_service.get_client(self.client_id_2)
        self.assertTrue(rented.number_of_books(), 2)
        self.assertTrue(rented.is_book_in(self.book_id_1))

    def test_add_remove_rented(self):
        client = self.client_service.find_client(self.client_id_1)
        book = self.book_service.get_book(self.book_id_1)
        self.rented_service.add_rented(client, book)
        self.assertIsNotNone(self.rented_service.get_client(client.get_id()))

        # ------------
        self.rented_service.remove_rented(client.get_id(), book.get_id())
        self.assertRaises(RentedExistError, self.rented_service.get_client, client.get_id())

        # ------------
        client = self.client_service.find_client(self.client_id_2)
        book = self.book_service.get_book(self.book_id_2)
        rented = self.rented_service.get_client(self.client_id_2)
        self.rented_service.add_rented(client, book)
        self.assertTrue(rented.is_book_in(self.book_id_2))
        self.rented_service.remove_book(book.get_id())
        self.assertFalse(rented.is_book_in(self.book_id_2))
        self.rented_service.remove_client(client.get_id())
        self.assertRaises(RentedExistError, self.rented_service.get_client, client.get_id())

    def test_tearDown(self):
        #rights to file the changes
        self.rented_service.wright_to_file()
        self.client_service.load()
        self.book_service.load()

    def test_sort(self):
        lista = [8, 7, 4, 3, 2, 6, 5, 9, 1]
        sort = Sort()
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9],sort.bubble_sort(lista))
        self.assertEqual([9, 8, 7, 6, 5, 4, 3, 2, 1],sort.bubble_sort(lista, reverse=True))
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9],sort.shell_sort(lista))
        self.assertEqual([9, 8, 7, 6, 5, 4, 3, 2, 1],sort.shell_sort(lista, reverse=True))
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9],sort.bubble_sort_recursive(lista))
        self.assertEqual([9, 8, 7, 6, 5, 4, 3, 2, 1],sort.bubble_sort_recursive(lista, reverse=True))
        self.assertEqual([1, 2, 3, 4, 5, 6, 7, 8, 9],sort.call_quick_sort(lista))