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

internal class Transaction(private val nativeEnv: Long, private val readOnly: Boolean) {
    private val nativeTransaction = Jni.beginTransaction(nativeEnv, readOnly)

    fun openDatabase(): Database = Database(nativeEnv, nativeTransaction, createIfNotExists = !readOnly)

    fun commit() {
        Jni.commitTransaction(nativeTransaction)
    }

    fun abort() {
        Jni.abortTransaction(nativeTransaction)
    }
}

internal inline fun <R> Transaction.use(commit: Boolean = true, block: (Transaction) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        try {
            if (commit && exception == null) commit() else abort()
        } catch (e: Throwable) {
            if (exception == null) throw e
        }
    }
}
