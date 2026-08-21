cmake_minimum_required(VERSION 3.22.1)

project("dfgcontroller")

add_library(
        dfg-native
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        dfg-native
        ${log-lib})
