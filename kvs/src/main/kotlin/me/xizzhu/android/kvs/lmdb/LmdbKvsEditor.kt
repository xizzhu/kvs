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

internal class LmdbKvsEditor(env: Env) : Kvs.Editor {
    private val transaction = env.newTransaction(readOnly = false)
    private val database = transaction.openDatabase()

    override fun set(key: ByteArray, value: ByteArray) {
        if (key.isEmpty()) {
            abortAndThrow("Key is empty")
        }
        if (value.isEmpty()) {
            abortAndThrow("Value is empty")
        }

        database.set(key, value)
    }

    private fun abortAndThrow(msg: String) {
        abort()
        throw KvsException(msg)
    }

    override fun remove(key: ByteArray): Boolean {
        if (key.isEmpty()) {
            abortAndThrow("Key is empty")
        }

        return database.remove(key)
    }

    override fun commit() {
        database.close()
        transaction.commit()
    }

    override fun abort() {
        database.close()
        transaction.abort()
    }
}
