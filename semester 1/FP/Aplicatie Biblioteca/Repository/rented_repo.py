from Domain.rented_class import RentedClass

class RentedClassRepo:

    def __init__(self, filename):
        self.__rented_list: list[RentedClass] = []
        self.filename = filename

    def read_from_file(self) -> None:
        with open(self.filename, "r") as file:
            for line in file:
                line = line.split(',')
                line[len(line) - 1] = line[len(line) - 1].strip()
                rented = RentedClass(line[0], line[1:])
                self.__rented_list.append(rented)

    def wright_to_file(self) -> None:
        with open(self.filename, "w") as file:
            for rented in self.__rented_list:
                file.write(rented.get_id_client())
                for books in rented.get_id_books():
                    file.write(f",{books}")
                file.write("\n")

    def get_list(self) -> list[RentedClass]:
        return self.__rented_list