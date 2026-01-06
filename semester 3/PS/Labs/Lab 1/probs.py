from itertools import permutations, combinations, combinations_with_replacement
from math import perm, comb, factorial
import numpy as np
import random as rd

def permutation(something):
    for letters in permutations(something):
        yield ''.join(letters)

def print_list(arr):
    result = []
    for item in arr:
        print(item)
        result.append(item)
    return result

def arrangement(word:str, n:int, num_total:bool = False, random:bool = False):
    if num_total:
        print(perm(n, num_total))
        return
    if random:
        arr = list(permutations(word, n))
        print(rd.choice(arr))
        return
    for x in permutations(word, n):
        print(x)

def combination(word:str, n:int, num_total:bool = False, random:bool = False):
    if num_total:
        print(comb(n, num_total))
        return
    if random:
        arr = list(combinations(word, n))
        print(rd.choice(arr))
        return
    for x in combinations(word, n):
        print(x)

def comb_with_rep(word:str, n:int):
    for letters in combinations_with_replacement(word, n):
        yield ''.join(letters)

def lab1():
    # prob 1
    arr = print_list(permutation('word'))
    print(len(arr))
    print(arr[np.random.randint(low = 0, high = len(arr))])

    #prob 2
    arrangement('word', 2)
    arrangement('word', 2, num_total=True)
    arrangement('word', 2, random=True)

    combination('word', 2)
    combination('word', 2, num_total=True)
    combination('word', 2, random=True)

    #prob 3
    print_list(comb_with_rep('ABCDE', 4))

    #prob 4
    print(factorial(5) * comb(8, 5))

lab1()