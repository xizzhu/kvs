# Copyright (C) 2020 Xizhi Zhu

cmake_minimum_required(VERSION 3.4.1)

add_library(
        lmdb SHARED

        lmdb/libraries/liblmdb/mdb.c
        lmdb/libraries/liblmdb/midl.c
        jni_lmdb.cpp
)
