cmake_minimum_required(VERSION 3.29)
project(Tema)

set(CMAKE_CXX_STANDARD 20)

add_executable(Tema main.cpp
        MDO.h
        MDO.cpp
        IteratorMDO.cpp
        TestScurt.cpp
        TestExtins.cpp
        TestScurt.h)
