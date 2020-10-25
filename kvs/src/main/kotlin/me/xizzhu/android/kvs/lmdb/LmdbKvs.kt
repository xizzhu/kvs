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

internal class LmdbKvs(config: KvsConfig) : Kvs {
    private val env = Env(config)

    override fun contains(key: ByteArray): Boolean = withTransaction(readOnly = true) { it.contains(key) }

    override fun get(key: ByteArray): ByteArray? = withTransaction(readOnly = true) { it[key] }

    override fun set(key: ByteArray, value: ByteArray) {
        withTransaction(readOnly = false) { it[key] = value }
    }

    override fun remove(key: ByteArray): Boolean = withTransaction(readOnly = false) { it.remove(key) }

    override fun <R> withTransaction(readOnly: Boolean, block: (Kvs) -> R): R {
        env.newTransaction(readOnly).use(commit = !readOnly) { transaction ->
            transaction.openDatabase().use { database ->
                return block(LmdbKvsTransaction(database))
            }
        }
    }

    override fun close() {
        env.close()
    }
}
