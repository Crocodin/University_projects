cmake_minimum_required(VERSION 3.30)
project(object_oriented_in_C C)

set(CMAKE_C_STANDARD 11)

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
        console/console.h)
