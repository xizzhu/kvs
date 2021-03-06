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
import me.xizzhu.android.kvs.KvsConfig
import me.xizzhu.android.kvs.KvsException

internal class LmdbKvs(config: KvsConfig) : Kvs {
    private val env = Env(config)

    override fun contains(key: ByteArray): Boolean {
        if (key.isEmpty()) {
            throw KvsException("Key is empty")
        }

        return withReadOnlyTransaction { it.contains(key) }
    }

    override fun get(key: ByteArray): ByteArray? {
        if (key.isEmpty()) {
            throw KvsException("Key is empty")
        }

        return withReadOnlyTransaction { it.get(key) }
    }

    override fun edit(): Kvs.Editor = LmdbKvsEditor(env)

    private inline fun <R> withReadOnlyTransaction(block: (Database) -> R): R {
        val transaction = env.newTransaction(readOnly = true)
        var exception: Throwable? = null
        try {
            transaction.openDatabase().use { database ->
                return block(database)
            }
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            try {
                transaction.abort()
            } catch (e: Throwable) {
                if (exception == null) throw e
            }
        }
    }

    override fun close() {
        env.close()
    }
}
