cmake_minimum_required(VERSION 3.28)
project(shop_store)

set(CMAKE_CXX_FLAGS "-Wall -Wextra -Werror")
set(CMAKE_CXX_FLAGS "--coverage -g ${CMAKE_CXX_FLAGS}")

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_CLANG_TIDY "/usr/bin/clang-tidy")
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(NOT CMAKE_CXX_CLANG_TIDY)
    message(STATUS "Clang-Tidy not set in CMake, using the default system path.")
else()
    message(STATUS "Clang-Tidy will be used from: ${CMAKE_CXX_CLANG_TIDY}")
endif()

message(STATUS "CXX flags: ${CMAKE_CXX_FLAGS}")

add_executable(shop_store main.cpp
        Domain/product.cpp
        Domain/product.h
        Tests/tests.h
        Tests/test_domain.cpp
        Domain/validator.cpp
        Domain/validator.h
        Repository/repo.cpp
        Repository/repo.h
        Tests/test_repo.cpp
        Service/service.cpp
        Service/service.h
        Controller/controller.cpp
        Controller/controller.h
        Tests/test_service.cpp
        DinamicList/list.hpp
        Tests/test_list.cpp
        .clang-tidy)
