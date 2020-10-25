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

import java.io.Closeable

internal class Database(private val nativeEnv: Long, private val nativeTransaction: Long, createIfNotExists: Boolean) : Closeable {
    private val nativeDatabase = Jni.openDatabase(nativeTransaction, createIfNotExists)

    override fun close() {
        Jni.closeDatabase(nativeEnv, nativeDatabase)
    }

    fun contains(key: ByteArray): Boolean = Jni.containsKey(nativeTransaction, nativeDatabase, key)

    fun get(key: ByteArray): ByteArray? = Jni.getData(nativeTransaction, nativeDatabase, key)

    fun set(key: ByteArray, value: ByteArray) {
        Jni.setData(nativeTransaction, nativeDatabase, key, value)
    }

    fun remove(key: ByteArray): Boolean = Jni.removeData(nativeTransaction, nativeDatabase, key)
}
