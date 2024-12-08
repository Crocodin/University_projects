import unittest

from Repository.book_repo import BookRepo
from Repository.rented_repo import RentedClassRepo
from Repository.client_repo import ClientRepo
from validators.valid_type import *
from Domain.book_class import Book
from Domain.client_class import Client
from Domain.rented_class import RentedClass
from Service.biblioteca_class import Biblioteca
from Service.rented_service import RentedService
from ui.options import Options
from errors.my_errors import *

class WhiteBoxTesting(unittest.TestCase):

    def setUp(self) -> None:

        self.__book_repo = BookRepo('data/test_book.txt')
        self.__client_repo = ClientRepo('data/test_client.txt')
        self.__rented_repo = RentedClassRepo('data/test_rented.txt')

        self.rented_service = RentedService(self.__rented_repo)

        self.__book_repo.read_from_file()
        self.__client_repo.read_from_file()

        self.biblioteca = Biblioteca(self.__book_repo.get_list(), self.__client_repo.get_list())
        self.options: Options = Options()
        self.rented_books: list[RentedClass] = self.rented_service.get_all()

    def test_create_book(self):
        book = Book()
        self.assertEqual(book.author, '')
        self.assertEqual(book.title, '')
        self.assertEqual(book.description, '')
        self.assertEqual(book.rented, False)
        self.assertEqual(book.get_how_many_time_rented(), 0)
        self.assertNotEqual(book.get_id(), None)

        book = Book('The great cod', 'Cod Rein')
        self.assertEqual(book.title, 'The great cod')
        self.assertEqual(book.author, 'Cod Rein')

    @staticmethod
    def aux_function():
        client = Client('george the fool', '7437435')

    def test_create_client(self):
        client = Client('Mircea the Great', '5050622261009')
        self.assertEqual(client.name, 'Mircea The Great')
        self.assertEqual(client.get_cnp(), '5050622261009')
        self.assertRaises(ValueError, self.aux_function)

    def test_equal_book(self) -> None:
        book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
        self.assertEqual(book1, '"Fahrenheit 451" - Ray Bradbury')
        book2 = Book('Fahrenheit 451', 'ray bradbury', 'not lorem')
        self.assertEqual(book1, book2)
        self.assertNotEqual(book1.get_id(), book2.get_id())

    def test_add_book(self):
        book = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
        self.biblioteca._Biblioteca__add_book(book)
        self.assertEqual(self.biblioteca.get_book_with_id(book).get_id(), book.get_id()) # if book1 that is in bib has the same id as book1

    def test_add_client(self) -> None:
        client1 = Client('george the fool', '1020621271506')
        client3 = Client('Mircea the great', '5020611271506')
        self.biblioteca += client1
        self.assertEqual(self.biblioteca.get_client(client1.get_id()), client1)
        self.biblioteca += client3
        self.assertEqual(self.biblioteca.get_client(client3.get_id()), client3)

    def test_remove_book(self):
        book = self.biblioteca.get_book_with_id('29e94c67-c49e-49f1-a665-96e5338a69ee')
        self.biblioteca.remove_book(book)
        self.assertNotEqual(self.biblioteca, book)

    def test_remove_client(self):
        client = self.biblioteca.get_client('c0a067b8-f133-475b-8dd5-16a531128c52')
        self.biblioteca -= client
        self.assertNotEqual(client, self.biblioteca)

    def test_cnp_valid(self):
        self.assertTrue(is_cnp('5050622261009'))
        self.assertFalse(is_cnp('9051622261009'))

    def test_reserve_book(self):
        book = self.biblioteca.get_book_with_id('627a87d3-be20-4b40-ac63-fb74d8eb4543')
        book.set_rented() # the book in rented now
        self.options.append_rent_book(self.rented_books, 'c0a067b8-f133-475b-8dd5-16a531128c52', book.get_id())
        self.assertTrue(book.rented)
        fast_aces = self.rented_books[0] # the book was added to the file that exists already
        self.assertEqual(fast_aces.get_id_client(), 'c0a067b8-f133-475b-8dd5-16a531128c52')
        self.assertTrue(fast_aces.is_book_in(book.get_id()))
        self.assertFalse(fast_aces.is_book_in('c0a067b8-f133-475b-8dd5-16a531128c52'))


    def test_reserve_rented_book(self):
        book = self.biblioteca.get_book_with_id('dae9e51d-2501-4f0d-b0ee-55c3355a6fa6')
        book.set_rented()  # the book in rented now
        self.assertRaises(BookFoundError, self.biblioteca.get_book_reserved, str(book), False)

    def test_find_book(self): # this works with the book title & author in mind
        self.assertTrue(self.biblioteca.find_book('"Fahrenheit 451" - Ray Bradbury'))
        self.assertFalse(self.biblioteca.find_book('"War" - Bob Marley'))

    def test_get_book(self): # this works with the book title & author in mind
        self.assertIsNotNone(self.biblioteca.get_book('"Fahrenheit 451" - Ray Bradbury'))
        self.assertRaises(BookFoundError, self.biblioteca.get_book, '"War" - Bob Marley')

    def test_find_client(self):
        self.assertTrue(self.biblioteca.find_client('c0a067b8-f133-475b-8dd5-16a531128c52'))
        self.assertFalse(self.biblioteca.find_client('i-invented-a-random-id'))

    def test_get_client(self):
        self.assertIsNotNone(self.biblioteca.get_client('c0a067b8-f133-475b-8dd5-16a531128c52'))
        self.assertRaises(ClientFoundError, self.biblioteca.get_client, 'i-invented-a-random-id')

    def test_remove_book(self):
        book = self.biblioteca.get_book('"Fahrenheit 451" - Ray Bradbury')
        self.biblioteca.remove_book(book)
        self.assertRaises(BookFoundError, self.biblioteca.get_book_with_id, book.get_id())
        book = Book()
        self.assertRaises(BookFoundError, self.biblioteca.remove_book, book)

    def test_pop_at_index(self):
        book = self.biblioteca._Biblioteca__book_list[3] # get the book that has the 3rd index
        self.biblioteca.pop_at_index(3)
        self.assertRaises(BookFoundError, self.biblioteca.get_book_with_id, book.get_id())
        self.assertRaises(BookExistError, self.biblioteca.pop_at_index, 100)

    def test_get_book_with_id(self):
        self.assertEqual(self.biblioteca.get_book_with_id('dae9e51d-2501-4f0d-b0ee-55c3355a6fa6').get_id(), 'dae9e51d-2501-4f0d-b0ee-55c3355a6fa6')
        self.assertRaises(BookFoundError, self.biblioteca.get_book_with_id, 'i-invented-a-random-id')

    def test_remove_client(self):
        client = self.biblioteca.get_client('c0a067b8-f133-475b-8dd5-16a531128c52')
        self.biblioteca.remove_client(client)
        self.assertFalse(self.biblioteca.find_client(client.get_id()))
        self.assertRaises(ClientFoundError, self.biblioteca.remove_client, client.get_id())

    def test_get_client_with_cnp(self):
        self.assertEqual(self.biblioteca.get_client_with_cnp('5050213110001').get_cnp(), '5050213110001')
        self.assertRaises(ClientFoundError, self.biblioteca.get_client_with_cnp, 'i-invented-a-random-cnp')


    def test_most_rented_book(self):
        self.assertEqual(self.biblioteca.most_rented_book(),
                         [(2, '"Lord of the flies" - William Golding'), (1, '"V de la vendeta" - Alan Moore'), (1, '"V de la vendeta" - Alan Moore'), (1, '"Prietena mea geniala" - Elena Ferrante'), (1, '"Maestrul si margareta" - Mihail Bulgakov'), (1, '"Man\'s search for meaning" - Viktor E. Frankel'), (0, '"V de la vendeta" - Alan Moore'), (0, '"The last snow" - Allen Eskens'), (0, '"The last snow" - Allen Eskens'), (0, '"Prietena mea geniala" - Elena Ferrante'), (0, '"Prietena mea geniala" - Elena Ferrante'), (0, '"Povestea noului nume" - Elena Ferrante'), (0, '"Crima si pedeapsa" - Feodor Dostoievski'), (0, '"Calatorul singuratic" - Jack Kerouac'), (0, '"Cel mai iubit dintre cetatieni" - Marin Preda'), (0, '"Cel mai iubit dintre cetatieni" - Marin Preda'), (0, '"Cel mai iubit dintre cetatieni" - Marin Preda'), (0, '"The book thief" - Markus Zusak'), (0, '"The broken earth" - N. K. Jemisin'), (0, '"Principele" - Niccolo Machiavelli'), (0, '"Principele" - Niccolo Machiavelli'), (0, '"Fahrenheit 451" - Ray Bradbury')])

    def test_add_rented(self):
        client = self.biblioteca.get_client('46857144-d401-4100-8f67-46bc4ac3d751')
        book = self.biblioteca.get_book_with_id('dae9e51d-2501-4f0d-b0ee-55c3355a6fa6')
        self.rented_service.add_rented(client, book)
        self.assertIsNotNone(self.rented_service.get_client('46857144-d401-4100-8f67-46bc4ac3d751'))
        self.assertRaises(RentedExistError, self.rented_service.get_client, 'i-invented-a-random-id')
