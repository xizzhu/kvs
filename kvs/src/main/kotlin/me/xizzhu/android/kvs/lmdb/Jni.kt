/*
 * Copyright (C) 2020 Xizhi Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xizzhu.android.kvs.lmdb

internal object Jni {
    init {
        System.loadLibrary("lmdb")
    }

    external fun createEnv(): Long

    external fun closeEnv(nativeEnv: Long)

    external fun openEnv(nativeEnv: Long, path: String, flags: Int, mode: Int)

    external fun beginTransaction(nativeEnv: Long, readOnly: Boolean): Long

    external fun commitTransaction(nativeTransaction: Long)

    external fun abortTransaction(nativeTransaction: Long)

    external fun openDatabase(nativeTransaction: Long, createIfNotExists: Boolean): Long

    external fun closeDatabase(nativeEnv: Long, nativeDatabase: Long)

    external fun getData(nativeTransaction: Long, nativeDatabase: Long, key: ByteArray): ByteArray?

    external fun setData(nativeTransaction: Long, nativeDatabase: Long, key: ByteArray, value: ByteArray)
}
