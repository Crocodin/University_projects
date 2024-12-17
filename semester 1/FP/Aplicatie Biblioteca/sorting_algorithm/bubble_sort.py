
class Sort:

    @staticmethod
    def deepcopy(array: list) -> list:
        array_copy: list = []
        for element in array:
            array_copy.append(element)
        return array_copy

    def bubble_sort(self, array: list, key = lambda element: element, reverse = False) -> list:
        # Sorts a copy of the original list
        array = self.deepcopy(array)
        length: int = len(array)
        i: int = 0
        while i < length:
            swapped: bool = False
            j: int = 0
            while j < length:
                if not reverse:
                    if key(array[i]) < key(array[j]):
                        # swap equivalent in python
                        array[i], array[j] = array[j], array[i]
                        swapped = True
                else:
                    if key(array[i]) > key(array[j]):
                        array[i], array[j] = array[j], array[i]
                        swapped = True
                j += 1
            if not swapped:
                break
            i += 1
        return array

    def __recursive_algorithm(self, array: list, key, reverse, i: int = 0, length: int = -1) -> list:
        swapped: bool = False
        j: int = 0

        if i == length:
            return array

        while j < length:
            if not reverse:
                if key(array[i]) < key(array[j]):
                    # swap equivalent in python
                    array[i], array[j] = array[j], array[i]
                    swapped = True
            else:
                if key(array[i]) > key(array[j]):
                    array[i], array[j] = array[j], array[i]
                    swapped = True
            j += 1
        if not swapped:
            return array
        return self.__recursive_algorithm(array, key, reverse, i + 1, length)

    def bubble_sort_recursive(self, array: list, key = lambda element: element, reverse = False) -> list:
        # Sorts recursively a copy of the original list
        array = self.deepcopy(array)
        length: int = len(array)
        return self.__recursive_algorithm(array, key, reverse, 0, length)

    def shell_sort(self, array:list, key = lambda element: element, reverse = False) -> list:
        # Sorts a copy of the original list
        array = self.deepcopy(array)
        length: int = len(array)

        gap: int = length // 2

        while gap > 0:
            j: int = gap
            # Check the array in from left to right
            while j < length:
                i: int = j - gap  # This will keep help in maintain gap value
                while i >= 0:
                    # If value on right side is already greater than left side value
                    # We don't do swap else we swap
                    if not reverse:
                        if key(array[i + gap]) > key(array[i]): break
                    else:
                        if key(array[i + gap]) < key(array[i]): break
                    array[i + gap], array[i] = array[i], array[i + gap]

                    i = i - gap  # To check left side also
                    # If the element present is greater than current element
                j += 1
            gap = gap // 2
        return array
