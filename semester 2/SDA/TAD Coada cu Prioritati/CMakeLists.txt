cmake_minimum_required(VERSION 3.30)
project(TAD_Coada_cu_Prioritati)

set(CMAKE_CXX_STANDARD 14)

include_directories("Proiect C++")
include_directories(Teste)

add_executable(TAD_Coada_cu_Prioritati
    "Proiect C++/App.cpp"
    "Proiect C++/CP.cpp"
    "Proiect C++/CP.h"
    Teste/TestExtins.cpp
    Teste/TestExtins.h
    Teste/TestScurt.cpp
    Teste/TestScurt.h)
