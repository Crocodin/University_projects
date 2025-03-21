cmake_minimum_required(VERSION 3.28.3)
project(object_oriented_in_C C)

set(CMAKE_C_STANDARD 11)

set(CMAKE_C_FLAGS "-Wall -Wextra")

add_executable(object_oriented_in_C main.c
        domain/meds.c
        domain/meds.h
        repository/repo.c
        repository/repo.h
        service/service.c
        service/service.h
        domain/validator.c
        domain/validator.h
        console/console.c
        console/console.h
        test/test.c
        test/test.h)
