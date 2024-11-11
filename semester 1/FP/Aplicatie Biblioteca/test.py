#tests file

from DOMAIN import _book
from DOMAIN import _client
from DOMAIN import _biblioteca

def create_book() -> None:
    book = _book.Book()
    assert book.author == '' and  book.title == '' and book.description == ''
    book = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    assert book.author == 'Ray Bradbury' and book.title == 'Fahrenheit 451' and book.description == 'lorem'

def equal_book() -> None:
    book1 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    assert book1 == '"Fahrenheit 451" - Ray Bradbury'
    book2 = _book.Book('Fahrenheit 451', 'ray bradbury', 'not lorem')
    assert book1 == book2
    assert book1 != 'Fahrenheit'
    assert book1 != 123

def change_book_things() -> None:
    book1 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book1.change_title('1984')
    book1.change_author('GEORGE orwell')
    assert book1 == '"1984" - George Orwell'

def create_client() -> None:
    try:
        client = _client.Client('george the fool', '7437435')
        assert False
    except ValueError:
        assert True
    client = _client.Client('george the fool', '1020621271506')
    assert client.name == 'George The Fool'

def add_book() -> None:
    biblioteca = _biblioteca.Biblioteca()
    book1 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book2 = _book.Book('1984', 'george orwell', 'not lorem')
    biblioteca += book1
    assert biblioteca == book1
    assert biblioteca != book2
    try:
        assert biblioteca == book2
        raise IndexError
    except AssertionError:
        pass
    biblioteca += book2
    assert biblioteca == book2
    book3 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    assert biblioteca != book3
    assert biblioteca.book_is_in(str(book3)) == True

def remove_book() -> None:
    biblioteca = _biblioteca.Biblioteca()
    book1 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    book2 = _book.Book('1984', 'george orwell', 'not lorem')
    book3 = _book.Book('Fahrenheit 451', 'ray bradbury', 'lorem')
    biblioteca += book1
    biblioteca += book2
    biblioteca -= book2
    assert biblioteca != book2
    assert biblioteca.book_is_in(str(book2)) == False
    try:
        biblioteca.delete_book(book3)
        raise AssertionError
    except IndexError:
        pass
    assert biblioteca == book1
    biblioteca.delete_book(book1)
    assert biblioteca != book1

def add_client() -> None:
    biblioteca = _biblioteca.Biblioteca()
    client1 = _client.Client('george the fool', '5001210110011')
    biblioteca += client1
    assert biblioteca == client1
    client2 = _client.Client('george the fool', '5001210110011')
    assert biblioteca != client2

def remove_client() -> None:
    biblioteca = _biblioteca.Biblioteca()
    client1 = _client.Client('george the fool', '5001210110011')
    client2 = _client.Client('mircea the great', '5001210110012')
    biblioteca += client1
    biblioteca += client2
    biblioteca -= client1
    assert biblioteca != client1
    try:
        assert biblioteca == client1
        raise IndexError
    except AssertionError:
        pass
    try:
        biblioteca -= client1
        raise AssertionError
    except IndexError:
        pass

# run test functions
def run_test_book() -> None:
    create_book()
    equal_book()
    change_book_things()
    add_book()
    remove_book()

def run_test_client() -> None:
    create_client()
    add_client()
    remove_client()

if __name__ == '__main__':
    run_test_book()
    run_test_client()
    print("passed all tests")