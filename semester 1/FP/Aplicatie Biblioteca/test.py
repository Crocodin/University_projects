#tests file

from Domain.book_class import Book
from Domain.client_class import Client
from Repository.biblioteca_class import Biblioteca
from errors.my_errors import *

def get_bib_with_elements() -> Biblioteca:
    biblioteca = Biblioteca()
    client1 = Client('george the fool', '1020621271506')
    client2 = Client('george the fool', '1020621271506')
    client3 = Client('Mircea the great', '5020611271506')
    biblioteca += client1
    biblioteca += client2
    biblioteca += client3
    book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book2 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book3 = Book("man's search for meaning",'viktor e. frankel')
    biblioteca += book1
    biblioteca._Biblioteca__add_book(book2)
    biblioteca += book3
    return biblioteca

def create_book_class() -> None:
    book = Book()
    assert book.author == '' and  book.title == '' and book.description == ''
    book = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    assert book.author == 'Ray Bradbury' and book.title == 'Fahrenheit 451' and book.description == 'lorem'

def equal_book_class() -> None:
    book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    assert book1 == '"Fahrenheit 451" - Ray Bradbury'
    book2 = Book('Fahrenheit 451', 'ray bradbury', 'not lorem')
    assert book1 == book2
    assert book1 != 'Fahrenheit'
    assert book1 != 123

def change_book_class_things() -> None:
    book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book1.change_title('1984')
    book1.change_author('GEORGE orwell')
    assert book1 == '"1984" - George Orwell'

def create_client_class() -> None:
    try:
        client = Client('george the fool', '7437435')
        assert False
    except ValueError:
        assert True
    client = Client('george the fool', '1020621271506')
    assert client.name == 'George The Fool'

def equal_client_class() -> None:
    client1 = Client('george the fool', '1020621271506')
    client2 = Client('george the fool', '1020621271506')
    assert client1 != client2

def change_client_class_things() -> None:
    client1 = Client('george the fool', '1020621271506')
    client1.change_name('mircea the great')
    assert client1.name == 'Mircea The Great'

def add_book() -> None:
    biblioteca = Biblioteca()
    book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book2 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    biblioteca += book1
    assert biblioteca.get_book_with_id(book1) == book1              # if book1 that is in bib has the same id as book1
    assert not biblioteca.get_book_with_id(book1).eq_with_id(book2) # they are the same books, but not the absolut same
    assert biblioteca.get_book(str(book1)) == book2                 # they have the same book, they are the same, but not absolut the same

def add_client() -> None:
    biblioteca = Biblioteca()
    client1 = Client('george the fool', '1020621271506')
    client2 = Client('george the fool', '1020621271506')
    client3 = Client('Mircea the great', '5020611271506')
    biblioteca += client1
    assert biblioteca.get_client(client1.get_id()) == client1
    try:
        biblioteca.get_client(client2.get_id())
        assert False
    except ClientFoundError:
        assert True
    biblioteca += client3
    assert biblioteca.get_client(client3.get_id()) == client3
    assert not biblioteca.get_client(client1.get_id()) == client3

def remove_book() -> None:
    book1 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book2 = Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book3 = Book("man's search for meaning"'viktor e. frankel')
    biblioteca = Biblioteca()
    biblioteca += book1
    biblioteca += book3
    try:
        biblioteca -= book2
        assert False
    except BookFoundError:
        assert True
    biblioteca._Biblioteca__add_book(book2)
    biblioteca -= book1
    try:
        biblioteca.get_book_with_id(book1)
        assert False
    except BookFoundError:
        assert biblioteca.get_book(str(book1)) == book2

def remove_client() -> None:
    biblioteca = Biblioteca()
    client1 = Client('george the fool', '1020621271506')
    client2 = Client('george the fool', '1020621271506')
    client3 = Client('Mircea the great', '5020611271506')
    biblioteca += client1
    biblioteca += client2
    biblioteca += client3
    biblioteca -= client1
    try:
        biblioteca.get_client(client1.get_id())
        assert False
    except ClientFoundError:
        assert biblioteca.get_client(client2.get_id()) == client2
    biblioteca -= client3
    try:
        biblioteca.get_client(client3.get_id())
        assert False
    except ClientFoundError:
        assert True

def edit_books(biblioteca: Biblioteca) -> None:
    book_name: str = '"Fahrenheit 451" - Ray Bradbury'
    book = biblioteca.get_book(book_name)
    book.change_description("Fahrenheit 451 is a 1953 dystopian novel by American writer Ray Bradbury")
    assert biblioteca.get_book(book_name).get_description() == "Fahrenheit 451 is a 1953 dystopian novel by American writer Ray Bradbury"
    book = biblioteca.get_description("lorem")
    book.change_description("no no no lorem")
    assert biblioteca.get_description("no no no lorem") == book

def edit_client(biblioteca: Biblioteca) -> None:
    client = biblioteca.get_client_with_cnp("5020611271506")
    client.change_name("marcus get big")
    assert biblioteca.get_client_with_cnp("5020611271506") == client

# run test functions
def run_test_book_class() -> None:
    create_book_class()
    equal_book_class()
    change_book_class_things()
    add_book()
    remove_book()
    edit_books(get_bib_with_elements())

def run_test_client_class() -> None:
    create_client_class()
    equal_client_class()
    change_client_class_things()
    add_client()
    remove_client()
    edit_client(get_bib_with_elements())

if __name__ == '__main__':
    run_test_book_class()
    run_test_client_class()
    print("passed all tests")