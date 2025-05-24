cmake_minimum_required(VERSION 3.31)
project(TAD_MultiDictionar_Ordonat)

set(CMAKE_CXX_STANDARD 14)

include_directories("Proiect C++")
include_directories(Teste)

add_executable(TAD_MultiDictionar_Ordonat
        "Proiect C++/App.cpp"
        "Proiect C++/IteratorMDO.cpp"
        "Proiect C++/IteratorMDO.h"
        "Proiect C++/MDO.cpp"
        "Proiect C++/MDO.h"
        Teste/TestExtins.cpp
        Teste/TestExtins.h
        Teste/TestScurt.cpp
        Teste/TestScurt.h)
