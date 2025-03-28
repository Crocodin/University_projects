cmake_minimum_required(VERSION 3.28)
project(shop_store)

set(CMAKE_CXX_STANDARD 20)

add_executable(shop_store main.cpp
        Domain/product.cpp
        Domain/product.h)
