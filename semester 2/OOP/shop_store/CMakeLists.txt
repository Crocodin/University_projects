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

configure_file(${CMAKE_SOURCE_DIR}/Utilities/template.html
        ${CMAKE_BINARY_DIR}/template.html COPYONLY)

# QT config

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

set(CMAKE_PREFIX_PATH "E:/Qt/6.9.0/mingw_64")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

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
        Ui/Controller/controller.cpp
        Ui/Controller/controller.h
        Tests/test_service.cpp
        DynamicList/list.hpp
        Tests/test_list.cpp
        Ui/Console/console.cpp
        Ui/Console/console.h
        .clang-tidy
        ShoppingCart/shoppingCart.cpp
        ShoppingCart/shoppingCart.h
        Tests/test_shopping_cart.cpp
        Tests/test_reopFile.cpp
        Tests/test_serviceFile.cpp
        Undo/undo.hpp
        Tests/test_undo.cpp
        GUI/guiController.cpp
        GUI/guiController.h)

target_link_libraries(shop_store
        Qt::Core
        Qt::Gui
        Qt::Widgets
)

if (WIN32 AND NOT DEFINED CMAKE_TOOLCHAIN_FILE)
    set(DEBUG_SUFFIX)
    if (MSVC AND CMAKE_BUILD_TYPE MATCHES "Debug")
        set(DEBUG_SUFFIX "d")
    endif ()
    set(QT_INSTALL_PATH "${CMAKE_PREFIX_PATH}")
    if (NOT EXISTS "${QT_INSTALL_PATH}/bin")
        set(QT_INSTALL_PATH "${QT_INSTALL_PATH}/..")
        if (NOT EXISTS "${QT_INSTALL_PATH}/bin")
            set(QT_INSTALL_PATH "${QT_INSTALL_PATH}/..")
        endif ()
    endif ()
    if (EXISTS "${QT_INSTALL_PATH}/plugins/platforms/qwindows${DEBUG_SUFFIX}.dll")
        add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
                COMMAND ${CMAKE_COMMAND} -E make_directory
                "$<TARGET_FILE_DIR:${PROJECT_NAME}>/plugins/platforms/")
        add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
                COMMAND ${CMAKE_COMMAND} -E copy
                "${QT_INSTALL_PATH}/plugins/platforms/qwindows${DEBUG_SUFFIX}.dll"
                "$<TARGET_FILE_DIR:${PROJECT_NAME}>/plugins/platforms/")
    endif ()
    foreach (QT_LIB Core Gui Widgets)
        add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
                COMMAND ${CMAKE_COMMAND} -E copy
                "${QT_INSTALL_PATH}/bin/Qt6${QT_LIB}${DEBUG_SUFFIX}.dll"
                "$<TARGET_FILE_DIR:${PROJECT_NAME}>")
    endforeach (QT_LIB)
endif ()

