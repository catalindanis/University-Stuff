cmake_minimum_required(VERSION 3.29)
project(Laborator_4)

set(CMAKE_CXX_STANDARD 14)

include_directories(tema)

add_executable(Laborator_4
        tema/main.cpp
        tema/TestExtins.cpp
    tema/TestExtins.h
    tema/TestScurt.cpp
    tema/TestScurt.h
        tema/Multime.cpp
        tema/Multime.h
        tema/IteratorMultime.cpp
        tema/IteratorMultime.h
        tema/Nod.cpp
        tema/Nod.h
)
