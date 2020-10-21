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

import me.xizzhu.android.kvs.Kvs
import me.xizzhu.android.kvs.KvsException

internal class LmdbKvsTransaction(private val database: Database) : Kvs {
    override fun get(key: ByteArray): ByteArray? {
        if (key.isEmpty()) {
            throw KvsException("Key is empty")
        }

        return database.get(key)
    }

    override fun set(key: ByteArray, value: ByteArray) {
        if (key.isEmpty()) {
            throw KvsException("Key is empty")
        }
        if (value.isEmpty()) {
            throw KvsException("Value is empty")
        }

        database.set(key, value)
    }

    override fun remove(key: ByteArray): Boolean {
        if (key.isEmpty()) {
            throw KvsException("Key is empty")
        }

        return database.remove(key)
    }

    override fun <R> withTransaction(readOnly: Boolean, block: (Kvs) -> R): R {
        throw KvsException("Nested transactions are not supported yet.")
    }

    override fun close() {
        // Do nothing.
    }
}
